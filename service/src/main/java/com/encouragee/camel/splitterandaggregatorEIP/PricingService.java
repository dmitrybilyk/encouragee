package com.encouragee.camel.splitterandaggregatorEIP;

import org.springframework.stereotype.Service;

@Service
public class PricingService {
  
  public OrderLine calculatePrice(final OrderLine orderLine ) {
    String category = orderLine.getProduct().getProductCategory();
    if("Electronics".equalsIgnoreCase(category))
       orderLine.setPrice(300.0);
//...
//...
    return orderLine;
    
  }

}