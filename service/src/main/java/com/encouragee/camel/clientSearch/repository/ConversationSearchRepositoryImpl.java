package com.encouragee.camel.clientSearch.repository;

import com.encouragee.ConversationProperties;
import com.encouragee.camel.clientSearch.conversation.converter.ClientConversationSearchConverter;
import com.encouragee.camel.clientSearch.model.ConversationSearchPermissions;
import com.encouragee.camel.clientSearch.model.EncourageSolrResultPage;
import com.encouragee.model.solr.ConversationDocument;
import com.google.common.collect.ImmutableList;
import com.zoomint.encourage.common.exceptions.accelerator.WrongParameterException;
import com.zoomint.encourage.model.search.ClientConversationSearch;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.HighlightPage;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.encouragee.model.solr.ConversationDocument.*;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static java.util.Collections.emptyList;
import static org.springframework.data.solr.core.query.AnyCriteria.any;
import static org.springframework.data.solr.core.query.Criteria.where;
import static org.springframework.util.StringUtils.hasText;

@Slf4j
public class ConversationSearchRepositoryImpl implements ConversationSearchRepositoryCustom {

	private static final Criteria WILDCARD_CRITERIA = any();

	private static final FilterQuery FILTER_WRONG_SCHEMA_VERSION = new SimpleFilterQuery(where(FIELD_SCHEMA_VERSION).is(CURRENT_SCHEMA_VERSION).not());

	private static final ConversationSearchPermissions NO_RESTRICTIONS = ConversationSearchPermissions.builder().allowAll(true).build();
	private static final EncourageSolrResultPage<ConversationDocument> NO_RESULTS = new EncourageSolrResultPage<>(emptyList(), PageRequest.of(0, 1), 0);

	private static final ImmutableList<SimpleField> RESULT_FIELDS =
			Stream.of(FIELD_CONVERSATION_ID, FIELD_VERSION, FIELD_EVENT_IDS, FIELD_NEW_ID, FIELD_PREVIOUS_IDS)
					.map(SimpleField::new).collect(toImmutableList());

	private final ConversationProperties properties;
	private final ClientConversationSearchConverter converter;
	private final EncourageSolrTemplate solrTemplate;

	public ConversationSearchRepositoryImpl(
			ConversationProperties properties,
			ClientConversationSearchConverter converter,
			EncourageSolrTemplate solrTemplate) {

		this.properties = properties;
		this.converter = converter;
		this.solrTemplate = solrTemplate;
	}

	/**
	 * Finds conversations based on #permissions, user #search input and #usersAssignedFilters
	 * In order to provide only allowed entities.
	 *
	 * @param search               user's input through the filter banner
	 * @param permissions          user's permissions
	 * @param clientTimestamp      current timestamp for the user
	 * @param cursorMark
	 * @param pageable
	 * @param usersAssignedFilters @{@link List} of user's assigned filters by manager
	 * @return {@link EncourageSolrResultPage} of {@link ConversationDocument}
	 */
	@Override
	public EncourageSolrResultPage<ConversationDocument> findConversations(
			@NotNull final ClientConversationSearch search,
			@NotNull final ConversationSearchPermissions permissions,
			@NotNull final ZonedDateTime clientTimestamp,
			@Nullable final String cursorMark,
			@NotNull final Pageable pageable,
			final List<ClientConversationSearch> usersAssignedFilters) {

		final Criteria fulltextCriteria = Optional.ofNullable(pageable.getSort())
				.map(sort -> sort.getOrderFor(FIELD_SCORE))
				// also apply boosted fulltext query to affect score
				.flatMap(scoreSort -> converter.getFulltextCriteria(search))
				.<Criteria>map(criteria -> criteria.or(WILDCARD_CRITERIA)) // ranking & highlighting only, so always return everything
				.orElse(WILDCARD_CRITERIA);

		SimpleHighlightQuery solrQuery = new SimpleHighlightQuery(fulltextCriteria, pageable);
		solrQuery.setHighlightOptions(properties.getDisplayOptions().getHighlight());

		// no permission restrictions
		if (permissions.isAllowAll()) {
			converter.getFilterCriteria(search, permissions, clientTimestamp)
					.map(SimpleFilterQuery::new)
					.forEach(solrQuery::addFilterQuery);
		} else {
			final List<Criteria> permissionsCriteriaList = converter.getPermissionsCriteria(permissions)
					.collect(Collectors.toList());
			final Criteria permissionCriteria = getCriteria(permissionsCriteriaList, true);
			final List<Criteria> uiCriteriaList = converter.getSearchCriteria(search, clientTimestamp).collect(Collectors.toList());
			final Criteria uiCriteria = getCriteria(uiCriteriaList, false);
			final Criteria mainCriteria = permissionCriteria.connect();

			usersAssignedFilters.forEach(assignedFilter -> {
				final List<Criteria> assignedFilterCriteriaList = converter.getSearchCriteria(assignedFilter, clientTimestamp)
						.collect(Collectors.toList());
				final Criteria assignedFilterCriteria = getCriteria(assignedFilterCriteriaList, false);
				mainCriteria.or(assignedFilterCriteria);

			});

			solrQuery.addFilterQuery(new SimpleFilterQuery(usersAssignedFilters.isEmpty() ? mainCriteria.and(uiCriteria) :
					mainCriteria.connect().and(uiCriteria)));
		}
		return findConversations(solrQuery, cursorMark);
	}

