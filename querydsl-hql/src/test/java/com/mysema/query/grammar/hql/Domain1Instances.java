/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar.hql;

import static com.mysema.query.Domain1.*;

/**
 * Domain1Instances provides.
 * 
 * @author tiwe
 * @version $Id$
 */
public interface Domain1Instances {
    
    Account account = new Account("account");
    
    Animal an = new Animal("an");
    
    AuditLog log = new AuditLog("log");
    
    Bar bar = new Bar("bar");
    
    Calendar calendar = new Calendar("calendar");
    
    Cat cat = new Cat("cat");
    Cat fatcat = new Cat("fatcat");
    Cat kittens = new Cat("kittens");
    Cat kitten = new Cat("kitten");
    Cat kit = new Cat("kit");
    Cat mate = new Cat("mate");
    Cat mother = new Cat("mother");
    Cat offspr = new Cat("offspr");
    Cat qat = new Cat("qat");
    Cat rival = new Cat("rival");
    
    Catalog catalog = new Catalog("catalog");
    
    Customer cust = new Customer("cust");
    
    doofus d = new doofus("d");
    
    Foo foo = new Foo("foo");
    
    Formula form = new Formula("form");
    
    Item item = new Item("item");
    
    Name name = new Name("name");
    
    Named m = new Named("m");
    Named n = new Named("n");
    
    NameList list = new NameList("list");
    
    Order ord = new Order("ord");
    
    Payment payment = new Payment("payment");
    
    Parameter param = new Parameter("param");
    
    Person person = new Person("person");    
    Person p = new Person("p");
    
    Player player = new Player("player");
    
    Price price = new Price("price");
    
    Product prod = new Product("prod");
    Product product = new Product("product");
    
    Show show = new Show("show");
    
    Status status = new Status("status");
    
    StatusChange statusChange = new StatusChange("statusChange");
    
    Store store = new Store("store");

}
