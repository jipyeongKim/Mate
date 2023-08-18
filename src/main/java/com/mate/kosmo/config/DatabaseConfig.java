package com.mate.kosmo.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfig {

    @Value("${spring.datasource.hikari.driver-class-name}")
    private String driverClassName;
    @Value("${spring.datasource.hikari.jdbc-url}")
    private String url;
    @Value("${spring.datasource.hikari.username}")
    private String username;
    @Value("${spring.datasource.hikari.password}")
    private String password;

    @Bean
    public HikariDataSource hikariDataSource(){
        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setDriverClassName(driverClassName);

        hikariConfig.setJdbcUrl(url);

        hikariConfig.setUsername(username);

        hikariConfig.setPassword(password);

        return new HikariDataSource(hikariConfig);

    }// hikariDataSource

}// DatabaseConfig