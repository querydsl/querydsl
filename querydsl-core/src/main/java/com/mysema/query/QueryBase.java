/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mysema.query.grammar.Types.Expr;
import com.mysema.query.grammar.Types.ExprBoolean;
import com.mysema.query.grammar.Types.ExprEntity;
import com.mysema.query.grammar.Types.OrderSpecifier;
/**
 * QueryBase provides a basic implementation of the Query interface
 *
 * @author tiwe
 * @version $Id$
 */
@SuppressWarnings("unchecked")
public class QueryBase<A extends QueryBase<A>> implements Query<A> {
    protected final List<Expr<?>> groupBy = new ArrayList<Expr<?>>();
    
    protected final List<ExprBoolean> having = new ArrayList<ExprBoolean>();
    
    protected final List<JoinExpression> joins = new ArrayList<JoinExpression>();
    protected final List<OrderSpecifier<?>> orderBy = new ArrayList<OrderSpecifier<?>>();
    protected final List<Expr<?>> select = new ArrayList<Expr<?>>();
    protected final List<ExprBoolean> where = new ArrayList<ExprBoolean>();
    protected void clear(){
        joins.clear();
        groupBy.clear();
        having.clear();
        orderBy.clear();
        select.clear();
        where.clear();
    }
    public A from(ExprEntity<?>... o) {
        for (ExprEntity<?> expr : o){
            joins.add(new JoinExpression(JoinType.DEFAULT,expr));
        }
        return (A) this;
    }
    
    public A groupBy(Expr<?>... o) {
        groupBy.addAll(Arrays.asList(o));
        return (A) this;
    }
        
    public A having(ExprBoolean... o) {
        having.addAll(Arrays.asList(o));
        return (A) this;
    }
    
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
    
    public A orderBy(OrderSpecifier<?>... o) {
        orderBy.addAll(Arrays.asList(o));
        return (A) this;
    }

    public A select(Expr<?>... o) {
        select.addAll(Arrays.asList(o));
        return (A) this;
    }

    public A where(ExprBoolean... o) {
        where.addAll(Arrays.asList(o));
        return (A) this;
    }


    public A with(ExprBoolean... o) {
        if (!joins.isEmpty()){
            joins.get(joins.size()-1).conditions = o;
        }
        return (A) this;
    }

    public static class JoinExpression{
        public ExprBoolean[] conditions;
        public final ExprEntity<?> target;
        public final JoinType type;
        JoinExpression(JoinType type, ExprEntity<?> target){
            this.type = type;
            this.target = target;
        }
    }
    
    public enum JoinType{
        DEFAULT,IJ,J,LJ
    }
    
}
