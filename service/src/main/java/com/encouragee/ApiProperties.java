package com.encouragee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

@Component
@Validated
@ConfigurationProperties("enc-int-api")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiProperties {

	@NotNull
	private Integer port = 8202;
	@NotEmpty
	private String authorizationToken = "ee3e3980fcbac953c85f6ce40934cef7";
	@NotNull
	private URL dataApi = url("http://localhost:8300/api");
	@Valid
	@NotNull
	private ChatProperties chat = new ChatProperties();
	@Valid
	@NotNull
	private EmailProperties email = new EmailProperties();

	@Valid
	@NotNull
	private QueueProperties correlationQueue =  QueueProperties.builder()
			.name("conversations-to-correlate")
			.exchange("amq.direct")
			.build();

	@NotNull
	private Duration publishTimeout = Duration.ofSeconds(2);

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ChatProperties {
		@NotEmpty
		private String externalSystemType = "chat";
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class EmailProperties {
		@NotEmpty
		private String externalSystemType = "email";
		@NotEmpty
		private String regexFile = "regexp.json";
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class QueueProperties {
		@NotEmpty
		private String name;
		@NotEmpty
		private String exchange;
	}



	private static URL url(String url) {
		try {
			return new URL(url);
		} catch (MalformedURLException exc) {
			throw new IllegalStateException("Invalid default URL: " + url, exc);
		}
	}
}
