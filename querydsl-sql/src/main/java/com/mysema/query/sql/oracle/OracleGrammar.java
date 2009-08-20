/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql.oracle;

import java.util.Date;

import com.mysema.query.sql.SumOver;
import com.mysema.query.types.expr.EComparable;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.PComparable;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PathMetadata;

/**
 * OracleGrammar provides Oracle specific extensions to the SqlGrammar
 * 
 * @author tiwe
 * @version $Id$
 */
public class OracleGrammar {

    // global columns

    public static ENumber<Integer> level = new PNumber<Integer>(Integer.class, createVariable("level"));

    public static ENumber<Integer> rownum = new PNumber<Integer>(Integer.class, createVariable("rownum"));

    public static EComparable<Date> sysdate = new PComparable<Date>(Date.class, createVariable("sysdate"));

    // custom functions

    public static <A extends Number & Comparable<? super A>> SumOver<A> sumOver(Expr<A> expr) {
        return new SumOver<A>(expr);
    }

    // internal

    private static PathMetadata<String> createVariable(String var) {
        return PathMetadata.forVariable(var);
    }

}
