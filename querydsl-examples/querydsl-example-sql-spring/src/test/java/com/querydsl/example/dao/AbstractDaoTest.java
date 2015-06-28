package com.querydsl.example.dao;

import com.querydsl.example.config.TestConfiguration;
import com.querydsl.example.config.TestDataService;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={TestConfiguration.class})
@TransactionConfiguration(defaultRollback = true)
@Transactional
public abstract class AbstractDaoTest {

    @Resource TestDataService testDataService;

    @Before
    public void setUp() {
        testDataService.addTestData();
    }

}
