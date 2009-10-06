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

/**
 * Definition of a Book. Extends basic Product class.
 */
@PersistenceCapable
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
public class Book extends Product {
    private String author = null;

    private String isbn = null;

    private String publisher = null;

    protected Book() {
        super();
    }

    public Book(String name, String description, double price, int amount, String author,
            String isbn, String publisher) {
        this(name, description, price, amount, author, isbn, publisher, new Date());
    }
    
    public Book(String name, String description, double price, int amount, String author,
            String isbn, String publisher, Date date) {
        super(name, description, price, amount, date);
        this.author = author;
        this.isbn = isbn;
        this.publisher = publisher;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String toString() {
        return "Book : " + author + " - " + getName();
    }
}