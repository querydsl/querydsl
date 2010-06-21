/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.hql;

import com.mysema.query.hql.domain.QAccount;
import com.mysema.query.hql.domain.QAnimal;
import com.mysema.query.hql.domain.QAuditLog;
import com.mysema.query.hql.domain.QBar;
import com.mysema.query.hql.domain.QCalendar;
import com.mysema.query.hql.domain.QCat;
import com.mysema.query.hql.domain.QCatalog;
import com.mysema.query.hql.domain.QCompany;
import com.mysema.query.hql.domain.QCustomer;
import com.mysema.query.hql.domain.QDocument;
import com.mysema.query.hql.domain.QDomesticCat;
import com.mysema.query.hql.domain.QFoo;
import com.mysema.query.hql.domain.QFormula;
import com.mysema.query.hql.domain.QItem;
import com.mysema.query.hql.domain.QName;
import com.mysema.query.hql.domain.QNameList;
import com.mysema.query.hql.domain.QNamed;
import com.mysema.query.hql.domain.QOrder;
import com.mysema.query.hql.domain.QParameter;
import com.mysema.query.hql.domain.QPayment;
import com.mysema.query.hql.domain.QPerson;
import com.mysema.query.hql.domain.QPlayer;
import com.mysema.query.hql.domain.QPrice;
import com.mysema.query.hql.domain.QProduct;
import com.mysema.query.hql.domain.QShow;
import com.mysema.query.hql.domain.QStatus;
import com.mysema.query.hql.domain.QStatusChange;
import com.mysema.query.hql.domain.QStore;
import com.mysema.query.hql.domain.QUser;

public interface Constants {

    QAccount account = new QAccount("account");

    QAnimal an = new QAnimal("an");

    QBar bar = new QBar("bar");

    QCalendar calendar = new QCalendar("calendar");

    // QCat
    QCat cat = new QCat("cat");

    QCat cat1 = new QCat("cat1");

    QCat cat2 = new QCat("cat2");

    QCat cat3 = new QCat("cat3");

    QCat cat4 = new QCat("cat4");

    QCat cat5 = new QCat("cat5");

    // QCatalog
    QCatalog catalog = new QCatalog("catalog");

    QCat child = new QCat("child");
    // QCompany
    QCompany company = new QCompany("company");

    QCompany company1 = new QCompany("company1");

    QCompany company2 = new QCompany("company2");

    QCompany company3 = new QCompany("company3");

    QCompany company4 = new QCompany("company4");

    QCompany company5 = new QCompany("company5");

    // Customer
    QCustomer cust = new QCustomer("cust");

//    Qdoofus d = new Qdoofus("d");

    // QDocument
    QDocument doc = new QDocument("doc");

    // DomesticQCat
    QDomesticCat domesticCat = new QDomesticCat("domesticCat");

    QCat fatcat = new QCat("fatcat");

    QFoo foo = new QFoo("foo");

    QFormula form = new QFormula("form");
    // QItem
    QItem item = new QItem("item");

    QCat kit = new QCat("kit");

    QCat kitten = new QCat("kitten");

    QCat kitten2 = new QCat("kitten2");

    QCat kittens = new QCat("kittens");

    QNameList list = new QNameList("list");

    // AuditLog
    QAuditLog log = new QAuditLog("log");

    QNamed m = new QNamed("m");

    QCat mate = new QCat("mate");

    QCat mother = new QCat("mother");

    QNamed n = new QNamed("n");

    QName name = new QName("name");

    QCat offspr = new QCat("offspr");

    QOrder ord = new QOrder("ord");
    // Order
    QOrder order = new QOrder("order");

    QPerson p = new QPerson("p");

    QParameter param = new QParameter("param");

    // Payment
    QPayment payment = new QPayment("payment");

    QPerson person = new QPerson("person");

    QPlayer player = new QPlayer("player");

    // Price
    QPrice price = new QPrice("price");

    QProduct prod = new QProduct("prod");

    // Product
    QProduct product = new QProduct("product");

    QCat qat = new QCat("qat");

    QCat rival = new QCat("rival");

    QShow show = new QShow("show");

    QStatus status = new QStatus("status");

    QStatusChange statusChange = new QStatusChange("statusChange");

    QStore store = new QStore("store");

    // User
    QUser user = new QUser("user");

    QUser user1 = new QUser("user1");

    QUser user2 = new QUser("user2");

    QUser user3 = new QUser("user3");

    QUser user4 = new QUser("user4");

    QUser user5 = new QUser("user5");

}
