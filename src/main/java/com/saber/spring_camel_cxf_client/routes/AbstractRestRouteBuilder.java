package com.saber.spring_camel_cxf_client.routes;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saber.spring_camel_cxf_client.dto.ServiceErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.base.HttpOperationFailedException;
import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import java.net.SocketException;
import java.net.SocketTimeoutException;

@Slf4j
public class AbstractRestRouteBuilder extends RouteBuilder {
    @Autowired
    private ObjectMapper mapper;

    @Override
    public void configure() throws Exception {


        onException(ConnectTimeoutException.class)
                .maximumRedeliveries(0)
                .handled(true)
                .process(exchange -> {
                    ConnectTimeoutException exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT,
                            ConnectTimeoutException.class);
                    String errorBody = exception.getLocalizedMessage();
                    int statusCode = HttpStatus.GATEWAY_TIMEOUT.value();
                    ServiceErrorResponse errorResponse = new ServiceErrorResponse();
                    errorResponse.setCode(2);
                    errorResponse.setMessage("خطای سرویس camel-client");
                    errorResponse.setOriginalMessage(String.format("{\"code\":%d,\"text\":\"%s\"}",
                            statusCode, errorBody));

                    log.error("Error timeout-exception  ====> {}", mapper.writeValueAsString(errorResponse));
                    exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE,statusCode);
                    exchange.getIn().setBody(errorResponse);

                })
                .removeHeader(Headers.NationalCode);

        onException(SocketTimeoutException.class)
                .maximumRedeliveries(0)
                .handled(true)
                .process(exchange -> {
                    SocketTimeoutException exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT,
                            SocketTimeoutException.class);
                    String errorBody = exception.getLocalizedMessage();
                    int statusCode = HttpStatus.GATEWAY_TIMEOUT.value();
                    ServiceErrorResponse errorResponse = new ServiceErrorResponse();
                    errorResponse.setCode(2);
                    errorResponse.setMessage("خطای سرویس camel-client");
                    errorResponse.setOriginalMessage(String.format("{\"code\":%d,\"text\":\"%s\"}",
                            statusCode, errorBody));

                    log.error("Error timeout-exception  ====> {}", mapper.writeValueAsString(errorResponse));
                    exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE,statusCode);
                    exchange.getIn().setBody(errorResponse);

                })
                .removeHeader(Headers.NationalCode);

        onException(SocketException.class)
                .maximumRedeliveries(0)
                .handled(true)
                .process(exchange -> {
                    SocketException exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT,
                            SocketException.class);
                    String errorBody = exception.getLocalizedMessage();
                    int statusCode = HttpStatus.GATEWAY_TIMEOUT.value();
                    ServiceErrorResponse errorResponse = new ServiceErrorResponse();
                    errorResponse.setCode(2);
                    errorResponse.setMessage("خطای سرویس camel-client");
                    errorResponse.setOriginalMessage(String.format("{\"code\":%d,\"text\":\"%s\"}",
                            statusCode, errorBody));

                    log.error("Error timeout-exception  ====> {}", mapper.writeValueAsString(errorResponse));
                    exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE,statusCode);
                    exchange.getIn().setBody(errorResponse);

                })
                .removeHeader(Headers.NationalCode);

        onException(JsonMappingException.class)
                .maximumRedeliveries(0)
                .handled(true)
                .process(exchange -> {
                    JsonMappingException exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT,
                            JsonMappingException.class);
                    String errorBody = exception.getLocalizedMessage();
                    int statusCode = HttpStatus.NOT_ACCEPTABLE.value();
                    ServiceErrorResponse errorResponse = new ServiceErrorResponse();
                    errorResponse.setCode(2);
                    errorResponse.setMessage("خطای سرویس person-api");
                    errorResponse.setOriginalMessage(String.format("{\"code\":%d,\"text\":\"%s\"}",
                            statusCode, errorBody));

                    log.error("Error json-exception  ====> {}", mapper.writeValueAsString(errorResponse));
                    exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE,statusCode);
                    exchange.getIn().setBody(errorResponse);

                })
                .removeHeader(Headers.NationalCode);


        onException(HttpOperationFailedException.class)
                .maximumRedeliveries(0)
                .handled(true)
                .process(exchange -> {
                    HttpOperationFailedException exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT,
                            HttpOperationFailedException.class);
                    String errorBody = exception.getResponseBody();
                    String url =exception.getUri();
                    int statusCode = exception.getStatusCode();


                    ServiceErrorResponse errorResponse = new ServiceErrorResponse();
                    errorResponse.setCode(2);
                    errorResponse.setMessage("خطای سرویس person-api");
                    if (errorBody.startsWith("{")){
                        errorResponse.setOriginalMessage(errorBody);
                    }else {
                        errorResponse.setOriginalMessage(String.format("{\"code\":%d,\"text\":\"%s\"}",
                                statusCode, errorBody));
                    }

                    log.error("Error for url {}  ====> {}",url, mapper.writeValueAsString(errorResponse));
                    exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE,statusCode);
                    exchange.getIn().setBody(errorResponse);

                })
                .removeHeader(Headers.NationalCode);



    }
}
