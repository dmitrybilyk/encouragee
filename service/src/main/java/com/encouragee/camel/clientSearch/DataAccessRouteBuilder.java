package com.encouragee.camel.clientSearch;

import com.encouragee.ConversationProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zoomint.encourage.common.model.label.LabelList;
import com.zoomint.encourage.common.model.label.LabelLookup;
import com.zoomint.encourage.common.model.search.MetadataKeyList;
import com.zoomint.encourage.common.model.search.SearchTemplateList;
import com.zoomint.encourage.common.model.settings.EncourageSettings;
import com.zoomint.encourage.common.model.settings.FieldConfigList;
import com.zoomint.encourage.common.model.speech.SpeechPhraseList;
import com.zoomint.encourage.common.model.speech.SpeechPhraseLookup;
import com.zoomint.encourage.model.conversation.event.EventList;
import com.zoomint.encourage.model.conversation.event.EventLookup;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.spi.DataFormat;
import org.restlet.data.Method;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

//import static com.zoomint.encourage.common.camel.util.ExchangeUtils.addHttpQuery;
import static com.zoomint.encourage.common.camel.util.ExchangeUtils.addHttpQuery;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.apache.camel.Exchange.CONTENT_TYPE;
import static org.restlet.data.Method.GET;
import static org.restlet.data.Method.POST;
//import static org.restlet.data.MediaType.APPLICATION_JSON;
//import static org.restlet.data.Method.GET;
//import static org.restlet.data.Method.POST;

/**
 * Routes for communicating with Data Access.
 */
@Component
@Profile("solr")
public class DataAccessRouteBuilder extends RouteBuilder {

	public static final String URI_LOOKUP_EVENTS = "direct:da_lookupEvents";
	public static final String URI_DELETE_EVENTS = "direct:da_deleteEvents";
	public static final String URI_UPDATE_EVENTS = "direct:da_updateEvents";

	public static final String URI_LOOKUP_LABELS = "direct:da_labelLookup";
	public static final String URI_LOOKUP_SPEECH_PHRASES = "direct:da_phraseLookup";

	public static final String URI_GET_ADMIN_SETTINGS = "direct:da_getAdminSettings";
	public static final String URI_GET_METADATA_KEYS_TO_ANONYMIZE = "direct:da_getMetadataKeysToAnonymize";
	public static final String URI_GET_FIELD_CONFIG_TO_ANONYMIZE = "direct:da_getFieldConfigToAnonymize";
	public static final String URI_GET_PROTECTION_SEARCH_TEMPLATES = "direct:da_getProtectionSearchTemplates";
	public static final String URI_GET_DELETION_SEARCH_TEMPLATES = "direct:da_getDeletionSearchTemplates";
	public static final String URI_GET_SAVED_FILTERS = "direct:da_getSavedFilters";

	private final ConversationProperties properties;
	private final ObjectMapper objectMapper;

	public DataAccessRouteBuilder(ConversationProperties properties, ObjectMapper objectMapper) {
		this.properties = properties;
		this.objectMapper = objectMapper;
	}

