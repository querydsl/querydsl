/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.hql;

import java.util.List;

import com.mysema.query.JoinExpression;
import com.mysema.query.JoinFlag;
import com.mysema.query.QueryMetadata;
import com.mysema.query.support.QueryMixin;
import com.mysema.query.types.expr.BooleanExpression;

/**
 * HQLQueryMixin extends QueryMixin to support JPQL join construction
 * 
 * @author tiwe
 *
 * @param <T>
 */
public class HQLQueryMixin<T> extends QueryMixin<T> {
    
    public static final JoinFlag FETCH = new JoinFlag("fetch ");
    
    public static final JoinFlag FETCH_ALL_PROPERTIES = new JoinFlag(" fetch all properties");
    
    public HQLQueryMixin() {}

    public HQLQueryMixin(QueryMetadata metadata) {
        super(metadata);
    }

    public HQLQueryMixin(T self, QueryMetadata metadata) {
        super(self, metadata);
    }

    public T fetch(){
        List<JoinExpression> joins = getMetadata().getJoins();
        joins.get(joins.size()-1).addFlag(FETCH);
        return getSelf();
    }

    public T fetchAll(){
        List<JoinExpression> joins = getMetadata().getJoins();
        joins.get(joins.size()-1).addFlag(FETCH_ALL_PROPERTIES);
        return getSelf();
    }

    public T with(BooleanExpression... conditions){
        for (BooleanExpression condition : conditions){
            getMetadata().addJoinCondition(condition);
        }
        return getSelf();
    }

}
