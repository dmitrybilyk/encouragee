package com.encouragee.camel;

import com.encouragee.model.camel.MyBean;
import com.encouragee.model.camel.OtherBean;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import static org.apache.camel.util.toolbox.AggregationStrategies.flexible;

//import static org.apache.camel.builder.AggregationStrategies.flexible;

@Component
public class BeanRouterBuilder extends RouteBuilder {
    public static final String URI_SEARCH_BEAN = "direct:beanSearchClient";
    public static final String SOME_PROPERTY = "SOME_PROPERTY";
    public static final String URI_GET_OTHER_BEAN = "direct:getOtherBean";
    public static final String OTHER_BEAN = "OTHER_BEAN";
    private final BeanConvertor beanConvertor;

    public BeanRouterBuilder(BeanConvertor beanConvertor) {
        this.beanConvertor = beanConvertor;
    }


    @Override
    public void configure() throws Exception {
        from(URI_SEARCH_BEAN).routeId("beanSearchClient")
                .convertBodyTo(MyBean.class)
                .process().body(MyBean.class, beanConvertor::updateBeanName)
                .setProperty(SOME_PROPERTY).body(MyBean.class, this::getSomeProperty)
//                .multicast()
//                .parallelProcessing()
//                .aggregationStrategy(useOriginal())
//                .stopOnException()
//                .to(URI_ENRICH_PERMISSION_FILTER, EnrichRouteBuilder.URI_ENRICH_SEARCH)
//                .end()
//                .setHeader(USER_ID, simple("${body?.onBehalfOf?.userId}"))
//                .enrich(URI_GET_SAVED_FILTERS, flexible().storeInProperty(USERS_ASSIGNED_FILTERS))
//                .setBody(this::findConversations)
//                .enrichWith(URI_LOOKUP_EVENTS).exchange(this::getCombinedResults)
//                .to(EnrichRouteBuilder.URI_ENRICH_CONVERSATIONS)
                .enrich(URI_GET_OTHER_BEAN, flexible().storeInProperty(OTHER_BEAN))
                .setBody(this::findBean)
                .to("direct:remoteService");
        ;
    }

    private MyBean findBean(Exchange exchange) {
        log.info("prop is used now " + exchange.getProperty(SOME_PROPERTY, String.class));
        log.info("other beans name " + exchange.getProperty(OTHER_BEAN, OtherBean.class).getOtherName());
        return (MyBean) exchange.getIn().getBody();
    }

    private String getSomeProperty(MyBean myBean) {
        return myBean.getName() + " from bean to property";
    }
}
