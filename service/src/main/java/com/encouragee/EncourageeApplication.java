package com.encouragee;

import com.encouragee.messaging.Receiver;
import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;

import static org.springframework.amqp.core.BindingBuilder.bind;

@SpringBootApplication
@ComponentScan(basePackages = {"com.encouragee.controller", "com.encouragee.messaging", "com.encouragee.camel",
        "com.encouragee.rabbit.controller"})
@EnableSolrRepositories(
        basePackages = "com.encouragee.repository.solr",
        namedQueriesLocation = "classpath:solr-named-queries.properties")
public class EncourageeApplication {
    private static final boolean NON_DURABLE = false;

    public final static String FANOUT_QUEUE_1_NAME = "com.baeldung.spring-amqp-simple.fanout.queue1";
    public final static String FANOUT_QUEUE_2_NAME = "com.baeldung.spring-amqp-simple.fanout.queue2";
    public final static String FANOUT_EXCHANGE_NAME = "com.baeldung.spring-amqp-simple.fanout.exchange";

    public final static String TOPIC_QUEUE_1_NAME = "com.baeldung.spring-amqp-simple.topic.queue1";
    public final static String TOPIC_QUEUE_2_NAME = "com.baeldung.spring-amqp-simple.topic.queue2";
    public final static String TOPIC_QUEUE_3_NAME = "com.baeldung.spring-amqp-simple.topic.queue3";
    public final static String TOPIC_EXCHANGE_NAME = "com.baeldung.spring-amqp-simple.topic.exchange";
    public static final String BINDING_PATTERN_IMPORTANT = "*.important.*";
    public static final String BINDING_PATTERN_ERROR = "#.error";


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
        return bind(queue).to(exchange).with("com.encouragee.messaging.#");
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



//    fanout ___________________________________________________________________________________________________________
@Bean
public Declarables fanoutBindings() {
    Queue fanoutQueue1 = new Queue(FANOUT_QUEUE_1_NAME, NON_DURABLE);
    Queue fanoutQueue2 = new Queue(FANOUT_QUEUE_2_NAME, NON_DURABLE);

    FanoutExchange fanoutExchange = new FanoutExchange(FANOUT_EXCHANGE_NAME, NON_DURABLE, false);

    return new Declarables(fanoutQueue1, fanoutQueue2, fanoutExchange,
            BindingBuilder
            .bind(fanoutQueue1)
            .to(fanoutExchange),
            BindingBuilder
            .bind(fanoutQueue2)
            .to(fanoutExchange));
}

    @Bean
    public Declarables topicBindings() {
        Queue topicQueue1 = new Queue(TOPIC_QUEUE_1_NAME, NON_DURABLE);
        Queue topicQueue2 = new Queue(TOPIC_QUEUE_2_NAME, NON_DURABLE);
        Queue topicQueue3 = new Queue(TOPIC_QUEUE_3_NAME, NON_DURABLE);

        TopicExchange topicExchange = new TopicExchange(TOPIC_EXCHANGE_NAME, NON_DURABLE, false);

        return new Declarables(topicQueue1, topicQueue2, topicQueue3, topicExchange,
                BindingBuilder
                .bind(topicQueue1)
                .to(topicExchange)
                .with(BINDING_PATTERN_IMPORTANT),
                BindingBuilder
                .bind(topicQueue3)
                .to(topicExchange)
                .with(BINDING_PATTERN_IMPORTANT),
                BindingBuilder
                .bind(topicQueue2)
                .to(topicExchange)
                .with(BINDING_PATTERN_ERROR));
    }

    @RabbitListener(queues = { FANOUT_QUEUE_1_NAME })
    public void receiveMessageFromFanout1(String message) {
        System.out.println("Received fanout 1 message: " + message);
    }

    @RabbitListener(queues = { FANOUT_QUEUE_2_NAME })
    public void receiveMessageFromFanout2(String message) {
        System.out.println("Received fanout 2 message: " + message);
    }

    @RabbitListener(queues = { TOPIC_QUEUE_1_NAME })
    public void receiveMessageFromTopic1(String message) {
        System.out.println("Received topic 1 (" + BINDING_PATTERN_IMPORTANT + ") message: " + message);
    }

    @RabbitListener(queues = { TOPIC_QUEUE_2_NAME })
    public void receiveMessageFromTopic2(String message) {
        System.out.println("Received topic 2 (" + BINDING_PATTERN_ERROR + ") message: " + message);
    }
    @RabbitListener(queues = { TOPIC_QUEUE_3_NAME })
    public void receiveMessageFromTopic3(String message) {
        System.out.println("Received topic 3 (" + BINDING_PATTERN_IMPORTANT + ") message: " + message);
    }

//    ____________________________________________________________________________________________________________________

}
