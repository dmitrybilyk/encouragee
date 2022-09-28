package com.encouragee;

//import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import com.encouragee.camel.clientSearch.repository.ConversationSearchRepository;
import com.encouragee.camel.clientSearch.repository.DefaultSolrRepository;
import com.encouragee.camel.clientSearch.repository.EncourageSolrTemplate;
import com.encouragee.kafka.KafkaConsumerConfig;
import com.encouragee.kafka.KafkaProducerConfig;
import com.encouragee.kafka.KafkaTopicConfig;
import com.encouragee.rabbit.configuration.RabbitMQConfiguration;
import com.zoomint.encourage.common.camel.EncourageCamelApplication;
import com.zoomint.encourage.common.spring.BuildProperties;
import com.zoomint.encourage.common.spring.WebSecurityConfig;
import com.zoomint.encourage.model.search.ClientConversationSearchConverter;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;
//import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
//import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
//import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.client.RestTemplate;

//@SpringBootApplication
//@ComponentScan(basePackages = {"com.encouragee.controller", "com.encouragee.messaging", "com.encouragee.camel",
//        "com.encouragee.rabbit.controller", "com.encouragee.camel", "com.encouragee.kafka.controller"})
//@Import({KafkaTopicConfig.class,
//        KafkaConsumerConfig.class, KafkaProducerConfig.class})
//@EnableSolrRepositories(
//        basePackages = "com.encouragee.repository.solr",
//        namedQueriesLocation = "classpath:solr-named-queries.properties")

@EncourageCamelApplication
@ComponentScan(basePackageClasses = {EncourageeApplication.class, BuildProperties.class})
@PropertySource("build.properties")
@Import({RabbitAutoConfiguration.class,
        WebSecurityConfig.class,
        RabbitMQConfiguration.class,
        KafkaTopicConfig.class,
        KafkaConsumerConfig.class,
        KafkaProducerConfig.class})
@EnableSolrRepositories(repositoryBaseClass = DefaultSolrRepository.class, basePackageClasses = ConversationSearchRepository.class)
public class EncourageeApplication {

    public static void main(String[] args) {
        SpringApplication.run(EncourageeApplication.class, args);
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
    public SolrClient solrClient() {
        return new HttpSolrClient.Builder("http://localhost:8983/solr").build();
//        return new HttpSolrClient.Builder("http://localhost:8983/solr/products").build();
    }

    @Bean
    public SolrTemplate solrTemplate(SolrClient client) throws Exception {
        return new SolrTemplate(client);
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
    EncourageSolrTemplate conversationSearchSolrTemplate(SolrClient solrClient) {
        return new EncourageSolrTemplate(solrClient);
    }

    @Bean
    public ClientConversationSearchConverter clientSearchConverter() {
        return new ClientConversationSearchConverter();
    }

//    Kafka
    @KafkaListener(topics = "topicName", groupId = "foo")
    public void listenGroupFoo(String message) {
        System.out.println("Received Message in group foo: " + message);
    }

}
