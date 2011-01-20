/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jdoql.testdomain;

import java.util.Date;

import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

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

    private Date publicationDate;

    @Persistent
    private java.sql.Date dateField;

    @Persistent
    private java.sql.Time timeField;

    private int amount;

    public Product() {
    }

    public Product(String name, String description, double price, int amount) {
        this(name, description, price, amount, new Date());
    }

    public Product(String name, String description, double price, int amount, Date publicationDate) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.amount = amount;
        this.publicationDate = publicationDate;
        this.dateField = new java.sql.Date(publicationDate.getTime());
        this.timeField = new java.sql.Time(publicationDate.getTime());
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

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public java.sql.Date getDateField() {
        return dateField;
    }

    public void setDateField(java.sql.Date dateField) {
        this.dateField = dateField;
    }

    public java.sql.Time getTimeField() {
        return timeField;
    }

    public void setTimeField(java.sql.Time timeField) {
        this.timeField = timeField;
    }

    public String toString() {
        return "Product : " + name + " [" + description + "]";
    }
}
