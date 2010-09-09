/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import com.mysema.query.types.Expr;
import com.mysema.query.types.custom.CSimple;
import com.mysema.query.types.expr.ENumber;

/**
 * Wildcard provides shortcuts for the wildcard (*) and wildcard count (count(*)) expressions
 * 
 * @author sasa
 *
 */
public final class Wildcard {

    private static final long serialVersionUID = -675749944676437551L;

    public static final Expr<Object[]> all = CSimple.create(Object[].class, "*");

    private Wildcard() {}
    
    public static ENumber<Long> count() {
        return all.count();
    }

}
