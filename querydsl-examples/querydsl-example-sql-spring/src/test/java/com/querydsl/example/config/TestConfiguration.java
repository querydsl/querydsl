package com.querydsl.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@Import(AppConfiguration.class)
@EnableTransactionManagement
public class TestConfiguration {

    @Bean
    public TestDataService testDataService() {
        return new TestDataServiceImpl();
    }

}