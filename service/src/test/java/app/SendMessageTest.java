package app;

import com.encouragee.EncourageeApplication;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.MockEndpoints;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = EncourageeApplication.class)
@CamelSpringBootTest
@MockEndpoints(BasicEndpointTest.DIRECT_REMOTE_SERVICE)
public class SendMessageTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;

//    @Test
//    void test() throws InterruptedException {
//        for (int i=0;i<1000;i++) {
//            rabbitTemplate.convertAndSend(EncourageeApplication.topicExchangeName, "com.encouragee.messaging.test", "Hello World! "+i);
//        }
//        Thread.sleep(100);
//    }
}
