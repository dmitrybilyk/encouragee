package com.encouragee.rest;

import com.encouragee.EncourageeApplication;
import com.zoomint.encourage.common.spring.BuildProperties;
import com.zoomint.keycloak.KeycloakClientConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.solr.SolrRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.solr.SolrAutoConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.web.client.RestTemplate;

@Configuration
@Profile("rest")
@ComponentScan(basePackageClasses = {EncourageeApplication.class, BuildProperties.class})
@PropertySource("build.properties")
@EnableAutoConfiguration(exclude = {SolrAutoConfiguration.class, KeycloakClientConfiguration.class,
        SolrRepositoriesAutoConfiguration.class})
public class RestConfiguration {

}
