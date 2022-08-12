package com.encouragee;

import com.encouragee.messaging.Receiver;
//import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import com.zoomint.encourage.common.camel.EncourageCamelApplication;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.orm.jpa.HibernateMetricsAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;

@EncourageCamelApplication(exclude = {HibernateMetricsAutoConfiguration.class})
@ComponentScan(basePackages = {"com.encouragee.controller","com.encouragee.messaging","com.encouragee.camel"})
@EnableSolrRepositories(
        basePackages = "com.encouragee.repository.solr",
        namedQueriesLocation = "classpath:solr-named-queries.properties")
public class EncourageeApplication {

    private final ApiProperties properties;

    public EncourageeApplication(ApiProperties properties) {
        this.properties = properties;
    }

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
//    public static void main(String[] args) {
//        SpringApplication.run(EncourageeApplication.class, args);
//    }

    @Value("${app.api.path}")
    String contextPath;
    public static final String queueName = "spring-boot";
    public static final String topicExchangeName = "spring-boot-exchange";



    @Bean
    Queue queue() {
        return new Queue(queueName, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("com.encouragee.messaging.#");
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter){
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver,"receiveMessage");
    }

    @Bean
    public SolrClient solrClient() {
        return new HttpSolrClient.Builder("http://localhost:8983/solr").build();
//        return new HttpSolrClient.Builder("http://localhost:8983/solr/products").build();
    }

    @Bean
    public SolrTemplate solrTemplate(SolrClient client) throws Exception {
        return new SolrTemplate(client);
    }

//    amqp
    @Bean
    public Queue myQueue() {
        return new Queue("myQueue", false);
    }

}
