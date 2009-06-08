package com.mysema.query.jdoql.testdomain;

import java.util.List;
import java.util.Map;

import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.Join;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
public class Store {

    private String name;
    
    @Persistent
    @Join
    private List<Product> products;
    
    @Persistent
    @Join
    private Map<String,Product> productsByName;
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Product> getProductsByName() {
        return productsByName;
    }

    public void setProductsByName(Map<String, Product> productsByName) {
        this.productsByName = productsByName;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
    
}