	@Override
	public void configure() {
		errorHandler(noErrorHandler()); // propagate exceptions back to original route

		DataFormat jsonAny = new JacksonDataFormat(objectMapper, Object.class);

		from(URI_LOOKUP_EVENTS).routeId("da_lookupEvents")
				.convertBodyTo(EventLookup.class)
				.filter().body(EventLookup.class, lookup -> !lookup.isEmpty())
				.marshal(jsonAny)
				.setHeader(CONTENT_TYPE, constant(APPLICATION_JSON))
				.process(addHttpQuery("projection=full"))
				.to(dataApi(POST, "/v3/events/lookup"))
				.removeHeaders("*") // remove all headers from previous request
				.unmarshal(new JacksonDataFormat(objectMapper, EventList.class))
		;
		from(URI_DELETE_EVENTS).routeId("da_deleteEvents")
				.convertBodyTo(EventLookup.class)
				.filter().body(EventLookup.class, lookup -> !lookup.isEmpty())
				.marshal(jsonAny)
				.setHeader(CONTENT_TYPE, constant(APPLICATION_JSON))
				.to(dataApi(POST, "/v3/events/delete/lookup"))
				.removeHeaders("*") // remove all headers from previous request
		;
		from(URI_UPDATE_EVENTS).routeId("da_updateEvents")
				.convertBodyTo(EventList.class)
				.filter().body(EventList.class, list -> !list.getEvents().isEmpty())
				.marshal(jsonAny)
				.setHeader(CONTENT_TYPE, constant(APPLICATION_JSON))
				.to(dataApi(POST, "/v3/events"))
				.removeHeaders("*") // remove all headers from previous request
				.unmarshal(new JacksonDataFormat(objectMapper, EventList.class))
		;


		from(URI_LOOKUP_LABELS).routeId("da_labelLookup")
				.convertBodyTo(LabelLookup.class)
				.filter().body(LabelLookup.class, lookup -> !lookup.isEmpty())
				.marshal(jsonAny)
				.setHeader(CONTENT_TYPE, constant(APPLICATION_JSON))
				.process(addHttpQuery("projection=full"))
				.to(dataApi(POST, "/v3/labels/lookup"))
				.removeHeaders("*") // remove all headers from previous request
				.unmarshal(new JacksonDataFormat(objectMapper, LabelList.class))
		;

		from(URI_LOOKUP_SPEECH_PHRASES).routeId("da_phraseLookup")
				.convertBodyTo(SpeechPhraseLookup.class)
				.filter().body(SpeechPhraseLookup.class, lookup -> !lookup.isEmpty())
				.marshal(jsonAny)
				.setHeader(CONTENT_TYPE, constant(APPLICATION_JSON))
				.process(addHttpQuery("projection=full"))
				.to(dataApi(POST, "/v3/speech-phrases/lookup"))
				.removeHeaders("*") // remove all headers from previous request
				.unmarshal(new JacksonDataFormat(objectMapper, SpeechPhraseList.class))
		;

		from(URI_GET_ADMIN_SETTINGS).routeId("da_getAdminSettings")
				.to(dataApi(GET, "/v3/administration/settings"))
				.removeHeaders("*") // remove all headers from previous request
				.unmarshal(new JacksonDataFormat(objectMapper, EncourageSettings.class))
		;
		from(URI_GET_METADATA_KEYS_TO_ANONYMIZE).routeId("da_getMetadataKeysToAnonymize")
				.to(dataApi(GET, "/v3/metadata-keys/search/anonymize-true"))
				.removeHeaders("*") // remove all headers from previous request
				.unmarshal(new JacksonDataFormat(objectMapper, MetadataKeyList.class))
		;
		from(URI_GET_FIELD_CONFIG_TO_ANONYMIZE).routeId("da_getFieldConfigToAnonymize")
				.to(dataApi(GET, "/v3/field-config/search/anonymize-true"))
				.removeHeaders("*") // remove all headers from previous request
				.unmarshal(new JacksonDataFormat(objectMapper, FieldConfigList.class))
		;
		from(URI_GET_PROTECTION_SEARCH_TEMPLATES).routeId("da_getProtectionSearchTemplates")
				.process(addHttpQuery("projection=full"))
				.to(dataApi(GET, "/v3/search-templates/search/protection-true"))
				.removeHeaders("*") // remove all headers from previous request
				.unmarshal(new JacksonDataFormat(objectMapper, SearchTemplateList.class))
		;
		from(URI_GET_DELETION_SEARCH_TEMPLATES).routeId("da_getDeletionSearchTemplates")
				.process(addHttpQuery("projection=full"))
				.to(dataApi(GET, "/v3/search-templates/search/deletion-true"))
				.removeHeaders("*") // remove all headers from previous request
				.unmarshal(new JacksonDataFormat(objectMapper, SearchTemplateList.class))
		;
		from(URI_GET_SAVED_FILTERS).routeId("da_getSavedFilters")
				.process(addHttpQuery("projection=full"))
				.choice()
				.when(exchange -> exchange.getIn().getHeader("userId") != null)
				.to(dataApi(GET, "/v3/search-templates/users/{userId}"))
				.removeHeaders("*") // remove all headers from previous request
				.unmarshal(new JacksonDataFormat(objectMapper, SearchTemplateList.class))
				.to(EnrichRouteBuilder.URI_ENRICH_SEARCH_TEMPLATES)
		;
	}

	private String dataApi(Method method, String path) {
		return "restlet:" + properties.getDataApi() + path
				+ "?restletMethod=" + method;
	}
}
