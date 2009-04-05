/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import com.mysema.query.grammar.HqlOps;
import com.mysema.query.grammar.HqlQueryBase;

/**
 * QueryBaseWithDomain provides
 *
 * @author tiwe
 * @version $Id$
 */
public abstract class QueryBaseWithDomain<A extends QueryBaseWithDomain<A>> extends HqlQueryBase<A>{
    
    public QueryBaseWithDomain(){super(new HqlOps());}
    
    QAccount account = new QAccount("account");
    
    QAnimal an = new QAnimal("an");
    
    QAuditLog log = new QAuditLog("log");
    
    QBar bar = new QBar("bar");
    
    QCalendar calendar = new QCalendar("calendar");
    
    QCat cat = new QCat("cat");
    QCat fatcat = new QCat("fatcat");
    QCat kittens = new QCat("kittens");
    QCat kitten = new QCat("kitten");
    QCat kit = new QCat("kit");
    QCat mate = new QCat("mate");
    QCat mother = new QCat("mother");
    QCat offspr = new QCat("offspr");
    QCat qat = new QCat("qat");
    QCat rival = new QCat("rival");
    
    QCatalog catalog = new QCatalog("catalog");
    
    QCustomer cust = new QCustomer("cust");
    
    Qdoofus d = new Qdoofus("d");
    
    QFoo foo = new QFoo("foo");
    
    QFormula form = new QFormula("form");
    
    QItem item = new QItem("item");
    
    QName name = new QName("name");
    
    QNamed m = new QNamed("m");
    QNamed n = new QNamed("n");
    
    QNameList list = new QNameList("list");
    
    QOrder ord = new QOrder("ord");
    
    QPayment payment = new QPayment("payment");
    
    QParameter param = new QParameter("param");
    
    QPerson person = new QPerson("person");    
    QPerson p = new QPerson("p");
    
    QPlayer player = new QPlayer("player");
    
    QPrice price = new QPrice("price");
    
    QProduct prod = new QProduct("prod");
    QProduct product = new QProduct("product");
    
    QShow show = new QShow("show");
    
    QStatus status = new QStatus("status");
    
    QStatusChange statusChange = new QStatusChange("statusChange");
    
    QStore store = new QStore("store");

}
