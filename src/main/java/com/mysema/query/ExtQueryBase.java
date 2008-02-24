/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import com.mysema.query.grammar.Types.ExprForBoolean;
import com.mysema.query.grammar.Types.ExprForEntity;

/**
 * ExtQueryBased provides a basic implementation of the ExtQuery interface
 *
 * @author tiwe
 * @version $Id$
 */
@SuppressWarnings("unchecked")
public class ExtQueryBase<A extends ExtQueryBase<A>> extends QueryBase<A> implements ExtQuery<A> { 

    public A innerJoin(ExprForEntity<?> object) {
        joins.add(new JoinExpression(JoinType.IJ,object));
        return (A) this;
    }
    
    public A join(ExprForEntity<?> object) {
        joins.add(new JoinExpression(JoinType.J,object));
        return (A) this;
    }

    public A leftJoin(ExprForEntity<?> object) {
        joins.add(new JoinExpression(JoinType.LJ,object));
        return (A) this;
    }
    
    public A with(ExprForBoolean... objects) {
        if (!joins.isEmpty()){
            joins.get(joins.size()-1).conditions = objects;
        }
        return (A) this;
    }
}
