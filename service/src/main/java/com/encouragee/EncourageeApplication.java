package com.encouragee;

//import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import com.encouragee.camel.clientSearch.repository.ConversationSearchRepository;
import com.encouragee.camel.clientSearch.repository.DefaultSolrRepository;
import com.encouragee.camel.clientSearch.repository.EncourageSolrTemplate;
//import com.encouragee.kafka.KafkaConsumerConfig;
//import com.encouragee.kafka.KafkaProducerConfig;
//import com.encouragee.kafka.KafkaTopicConfig;
import com.encouragee.rabbit.configuration.RabbitMQConfiguration;
import com.zoomint.encourage.common.camel.EncourageCamelApplication;
import com.zoomint.encourage.common.spring.BuildProperties;
import com.zoomint.encourage.common.spring.WebSecurityConfig;
import com.zoomint.encourage.model.search.ClientConversationSearchConverter;
import com.zoomint.keycloak.KeycloakClientConfiguration;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.data.solr.SolrRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.solr.SolrAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;
//import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
//import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
//import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
//import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.data.solr.repository.support.SolrRepositoryFactory;
import org.springframework.web.client.RestTemplate;

import static org.springframework.amqp.core.BindingBuilder.bind;

//@SpringBootApplication
//@ComponentScan(basePackages = {"com.encouragee.controller", "com.encouragee.messaging", "com.encouragee.camel",
//        "com.encouragee.rabbit.controller", "com.encouragee.camel", "com.encouragee.kafka.controller"})
//@Import({KafkaTopicConfig.class,
//        KafkaConsumerConfig.class, KafkaProducerConfig.class})
//@EnableSolrRepositories(
//        basePackages = "com.encouragee.repository.solr",
//        namedQueriesLocation = "classpath:solr-named-queries.properties")

//@EncourageCamelApplication
@SpringBootApplication(exclude = {SolrAutoConfiguration.class, KeycloakClientConfiguration.class,
        SolrRepositoriesAutoConfiguration.class})
@ComponentScan(basePackageClasses = {EncourageeApplication.class, BuildProperties.class})
@PropertySource("build.properties")
@Import({RabbitAutoConfiguration.class,
//        WebSecurityConfig.class,
        RabbitMQConfiguration.class,
//        KafkaTopicConfig.class,
//        KafkaConsumerConfig.class,
//        KafkaProducerConfig.class
})
//@EnableSolrRepositories(repositoryBaseClass = DefaultSolrRepository.class, basePackageClasses = ConversationSearchRepository.class)
//@EnableAutoConfiguration(exclude = {SolrAutoConfiguration.class, KeycloakClientConfiguration.class})
public class EncourageeApplication {

//    public static void main(String[] args) {
//        SpringApplication.run(EncourageeApplication.class, args);
//    }

    public static void main(String... args) {
        run(args);
    }

    public static ConfigurableApplicationContext run(String... args) {
        return new SpringApplicationBuilder()
                .sources(EncourageeApplication.class)
                .bannerMode(Banner.Mode.OFF)
                .build()
                .run(args);
    }

//    @Value("${app.api.path}")
//    String contextPath;

//    public static final String topicConversationExchange = "conversation-exchange";

//    @Bean
//    ServletRegistrationBean servletRegistrationBean() {
//        ServletRegistrationBean servlet = new ServletRegistrationBean
//                (new CamelHttpTransportServlet(), contextPath+"/*");
//        servlet.setName("CamelServlet");
//        return servlet;
//    }

    @Bean
    @Profile("solr")
    public SolrClient solrClient() {
        return new HttpSolrClient.Builder("http://localhost:8983/solr").build();
//        return new HttpSolrClient.Builder("http://localhost:8983/solr/products").build();
    }

    @Bean
    @Profile("solr")
    public SolrTemplate solrTemplate(SolrClient client) throws Exception {
        return new SolrTemplate(client);
    }

    @Bean
    @Profile("solr")
    Queue searchErrorQueue(ConversationProperties properties) {
        return QueueBuilder
                .durable(properties.getIndexErrorQueue())
                .withArgument("x-max-length", properties.getIndexErrorQueueSize())
                .withArgument("x-overflow", "reject-publish")
                .build();
    }

    @Bean
    @Profile("solr")
    Queue searchIndexQueue(ConversationProperties properties) {
        return QueueBuilder
                .durable(properties.getIndexTaskQueue())
                .withArgument("x-max-length", properties.getIndexTaskQueueSize())
                .withArgument("x-overflow", "reject-publish")
                .build();
    }

    /**
     * Auto-created by {@link RabbitAdmin}.
     * Note: cannot use default exchange "" which auto-binds queues with their names due to the following issues:
     * https://issues.apache.org/jira/browse/CAMEL-9561
     * https://issues.apache.org/jira/browse/CAMEL-12471
     */
    @Bean
    @Profile("solr")
    Binding bindSearchIndexWithReplyQueue(Queue searchIndexQueue) {
        return bind(searchIndexQueue)
                .to(new DirectExchange("amq.direct"))
                .withQueueName();
    }

    @Bean
    @Profile("solr")
    Binding bindIndexErrorQueue(Queue searchErrorQueue) {
        return bind(searchErrorQueue)
                .to(new DirectExchange("amq.direct"))
                .withQueueName();
    }

//    @Bean
//    public WebClient webClient(ClientRegistrationRepository clientRegistrationRepository,
//                               OAuth2AuthorizedClientRepository oAuth2AuthorizedClientRepository) {
//        ServletOAuth2AuthorizedClientExchangeFilterFunction oAuth2AuthorizedClientExchangeFilterFunction =
//                new ServletOAuth2AuthorizedClientExchangeFilterFunction(clientRegistrationRepository,
//                        oAuth2AuthorizedClientRepository);
//        oAuth2AuthorizedClientExchangeFilterFunction.setDefaultOAuth2AuthorizedClient(true);
//
//        return WebClient.builder().apply(oAuth2AuthorizedClientExchangeFilterFunction.oauth2Configuration()).build();
//    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @Profile("solr")
    EncourageSolrTemplate conversationSearchSolrTemplate(SolrClient solrClient) {
        return new EncourageSolrTemplate(solrClient);
    }

    @Bean
    @Profile("solr")
    public ClientConversationSearchConverter clientSearchConverter() {
        return new ClientConversationSearchConverter();
    }

//    Kafka
//    @KafkaListener(topics = "topicName", groupId = "foo")
//    public void listenGroupFoo(String message) {
//        System.out.println("Received Message in group foo: " + message);
//    }

}
