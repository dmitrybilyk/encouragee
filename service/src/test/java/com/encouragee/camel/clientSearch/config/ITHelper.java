package com.encouragee.camel.clientSearch.config;

import com.encouragee.camel.clientSearch.model.ConversationsPage;
import com.zoomint.encourage.model.conversation.Conversation;
import com.zoomint.encourage.model.conversation.ConversationList;
import com.zoomint.encourage.model.search.ClientConversationSearch;
import io.prometheus.client.CollectorRegistry;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static java.net.HttpURLConnection.HTTP_NO_CONTENT;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.awaitility.Awaitility.await;

/**
 * Helper for sending REST requests to DataAccess - for use in integration tests.
 */
@Slf4j
public class ITHelper {
	private final RabbitTemplate rabbitTemplate;
	private final CollectorRegistry registry;
	private int indexBatchExpected;

	public ITHelper(RabbitTemplate rabbitTemplate, CollectorRegistry registry) {
		this.rabbitTemplate = rabbitTemplate;
		this.registry = registry;
		indexBatchExpected = getConversationIndexerBatchesTotal();
	}

	private int getConversationIndexerBatchesTotal() {
		return registry.getSampleValue("conversation_indexer_batches_total").intValue();
	}

	public void deleteConversations() {
		given()
				.when().delete("/conversations/")
				.then().statusCode(HTTP_NO_CONTENT);
		indexBatchExpected = getConversationIndexerBatchesTotal();
	}

	public void waitForIndexer() {
		await()
				.atMost(70, TimeUnit.SECONDS)
				.pollInterval(10, TimeUnit.MILLISECONDS)
				.until(() -> getConversationIndexerBatchesTotal() >= indexBatchExpected);
	}

	public void indexConversations(ConversationList conversations) {
		rabbitTemplate.invoke(operations -> {
			rabbitTemplate.convertAndSend(ITConfiguration.INDEX_QUEUE, conversations);
			rabbitTemplate.waitForConfirmsOrDie(2_000);
			return null;
		});
		indexBatchExpected++;
	}

	public ConversationsPage searchConversations(SearchRequest request) {
		return given().contentType(JSON).body(request.getSearch())
				.queryParams(request.getParams())
				.when().post("/conversations/client-search")
				.then().statusCode(HTTP_OK)
				.extract().body().as(ConversationsPage.class);
	}

	public Conversation getConversationById(String id) {
		return given()
				.when().get("/conversations/{conversationId}", id)
				.then().statusCode(HTTP_OK)
				.extract().body().as(Conversation.class);
	}

	public ConversationsPage searchConversations(ClientConversationSearch search) {
		return searchConversations(SearchRequest.builder().search(search).build());
	}

	/**
	 * A wrapper for ClientConversationSearch with additional query parameters that directly affect the search and results.
	 */
	@Data
	@Builder(toBuilder = true)
	public static class SearchRequest {
		private ClientConversationSearch search;
		private ZonedDateTime clientTimestamp;
		private Integer page;
		private Integer size;
		private String cursorMark;
		@Singular("sort")
		private List<String> sort;

		public Map<String, Object> getParams() {
			Map<String, Object> params = new HashMap<>(4);
			if (clientTimestamp != null) {
				params.put("clientTimestamp", clientTimestamp.toString());
			}
			if (page != null) {
				params.put("page", page.toString());
			}
			if (size != null) {
				params.put("size", size.toString());
			}
			if (!sort.isEmpty()) {
				params.put("sort", sort);
			}

			if (cursorMark != null) {
				params.put("cursorMark", cursorMark);
			}
			return params;
		}
	}
}
