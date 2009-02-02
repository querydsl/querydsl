/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar.types;

import com.mysema.query.Query;
import com.mysema.query.QueryBase;
import com.mysema.query.grammar.OrderSpecifier;

/**
 * 
 * SubQuery is a sub query
 *
 * @author tiwe
 * @version $Id$
 *
 * @param <A>
 */
public class SubQuery<JM,A> extends Expr<A> implements Query<SubQuery<JM,A>>, CollectionType<A>{
    @SuppressWarnings("unchecked")
    private QueryWithPublicSelect<JM> query = new QueryWithPublicSelect<JM>();
    public SubQuery(Expr<A> select) {
        super(null);
        query.s(select);
    }
    public SubQuery(){
        super(null);
    }
    @SuppressWarnings("unchecked")
    public SubQuery<JM,A> from(EEntity... o) {query.from(o); return this;}
    public SubQuery<JM,A> fullJoin(EEntity<?> o) {query.fullJoin(o); return this;}
    public QueryBase<JM,?> getQuery(){ return query;}
    public SubQuery<JM,A> groupBy(Expr<?>... o) {query.groupBy(o); return this;}
    public SubQuery<JM,A> having(EBoolean... o) {query.having(o); return this;}
    public SubQuery<JM,A> innerJoin(EEntity<?> o) {query.innerJoin(o); return this;}
    public SubQuery<JM,A> join(EEntity<?> o) {query.join(o); return this;}
    public SubQuery<JM,A> leftJoin(EEntity<?> o) {query.leftJoin(o); return this;}
    public SubQuery<JM,A> on(EBoolean o) {query.on(o); return this;}
    public SubQuery<JM,A> orderBy(OrderSpecifier<?>... o) {query.orderBy(o); return this;}
    public SubQuery<JM,A> select(Expr<?>... o) {
        query.s(o); return this;}
    public SubQuery<JM,A> where(EBoolean... o) {query.where(o); return this;}    
    private static class QueryWithPublicSelect<JM> extends QueryBase<JM,QueryWithPublicSelect<JM>>{
        public void s(Expr<?>... expr){
            select(expr);
        }
    }
}