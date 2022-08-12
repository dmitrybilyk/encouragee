package com.encouragee.camel.converter;

//import com.zoomint.encourage.integrations.api.ApiProperties;
//import com.zoomint.encourage.integrations.api.model.Email;
//import com.zoomint.encourage.integrations.common.regex.FieldRegEx;
//import com.zoomint.encourage.integrations.common.regex.RegExUtils;
import com.encouragee.ApiProperties;
import com.encouragee.camel.model.Email;
import com.encouragee.camel.regex.FieldRegEx;
import com.encouragee.camel.regex.RegExUtils;
import com.zoomint.encourage.model.conversation.Conversation;
import com.zoomint.encourage.model.conversation.ConversationParticipant;
import com.zoomint.encourage.model.conversation.ConversationResource;
import com.zoomint.encourage.model.conversation.event.EmailEvent;
import org.apache.camel.Converter;
import org.apache.camel.Exchange;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.zoomint.encourage.model.conversation.ConversationParticipantType.UNKNOWN;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.springframework.util.StringUtils.hasText;

@Component
@Converter
public class EmailConverter {

	private static final UriComponents EMAIL_ID_URI =
			UriComponentsBuilder.fromUriString("{systemType}:emailid:{emailId}").build();

	private final Map<String, List<FieldRegEx>> regExMap;
	private final ApiProperties properties;

	public EmailConverter(Map<String, List<FieldRegEx>> regExMap, ApiProperties properties) {
		this.regExMap = regExMap;
		this.properties = properties;
	}

	@Converter
	@NotNull
	public Conversation fromEmail(@NotNull Email email, Exchange exchange) {
		return fromEmail(email, exchange.getProperty(ConversationEntities.ENTITIES_PROPERTY, ConversationEntities.class));
	}

	@NotNull
	Conversation fromEmail(@NotNull Email email, @NotNull ConversationEntities entities) {
		Assert.notNull(email.getSendDate(), "Send date must be present and in ISO format!");

		RegExUtils.applyRegexesToObject(regExMap, email);

		return Conversation.builder()
				.event(createEmailEvent(email, entities))
				.build();
	}

	private EmailEvent createEmailEvent(@NotNull Email email, @NotNull ConversationEntities entities) {
		return EmailEvent.builder()
				.start(email.getSendDate())
				.correlationId(getCaseId(email))
				.resource(new ConversationResource(getURIFromEmail(email)))
				.direction(email.getDirection())
				.subject(email.getSubject())
				.emailText(email.getEmailText())
				.mimeType(email.getMimeType())
				.data(getValidAttachedData(email.getData()))
				// this participant is the agent or customer who sent the email
				.participant(fromEmailParticipant(email.getAddressFrom(), entities))
				.participantsTo(fromEmailParticipants(email.getAddressesTo(), entities))
				.participantsCC(fromEmailParticipants(email.getCc(), entities))
				.participantsBCC(fromEmailParticipants(email.getBcc(), entities))
				.eventId(getEmailId(email))
				.build();
	}

	private Map<String, String> getValidAttachedData(Map<String, String> originalData) {
		return originalData.entrySet().stream()
				.filter(entry -> hasText(entry.getValue()))
				.collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	private ConversationParticipant fromEmailParticipant(Email.EmailParticipant participant, @NotNull ConversationEntities entities) {
		if (participant == null || !hasText(participant.getEmail())) {
			return null;
		}
		return ConversationParticipant.builder()
				.type(UNKNOWN)
				.address(participant.getEmail())
				.user(entities.getUser(participant.getEmail()))
				.build();
	}

	private List<ConversationParticipant> fromEmailParticipants(List<Email.EmailParticipant> participants, @NotNull ConversationEntities entities) {
		return participants.stream()
				.map(participant -> fromEmailParticipant(participant, entities))
				.filter(Objects::nonNull)
				.collect(toList());
	}

	private String getEmailId(Email email) {
		return properties.getEmail().getExternalSystemType()
				+ ":emailid:" + email.getId();
	}

	private String getCaseId(Email email) {
		return properties.getEmail().getExternalSystemType()
				+ ":caseid:" + email.getCaseId();
	}

	private URI getURIFromEmail(@NotNull Email email) {
		Assert.hasText(email.getId(), "Email ID is null or empty!");
		return EMAIL_ID_URI
				.expand(
						properties.getEmail().getExternalSystemType(),
						email.getId())
				.toUri();
	}
}
