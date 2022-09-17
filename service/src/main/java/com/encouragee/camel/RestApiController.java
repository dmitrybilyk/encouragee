//package com.encouragee.camel;
//
//import com.encouragee.model.camel.Conversation;
//import com.encouragee.model.camel.MyBean;
//import org.apache.camel.Exchange;
//import org.apache.camel.builder.RouteBuilder;
//import org.apache.camel.model.rest.RestBindingMode;
//import org.apache.camel.model.rest.RestParamType;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import javax.ws.rs.core.MediaType;
//
//import static com.encouragee.camel.BeanRouterBuilder.URI_SEARCH_BEAN;
//import static java.net.HttpURLConnection.HTTP_OK;
//import static org.apache.camel.model.rest.RestParamType.*;
//
//@Component
//public class RestApiController extends RouteBuilder {
//    private static final String APPLICATION_JSON_UTF_8 = "application/json;charset=UTF-8";
////    @Value("${app.api.path}")
////    private String contextPath;
//    @Value("${server.port}")
//    String serverPort;
//
//    @Override
//    public void configure() throws Exception {
//        //CamelContext context = new DefaultCamelContext();
//
//        restConfiguration().contextPath("camel") //
//                .port(serverPort)
//                .enableCORS(true)
//                .apiContextPath("/api-doc")
//                .apiProperty("api.title", "Test REST API")
//                .apiProperty("api.version", "v1")
//                .apiProperty("cors", "true") // cross-site
//                .apiContextRouteId("doc-api")
//                .component("servlet")
//                .bindingMode(RestBindingMode.json)
//                .dataFormatProperty("prettyPrint", "true");
//
////        rest("/api/").description("Teste REST Service")
////                .id("api-route")
////                .post("/bean")
////                .param().type(query).name("myParam").required(false).
////                dataType("string").endParam()
////                .param().type(body).name("body").description("The search object").endParam()
////                .produces(MediaType.APPLICATION_JSON)
////                .consumes(MediaType.APPLICATION_JSON).type(MyBean.class)
//////                .get("/hello/{place}")
////                .bindingMode(RestBindingMode.auto)
////                .type(MyBean.class)
////                .enableCORS(true)
////                //.outType(OutBean.class)
//
////                .to("direct:remoteService");
////                    .to(URI_SEARCH_BEAN);
//
//        from("direct:remoteService").routeId("direct-route")
//                .tracing()
//                .log(">>> ${body.id}")
//                .log(">>> ${body.name}")
//                //.transform().simple("blue ${in.body.name}")
//                .process(exchange -> {
//                    MyBean bodyIn = (MyBean) exchange.getIn()
//                            .getBody();
//                    bodyIn.setId(bodyIn.getId()+1);
//                    bodyIn.setName("Hello "+bodyIn.getName());
//                    //ExampleServices.example(bodyIn);
//
//                    exchange.getIn()
//                            .setBody(bodyIn);
//
//                })
//                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(201));
//
//
//
//
//        from("direct:logService").routeId("log-route")
//                .tracing()
//                .log(">>> ${body}")
////                .log(">>> ${body.name}")
////                //.transform().simple("blue ${in.body.name}")
////                .process(exchange -> {
////                    MyBean bodyIn = (MyBean) exchange.getIn()
////                            .getBody();
////                    bodyIn.setId(bodyIn.getId()+1);
////                    bodyIn.setName("Hello "+bodyIn.getName());
////                    //ExampleServices.example(bodyIn);
////
////                    exchange.getIn()
////                            .setBody(bodyIn);
////
////                })
//                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(201));
//
////        rest("/api/v3/conversations")
////                .id("conversations-route")
////                .get("/{conversationId}")
////                .description("Return conversation with the specified ID")
////
////                .param().type(path).name("conversationId").dataType("string").description("ID of the conversation").endParam()
////
////                .responseMessage().code(HTTP_OK).message("The conversation").endResponseMessage()
////                .produces(APPLICATION_JSON_UTF_8).outType(Conversation.class)
////                .to("direct:logService");
//    }
//}
