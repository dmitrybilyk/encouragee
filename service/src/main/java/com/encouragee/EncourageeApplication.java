package com.encouragee;

import com.encouragee.messaging.Receiver;
import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.encouragee.controller", "com.encouragee.messaging", "com.encouragee.camel",
        "com.encouragee.rabbit.controller"})
@EnableSolrRepositories(
        basePackages = "com.encouragee.repository.solr",
        namedQueriesLocation = "classpath:solr-named-queries.properties")
public class EncourageeApplication {
    public static void main(String[] args) {
        SpringApplication.run(EncourageeApplication.class, args);
    }

    @Value("${app.api.path}")
    String contextPath;
    public static final String queueName = "spring-boot";
    public static final String topicExchangeName = "spring-boot-exchange";

    @RabbitListener(queues = queueName)
    public void listen(String in) {
        System.out.println("Message read from myQueue : " + in);
    }

    @Bean
    ServletRegistrationBean servletRegistrationBean() {
        ServletRegistrationBean servlet = new ServletRegistrationBean
                (new CamelHttpTransportServlet(), contextPath+"/*");
        servlet.setName("CamelServlet");
        return servlet;
    }

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
