package com.querydsl.example.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.querydsl.example.dto.Customer;

public class CustomerDaoTest extends AbstractDaoTest {

    @Resource CustomerDao customerDao;

    @Test
    public void FindAll() {
        List<Customer> customers = customerDao.findAll(null);
        assertFalse(customers.isEmpty());
    }

    @Test
    public void FindById() {
        assertNotNull(customerDao.findById(1));
    }

    @Test
    public void Update() {
        Customer customer = customerDao.findById(1);
        customerDao.save(customer);
    }

    @Test
    public void Delete() {
        Customer customer = customerDao.findById(1);
        customerDao.delete(customer);
        assertNull(customerDao.findById(1));
    }

}
