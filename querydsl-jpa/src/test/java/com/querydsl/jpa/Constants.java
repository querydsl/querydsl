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
package com.querydsl.jpa;

import com.querydsl.jpa.domain.QAccount;
import com.querydsl.jpa.domain.QAnimal;
import com.querydsl.jpa.domain.QAuditLog;
import com.querydsl.jpa.domain.QBar;
import com.querydsl.jpa.domain.QCalendar;
import com.querydsl.jpa.domain.QCat;
import com.querydsl.jpa.domain.QCatalog;
import com.querydsl.jpa.domain.QCompany;
import com.querydsl.jpa.domain.QCustomer;
import com.querydsl.jpa.domain.QDocument;
import com.querydsl.jpa.domain.QDomesticCat;
import com.querydsl.jpa.domain.QFoo;
import com.querydsl.jpa.domain.QFormula;
import com.querydsl.jpa.domain.QItem;
import com.querydsl.jpa.domain.QName;
import com.querydsl.jpa.domain.QNameList;
import com.querydsl.jpa.domain.QNamed;
import com.querydsl.jpa.domain.QOrder;
import com.querydsl.jpa.domain.QParameter;
import com.querydsl.jpa.domain.QPayment;
import com.querydsl.jpa.domain.QPerson;
import com.querydsl.jpa.domain.QPlayer;
import com.querydsl.jpa.domain.QPrice;
import com.querydsl.jpa.domain.QProduct;
import com.querydsl.jpa.domain.QShow;
import com.querydsl.jpa.domain.QStatus;
import com.querydsl.jpa.domain.QStatusChange;
import com.querydsl.jpa.domain.QStore;
import com.querydsl.jpa.domain.QUser;

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
