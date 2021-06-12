package com.querydsl.example.dao;

import com.querydsl.example.config.TestConfiguration;
import com.querydsl.example.config.TestDataService;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfiguration.class})
@Rollback
@Transactional
public abstract class AbstractDaoTest {

    @Autowired
    TestDataService testDataService;

    @Before
    public void setUp() {
        testDataService.addTestData();
    }

}
