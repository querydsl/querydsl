/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mysema.query.grammar.OrderSpecifier;
import com.mysema.query.grammar.types.Expr;
/**
 * QueryBase provides a basic implementation of the Query interface
 *
 * @author tiwe
 * @version $Id$
 */
@SuppressWarnings("unchecked")
public class QueryBase<A extends QueryBase<A>> implements Query<A> {
    protected final List<Expr<?>> groupBy = new ArrayList<Expr<?>>();
    
    protected final List<Expr.Boolean> having = new ArrayList<Expr.Boolean>();
    
    protected final List<JoinExpression> joins = new ArrayList<JoinExpression>();
    protected final List<OrderSpecifier<?>> orderBy = new ArrayList<OrderSpecifier<?>>();
    protected final List<Expr<?>> select = new ArrayList<Expr<?>>();
    protected final CascadingBoolean where = new CascadingBoolean();
    protected void clear(){
        joins.clear();
        groupBy.clear();
        having.clear();
        orderBy.clear();
        select.clear();
        where.clear();
    }
    
    private final Metadata metadata = new Metadata();
    
    public A from(Expr.Entity<?>... o) {
        for (Expr.Entity<?> expr : o){
            joins.add(new JoinExpression(JoinType.DEFAULT,expr));
        }
        return (A) this;
    }
    
    public A groupBy(Expr<?>... o) {
        groupBy.addAll(Arrays.asList(o));
        return (A) this;
    }
        
    public A having(Expr.Boolean... o) {
        having.addAll(Arrays.asList(o));
        return (A) this;
    }
    
    public A innerJoin(Expr.Entity<?> o) {
        joins.add(new JoinExpression(JoinType.INNERJOIN,o));
        return (A) this;
    }
    
    public A fullJoin(Expr.Entity<?> o) {
        joins.add(new JoinExpression(JoinType.FULLJOIN,o));
        return (A) this;
    }
 
    public A join(Expr.Entity<?> o) {
        joins.add(new JoinExpression(JoinType.JOIN,o));
        return (A) this;
    }
 
    public A leftJoin(Expr.Entity<?> o) {
        joins.add(new JoinExpression(JoinType.LEFTJOIN,o));
        return (A) this;
    }
    
    public A orderBy(OrderSpecifier<?>... o) {
        orderBy.addAll(Arrays.asList(o));
        return (A) this;
    }

    public A select(Expr<?>... o) {
        select.addAll(Arrays.asList(o));
        return (A) this;
    }

    public A where(Expr.Boolean... o) {
        for (Expr.Boolean expr : o){
            where.and(expr);
        }
        return (A) this;
    }
    
    public A with(Expr.Boolean... o) {
        if (!joins.isEmpty()){
            CascadingBoolean cb = new CascadingBoolean();
            for (Expr.Boolean expr : o) cb.and(expr);
            joins.get(joins.size()-1).setCondition(cb.self());
        }
        return (A) this;
    }
    
    public Metadata getMetadata(){
        return metadata;
    }

    public class Metadata{
        public List<Expr<?>> getGroupBy() {
            return groupBy;
        }
        public List<Expr.Boolean> getHaving() {
            return having;
        }
        public List<JoinExpression> getJoins() {
            return joins;
        }
        public List<OrderSpecifier<?>> getOrderBy() {
            return orderBy;
        }
        public List<Expr<?>> getSelect() {
            return select;
        }
        public Expr.Boolean getWhere() {
            return where.self();
        }
    }

}
