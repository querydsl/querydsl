package com.querydsl.example.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.google.common.collect.ImmutableSet;
import com.querydsl.example.dto.Product;
import com.querydsl.example.dto.ProductL10n;

public class ProductDaoTest extends AbstractDaoTest {

    @Resource SupplierDao supplierDao;

    @Resource ProductDao productDao;

    @Test
    public void FindAll() {
        List<Product> products = productDao.findAll(null);
        assertFalse(products.isEmpty());
    }

    @Test
    public void FindById() {
        assertNotNull(productDao.findById(1));
    }

    @Test
    public void Update() {
        Product product = productDao.findById(1);
        productDao.save(product);
    }

    @Test
    public void Delete() {
        Product product = new Product();
        product.setSupplier(supplierDao.findById(1));
        product.setName("ProductX");
        product.setLocalizations(ImmutableSet.of(new ProductL10n()));
        productDao.save(product);
        assertNotNull(productDao.findById(product.getId()));
        productDao.delete(product);
        assertNull(productDao.findById(product.getId()));
    }

}
