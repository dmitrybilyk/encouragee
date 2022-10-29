package com.encouragee.solr;

import com.encouragee.ConversationProperties;
import com.encouragee.EncourageeApplication;
import com.encouragee.camel.clientSearch.repository.ConversationSearchRepository;
import com.encouragee.camel.clientSearch.repository.DefaultSolrRepository;
import com.encouragee.camel.clientSearch.repository.EncourageSolrTemplate;
import com.zoomint.encourage.common.camel.EncourageCamelApplication;
import com.zoomint.encourage.common.spring.BuildProperties;
//import com.zoomint.encourage.common.spring.WebSecurityConfig;
import com.zoomint.encourage.model.search.ClientConversationSearchConverter;
import com.zoomint.keycloak.KeycloakClientConfiguration;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.solr.SolrAutoConfiguration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.*;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;

import static org.springframework.amqp.core.BindingBuilder.bind;

@Configuration
@Profile("solr")
//@Import({WebSecurityConfig.class})
//@EnableAutoConfiguration(exclude = {SolrAutoConfiguration.class, KeycloakClientConfiguration.class})
//@EncourageCamelApplication
@EnableSolrRepositories(repositoryBaseClass = DefaultSolrRepository.class, basePackageClasses = ConversationSearchRepository.class)
//@EnableSolrRepositories(
//        basePackages = "com.encouragee.repository.solr",
//        namedQueriesLocation = "classpath:solr-named-queries.properties")
@ComponentScan(basePackageClasses = {EncourageeApplication.class, BuildProperties.class})
@PropertySource("build.properties")
public class SolrConfiguration {

//    @Bean
//    ServletRegistrationBean servletRegistrationBean() {
//        ServletRegistrationBean servlet = new ServletRegistrationBean
//                (new CamelHttpTransportServlet(), contextPath+"/*");
//        servlet.setName("CamelServlet");
//        return servlet;
//    }

    @Bean
//    @Profile("solr")
    public SolrClient solrClient() {
        return new HttpSolrClient.Builder("http://localhost:8983/solr").build();
//        return new HttpSolrClient.Builder("http://localhost:8983/solr/products").build();
    }

    @Bean
//    @Profile("solr")
    public SolrTemplate solrTemplate(SolrClient client) throws Exception {
        return new SolrTemplate(client);
    }

    @Bean
//    @Profile("solr")
    Queue searchErrorQueue(ConversationProperties properties) {
        return QueueBuilder
                .durable(properties.getIndexErrorQueue())
                .withArgument("x-max-length", properties.getIndexErrorQueueSize())
                .withArgument("x-overflow", "reject-publish")
                .build();
    }

    @Bean
//    @Profile("solr")
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
//    @Profile("solr")
    Binding bindSearchIndexWithReplyQueue(Queue searchIndexQueue) {
        return bind(searchIndexQueue)
                .to(new DirectExchange("amq.direct"))
                .withQueueName();
    }

    @Bean
//    @Profile("solr")
    Binding bindIndexErrorQueue(Queue searchErrorQueue) {
        return bind(searchErrorQueue)
                .to(new DirectExchange("amq.direct"))
                .withQueueName();
    }

    @Bean
//    @Profile("solr")
    EncourageSolrTemplate conversationSearchSolrTemplate(SolrClient solrClient) {
        return new EncourageSolrTemplate(solrClient);
    }

    @Bean
//    @Profile("solr")
    public ClientConversationSearchConverter clientSearchConverter() {
        return new ClientConversationSearchConverter();
    }
}
