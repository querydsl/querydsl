/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa.support;

import org.hibernate.Hibernate;
import org.hibernate.dialect.HSQLDialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;

/**
 * @author tiwe
 *
 */
public class ExtendedHSQLDialect extends HSQLDialect{

    public ExtendedHSQLDialect() {
        registerFunction( "trim", new SQLFunctionTemplate( Hibernate.STRING, "trim(both from ?1)" ) );
    }
}
