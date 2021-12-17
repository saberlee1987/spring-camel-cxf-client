package com.saber.spring_camel_cxf_client.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import java.util.ArrayList;
import java.util.List;
@Configuration
@EnableSwagger2
@Primary
public class SwaggerConfig implements SwaggerResourcesProvider {
    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> swaggers = new ArrayList<>();
        SwaggerResource resource =new SwaggerResource();
        resource.setName("camel-client");
        resource.setUrl("/services/camel-client/v2/api-docs");
        resource.setSwaggerVersion("version1.2");
        swaggers.add(resource);
        return swaggers;
    }
}
