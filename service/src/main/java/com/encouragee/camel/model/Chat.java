package com.encouragee.camel.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ApiModel(description = "Represents a chat of the system")
public class Chat {

	@ApiModelProperty(value = "Chat ID", required = true)
	private String messageId;

	@ApiModelProperty(value = "Chat thread id, typically room id", required = true)
	private String chatId;

	private String chatName;

	@ApiModelProperty(value = "Message text", required = true)
	private String text;

	@ApiModelProperty(value = "Sending date in ISO format", dataType = "string", example = "2016-11-02T09:33:35.609Z")
	private ZonedDateTime created;

	@ApiModelProperty(value = "Sender", required = true)
	private ChatParticipant from;

	@ApiModelProperty("Recipient")
	@Singular("to")
	private List<ChatParticipant> to = new ArrayList<>(0);

	@ApiModelProperty("Attachments - set of strings, may be used for url")
	@Singular
	private List<String> attachments = new ArrayList<>(0);

	@ApiModelProperty("Additional data - key value pairs")
	@Singular("data")
	private Map<String, String> data = new HashMap<>();

	@Override
	public String toString() {
		return "Chat{" +
				"messageId='" + messageId + '\'' +
				", chatId='" + chatId + '\'' +
				", chatName='" + chatName + '\'' +
				", text='" + text + '\'' +
				", created=" + created +
				", from=" + from +
				", to=" + to +
				", attachments=" + attachments +
				", data=" + data +
				'}';
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ChatParticipant {
		private String id;
		private String email;
		private String displayName;

		@Override
		public String toString() {
			return "ChatParticipant{" +
					"id='" + id + '\'' +
					", email='" + email + '\'' +
					", displayName='" + displayName + '\'' +
					'}';
		}
	}
}
