package com.saber.spring_camel_cxf_client.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.camel.CamelContext;
import org.apache.camel.component.metrics.routepolicy.MetricsRoutePolicyFactory;
import org.apache.camel.component.micrometer.messagehistory.MicrometerMessageHistoryFactory;
import org.apache.camel.component.micrometer.routepolicy.MicrometerRoutePolicyFactory;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.apache.camel.support.jsse.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;

@Configuration
@ImportResource(value = "classpath:*camel.xml")
public class AppConfig {

    @Autowired
    private CamelContext camelContext;

    @Value(value = "${service.person-api.personApiNetFile}")
    private String personApiNetFile;
    @Value(value = "${service.person-api.personApiNetFilePassword}")
    private String personApiNetFilePassword;

    @Bean
    public ObjectMapper mapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, false);
        mapper.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, false);
        mapper.configure(SerializationFeature.FAIL_ON_UNWRAPPED_TYPE_IDENTIFIERS, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        return mapper;
    }

    @Bean
    public CamelContextConfiguration camelContextConfiguration() {
        return new CamelContextConfiguration() {
            @Override
            public void beforeApplicationStart(CamelContext camelContext) {
                camelContext.addRoutePolicyFactory(new MicrometerRoutePolicyFactory());
                camelContext.addRoutePolicyFactory(new MetricsRoutePolicyFactory());
                camelContext.setMessageHistoryFactory(new MicrometerMessageHistoryFactory());
            }

            @Override
            public void afterApplicationStart(CamelContext camelContext) {

            }
        };
    }

    @Bean(name = "sslContextParameters")
    public SSLContextParameters sslContextParameters() throws GeneralSecurityException, IOException {
        SSLContextParameters sslContextParameters = new SSLContextParameters();

        KeyStore personApiNetKeyStore = KeyStore.getInstance("JKS");
        personApiNetKeyStore.load(new FileInputStream(personApiNetFile), personApiNetFilePassword.toCharArray());

        /// create Key store Parameters

        KeyStoreParameters keyStoreParameters= new KeyStoreParameters();
        keyStoreParameters.setResource(personApiNetFile);
        keyStoreParameters.setPassword(personApiNetFilePassword);
        keyStoreParameters.setType("JKS");

        KeyManagersParameters keyManagersParameters =new KeyManagersParameters();
        keyManagersParameters.setKeyPassword(personApiNetFilePassword);
        keyManagersParameters.setKeyStore(keyStoreParameters);
        /// create Key store Parameters

        /// create Trust store Manager

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(personApiNetKeyStore);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

        TrustManagersParameters trustManagersParameters = new TrustManagersParameters();
        trustManagersParameters.setKeyStore(keyStoreParameters);
        trustManagersParameters.setTrustManager(trustManagers[0]);

        /// create Trust store Manager

        FilterParameters filter = new FilterParameters();
        filter.getInclude().add(".*");

        SSLContextClientParameters sslContextClientParameters = new SSLContextClientParameters();
        sslContextClientParameters.setCipherSuitesFilter(filter);

        sslContextParameters.setClientParameters(sslContextClientParameters);
        sslContextParameters.setKeyManagers(keyManagersParameters);
        sslContextParameters.setTrustManagers(trustManagersParameters);

        return sslContextParameters;
    }

}
