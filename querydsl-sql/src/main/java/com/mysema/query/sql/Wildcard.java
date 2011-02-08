/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import com.mysema.query.types.expr.NumberExpression;
import com.mysema.query.types.expr.SimpleExpression;
import com.mysema.query.types.template.SimpleTemplate;

/**
 * Wildcard provides shortcuts for the wildcard (*) and wildcard count (count(*)) expressions
 *
 * @deprecated use com.mysema.query.types.expr.Wildcard instead
 *
 * @author sasa
 *
 */
@Deprecated
public final class Wildcard {

    private static final long serialVersionUID = -675749944676437551L;

    public static final SimpleExpression<Object[]> all = SimpleTemplate.create(Object[].class, "*");

    private Wildcard() {}

    public static NumberExpression<Long> count() {
        return all.count();
    }

}
