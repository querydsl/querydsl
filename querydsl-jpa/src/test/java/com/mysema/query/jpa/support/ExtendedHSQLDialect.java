/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa.support;

import org.hibernate.dialect.HSQLDialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StandardBasicTypes;

/**
 * @author tiwe
 *
 */
public class ExtendedHSQLDialect extends HSQLDialect{

    public ExtendedHSQLDialect() {
        registerFunction( "trim", new SQLFunctionTemplate( StandardBasicTypes.STRING, "trim(both from ?1)" ) );
    }
}
