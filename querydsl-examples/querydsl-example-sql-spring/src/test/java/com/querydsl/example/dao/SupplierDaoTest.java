package com.querydsl.example.dao;

import static org.junit.Assert.*;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.querydsl.example.dto.Supplier;

public class SupplierDaoTest extends AbstractDaoTest {
    
    @Resource SupplierDao supplierDao;
    
    @Test
    public void FindAll() {
        List<Supplier> suppliers = supplierDao.findAll();
        assertFalse(suppliers.isEmpty());
    }
    
    @Test
    public void FindById() {
        assertNotNull(supplierDao.findById(1));
    }
    
    @Test
    public void Update() {
        Supplier supplier = supplierDao.findById(1);
        supplierDao.save(supplier);
    }
    
    @Test
    public void Delete() {
        Supplier supplier = new Supplier();
        supplierDao.save(supplier);
        assertNotNull(supplier.getId());
        supplierDao.delete(supplier);
        assertNull(supplierDao.findById(supplier.getId()));
    }

}
