/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.support;

import org.hibernate.Hibernate;
import org.hibernate.dialect.HSQLDialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;

public class ExtendedHSQLDialect extends HSQLDialect{

    public ExtendedHSQLDialect() {
        registerFunction( "trim", new SQLFunctionTemplate( Hibernate.STRING, "trim(both from ?1)" ) );
    }
}
