package com.encouragee.camel.clientSearch;

import com.encouragee.ConversationProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zoomint.encourage.model.QuestionnaireList;
import com.zoomint.encourage.model.conversation.event.EventList;
import com.zoomint.encourage.model.conversation.event.EventLookup;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.spi.DataFormat;
import org.restlet.data.Method;
import org.springframework.stereotype.Component;

import static org.restlet.data.MediaType.APPLICATION_JSON;
import static org.restlet.data.Method.GET;
import static org.restlet.data.Method.POST;
import static org.restlet.engine.header.HeaderConstants.HEADER_CONTENT_TYPE;

/**
 * Routes for communicating with ZQM Connector.
 */
@Component
public class ZQMConnectorRouteBuilder extends RouteBuilder {
	public static final String URI_LOOKUP_EVENTS = "direct:zqmLookupEvents";
	public static final String URI_UPDATE_EVENTS = "direct:zqmUpdateEvents";
	public static final String URI_GET_QUESTIONNAIRES = "direct:zqmGetQuestionnaires";

	private final ConversationProperties properties;
	private final ObjectMapper objectMapper;

	public ZQMConnectorRouteBuilder(ConversationProperties properties, ObjectMapper objectMapper) {
		this.properties = properties;
		this.objectMapper = objectMapper;
	}

	@Override
	public void configure() {
		errorHandler(noErrorHandler()); // propagate exceptions back to original route

		DataFormat jsonAny = new JacksonDataFormat(objectMapper, Object.class);
		DataFormat jsonEventList = new JacksonDataFormat(objectMapper, EventList.class);

		from(URI_LOOKUP_EVENTS).routeId("zqmLookupEvents")
				.convertBodyTo(EventLookup.class)
				.marshal(jsonAny)
				.setHeader(HEADER_CONTENT_TYPE, constant(APPLICATION_JSON))
				.to(zqmConnectorApi(POST, "/events/lookup"))
				.removeHeaders("*") // remove all headers from previous request
				.unmarshal(jsonEventList)
		;

		from(URI_UPDATE_EVENTS).routeId("zqmUpdateEvents")
				.convertBodyTo(EventList.class)
				.marshal(jsonAny)
				.setHeader(HEADER_CONTENT_TYPE, constant(APPLICATION_JSON))
				.to(zqmConnectorApi(POST, "/events"))
				.removeHeaders("*") // remove all headers from previous request
				.unmarshal(jsonEventList)
		;

		from(URI_GET_QUESTIONNAIRES).routeId("zqmGetQuestionnaires")
				.to(zqmConnectorApi(GET, "/questionnaires"))
				.removeHeaders("*") // remove all headers from previous request
				.unmarshal(new JacksonDataFormat(objectMapper, QuestionnaireList.class))
		;
	}

	private String zqmConnectorApi(Method method, String path) {
		return "restlet:" + properties.getZqmConnectorApi() + path
				+ "?restletMethod=" + method;
	}
}
