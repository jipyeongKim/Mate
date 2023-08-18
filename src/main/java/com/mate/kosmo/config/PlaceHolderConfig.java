package com.mate.kosmo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@PropertySource({"classpath:application.properties"})
@Configuration
public class PlaceHolderConfig {
    // [.properties 파일 빈으로 생성 및 등록하기]
    // 1) Java 코드 사용하여 빈으로 생성 및 빈 등록하기
    //  1-1) 해당 클래스에 @Configuration 사용
    //  1-2) 해당 클래스에 @PropertySource 사용
    //  1-3) .properties 파일의 경로를 배열 형식으로 등록
    //  1-4) PropertySourcesPlaceholderConfigurer 객체를 반환하는 메소드 정의
    //  1-5) 해당 메소드에 @Bean 사용하기
    @Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer(){
        return new PropertySourcesPlaceholderConfigurer();
    }// propertySourcesPlaceholderConfigurer

}// PlaceHolderConfig