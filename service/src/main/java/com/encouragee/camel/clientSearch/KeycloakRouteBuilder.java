//package com.encouragee.camel.clientSearch;
//
//import com.zoomint.keycloak.clienttokenprovider.ClientTokenProvider;
//import com.zoomint.keycloak.lib.common.rest.paging.Page;
//import com.zoomint.keycloak.lib.common.rest.paging.PageRequest;
//import com.zoomint.keycloak.provider.api.client.KeycloakApiProviderClient;
//import com.zoomint.keycloak.provider.api.dto.Group;
//import com.zoomint.keycloak.provider.api.dto.User;
//import com.zoomint.keycloak.provider.api.dto.UserLookup;
//import org.apache.camel.builder.RouteBuilder;
//import org.springframework.stereotype.Component;
//
//import java.util.Arrays;
//import java.util.HashSet;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//import static org.apache.camel.LoggingLevel.DEBUG;
//
//@Component
//public class KeycloakRouteBuilder extends RouteBuilder {
//	public static final String URI_LOOKUP_USER_GROUP_MEMBERSHIP = "direct:kc_userGroupMembershipLookup";
//	public static final String URI_LOOKUP_USER = "direct:kc_userLookup";
//	public static final String URI_LOOKUP_SUBGROUPS = "direct:kc_subGroupsLookup";
//
//	private final ClientTokenProvider clientTokenProvider;
//	private final KeycloakApiProviderClient keycloakClient;
//
//	public KeycloakRouteBuilder(ClientTokenProvider clientTokenProvider, KeycloakApiProviderClient keycloakClient) {
//		this.clientTokenProvider = clientTokenProvider;
//		this.keycloakClient = keycloakClient;
//	}
//
//	@Override
//	public void configure() {
//		errorHandler(noErrorHandler()); // propagate exceptions back to original route
//
//		from(URI_LOOKUP_USER).routeId("kc_userLookup")
//				.log(DEBUG, "Looking for users in keycloak")
//				.transform().body(UserLookup.class, this::doUsersLookup)
//				.log(DEBUG, "Found '${body.size}' users in keycloak")
//		;
//
//		from(URI_LOOKUP_SUBGROUPS).routeId("kc_subGroupsLookup")
//				.transform().body(Set.class, this::findSubGroups)
//				.log(DEBUG, "Found '${body.size}' subgroups groups in keycloak")
//		;
//
//		from(URI_LOOKUP_USER_GROUP_MEMBERSHIP).routeId("kc_userGroupMembershipLookup")
//				.convertBodyTo(UserLookup.class)
//				.log(DEBUG, "Looking for users in keycloak")
//				.transform().body(UserLookup.class, this::doUsersLookup)
//				.log(DEBUG, "Found '${body.size}' users in keycloak")
//		;
//
//	}
//
//	private Set<User> doUsersLookup(UserLookup lookup) {
//		if (lookup.isEmpty()) {
//			log.trace("UserLookup empty. Will not contact Keycloak");
//			return Set.of();
//		}
//
//		log.debug("Looking for users in keycloak");
//		Page<User> page = keycloakClient.getUsers(
//				clientTokenProvider.getRealm(),
//				clientTokenProvider.getAccessTokenString(),
//				lookup,
//				PageRequest.of(0, PageRequest.MAX_PAGE_SIZE)
//		);
//		Set<User> users = new HashSet<>(page.getContent());
//
//		while (page.hasNext()) {
//			page =  keycloakClient.getUsers(clientTokenProvider.getRealm(), clientTokenProvider.getAccessTokenString(), lookup, page.nextPage());
//			users.addAll(page.getContent());
//		}
//
//		log.debug("Found {} users in keycloak", users.size());
//		return users;
//	}
//
//	private Set<Group> findSubGroups(Set<String> parentGroupIds) {
//		log.debug("Looking for groups by parentIds {} in keycloak", Arrays.toString(parentGroupIds.toArray()));
//		return parentGroupIds.stream()
//				.flatMap(groupId -> getSubgroups(groupId).stream())
//				.collect(Collectors.toSet());
//	}
//
//	private Set<Group> getSubgroups(String groupId) {
//		Page<Group> page = keycloakClient.getSubGroups(
//				clientTokenProvider.getRealm(),
//				clientTokenProvider.getAccessTokenString(),
//				groupId,
//				PageRequest.of(0, PageRequest.MAX_PAGE_SIZE)
//		);
//		Set<Group> groups = new HashSet<>(page.getContent());
//
//		while (page.hasNext()) {
//			page =  keycloakClient.getSubGroups(
//					clientTokenProvider.getRealm(),
//					clientTokenProvider.getAccessTokenString(), groupId, page.nextPage());
//			groups.addAll(page.getContent());
//		}
//
//		return groups;
//	}
//}
