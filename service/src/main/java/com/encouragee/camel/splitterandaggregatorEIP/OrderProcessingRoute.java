package com.encouragee.camel.splitterandaggregatorEIP;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderProcessingRoute extends RouteBuilder {
  
  @Autowired
  private PriceAggregationStrategy priceAggregationStrategy;

  @Override
  public void configure() throws Exception {
    from("direct:fetchProcess")
    .split(body(), priceAggregationStrategy).parallelProcessing()
    .bean(PricingService.class, "calculatePrice")
    .end();
  }
}