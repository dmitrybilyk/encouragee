package com.encouragee.camel;

import com.encouragee.ApiProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.zoomint.encourage.common.camel.restlet.RestletUtil;
import com.zoomint.encourage.common.model.search.MetadataKeyLookup;
import com.zoomint.encourage.common.model.user.UserList;
import com.zoomint.encourage.common.model.user.UserLookup;
//import com.zoomint.encourage.integrations.api.ApiProperties;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.spi.DataFormat;
import org.restlet.data.Method;
import org.springframework.stereotype.Component;

import static org.apache.camel.Exchange.CONTENT_TYPE;
import static org.apache.camel.Exchange.HTTP_QUERY;
import static org.restlet.data.MediaType.APPLICATION_JSON;
import static org.restlet.data.Method.POST;
import static org.restlet.engine.header.HeaderConstants.ATTRIBUTE_HEADERS;

@Component
public class DataAccessRouteBuilder extends RouteBuilder {
	public static final String URI_SEARCH_USERS = "direct:daUserSearch";
	public static final String URI_SEARCH_METADATA_KEYS = "direct:daMetadataKeySearch";

	private final ApiProperties properties;
	private final ObjectMapper objectMapper;

	public DataAccessRouteBuilder(ApiProperties properties, ObjectMapper objectMapper) {
		this.properties = properties;
		this.objectMapper = objectMapper;
	}

	@Override
	public void configure() {
//		RestletUtil.outgoingRestCallsExceptionHandling(this);

		DataFormat jsonAny = new JacksonDataFormat(objectMapper, Object.class);
		DataFormat jsonUserList = new JacksonDataFormat(objectMapper, UserList.class);

		from(URI_SEARCH_USERS).routeId("daUserSearch")
				.convertBodyTo(UserLookup.class)
				.marshal(jsonAny)
				.setHeader(CONTENT_TYPE, constant(APPLICATION_JSON))
				.setHeader(HTTP_QUERY, constant("projection=summary"))
				.to(dataApi(POST, "/v2/users/lookup"))
				.removeHeader(HTTP_QUERY)
				.removeHeaders(ATTRIBUTE_HEADERS) // remove HTTP headers from previous request
				.unmarshal(jsonUserList)
		;

		from(URI_SEARCH_METADATA_KEYS).routeId("daMetadataKeySearch")
				.convertBodyTo(MetadataKeyLookup.class)
				.marshal(jsonAny)
				.setHeader(CONTENT_TYPE, constant(APPLICATION_JSON))
				.to(dataApi(POST, "/v2/metadata-keys/lookup"))
				.removeHeaders(ATTRIBUTE_HEADERS) // remove HTTP headers from previous request
		// result is not used - ignore
		;
	}

	private String dataApi(Method method, String path) {
		return "restlet:" + properties.getDataApi() + path
				+ "?restletMethod=" + method;
	}
}
