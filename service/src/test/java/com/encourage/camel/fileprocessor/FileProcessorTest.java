package com.encourage.camel.fileprocessor;

import com.encouragee.camel.fileprocessor.FileProcessor;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.testng.annotations.Test;
//import org.junit.jupiter.api.Test;

public class FileProcessorTest {
    private static final long DURATION_MILIS = 10000;
    private static final String SOURCE_FOLDER = "src/test/source-folder";
    private static final String DESTINATION_FOLDER
            = "src/test/destination-folder";

    @Test
    public void moveFolderContentJavaDSLTest() throws Exception {
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
