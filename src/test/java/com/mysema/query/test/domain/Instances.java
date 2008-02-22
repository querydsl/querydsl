package com.mysema.query.test.domain;

import com.mysema.query.test.domain.Domain.*;

/**
 * Instances provides
 *
 * @author tiwe
 * @version $Id$
 */
public class Instances {

    // AuditLog
    public static final _AuditLog<AuditLog> log = new _AuditLog<AuditLog>("log");
    
    // Cat
    public static final _Cat<Cat> cat = new _Cat<Cat>("cat");
    public static final _Cat<Cat> cat1 = new _Cat<Cat>("cat1");
    public static final _Cat<Cat> cat2 = new _Cat<Cat>("cat2");
    public static final _Cat<Cat> cat3 = new _Cat<Cat>("cat3");
    public static final _Cat<Cat> cat4 = new _Cat<Cat>("cat4");
    public static final _Cat<Cat> cat5 = new _Cat<Cat>("cat5");
    
    public static final _Cat<Cat> kitten = new _Cat<Cat>("kitten");
    public static final _Cat<Cat> child = new _Cat<Cat>("child");
    public static final _Cat<Cat> mate = new _Cat<Cat>("mate"); 
    
    // Company
    public static final _Company<Company> company = new _Company<Company>("company");
    public static final _Company<Company> company1 = new _Company<Company>("company1");
    public static final _Company<Company> company2 = new _Company<Company>("company2");
    public static final _Company<Company> company3 = new _Company<Company>("company3");
    public static final _Company<Company> company4 = new _Company<Company>("company4");
    public static final _Company<Company> company5 = new _Company<Company>("company5");
    
    // Customer
    public static final _Customer<Customer> cust = new _Customer<Customer>("cust");
    
    // Document
    public static final _Document<Document> doc = new _Document<Document>("doc");
    
    // DomesticCat
    public static final _DomesticCat<DomesticCat> domesticCat = new _DomesticCat<DomesticCat>("domesticCat");
    
    // Payment
    public static final _Payment<Payment> payment = new _Payment<Payment>("payment");
    
    // User
    public static final _User<User> user = new _User<User>("user");
    public static final _User<User> user1 = new _User<User>("user1");
    public static final _User<User> user2 = new _User<User>("user2");
    public static final _User<User> user3 = new _User<User>("user3");
    public static final _User<User> user4 = new _User<User>("user4");
    public static final _User<User> user5 = new _User<User>("user5");

}
