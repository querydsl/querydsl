package com.querydsl.example.config;

import org.springframework.transaction.annotation.Transactional;

    @Transactional
public interface TestDataService {

    void addTestData();

}
