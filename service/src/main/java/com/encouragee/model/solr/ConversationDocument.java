package com.encouragee.model.solr;

import lombok.Data;
import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.Score;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.time.Instant;
import java.util.*;
import java.util.regex.Pattern;

@SolrDocument(collection = ConversationDocument.SOLR_COLLECTION_NAME)
@Data
public class ConversationDocument {
	public static final String SOLR_COLLECTION_NAME = "conversation";

	/**
	 * Version of the schema being used to index documents currently.
	 * This value should be incremented whenever schema changes in configset: {@code configsets/conversation/managed-schema}.
	 */
	public static final Integer CURRENT_SCHEMA_VERSION = 6;

	public static final Pattern NUMBER_PATTERN = Pattern.compile("[+-]?\\d+");

	public static final String FIELD_CONVERSATION_ID = "conversationId";
	public static final String FIELD_VERSION = "_version_";
	public static final String FIELD_EVENT_IDS = "eventIds";
	public static final String FIELD_NEW_ID = "newId";
	public static final String FIELD_PREVIOUS_IDS = "previousIds";
	public static final String FIELD_SCHEMA_VERSION = "schemaVersion";
	public static final String FIELD_CORRELATION_IDS = "correlationIds";
	public static final String FIELD_DIRECTION = "direction";
	public static final String FIELD_DURATION = "duration";
	public static final String FIELD_DAY_OF_WEEK = "dayOfWeek";
	public static final String FIELD_SECOND_OF_DAY = "secondOfDay";
	public static final String FIELD_START_DATE_TIME = "startDateTime";
	public static final String FIELD_LATEST_SEGMENT_START_DATE_TIME = "latestSegmentStartDateTime";
	public static final String FIELD_COMMUNICATION_TYPES = "communicationTypes";
	public static final String FIELD_SPEECH_PHRASE_DYNAMIC_PREFIX = "sp_";
	public static final String FIELD_METADATA_DYNAMIC_PREFIX = "md_";
	public static final String FIELD_METADATA_NUMBER_DYNAMIC_PREFIX = "mdn_";
	public static final String FIELD_LABELS =  "labels";
	public static final String FIELD_COMMENTS = "comments";
	public static final String FIELD_SURVEY_COMMENTS = "surveyComments";
	public static final String FIELD_CONTACTS_FROM = "contactsFrom";
	public static final String FIELD_CONTACTS_TO = "contactsTo";
	public static final String FIELD_TEXT_SUBJECT = "textSubject";
	public static final String FIELD_TEXT_BODY = "textBody";
	public static final String FIELD_USER_IDS = "userIds";
	public static final String FIELD_USER_UUIDS = "userUUIDs";
	public static final String FIELD_USER_IDS_STRING = "userIdsString";
	public static final String FIELD_GROUP_IDS = "groupIds";
	public static final String FIELD_GROUP_UUIDS = "groupUUIDs";

	public static final String FIELD_REVIEW_IDS = "reviewIds";
	public static final String FIELD_REVIEW_QUESTIONNAIRE_IDS = "reviewQuestionnaireIds";
	public static final String FIELD_REVIEWER_USER_IDS = "reviewerUserIds";
	public static final String FIELD_REVIEWER_USER_UUIDS = "reviewerUserUUIDs";
	public static final String FIELD_REVIEWED_USER_IDS = "reviewedUserIds";
	public static final String FIELD_REVIEWED_USER_UUIDS = "reviewedUserUUIDs";
	public static final String FIELD_REVIEW_SCORING_SYSTEMS = "reviewScoringSystems";
	public static final String FIELD_REVIEW_SCORE_PERCENTAGE = "reviewScoresPercentage";
	public static final String FIELD_REVIEW_SCORE_POINTS = "reviewScoresPoints";

	public static final String FIELD_SURVEY_IDS = "surveyIds";
	public static final String FIELD_SURVEYED_USER_IDS = "surveyedUserIds";
	public static final String FIELD_SURVEYED_USER_UUIDS = "surveyedUserUUIDs";
	public static final String FIELD_SURVEY_QUESTIONNAIRE_IDS = "surveyQuestionnaireIds";
	public static final String FIELD_SURVEY_SCORING_SYSTEMS = "surveyScoringSystems";
	public static final String FIELD_SURVEY_SCORE_PERCENTAGE = "surveyScoresPercentage";
	public static final String FIELD_SURVEY_SCORE_POINTS = "surveyScoresPoints";

	public static final String FIELD_RESOURCE_FLAGS = "resourceFlags";
	public static final String FIELD_SEGMENTS_COUNT = "segmentsCount";
	public static final String FIELD_AGENTS_COUNT = "agentsCount";
	public static final String FIELD_SCORE = "score";
	public static final String FIELD_RANDOM_PREFIX = "random_";

	@Id
	@Indexed
	@Field(FIELD_CONVERSATION_ID)
	private String conversationId;

	/**
	 * Version of this document as handled by Solr itself (used for optimistic lock).
	 */
	@Version
	@Field(FIELD_VERSION)
	private Long version;

	@Indexed
	@Field(FIELD_EVENT_IDS)
	private List<String> eventIds = new ArrayList<>();

	/**
	 * If not empty, current conversation was merged into the conversation specified by this ID.
	 */
	@Indexed
	@Field(FIELD_NEW_ID)
	private String newId;

	/**
	 * IDs of any conversations that were merged into this one.
	 */
	@Indexed
	@Field(FIELD_PREVIOUS_IDS)
	private List<String> previousIds = new ArrayList<>();

	/**
	 * Version of the schema used when indexing this document.
	 */
	@Indexed
	@Field(FIELD_SCHEMA_VERSION)
	private Integer schemaVersion;

