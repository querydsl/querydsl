/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jdoql.testdomain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.Join;
import javax.jdo.annotations.Key;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Value;

@PersistenceCapable
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
public class Store {

    private String name;

//    @Persistent
    @Join
    @Element(types=Product.class)
    private List<Product> products = new ArrayList<Product>();

//    @Persistent
    @Join
    @Key(types=String.class)
    @Value(types=Product.class)
    private Map<String,Product> productsByName = new HashMap<String,Product>();

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
