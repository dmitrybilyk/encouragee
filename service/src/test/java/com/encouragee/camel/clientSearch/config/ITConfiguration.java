package com.encouragee.camel.clientSearch.config;

import com.encouragee.EncourageeApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.zoomint.encourage.common.jackson.EncourageObjectMapper;
import com.zoomint.encourage.model.search.ClientConversationSearchConverter;
import com.zoomint.encourage.wiremock.RequestNotifier;
import io.prometheus.client.CollectorRegistry;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.LBHttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static com.zoomint.encourage.testing.IntegrationTestUtils.writeTempPropertiesFile;
import static io.restassured.config.LogConfig.logConfig;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static java.util.Collections.singletonList;
import static org.springframework.util.SocketUtils.findAvailableTcpPort;

/**
 * Note 1: don't forget to configure http://artifactory.office.zoomint.com as the registry mirror:
 * - see https://docs.docker.com/registry/recipes/mirror/
 *
 * Note 2: for Docker on Windows add routing to Docker images (assuming default config):
 * {@code route /P add 172.16.0.0 MASK 255.240.0.0 10.0.75.2}
 */
@Profile("integration-tests") // don't let it be discovered during startup of the service under test
@Configuration
@Import({RabbitAutoConfiguration.class, ITKeycloakConfiguration.class})
@PropertySource("it.properties")
@PropertySource("file:${docker.ports.file}")
public class ITConfiguration {
	public static final String MONITORING_USER = "monitoring";
	public static final String MONITORING_PASSWORD = "xyz";

	public static final String INDEX_QUEUE = "conversations-to-save";
	public static final String INDEX_QUEUE_DEAD = "conversations-to-save-dead-letter";
	public static final String DELETE_QUEUE = "conversations-deleted";

	public static final Duration DELAY_ON_ERROR = Duration.ofMillis(100);
	public static final Duration PUBLISH_TIMEOUT = Duration.ofMillis(300);

	@Autowired
	private Environment environment;
	@Autowired
	private AmqpAdmin admin;

	@Bean
	Integer servicePort() {
		return findAvailableTcpPort(9000);
	}

	@Bean
	ObjectMapper objectMapper() {
		return new EncourageObjectMapper();
	}

	@Bean
	RequestNotifier signalExtension() {
		return new RequestNotifier(objectMapper(), "notifier");
	}

	@Bean
	public Jackson2JsonMessageConverter amqpMessageConverter(ObjectMapper objectMapper) {
		return new Jackson2JsonMessageConverter(objectMapper);
	}

	@Bean
	SolrClient solrClient() {
		List<String> zkHosts = singletonList(environment.getRequiredProperty("zk.host") + ":" + environment.getRequiredProperty("zk.port"));
		return new CloudSolrClient.Builder(zkHosts, Optional.empty())
				.withLBHttpSolrClient(new LBHttpSolrClient.Builder()
						.withResponseParser(new XMLResponseParser())
						.build())
				.build();
	}

	@Bean(destroyMethod = "stop")
	WireMockServer dataAccess() {
		WireMockServer server = new WireMockServer(options()
				.extensions(signalExtension())
				.dynamicPort());
		server.start();
		return server;
	}

	@Bean(destroyMethod = "stop")
	WireMockServer zqmConnector() {
		WireMockServer server = new WireMockServer(options()
				.extensions(signalExtension())
				.dynamicPort());
		server.start();
		return server;
	}

	@Bean(destroyMethod = "stop")
	WireMockServer scheduler() {
		WireMockServer server = new WireMockServer(options()
				.extensions(signalExtension())
				.dynamicPort());
		server.start();
		return server;
	}

	@Bean
	public ITHelper itHelper(RabbitTemplate rabbitTemplate) throws IOException {
		return new ITHelper(rabbitTemplate, service().getBean(CollectorRegistry.class));
	}

	@Bean
	public ClientConversationSearchConverter clientSearchConverter() {
		return new ClientConversationSearchConverter();
	}

	@Bean(destroyMethod = "close")
	ConfigurableApplicationContext service() throws IOException {
		// mock queue created by the conversation service
		Queue queue = QueueBuilder
				.nonDurable(DELETE_QUEUE)
				.withArgument("x-max-length", 100)
				.withArgument("x-overflow", "reject-publish")
				.build();
		admin.declareQueue(queue);
		admin.declareBinding(BindingBuilder
				.bind(queue)
				.to(new DirectExchange("amq.direct"))
				.withQueueName());

		Properties override = new Properties();

		override.setProperty("enc-conversations.port", "" + servicePort());
		override.setProperty("enc-conversations.data-api", "http://localhost:" + dataAccess().port());
		override.setProperty("enc-conversations.zqm-connector-api", "http://localhost:" + zqmConnector().port());
		override.setProperty("enc-conversations.solr.zookeeper", environment.resolveRequiredPlaceholders("${zk.host}:${zk.port}"));
		override.setProperty("spring.rabbitmq.host", environment.getRequiredProperty("rabbit.host"));
		override.setProperty("spring.rabbitmq.port", environment.getRequiredProperty("rabbit.port"));

		override.setProperty("tracing.reporter.enabled", "false");
		override.setProperty("camel.opentracing.enabled", "false");
		override.setProperty("security.user.name", MONITORING_USER);
		override.setProperty("security.user.password", MONITORING_PASSWORD);

		override.setProperty("enc-conversations.delay-on-error", DELAY_ON_ERROR.toString());
		override.setProperty("enc-conversations.publish-timeout", PUBLISH_TIMEOUT.toString());
		override.setProperty("enc-conversations.index-task-queue-size", "1");
		override.setProperty("enc-conversations.index-error-queue-size", "1");

		override.setProperty("keycloak-client-token-provider.server-url", "http://localhost:" + getKeycloakPort() + "/auth");
		override.setProperty("keycloak-client-token-provider.realm", "default");
		override.setProperty("keycloak-client-token-provider.client-id", "encourage-conversations-client");
		override.setProperty("keycloak-client-token-provider.permissions.realm-management", "view-users");
		override.setProperty("keycloak-client-token-provider.scope-roles.realm-management", "view-users");
		override.setProperty("keycloak-client-token-provider.bypass-shutdown-and-cache", "true");
		override.setProperty("keycloak-client-token-provider.master-client-secret", "password");

		Path tempDirectory = Files.createTempDirectory("conversation-service");
		Resource[] resources = new PathMatchingResourcePatternResolver().getResources("/config/solr-config-set/conversation/*");
		for (Resource resource : resources) {
			Files.copy(resource.getInputStream(), tempDirectory.resolve(resource.getFilename()));
		}
		override.setProperty("enc-conversations.solr-configset-path", tempDirectory.toString());

		return EncourageeApplication.run("--spring.config.additional-location="
				+ "file:" + environment.getRequiredProperty("docker.ports.file") + ","
				+ "file:" + writeTempPropertiesFile(override));
	}

	public Integer getKeycloakPort() {
		return environment.getRequiredProperty("keycloak.port", Integer.class);
	}

	@PostConstruct
	void configureRestAssured() {
		RestAssured.config = RestAssuredConfig.config()
				.logConfig(logConfig()
						.enableLoggingOfRequestAndResponseIfValidationFails())
				.objectMapperConfig(objectMapperConfig()
						.jackson2ObjectMapperFactory((cls, charset) -> objectMapper()));
		RestAssured.baseURI = "http://localhost:" + servicePort();
		RestAssured.basePath = "/api/v3";
	}
}

