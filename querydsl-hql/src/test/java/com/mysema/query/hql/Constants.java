/*
 * Copyright (c) 2009 Mysema Ltd.
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
import com.mysema.query.hql.domain.QCustomer;
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
import com.mysema.query.hql.domain.Qdoofus;

public interface Constants {

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
