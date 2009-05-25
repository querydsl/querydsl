/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql;

import com.mysema.query.types.Grammar;
import com.mysema.query.types.SubQuery;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.Ops;

/**
 * SqlGrammar provides SQL specific extensions to the general Querydsl Grammar
 *
 * @author tiwe
 * @version $Id$
 */
public class SQLGrammar extends Grammar{
    
    public static <A> SubQuery<Object,A> select(Expr<A> select){
        return new SubQuery<Object,A>(select);
    }
    
    public static SubQuery<Object,Object[]> select(Expr<?>... select){
        return new SubQuery<Object,Object[]>().select(select);
    }
    
    public static EBoolean exists(SubQuery<Object,?> sq){
        return operationFactory.createBoolean(Ops.EXISTS, sq);
    }

}
