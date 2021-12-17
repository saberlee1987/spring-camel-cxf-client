package com.saber.spring_camel_cxf_client.routes;

import com.saber.spring_camel_cxf_client.dto.PersonResponse;
import com.saber.spring_camel_cxf_client.dto.ServiceErrorResponse;
import org.apache.camel.Exchange;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class FindAllPersonsRoute extends AbstractRestRouteBuilder {

    @Value(value = "${service.person-api.url}")
    private String url;
    @Value(value = "${service.person-api.port}")
    private String port;
    @Value(value = "${service.person-api.baseUrl}")
    private String baseUrl;
    @Value(value = "${service.person-api.findAllPerson}")
    private String findAllPersonUrl;

    @Override
    public void configure() throws Exception {
        super.configure();
        rest("/person")
                .get("/findAll")
                .description("get All Person")
                .produces(MediaType.APPLICATION_JSON_VALUE)
                .responseMessage().code(200).responseModel(PersonResponse.class).endResponseMessage()
                .responseMessage().code(400).responseModel(ServiceErrorResponse.class).endResponseMessage()
                .responseMessage().code(406).responseModel(ServiceErrorResponse.class).endResponseMessage()
                .responseMessage().code(500).responseModel(ServiceErrorResponse.class).endResponseMessage()
                .responseMessage().code(504).responseModel(ServiceErrorResponse.class).endResponseMessage()
                .bindingMode(RestBindingMode.json)
                .enableCORS(true)
                .route()
                .routeId(Routes.FIND_ALL_PERSON_ROUTE)
                .threads().threadName(Routes.FIND_ALL_PERSON_ROUTE)
                .routeGroup(Routes.FIND_ALL_PERSON_ROUTE_GROUP)
                .to("direct:find-all-person-route-gateway");

        from("direct:find-all-person-route-gateway")
                .routeId(Routes.FIND_ALL_PERSON_ROUTE_GATEWAY)
                .routeGroup(Routes.FIND_ALL_PERSON_ROUTE_GROUP)
                .log("Request Routes.FIND_ALL_PERSON_ROUTE go to this url " + url + ":" + port + baseUrl + findAllPersonUrl)
                .to(url + ":" + port + baseUrl + findAllPersonUrl + "?bridgeEndpoint=true&sslContextParameters=#sslContextParameters")
                .convertBodyTo(String.class)
                .log("Response for Routes.FIND_ALL_PERSON_ROUTE ===> ${in.body} ")
                .unmarshal().json(JsonLibrary.Jackson,PersonResponse.class)
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200));

    }
}
