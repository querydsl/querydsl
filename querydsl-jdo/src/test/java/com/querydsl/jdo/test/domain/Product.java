/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.jdo.test.domain;

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
        this.publicationDate = new Date(publicationDate.getTime());
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
        this.publicationDate = new java.sql.Date(publicationDate.getTime());
    }

    public java.sql.Date getDateField() {
        return new java.sql.Date(dateField.getTime());
    }

    public void setDateField(java.sql.Date dateField) {
        this.dateField = new java.sql.Date(dateField.getTime());
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
