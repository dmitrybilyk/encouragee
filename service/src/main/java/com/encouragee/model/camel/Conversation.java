package com.encouragee.model.camel;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ApiModel(description = "Conversation between customer and agents across any number of communication channels")
public class Conversation {

//	@Id
	@ApiModelProperty("Unique identifier of the conversation within this system.")
	private String conversationId;

	@ApiModelProperty("IDs of any conversations that were merged into this one.")
	private List<String> previousIds = new ArrayList<>(0);

//	/**
//	 * Individual events linked together into this conversation.
//	 */
//	@NonNull
//	@ApiModelProperty("Individual events linked together into this conversation.")
//	private List<ConversationEvent> events = new ArrayList<>();
//
//	/**
//	 * Start of the conversation. Corresponds to the first communication (e.g., call, e-mail).
//	 */
//	@ApiModelProperty(value = "Start of the conversation. Corresponds to the first communication (e.g., call, e-mail). ISO8601 timestamp in UTC.",
//			example = "2001-02-03T04:05:06.007Z")
//	private Instant start;
//
//	/**
//	 * Total duration of the conversation from the first communication to the last one.
//	 */
//	@ApiModelProperty(value = "Total duration of the conversation from the first communication to the last one. ISO8601 duration.",
//			example = "PT2M5S")
//	private Duration duration;
//
//	/**
//	 * Direction of the first communication (e.g., call, e-mail) in the conversation.
//	 */
//	@NonNull
//	@ApiModelProperty("Direction of the first communication (e.g., call, e-mail) in the conversation.")
//	private Direction direction = Direction.UNKNOWN;
//
//	/**
//	 * Types of the communication channels involved in this conversation.
//	 *
//	 * @deprecated no longer used, calculated at the front-end
//	 */
//	@Deprecated
//	@NonNull
//	@JsonDeserialize(as = LinkedHashSet.class)
//	@ApiModelProperty("Distinct types of the communication channels involved in this conversation.")
//	private Set<CommunicationType> communicationTypes = new LinkedHashSet<>(0);
//
//	/**
//	 * Anyone who participated in this conversation, such as customer or agents.
//	 *
//	 * @deprecated no longer used, calculated at the front-end
//	 */
//	@Deprecated
//	@NonNull
//	@ApiModelProperty(hidden = true)
//	private List<ConversationParticipant> participants = new ArrayList<>(0);
//
//	/**
//	 * This constructor does not set any default data.
//	 *
//	 * @param conversationId id of conversation
//	 */
//
//	public Conversation(@Nullable String conversationId) {
//		this.conversationId = conversationId;
//	}
//
//	/**
//	 * Creates a new conversation with {@link Instant#now()} lastUpdated and empty mutable list of events.
//	 */
//	public Conversation() {
//		this(null, new ArrayList<>(0), new ArrayList<>(),
//				Direction.UNKNOWN, null, Duration.ZERO);
//	}
//
//	@Builder(toBuilder = true)
//	private Conversation(String conversationId,
//                         @Singular List<String> previousIds,
//                         @Singular List<ConversationEvent> events,
//                         Direction direction,
//                         Instant start,
//                         Duration duration) {
//
//		this.conversationId = conversationId;
//		this.previousIds = previousIds == null ? new ArrayList<>(0) : previousIds;
//		this.events = events == null ? new ArrayList<>(0) : events;
//		this.direction = direction;
//		this.start = start;
//		this.duration = duration;
//	}
//
//	@Override
//	public boolean equals(Object other) {
//		if (this == other) {
//			return true;
//		}
//		return other instanceof Conversation
//				&& conversationId != null
//				&& conversationId.equals(((Conversation) other).getConversationId());
//	}
//
//	@Override
//	public int hashCode() {
//		return conversationId != null ? conversationId.hashCode() : super.hashCode();
//	}
//
//	@Override
//	public String toString() {
//		return MoreObjects.toStringHelper(Conversation.class)
//				.add("conversationId", conversationId)
//				.add("previousIds", previousIds)
//				.add("events", events)
//				.add("start", start)
//				.add("duration", duration)
//				.add("direction", direction)
//				.omitNullValues()
//				.toString();
//	}
}
