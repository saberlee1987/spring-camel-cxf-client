package com.saber.spring_camel_cxf_client.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RouteDefinition extends RouteBuilder {

    @Value(value = "${service.api.base-path}")
    private String apiBasePath;
    @Value(value = "${service.api.swagger-path}")
    private String swaggerPath;
    @Value(value = "${service.log.pretty-print}")
    private String prettyPrint;

    @Override
    public void configure() throws Exception {
        restConfiguration()
                .contextPath(apiBasePath)
                .apiContextPath(swaggerPath)
                .enableCORS(true)
                .apiProperty("api.title", "spring camel cxf client")
                .apiProperty("api.version", "version 1.0")
                .apiProperty("cors", "true")
                .component("servlet")
                .dataFormatProperty("prettyPrint", prettyPrint)
                .bindingMode(RestBindingMode.json);
    }
}
