package com.encouragee.camel.model;

import com.zoomint.encourage.model.search.Direction;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApiModel(description = "Represents an email of the system")
@Builder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Email {
	@ApiModelProperty(value = "The id of the email", required = true)
	private String id;
	private String subject;
	private Organization organization;
	private Integer status;
	private String activityId;
	@ApiModelProperty(value = "Sender address", required = true)
	private EmailParticipant addressFrom;
	@ApiModelProperty(value = "Recipient address(es)", required = true)
	private List<EmailParticipant> addressesTo = new ArrayList<>();
	@ApiModelProperty(value = "Conversation position", required = false)
	private String replyToId;
	private EmailParticipant replyTo;
	@ApiModelProperty(value = "Sending date in ISO format", dataType = "string", example = "2016-11-02T09:33:35.609Z", required = true)
	private ZonedDateTime sendDate;
	@ApiModelProperty(value = "Recipient carbon copy address(es)", required = false)
	private List<EmailParticipant> cc = new ArrayList<>(0);
	@ApiModelProperty(value = "Recipient blind carbon copy address(es)", required = false)
	private List<EmailParticipant> bcc = new ArrayList<>(0);
	@ApiModelProperty(value = "Body of the email in plaintext", required = false)
	private String emailText;
	private String mimeType;
	private String language;
	private Direction direction;
	@ApiModelProperty(value = "Conversation reference", required = false)
	private String caseId;
	@ApiModelProperty(value = "Metadata of the email. Pair of string key values", required = false)
	private Map<String, String> data = new HashMap<>();


	@Override
	public String toString() {
		return "Email{" +
				"id='" + id + '\'' +
				", subject='" + subject + '\'' +
				", organization=" + organization +
				", status=" + status +
				", activityId='" + activityId + '\'' +
				", addressFrom=" + addressFrom +
				", addressesTo=" + addressesTo +
				", replyToId='" + replyToId + '\'' +
				", replyTo=" + replyTo +
				", sendDate=" + sendDate +
				", cc=" + cc +
				", bcc=" + bcc +
				", emailText='" + emailText + '\'' +
				", mimeType='" + mimeType + '\'' +
				", language='" + language + '\'' +
				", direction=" + direction +
				", caseId='" + caseId + '\'' +
				", data=" + data +
				'}';
	}

	@Builder(toBuilder = true)
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Organization {
		private String orgId;
		private String legalId;
		private String name;

		@Override
		public String toString() {
			return "Organization{" +
					"orgId='" + orgId + '\'' +
					", legalId='" + legalId + '\'' +
					", name='" + name + '\'' +
					'}';
		}
	}

	@ApiModel(description = "Represents a user in the email")
	@Builder(toBuilder = true)
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class EmailParticipant {
		private String email;
		private String fullName;

		@Override
		public String toString() {
			return "EmailParticipant{" +
					"email='" + email + '\'' +
					", fullName='" + fullName + '\'' +
					'}';
		}
	}

}
