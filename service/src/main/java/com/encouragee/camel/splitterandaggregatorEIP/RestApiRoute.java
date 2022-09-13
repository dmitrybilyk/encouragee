package com.encouragee.camel.splitterandaggregatorEIP;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class RestApiRoute  extends RouteBuilder {

  @Autowired
  private Environment env;

  @Override
  public void configure() throws Exception {

    restConfiguration()
            .contextPath("/ecommapp")
            .apiContextPath("/api-doc")
            .apiProperty("api.title", "REST API for processing Order")
            .apiProperty("api.version", "1.0")
            .apiProperty("cors", "true")
            .apiContextRouteId("doc-api")
            .port(env.getProperty("server.port", "8080"))
            .bindingMode(RestBindingMode.json);

    rest("/order/")
            .get("/process").description("Process order")
            .route().routeId("orders-api")
            .bean(OrderService.class, "generateOrder")
            .to("direct:fetchProcess")
            .endRest();

  }
}