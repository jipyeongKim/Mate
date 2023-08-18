package com.mate.kosmo.config;

import com.google.gson.JsonParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        return restTemplate;
    }// restTemplate

    @Bean
    public HttpHeaders httpHeaders(){
        return new HttpHeaders();
    }// httpHeaders

    @Bean
    public MultiValueMap multiValueMap(){
        return new LinkedMultiValueMap();
    }// MultiValueMap

    @Bean
    public JsonParser jsonParser(){
        return new JsonParser();
    }// jsonParser

}// RestTemplateConfig