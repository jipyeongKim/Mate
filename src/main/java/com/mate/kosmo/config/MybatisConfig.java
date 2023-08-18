package com.mate.kosmo.config;

import com.zaxxer.hikari.HikariDataSource;

import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
@RequiredArgsConstructor
public class MybatisConfig {

   private final HikariDataSource hikariDataSource;

    @Bean
    public SqlSessionFactory sqlSessionFactory(HikariDataSource hikariDataSource){
        SqlSessionFactory factory = null;
        try {
            SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();

            factoryBean.setDataSource(hikariDataSource);

            factory = factoryBean.getObject();

        } catch(Exception e) {
            e.printStackTrace();
        }// catch

        return factory;

    }// sqlSessionFactory

    // 2) 마이바티스 SqlSessionTemplate 빈 생성 및 빈 등록하기
    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {

        return new SqlSessionTemplate(sqlSessionFactory);

    }// sqlSessionTemplate

    // 3) 마이바티스 transaction 빈 생성 및 빈 등록하기
    @Bean
    public DataSourceTransactionManager transactionManager(HikariDataSource hikariDataSource) {

        return new DataSourceTransactionManager(hikariDataSource);

    }// transactionManager

    @Bean
    public TransactionTemplate transactionTemplate(DataSourceTransactionManager transactionManager) {

        TransactionTemplate transactionTemplate = new TransactionTemplate();

        transactionTemplate.setTransactionManager(transactionManager);

        return transactionTemplate;

    }// transactionTemplate

}// MybatisConfig