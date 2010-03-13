/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql.oracle;

import java.util.Date;

import com.mysema.query.types.expr.EDate;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.PDate;
import com.mysema.query.types.path.PNumber;

/**
 * OracleGrammar provides Oracle specific extensions to the SqlGrammar
 * 
 * @author tiwe
 * @version $Id$
 */
public final class OracleGrammar {

    private OracleGrammar(){}
    
    // global columns

    public static final ENumber<Integer> level = new PNumber<Integer>(Integer.class, "level");

    public static final ENumber<Integer> rownum = new PNumber<Integer>(Integer.class, "rownum");

    public static final EDate<Date> sysdate = new PDate<Date>(Date.class, "sysdate");

    // custom functions

    public static <A extends Number & Comparable<? super A>> SumOver<A> sumOver(Expr<A> expr) {
        return new SumOver<A>(expr);
    }

}
