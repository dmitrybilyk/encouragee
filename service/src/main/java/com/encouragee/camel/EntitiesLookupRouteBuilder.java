package com.encouragee.camel;

import com.encouragee.camel.converter.ConversationEntities;
import com.zoomint.encourage.common.model.search.MetadataKeyLookup;
import com.zoomint.encourage.common.model.user.UserList;
import com.zoomint.encourage.common.model.user.UserLookup;
//import com.zoomint.encourage.integrations.api.converter.ConversationEntities;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

//import static com.zoomint.encourage.integrations.api.converter.ConversationEntities.ENTITIES_PROPERTY;
import static com.encouragee.camel.converter.ConversationEntities.ENTITIES_PROPERTY;
import static org.apache.camel.LoggingLevel.DEBUG;
import static org.apache.camel.util.toolbox.AggregationStrategies.useOriginal;

@Component
public class EntitiesLookupRouteBuilder extends RouteBuilder {

	public static final String URI_LOOKUP_ENTITIES = "direct:enrichLookupEntities";

	private static final String URI_LOOKUP_USERS = "direct:enrichLookupUsers";
	private static final String URI_LOOKUP_METADATA_KEYS = "direct:enrichLookupMetadataKeys";

	@Override
	public void configure() {
		from(URI_LOOKUP_ENTITIES).routeId("enrichLookupEntities")
				.log(DEBUG, "Looking up entities for conversion to conversations")
				.multicast(useOriginal(), true).parallelAggregate().streaming() // bulk lookup all entities in parallel
				.to(URI_LOOKUP_USERS, URI_LOOKUP_METADATA_KEYS)
		;

		from(URI_LOOKUP_USERS).routeId("enrichLookupUsers")
				.setBody().exchange(exchange -> exchange.getProperty(ENTITIES_PROPERTY, ConversationEntities.class).getUserSearch())
				.filter().body(UserLookup.class, search -> !search.isEmpty())
				.log(DEBUG, "Looking up users for conversations")
				.to(DataAccessRouteBuilder.URI_SEARCH_USERS)
				.process(exchange -> exchange.getProperty(ENTITIES_PROPERTY, ConversationEntities.class)
						.addUsers(exchange.getIn().getBody(UserList.class).getUsers()))
		;

		from(URI_LOOKUP_METADATA_KEYS).routeId("enrichLookupMetadataKeys")
				.setBody().exchange(exchange -> exchange.getProperty(ENTITIES_PROPERTY, ConversationEntities.class).getMetadataSearch())
				.filter().body(MetadataKeyLookup.class, search -> !search.isEmpty())
				.log(DEBUG, "Looking up metadata keys for conversations")
				.to(DataAccessRouteBuilder.URI_SEARCH_METADATA_KEYS)
		// note: key IDs are not saved, keys are just auto-created, but then used by the actual string key
		;
	}

}
