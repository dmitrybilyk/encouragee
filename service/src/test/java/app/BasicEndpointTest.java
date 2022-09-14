//package app;
//
//import com.encouragee.EncourageeApplication;
//import com.encouragee.model.camel.MyBean;
//import org.apache.camel.ProducerTemplate;
////import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
////import org.apache.camel.test.spring.junit5.MockEndpoints;
////import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.testng.annotations.Test;
//
//@SpringBootTest(classes = EncourageeApplication.class)
//@CamelSpringBootTest
//@MockEndpoints(BasicEndpointTest.DIRECT_REMOTE_SERVICE)
//public class BasicEndpointTest {
//    public static final String DIRECT_REMOTE_SERVICE = "direct:remoteService";
//    @Autowired
//    private ProducerTemplate template;
//
//    @Test
//    void whenSentBody_resultIs_AsExpected() {
//        MyBean bean = new MyBean();
//        bean.setId(10);
//        bean.setName("name");
//        template.sendBody(DIRECT_REMOTE_SERVICE, bean);
//        System.out.println(bean);
//
//    }
//}
