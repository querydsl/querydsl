/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql.oracle;

import java.util.Date;

import com.mysema.query.types.expr.EComparableBase;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.PComparable;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PathMetadataFactory;

/**
 * OracleGrammar provides Oracle specific extensions to the SqlGrammar
 * 
 * @author tiwe
 * @version $Id$
 */
public final class OracleGrammar {

    private OracleGrammar(){}
    
    // global columns

    public static final ENumber<Integer> level = new PNumber<Integer>(Integer.class, PathMetadataFactory.forVariable("level"));

    public static final ENumber<Integer> rownum = new PNumber<Integer>(Integer.class, PathMetadataFactory.forVariable("rownum"));

    public static final EComparableBase<Date> sysdate = new PComparable<Date>(Date.class, PathMetadataFactory.forVariable("sysdate"));

    // custom functions

    public static <A extends Number & Comparable<? super A>> SumOver<A> sumOver(Expr<A> expr) {
        return new SumOver<A>(expr);
    }

}