	@Indexed
	@Field(FIELD_CORRELATION_IDS)
	private Set<String> correlationIds = new HashSet<>();

	@Indexed
	@Field(FIELD_SURVEY_COMMENTS)
	private List<String> surveyComments = new ArrayList<>();

	@Score
	private Float score;

	@Indexed
	@Field(FIELD_DIRECTION)
	private String direction;

	@Indexed
	@Field(FIELD_DURATION)
	private Long duration;

	@Indexed
	@Field(FIELD_DAY_OF_WEEK)
	private Integer dayOfWeek;

	@Indexed
	@Field(FIELD_SECOND_OF_DAY)
	private Integer secondOfDay;

	@Indexed
	@Field(FIELD_START_DATE_TIME)
	private Instant startDateTime;

	@Indexed
	@Field(FIELD_LATEST_SEGMENT_START_DATE_TIME)
	private Instant latestSegmentStartDateTime;

	@Indexed
	@Field(FIELD_COMMUNICATION_TYPES)
	private Set<String> communicationTypes = new HashSet<>();

	@Indexed
	@Field(FIELD_SPEECH_PHRASE_DYNAMIC_PREFIX + "*")
	private Map<String, List<Integer>> speechPhrase = new HashMap<>();

	@Indexed
	@Field(FIELD_METADATA_DYNAMIC_PREFIX + "*")
	private Map<String, List<String>> metadata = new HashMap<>();

	@Indexed
	@Field(FIELD_METADATA_NUMBER_DYNAMIC_PREFIX + "*")
	private Map<String, List<Long>> metadataNumber = new HashMap<>();

	@Indexed
	@Field(FIELD_LABELS)
	private Set<Long> labels = new HashSet<>();

	@Indexed
	@Field(FIELD_COMMENTS)
	private List<String> comments = new ArrayList<>();

	@Indexed
	@Field(FIELD_CONTACTS_FROM)
	private Set<String> contactsFrom = new HashSet<>();

	@Indexed
	@Field(FIELD_CONTACTS_TO)
	private Set<String> contactsTo = new HashSet<>();

	@Indexed
	@Field(FIELD_TEXT_SUBJECT)
	private Set<String> textSubject = new HashSet<>();

	@Indexed
	@Field(FIELD_TEXT_BODY)
	private Set<String> textBody = new HashSet<>();

	@Deprecated
	@Indexed
	@Field(FIELD_USER_IDS)
	private Set<Long> userIds = new HashSet<>();

	@Field(FIELD_USER_UUIDS)
	private Set<String> userUUIDs = new HashSet<>();

	@Indexed
	@Field(FIELD_REVIEW_IDS)
	private Set<String> reviewIds = new HashSet<>();
	@Indexed
	@Field(FIELD_REVIEW_QUESTIONNAIRE_IDS)
	private Set<String> reviewQuestionnaireIds = new HashSet<>();

	@Deprecated
	@Indexed
	@Field(FIELD_REVIEWER_USER_IDS)
	private Set<Long> reviewerUserIds = new HashSet<>();

	@Indexed
	@Field(FIELD_REVIEWER_USER_UUIDS)
	private Set<String> reviewerUserUUIDs = new HashSet<>();

	@Deprecated
	@Indexed
	@Field(FIELD_REVIEWED_USER_IDS)
	private Set<Long> reviewedUserIds = new HashSet<>();

	@Indexed
	@Field(FIELD_REVIEWED_USER_UUIDS)
	private Set<String> reviewedUserUUIDs = new HashSet<>();

	@Indexed
	@Field(FIELD_REVIEW_SCORING_SYSTEMS)
	private Set<String> reviewScoringSystems = new HashSet<>();
	@Indexed
	@Field(FIELD_REVIEW_SCORE_PERCENTAGE)
	private List<Float> reviewScoresPercentage = new ArrayList<>();
	@Indexed
	@Field(FIELD_REVIEW_SCORE_POINTS)
	private List<Float> reviewScoresPoints = new ArrayList<>();

	@Indexed
	@Field(FIELD_SURVEY_IDS)
	private Set<String> surveyIds = new HashSet<>();
	@Indexed
	@Field(FIELD_SURVEY_QUESTIONNAIRE_IDS)
	private Set<String> surveyQuestionnaireIds = new HashSet<>();

	@Deprecated
	@Indexed
	@Field(FIELD_SURVEYED_USER_IDS)
	private Set<Long> surveyedUserIds = new HashSet<>();

	@Indexed
	@Field(FIELD_SURVEYED_USER_UUIDS)
	private Set<String> surveyedUserUUIDs = new HashSet<>();

	@Indexed
	@Field(FIELD_SURVEY_SCORING_SYSTEMS)
	private Set<String> surveyScoringSystems = new HashSet<>();
	@Indexed
	@Field(FIELD_SURVEY_SCORE_PERCENTAGE)
	private List<Float> surveyScoresPercentage = new ArrayList<>();
	@Indexed
	@Field(FIELD_SURVEY_SCORE_POINTS)
	private List<Float> surveyScoresPoints = new ArrayList<>();

	@Deprecated
	@Indexed
	@Field(FIELD_GROUP_IDS)
	private Set<Long> groupIds = new HashSet<>();

	@Indexed
	@Field(FIELD_GROUP_UUIDS)
	private Set<String> groupUUIDs = new HashSet<>();

	@Indexed
	@Field(FIELD_RESOURCE_FLAGS)
	private Set<String> resourceFlags = new HashSet<>();

	@Indexed
	@Field(FIELD_SEGMENTS_COUNT)
	private Integer segmentsCount;

	@Indexed
	@Field(FIELD_AGENTS_COUNT)
	private Integer agentsCount;

}
