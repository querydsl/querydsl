/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import java.util.ArrayList;
import java.util.List;

import com.mysema.query.grammar.Types.ExprForBoolean;
import com.mysema.query.grammar.Types.ExprForEntity;
import com.mysema.query.grammar.Types.Expr;
import com.mysema.query.grammar.Types.OrderSpecifier;
/**
 * QueryBase provides a basic implementation of the Query interface
 *
 * @author tiwe
 * @version $Id$
 */
@SuppressWarnings("unchecked")
public class QueryBase<A extends QueryBase<A>> implements Query<A> {
    public enum JoinType{
        DEFAULT,IJ,LJ,J
    }
    
    public class JoinExpression{
        public final JoinType type;
        public final ExprForEntity<?> target;
        JoinExpression(JoinType type, ExprForEntity<?> target){
            this.type = type;
            this.target = target;
        }
        public ExprForBoolean[] conditions;
    }
    
    protected List<JoinExpression> joins = new ArrayList<JoinExpression>();
    protected Expr<?>[] groupBy;
    protected ExprForBoolean[] having;
    protected OrderSpecifier<?>[] orderBy;
    protected Expr<?>[] select;
    protected ExprForBoolean[] where;
    
    protected void clear(){
        joins.clear();
        groupBy = null;
        having = null;
        orderBy = null;
        select = null;
        where = null;
    }
    
    public A from(ExprForEntity<?>... objects) {
        for (ExprForEntity<?> expr : objects){
            joins.add(new JoinExpression(JoinType.DEFAULT,expr));
        }
        return (A) this;
    }

    public A groupBy(Expr<?>... objects) {
        groupBy = objects;
        return (A) this;
    }

    public A having(ExprForBoolean... objects) {
        having = objects;
        return (A) this;
    }


    public A orderBy(OrderSpecifier<?>... objects) {
        orderBy = objects;
        return (A) this;
    }

    public A select(Expr<?>... objects) {
        select = objects;
        return (A) this;
    }
    
    public A where(ExprForBoolean... objects) {
        where = objects;
        return (A) this;
    }
    
}
