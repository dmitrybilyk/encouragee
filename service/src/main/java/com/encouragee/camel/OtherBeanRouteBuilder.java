package com.encouragee.camel;

import com.encouragee.model.camel.MyBean;
import com.encouragee.model.camel.OtherBean;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import static com.encouragee.camel.BeanRouterBuilder.URI_GET_OTHER_BEAN;

@Component
public class OtherBeanRouteBuilder extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from(URI_GET_OTHER_BEAN).routeId("get_other_bean")
                .convertBodyTo(MyBean.class)
                .setBody(exchange -> new OtherBean("other name"));
    }
}