	/**
	 * Finds conversations based on #permissions and user #search input
	 * In order to provide only allowed entities.
	 *
	 * @param search          user's input through the filter banner
	 * @param permissions     user's permissions
	 * @param clientTimestamp current timestamp for the user
	 * @param cursorMark
	 * @param pageable
	 * @return {@link EncourageSolrResultPage} of {@link ConversationDocument}
	 */
	@Override
	public EncourageSolrResultPage<ConversationDocument> findConversations(@NotNull final ClientConversationSearch search,
	                                                                       @NotNull final ConversationSearchPermissions permissions,
	                                                                       @NotNull final ZonedDateTime clientTimestamp,
	                                                                       @Nullable final String cursorMark,
	                                                                       @NotNull final Pageable pageable) {
		return findConversations(search, permissions, clientTimestamp, cursorMark, pageable, emptyList());
	}

	@Override
	public EncourageSolrResultPage<ConversationDocument> findConversationsToReindexAfter(
			@Nullable String lastConversationId, @NotNull Pageable pageable) {

		SimpleHighlightQuery solrQuery = new SimpleHighlightQuery(WILDCARD_CRITERIA, pageable);
		solrQuery.addFilterQuery(FILTER_WRONG_SCHEMA_VERSION);
		if (lastConversationId != null) {
			solrQuery.addFilterQuery(conversationIdGreaterThan(lastConversationId));
		}
		return findConversations(solrQuery, null);
	}

	@Override
	public boolean checkConversationMatchesAnySearch(
			@NotNull String conversationId,
			@NotNull List<ClientConversationSearch> searches,
			@NotNull ZonedDateTime clientTimestamp) {

		if (searches.isEmpty()) {
			return false;
		}

		SimpleHighlightQuery solrQuery = new SimpleHighlightQuery(WILDCARD_CRITERIA);
		solrQuery.addFilterQuery(new SimpleFilterQuery(where(FIELD_CONVERSATION_ID).is(conversationId)));

		getCriteriaToMatchAnySearch(searches, clientTimestamp)
				.map(SimpleFilterQuery::new)
				.ifPresent(solrQuery::addFilterQuery);

		solrQuery.addProjectionOnField(FIELD_CONVERSATION_ID);

		try {
			return solrTemplate.queryForObject(SOLR_COLLECTION_NAME, solrQuery, ConversationDocument.class).isPresent();
		} catch (IllegalArgumentException exc) {
			throw new WrongParameterException(String.format(
					"Failed checking whether conversation '%s' matches searches: %s", conversationId, searches), exc);
		}
	}

