/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar.types;

import com.mysema.query.Query;
import com.mysema.query.QueryBase;
import com.mysema.query.grammar.JoinMeta;
import com.mysema.query.grammar.OrderSpecifier;

/**
 * HqlTypes provides general HQL specific types
 * 
 * @author tiwe
 * @version $Id$
 */
public class HqlTypes {
    
    private HqlTypes(){}
    
    /**
     * The Class DistinctPath.
     */
    public static class DistinctPath<T> extends Expr<T>{
        private final Path<T> path;
        @SuppressWarnings("unchecked")
        public DistinctPath(Path<T> path) {
            super(((Expr<T>)path).getType());
            this.path = path;
        }
        public Path<T> getPath(){ return path; }
    }
    
    /**
     * The Class SubQuery.
     */
    public static class SubQuery<A> extends Expr<A> implements Query<SubQuery<A>>, CollectionType<A>{
        @SuppressWarnings("unchecked")
        private QueryWithPublicSelect query = new QueryWithPublicSelect();
        public SubQuery(Expr<A> select) {
            super(null);
            query.s(select);
        }
        @SuppressWarnings("unchecked")
        public SubQuery<A> from(EEntity... o) {query.from(o); return this;}
        public SubQuery<A> fullJoin(EEntity<?> o) {query.fullJoin(o); return this;}
        public QueryBase<JoinMeta,?> getQuery(){ return query;}
        public SubQuery<A> groupBy(Expr<?>... o) {query.groupBy(o); return this;}
        public SubQuery<A> having(EBoolean... o) {query.having(o); return this;}
        public SubQuery<A> innerJoin(EEntity<?> o) {query.innerJoin(o); return this;}
        public SubQuery<A> join(EEntity<?> o) {query.join(o); return this;}
        public SubQuery<A> leftJoin(EEntity<?> o) {query.leftJoin(o); return this;}
        public SubQuery<A> orderBy(OrderSpecifier<?>... o) {query.orderBy(o); return this;}
        public SubQuery<A> select(Expr<?>... o) {query.s(o); return this;}
        public SubQuery<A> where(EBoolean... o) {query.where(o); return this;}
        public SubQuery<A> with(EBoolean o) {query.with(o); return this;}        
    }
    
    private static class QueryWithPublicSelect extends QueryBase<JoinMeta,QueryWithPublicSelect>{
        public void s(Expr<?>... expr){
            select(expr);
        }
    }
    
}
