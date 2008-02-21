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
    public static final qAuditLog<AuditLog> log = new qAuditLog<AuditLog>("log");
    
    // Cat
    public static final qCat<Cat> cat = new qCat<Cat>("cat");
    public static final qCat<Cat> cat1 = new qCat<Cat>("cat1");
    public static final qCat<Cat> cat2 = new qCat<Cat>("cat2");
    public static final qCat<Cat> cat3 = new qCat<Cat>("cat3");
    public static final qCat<Cat> cat4 = new qCat<Cat>("cat4");
    public static final qCat<Cat> cat5 = new qCat<Cat>("cat5");
    
    public static final qCat<Cat> kitten = new qCat<Cat>("kitten");
    public static final qCat<Cat> child = new qCat<Cat>("child");
    public static final qCat<Cat> mate = new qCat<Cat>("mate"); 
    
    // Company
    public static final qCompany<Company> company = new qCompany<Company>("company");
    public static final qCompany<Company> company1 = new qCompany<Company>("company1");
    public static final qCompany<Company> company2 = new qCompany<Company>("company2");
    public static final qCompany<Company> company3 = new qCompany<Company>("company3");
    public static final qCompany<Company> company4 = new qCompany<Company>("company4");
    public static final qCompany<Company> company5 = new qCompany<Company>("company5");
    
    // Customer
    public static final qCustomer<Customer> cust = new qCustomer<Customer>("cust");
    
    // Document
    public static final qDocument<Document> doc = new qDocument<Document>("doc");
    
    // DomesticCat
    public static final qDomesticCat<DomesticCat> domesticCat = new qDomesticCat<DomesticCat>("domesticCat");
    
    // Payment
    public static final qPayment<Payment> payment = new qPayment<Payment>("payment");
    
    // User
    public static final qUser<User> user = new qUser<User>("user");
    public static final qUser<User> user1 = new qUser<User>("user1");
    public static final qUser<User> user2 = new qUser<User>("user2");
    public static final qUser<User> user3 = new qUser<User>("user3");
    public static final qUser<User> user4 = new qUser<User>("user4");
    public static final qUser<User> user5 = new qUser<User>("user5");

}
