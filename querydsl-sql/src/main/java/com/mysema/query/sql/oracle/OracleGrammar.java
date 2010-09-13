/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql.oracle;

import java.util.Date;

import com.mysema.query.types.Expression;
import com.mysema.query.types.expr.DateExpression;
import com.mysema.query.types.expr.NumberExpression;
import com.mysema.query.types.path.DatePath;
import com.mysema.query.types.path.NumberPath;

/**
 * Convenience functions and constants for Oracle DB usage
 *
 * @author tiwe
 * @version $Id$
 */
public final class OracleGrammar {

    private OracleGrammar(){}

    public static final NumberExpression<Integer> level = new NumberPath<Integer>(Integer.class, "level");

    public static final NumberExpression<Integer> rownum = new NumberPath<Integer>(Integer.class, "rownum");

    public static final DateExpression<Date> sysdate = new DatePath<Date>(Date.class, "sysdate");

    public static <A extends Number & Comparable<? super A>> SumOver<A> sumOver(Expression<A> expr) {
        return new SumOver<A>(expr);
    }

}
