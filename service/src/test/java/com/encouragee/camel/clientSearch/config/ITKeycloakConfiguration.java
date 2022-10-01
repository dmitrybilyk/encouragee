package com.encouragee.camel.clientSearch.config;

import com.zoomint.keycloak.lib.helper.core.RealmDtoAdapter;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import java.net.MalformedURLException;
import java.net.URL;

@Profile("integration-tests") // don't let it be discovered during startup of the service under test
@Configuration
public class ITKeycloakConfiguration {
	@Autowired
	private Environment environment;

	@Bean
	public Keycloak keycloakAdmin() {
		return KeycloakBuilder.builder()
				.serverUrl("http://localhost:" + environment.getRequiredProperty("keycloak.port") + "/auth")
				.realm("master")
				.clientId("admin-cli")
				.username("admin")
				.password("password")
				.build();
	}

	@Bean
	@DependsOn("service")
	public URL keyCloakURL() throws MalformedURLException {
		return new URL("http://localhost:" + environment.getRequiredProperty("keycloak.port") + "/auth");
	}

	@Bean
	public RealmDtoAdapter keycloakRealm(Keycloak keycloakAdmin) {
		return new RealmDtoAdapter("http://localhost:" + environment.getRequiredProperty("keycloak.port", Integer.class) + "/auth", "default", keycloakAdmin);
	}
}
