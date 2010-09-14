/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa.domain;

import java.util.Arrays;
import java.util.List;

public final class Domain {

    private Domain(){}

    public static final List<Class<?>> classes = Arrays.<Class<?>>asList(
            Account.class,
            Animal.class,
            AuditLog.class,
            Bar.class,
            Calendar.class,
            Cat.class,
            Catalog.class,
            Color.class,
            Company.class,
            Customer.class,
            Department.class,
            Document.class,
            DomesticCat.class,
            EmbeddedType.class,
            Employee.class,
            EvilType.class,
            Family.class,
            Foo.class,
            Formula.class,
            InheritedProperties.class,
            Item.class,
            Location.class,
            Name.class,
            Named.class,
            NameList.class,
            Nationality.class,
            Order.class,
            Parameter.class,
            Payment.class,
            PaymentStatus.class,
            Person.class,
            PersonId.class,
            Player.class,
            Price.class,
            Product.class,
            Show.class,
            SimpleTypes.class,
            Status.class,
            StatusChange.class,
            Store.class,
            Superclass.class,
            User.class
    );
}
