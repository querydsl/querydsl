/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.jdoql.testdomain;

import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;

/**
 * Definition of a Product Represents a product, and contains the key aspects of
 * the item.
 */
@PersistenceCapable
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
public class Product {
    private String name = null;

    private String description = null;

    private double price = 0.0;

    private int amount;
    
    protected Product() {
    }

    public Product(String name, String description, double price, int amount) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String toString() {
        return "Product : " + name + " [" + description + "]";
    }
}