/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import com.mysema.query.grammar.Types.ExprBoolean;
import com.mysema.query.grammar.Types.ExprEntity;

/**
 * ExtQueryBased provides a basic implementation of the ExtQuery interface
 *
 * @author tiwe
 * @version $Id$
 */
@SuppressWarnings("unchecked")
public class ExtQueryBase<A extends ExtQueryBase<A>> extends QueryBase<A> implements ExtQuery<A> { 

    public A innerJoin(ExprEntity<?> o) {
        joins.add(new JoinExpression(JoinType.IJ,o));
        return (A) this;
    }
    
    public A join(ExprEntity<?> o) {
        joins.add(new JoinExpression(JoinType.J,o));
        return (A) this;
    }

    public A leftJoin(ExprEntity<?> o) {
        joins.add(new JoinExpression(JoinType.LJ,o));
        return (A) this;
    }
    
    public A with(ExprBoolean... o) {
        if (!joins.isEmpty()){
            joins.get(joins.size()-1).conditions = o;
        }
        return (A) this;
    }
}
