package com.encouragee;

//import org.apache.camel.component.servlet.CamelHttpTransportServlet;
//import com.encouragee.kafka.KafkaConsumerConfig;
//import com.encouragee.kafka.KafkaProducerConfig;
//import com.encouragee.kafka.KafkaTopicConfig;
import org.springframework.boot.Banner;
        import org.springframework.boot.autoconfigure.SpringBootApplication;
        import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.*;
//import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
//import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
//import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
//import org.springframework.kafka.annotation.KafkaListener;
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
@SpringBootApplication
//@ComponentScan(basePackageClasses = {EncourageeApplication.class, BuildProperties.class})
//@PropertySource("build.properties")
//rabbit profile
//@Import({RabbitAutoConfiguration.class,
////        WebSecurityConfig.class,
//        RabbitMQConfiguration.class,
////        KafkaTopicConfig.class,
////        KafkaConsumerConfig.class,
////        KafkaProducerConfig.class
//})
//rest profile - no import is needed
//@EnableSolrRepositories(repositoryBaseClass = DefaultSolrRepository.class, basePackageClasses = ConversationSearchRepository.class)
//@EnableAutoConfiguration(exclude = {SolrAutoConfiguration.class, KeycloakClientConfiguration.class})
public class EncourageeApplication {

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

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
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

//    Kafka
//    @KafkaListener(topics = "topicName", groupId = "foo")
//    public void listenGroupFoo(String message) {
//        System.out.println("Received Message in group foo: " + message);
//    }

}
