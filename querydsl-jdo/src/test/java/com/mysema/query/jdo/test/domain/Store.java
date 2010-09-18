/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jdo.test.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.annotations.*;

@PersistenceCapable
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
@FetchGroups({
    @FetchGroup(name="products", members={@Persistent(name="products")})
})
public class Store {

    private String name;

    @Join
    @Element(types=Product.class)
    private List<Product> products = new ArrayList<Product>();

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
