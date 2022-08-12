package com.encouragee.camel.converter;

import com.encouragee.camel.model.Chat;
import com.encouragee.camel.model.Email;
import com.zoomint.encourage.common.model.search.MetadataKeyLookup;
import com.zoomint.encourage.common.model.user.User;
import com.zoomint.encourage.common.model.user.UserLookup;
import com.zoomint.encourage.common.model.user.UserState;
//import com.zoomint.encourage.integrations.api.model.Chat;
//import com.zoomint.encourage.integrations.api.model.Chat.ChatParticipant;
//import com.zoomint.encourage.integrations.api.model.Email;
//import com.zoomint.encourage.integrations.api.model.Email.EmailParticipant;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.Collections.singleton;
import static org.springframework.util.StringUtils.hasText;

/**
 * Facilitates lookup of entities used in conversion of emails/chats to conversations
 * by providing the search objects and storing the lookup results.
 */
public class ConversationEntities {

	public static final String ENTITIES_PROPERTY = "entities";

	private Map<String, List<User>> userCurrentByEmail = new HashMap<>();
	private Map<String, List<User>> userDeletedByEmail = new HashMap<>();

	private Set<String> emails = new HashSet<>();
	private Set<String> metadataKeys = new HashSet<>();

	public ConversationEntities forEmails(List<Email> emails) {
		emails.forEach(this::forEmail);
		return this;
	}

	public ConversationEntities forChats(List<Chat> chats) {
		chats.forEach(this::forChat);
		return this;
	}

	private void forEmail(Email email) {
		Stream.of(
				singleton(email.getAddressFrom()),
				email.getAddressesTo(),
				email.getCc(),
				email.getBcc()
		)
				.filter(Objects::nonNull)
				.flatMap(Collection::stream)
				.filter(Objects::nonNull)
				.map(Email.EmailParticipant::getEmail)
				.filter(Objects::nonNull)
				.forEach(emails::add);

		if (email.getData() != null) {
			email.getData().forEach((key, value) -> metadataKeys.add(key));
		}
	}

	private void forChat(Chat chat) {
		Stream.of(
				singleton(chat.getFrom()),
				chat.getTo()
		)
				.filter(Objects::nonNull)
				.flatMap(Collection::stream)
				.filter(Objects::nonNull)
				.map(Chat.ChatParticipant::getEmail)
				.filter(Objects::nonNull)
				.forEach(emails::add);

		if (chat.getData() != null) {
			chat.getData().forEach((key, value) -> metadataKeys.add(key));
		}
	}

	public User getUser(String email) {
		String emailLower = email.toLowerCase(Locale.ROOT);
		return Stream.<Supplier<List<User>>>of(
				() -> userCurrentByEmail.get(emailLower),
				() -> userDeletedByEmail.get(emailLower)
		)
				.map(Supplier::get)
				// find first list (criteria) that has matches...
				.filter(users -> users != null && !users.isEmpty())
				.findFirst()
				// but ensure it's a unique match, otherwise return nothing
				.filter(users -> users.size() == 1)
				.map(users -> users.get(0))
				.orElse(null);
	}

	public UserLookup getUserSearch() {
		return UserLookup.builder().emails(emails).build();
	}

	public MetadataKeyLookup getMetadataSearch() {
		return MetadataKeyLookup.builder().keys(metadataKeys).build();
	}

	public void addUsers(List<User> users) {
		users.forEach(this::addUser);
	}

	public ConversationEntities addUser(User user) {
		if (hasText(user.getEmail())) {
			(user.getState() == UserState.DELETED ? userDeletedByEmail : userCurrentByEmail)
					.computeIfAbsent(user.getEmail().toLowerCase(Locale.ROOT), absent -> new LinkedList<>()).add(user);
		}
		return this;
	}
}
