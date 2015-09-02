/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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

import com.querydsl.jpa.domain.*;
import com.querydsl.jpa.domain.QPerson;

public final class Constants {

    private Constants() { }

    static final QAccount account = new QAccount("account");

    static final QAnimal an = new QAnimal("an");

    static final QBar bar = new QBar("bar");

    static final QCalendar calendar = new QCalendar("calendar");

    // QCat
    static final QCat cat = new QCat("cat");

    static final QCat cat1 = new QCat("cat1");

    static final QCat cat2 = new QCat("cat2");

    static final QCat cat3 = new QCat("cat3");

    static final QCat cat4 = new QCat("cat4");

    static final QCat cat5 = new QCat("cat5");

    // QCatalog
    static final QCatalog catalog = new QCatalog("catalog");

    static final QCat child = new QCat("child");
    // QCompany
    static final QCompany company = new QCompany("company");

    static final QCompany company1 = new QCompany("company1");

    static final QCompany company2 = new QCompany("company2");

    static final QCompany company3 = new QCompany("company3");

    static final QCompany company4 = new QCompany("company4");

    static final QCompany company5 = new QCompany("company5");

    // Customer
    static final QCustomer cust = new QCustomer("cust");

//    Qdoofus d = new Qdoofus("d");

    // QDocument
    static final QDocument doc = new QDocument("doc");

    // DomesticQCat
    static final QDomesticCat domesticCat = new QDomesticCat("domesticCat");

    static final QCat fatcat = new QCat("fatcat");

    static final QFoo foo = new QFoo("foo");

    static final QFormula form = new QFormula("form");
    // QItem
    static final QItem item = new QItem("item");

    static final QCat kit = new QCat("kit");

    static final QCat kitten = new QCat("kitten");

    static final QCat kitten2 = new QCat("kitten2");

    static final QCat kittens = new QCat("kittens");

    static final QNameList list = new QNameList("list");

    // AuditLog
    static final QAuditLog log = new QAuditLog("log");

    static final QNamed m = new QNamed("m");

    static final QCat mate = new QCat("mate");

    static final QCat mother = new QCat("mother");

    static final QNamed n = new QNamed("n");

    static final QName name = new QName("name");

    static final QCat offspr = new QCat("offspr");

    static final QOrder ord = new QOrder("ord");
    // Order
    static final QOrder order = new QOrder("order");

    static final QPerson p = new QPerson("p");

    static final QParameter param = new QParameter("param");

    // Payment
    static final QPayment payment = new QPayment("payment");

    static final QPerson person = new QPerson("person");

    static final QPlayer player = new QPlayer("player");

    // Price
    static final QPrice price = new QPrice("price");

    static final QProduct prod = new QProduct("prod");

    // Product
    static final QProduct product = new QProduct("product");

    static final QCat qat = new QCat("qat");

    static final QCat rival = new QCat("rival");

    static final QShow show = new QShow("show");

    static final QStatus status = new QStatus("status");

    static final QStatusChange statusChange = new QStatusChange("statusChange");

    static final QStore store = new QStore("store");

    // User
    static final QUser user = new QUser("user");

    static final QUser user1 = new QUser("user1");

    static final QUser user2 = new QUser("user2");

    static final QUser user3 = new QUser("user3");

    static final QUser user4 = new QUser("user4");

    static final QUser user5 = new QUser("user5");

}
