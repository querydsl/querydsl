package com.querydsl.example.dao;

import com.querydsl.example.dto.Customer;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

import static org.junit.Assert.*;

public class CustomerDaoTest extends AbstractDaoTest {
    
    @Resource CustomerDao customerDao;
    
    @Test
    public void FindAll() {
        List<Customer> customers = customerDao.findAll();
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
