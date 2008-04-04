/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar.types;

import com.mysema.query.Query;
import com.mysema.query.QueryBase;
import com.mysema.query.grammar.OrderSpecifier;
import com.mysema.query.grammar.types.Expr.CollectionType;

/**
 * HqlTypes provides
 *
 * @author tiwe
 * @version $Id$
 */
public class HqlTypes {
    
    public static class Constructor<D> extends Expr<D>{
        private final Expr<?>[] args;
        public Constructor(Class<D> type, Expr<?>... args){
            super(type);
            this.args = args;
        }
        public Expr<?>[] getArgs(){ return args; }
    }
    
    public static class CountExpression extends Expr.Comparable<Long>{
        private final Expr<?> target;
        public CountExpression(Expr<?> expr) {
            super(Long.class);
            this.target = expr;
        }
        public Expr<?> getTarget(){ return target; }
    }
    
    public static class DistinctPath<T> extends Expr<T>{
        private final Path<T> path;
        @SuppressWarnings("unchecked")
        public DistinctPath(Path<T> path) {
            super(((Expr<T>)path).getType());
            this.path = path;
        }
        public Path<T> getPath(){ return path; }
    }
    
    public static class SubQuery<A> extends Expr<A> implements Query<SubQuery<A>>, CollectionType<A>{
        @SuppressWarnings("unchecked")
        private QueryBase<?> query = new QueryBase();
        public SubQuery(Expr<A> select) {
            super(null);
            query.select(select);
        }
        @SuppressWarnings("unchecked")
        public SubQuery<A> from(Entity... o) {query.from(o); return this;}
        public SubQuery<A> fullJoin(Entity<?> o) {query.fullJoin(o); return this;}
        public QueryBase<?> getQuery(){ return query;}
        public SubQuery<A> groupBy(Expr<?>... o) {query.groupBy(o); return this;}
        public SubQuery<A> having(Boolean o) {query.having(o); return this;}
        public SubQuery<A> innerJoin(Entity<?> o) {query.innerJoin(o); return this;}
        public SubQuery<A> join(Entity<?> o) {query.join(o); return this;}
        public SubQuery<A> leftJoin(Entity<?> o) {query.leftJoin(o); return this;}
        public SubQuery<A> orderBy(OrderSpecifier<?>... o) {query.orderBy(o); return this;}
        public SubQuery<A> select(Expr<?>... o) {query.select(o); return this;}
        public SubQuery<A> where(Boolean o) {query.where(o); return this;}
        public SubQuery<A> with(Boolean o) {query.with(o); return this;}
        
    }

}
