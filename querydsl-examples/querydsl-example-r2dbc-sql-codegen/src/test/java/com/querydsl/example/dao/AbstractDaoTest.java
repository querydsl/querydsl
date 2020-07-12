package com.querydsl.example.dao;

import com.querydsl.example.config.TestConfiguration;
import com.querydsl.example.config.TestDataService;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfiguration.class})
public abstract class AbstractDaoTest {

    @Resource
    TestDataService testDataService;

    @Before
    public void setUp() {
        testDataService.addTestData();
    }

    @After
    public void tearDown() throws Exception {
        testDataService.removeTestData();
    }
}
