package com.querydsl.example.dao;

import com.querydsl.example.dto.Supplier;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class SupplierDaoTest extends AbstractDaoTest {

    @Autowired
    SupplierDao supplierDao;

    @Test
    public void findAll() {
        List<Supplier> suppliers = supplierDao.findAll();
        assertFalse(suppliers.isEmpty());
    }

    @Test
    public void findById() {
        assertNotNull(supplierDao.findById(1));
    }

    @Test
    public void update() {
        Supplier supplier = supplierDao.findById(1);
        supplierDao.save(supplier);
    }

    @Test
    public void delete() {
        Supplier supplier = new Supplier();
        supplierDao.save(supplier);
        assertNotNull(supplier.getId());
        supplierDao.delete(supplier);
        assertNull(supplierDao.findById(supplier.getId()));
    }

}
