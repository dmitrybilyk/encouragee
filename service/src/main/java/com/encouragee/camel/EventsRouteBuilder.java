package com.encouragee.camel;

import com.encouragee.ApiProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.zoomint.encourage.integrations.api.ApiProperties;
import com.zoomint.encourage.model.conversation.event.EventList;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.spi.DataFormat;
import org.restlet.data.Method;
import org.springframework.stereotype.Component;

import static org.apache.camel.Exchange.CONTENT_TYPE;
import static org.apache.camel.Exchange.HTTP_QUERY;
import static org.apache.camel.LoggingLevel.TRACE;
import static org.restlet.data.MediaType.APPLICATION_JSON;
import static org.restlet.data.Method.POST;
import static org.restlet.engine.header.HeaderConstants.ATTRIBUTE_HEADERS;

@Component
public class EventsRouteBuilder extends RouteBuilder {
	public static final String URI_SAVE_EVENTS = "direct:daSaveEvents";

	private final ApiProperties properties;
	private final ObjectMapper objectMapper;

	public EventsRouteBuilder(ApiProperties properties, ObjectMapper objectMapper) {
		this.properties = properties;
		this.objectMapper = objectMapper;
	}

	@Override
	public void configure() throws Exception {
		DataFormat jsonAny = new JacksonDataFormat(objectMapper, Object.class);

		from(URI_SAVE_EVENTS).routeId("daSaveEvents")
				.removeHeader(HTTP_QUERY)
				.setHeader(CONTENT_TYPE, constant(APPLICATION_JSON))
				.setProperty("conversations").body()
				.convertBodyTo(EventList.class)
				.marshal(jsonAny)
				.to(eventsApi(POST, "/v2/events") + "&socketTimeout=180000")
				.setBody().exchangeProperty("conversations")
				.removeProperty("conversations")
				.log(TRACE, "Sending conversations: ${body}")
				.removeHeaders(ATTRIBUTE_HEADERS) // remove HTTP headers from previous request
		;
	}

	private String eventsApi(Method method, String path) {
		return "restlet:" + properties.getDataApi() + path
				+ "?restletMethod=" + method;
	}
}
