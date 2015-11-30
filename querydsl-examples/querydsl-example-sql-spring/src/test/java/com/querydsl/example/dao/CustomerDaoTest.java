package com.querydsl.example.dao;

import com.querydsl.example.dto.Customer;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

import static org.junit.Assert.*;

public class CustomerDaoTest extends AbstractDaoTest {

    @Resource CustomerDao customerDao;

    @Test
    public void findAll() {
        List<Customer> customers = customerDao.findAll();
        assertFalse(customers.isEmpty());
    }

    @Test
    public void findById() {
        assertNotNull(customerDao.findById(1));
    }

    @Test
    public void update() {
        Customer customer = customerDao.findById(1);
        customerDao.save(customer);
    }

    @Test
    public void delete() {
        Customer customer = customerDao.findById(1);
        customerDao.delete(customer);
        assertNull(customerDao.findById(1));
    }

}
