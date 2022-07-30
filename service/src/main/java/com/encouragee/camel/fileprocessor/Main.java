package com.encouragee.camel.fileprocessor;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class Main {
    private static final long DURATION_MILIS = 5000;
    private static final String SOURCE_FOLDER = "/home/nikita/IdeaProjects/encouragee/service/src/test/source-folder";
    private static final String DESTINATION_FOLDER
            = "/home/nikita/IdeaProjects/encouragee/service/src/test/destination-folder";
    public static void main(String[] args) throws Exception {

        CamelContext camelContext = new DefaultCamelContext();
        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("file://" + SOURCE_FOLDER + "?delete=true").process(
                        new FileProcessor()).to("file://" + DESTINATION_FOLDER);
            }
        });
        camelContext.start();
        Thread.sleep(DURATION_MILIS);
        camelContext.stop();
    }
}
