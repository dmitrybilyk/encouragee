package com.encouragee.camel.splitterandaggregatorEIP;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

/**
 * @author pratikdas
 *
 */
@Service
public class OrderService {
	
	public double getPrice(final String productName) {
		
		return 2.4;
		
	}
	
	public List<OrderLine> generateOrder() {
		System.out.println("generating orders");
		
		List<OrderLine> orderLines = new ArrayList<OrderLine>();
		
		OrderLine orderLine = new OrderLine();
		orderLine.setProduct(new Product("Television", "Electronics"));
		
		orderLines.add(orderLine);
		
		orderLine = new OrderLine();
		orderLine.setProduct(new Product("Washing Machine", "Household"));
		
		orderLines.add(orderLine);
		return orderLines;
	}
	
	public List<Order> getOrders() {
		System.out.println("fetching orders");
		
		List<Order> orders = new ArrayList<Order>();
		
		Order order = new Order();
		order.setOrderNo("ORD-001");
		order.setOrderDate("10/02/2021");
		
		orders.add(order);
		return orders;
	}
	

}