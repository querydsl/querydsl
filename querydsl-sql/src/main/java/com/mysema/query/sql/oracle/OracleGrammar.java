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
 * Convenience functions and constants for Oracle DB usage
 * 
 * @author tiwe
 * @version $Id$
 */
public final class OracleGrammar {

    private OracleGrammar(){}
   
    public static final ENumber<Integer> level = new PNumber<Integer>(Integer.class, "level");

    public static final ENumber<Integer> rownum = new PNumber<Integer>(Integer.class, "rownum");
    
    public static final EDate<Date> sysdate = new PDate<Date>(Date.class, "sysdate");

    public static <A extends Number & Comparable<? super A>> SumOver<A> sumOver(Expr<A> expr) {
        return new SumOver<A>(expr);
    }

}
