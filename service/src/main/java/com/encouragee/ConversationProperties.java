package com.encouragee;

import com.google.common.base.Splitter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.data.solr.core.query.HighlightOptions;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

@Component
@Validated
@ConfigurationProperties("enc-conversations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@Profile("solr")
public class ConversationProperties {

	public static final String SOURCE_CALLREC = "callrec";
	public static final String SOURCE_ENCOURAGE = "enc";

	private final Splitter identifierSplitter = Splitter.on(":").limit(2);

	@NotNull
	private Integer port;

	@NotNull
	private URL dataApi = url("http://localhost:8300/api");

	@NotNull
	private URL zqmConnectorApi = url("http://localhost:8201/api");

	@NotNull
	private Duration delayOnError = Duration.ofSeconds(1);

	@NotNull
	private Duration publishTimeout = Duration.ofSeconds(2);

	@NotNull
	@Valid
	private SolrProperties solr = new SolrProperties();

	@NotNull
	@Min(1)
	private Integer consumerCount = 4;

	/**
	 * Queue for conversation index tasks.
	 * This queue is persistent and all messages will be processed eventually.
	 */
	@NotEmpty
	private String indexTaskQueue = "conversations-to-save";

	@NotNull
	private Integer indexTaskQueueSize = 100;

	@NotEmpty
	private String indexErrorQueue = "conversations-to-save-dead-letter";

	@NotNull
	private Integer indexErrorQueueSize = 200;

	@NotEmpty
	private String conversationDeletedQueue = "conversations-deleted";

	@NotNull
	private String solrConfigsetPath = "/app/config/solr-config-set/conversation";

	@NotNull
	@Valid
	private DisplayOptionsProperties displayOptions = new DisplayOptionsProperties();

	public static String getSourceSystem(String eventId) {
		if (eventId.startsWith(SOURCE_CALLREC + ":")) {
			return SOURCE_CALLREC;
		}
		return SOURCE_ENCOURAGE;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ExternalSystemProperties {

		@NotNull
		private URL api;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class SolrProperties {
		@NotNull
		private String zookeeper = "localhost:9983";
		/**
		 * The replication factor. Applies to all collections.
		 * This is how many times the data is saved, hence minimum is 1.
		 */
		@NotNull
		@Min(1)
		private Integer replicas = 1;
		/**
		 * The total number of shards. Applies to all collections.
		 */
		@NotNull
		@Min(1)
		private Integer shards = 4;
		/**
		 * The maximum number of shards per node. Applies to all collections.
		 * Defaults to -1, which is unlimited (i.e., all shards can be on a single node).
		 */
		private Integer maxShardsPerNode = -1;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class DisplayOptionsProperties {
		@NotNull
		private HighlightOptions highlight = new HighlightOptions()
				.setNrSnipplets(1000) //how many correctly found results should be highlighted
				.setFragsize(200)
				.setSimplePrefix("<em>")
				.setSimplePostfix("</em>");
	}

	private static URL url(String url) {
		try {
			return new URL(url);
		} catch (MalformedURLException exc) {
			throw new IllegalStateException("Invalid default URL: " + url, exc);
		}
	}
}
