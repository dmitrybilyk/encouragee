//package app;
//
//import com.encouragee.EncourageeApplication;
//import com.encouragee.model.camel.MyBean;
//import io.restassured.RestAssured;
//import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import static io.restassured.RestAssured.given;
//import static org.hamcrest.Matchers.equalTo;
//
//@ExtendWith(SpringExtension.class)
//@CamelSpringBootTest
//@SpringBootTest(classes = EncourageeApplication.class)
//public class ExternalApiIT {
//    @Value("${server.port}")
//    private int port;
//    @Test
//    void callBasicMethod() {
//        EncourageeApplication.main(new String[0]);
//        RestAssured.port = port;
//        MyBean bean = new MyBean();
//        bean.setName("Name");
//        bean.setId(100);
//        given()
//                .contentType("application/json")
//                .body("{\"name\":\"A name\", \"id\":100}")
//        .when()
//                .post("/camel/api/bean")
//        .then()
//                .body("id",equalTo(101));
//    }
//}