	@Override
	@NotNull
	public EncourageSolrResultPage<ConversationDocument> findConversationsIncludeExcludeAfter(
			@NotNull List<ClientConversationSearch> includeSearches,
			@NotNull List<ClientConversationSearch> excludeSearches,
			@Nullable String lastConversationId,
			@NotNull ZonedDateTime clientTimestamp,
			@NotNull Pageable pageable) {

		if (includeSearches.isEmpty()) {
			return NO_RESULTS;
		}

		SimpleHighlightQuery solrQuery = new SimpleHighlightQuery(WILDCARD_CRITERIA, pageable);

		getCriteriaToMatchAnySearch(includeSearches, clientTimestamp)
				.map(SimpleFilterQuery::new)
				.ifPresent(solrQuery::addFilterQuery);

		getCriteriaToMatchAnySearch(excludeSearches, clientTimestamp)
				.map(criteria -> WILDCARD_CRITERIA.and(criteria.notOperator())) // Solr doesn't handle simple negation
				.map(SimpleFilterQuery::new)
				.ifPresent(solrQuery::addFilterQuery);

		Optional.ofNullable(lastConversationId)
				.map(this::conversationIdGreaterThan)
				.ifPresent(solrQuery::addFilterQuery);

		return findConversations(solrQuery, null);
	}

	@NotNull
	@Override
	public List<ConversationDocument> findByConversationIdIn(@NotNull List<String> conversationIds) {
		// use realtime get: https://lucene.apache.org/solr/guide/7_6/realtime-get.html
		return (List<ConversationDocument>) solrTemplate.getByIds(SOLR_COLLECTION_NAME, conversationIds, ConversationDocument.class);
	}

	private Criteria getCriteria(final List<Criteria> criteriaList, final boolean connectMainPart) {
		Criteria criteriaToBeAdded = null;
		for (Criteria criteria : criteriaList) {
			if (criteriaToBeAdded == null) {
				criteriaToBeAdded = connectMainPart ? criteria.connect() : criteria;
			} else if (!criteriaToBeAdded.equals(criteria)) {
				criteriaToBeAdded = criteriaToBeAdded.and(criteria);
			}
		}
		if (criteriaToBeAdded != null) {
			return connectMainPart ? criteriaToBeAdded.connect().and(new Criteria(FIELD_NEW_ID).isNull()) :
					criteriaToBeAdded.and(new Criteria(FIELD_NEW_ID).isNull());
		} else {
			return new Criteria();
		}
	}

	private EncourageSolrResultPage<ConversationDocument> findConversations(@NotNull HighlightQuery solrQuery, @Nullable String cursorMark) {
		// ignore conversations without events (redirects and such)
		RESULT_FIELDS.forEach(solrQuery::addProjectionOnField);

		if (solrQuery.getCriteria() == null) {
			solrQuery.addCriteria(WILDCARD_CRITERIA); // required field
		}

		if (hasText(cursorMark)) {
			try {
				return solrTemplate.queryForCursor(SOLR_COLLECTION_NAME, solrQuery, ConversationDocument.class, cursorMark);
			} catch (IllegalArgumentException ex) {
				log.error("Conversation search failed", ex);
				throw new WrongParameterException("Failed executing query based on: " + solrQuery, ex);
			}
		} else {
			try {
				HighlightPage<ConversationDocument> page = solrTemplate.queryForHighlightPage(SOLR_COLLECTION_NAME, solrQuery, ConversationDocument.class);
				EncourageSolrResultPage<ConversationDocument> encourageSolrResultPage = new EncourageSolrResultPage<>(
						page.getContent(),
						page.getPageable(),
						page.getTotalElements());

				encourageSolrResultPage.setHighlighted(page.getHighlighted());

				return encourageSolrResultPage;
			} catch (IllegalArgumentException ex) {
				log.error("Conversation search failed", ex);
				throw new WrongParameterException("Failed executing query based on: " + solrQuery, ex);
			}
		}
	}

	@NotNull
	private Optional<Criteria> getCriteriaToMatchAnySearch(@NotNull List<ClientConversationSearch> searches, @NotNull ZonedDateTime clientTimestamp) {
		return searches.stream()
				.map(search -> converter.getFilterCriteria(search, NO_RESTRICTIONS, clientTimestamp)
						.reduce(Criteria::and)
						.orElse(WILDCARD_CRITERIA))
				.filter(criteria -> !WILDCARD_CRITERIA.equals(criteria))
				.map(Criteria::connect) // connect to prevent Solr handling operators differently :/
				.reduce(Criteria::or); // matches any = criteria must be ORed
	}

	@NotNull
	private SimpleFilterQuery conversationIdGreaterThan(@NotNull String lastConversationId) {
		return new SimpleFilterQuery(where(FIELD_CONVERSATION_ID).greaterThan(lastConversationId));
	}

}
