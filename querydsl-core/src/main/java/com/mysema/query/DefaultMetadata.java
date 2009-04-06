/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import java.util.*;

import com.mysema.query.grammar.OrderSpecifier;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Expr.EBoolean;

/**
 * DefaultMetadata provides
 *
 * @author tiwe
 * @version $Id$
 */
public class DefaultMetadata<JoinMeta> implements QueryMetadata<JoinMeta>{

    private final Set<Expr<?>> exprInJoins = new HashSet<Expr<?>>();
    
    private List<Expr<?>> groupBy = new ArrayList<Expr<?>>();
    
    private CascadingBoolean having = new CascadingBoolean();

    private List<JoinExpression<JoinMeta>> joins = new ArrayList<JoinExpression<JoinMeta>>();

    private List<OrderSpecifier<?>> orderBy = new ArrayList<OrderSpecifier<?>>();

    private List<Expr<?>> projection = new ArrayList<Expr<?>>();

    private CascadingBoolean where = new CascadingBoolean();

    public List<? extends Expr<?>> getGroupBy() {
        return Collections.unmodifiableList(groupBy);
    }

    public EBoolean getHaving() {
        return having.create();
    }

    public List<JoinExpression<JoinMeta>> getJoins() {
        return Collections.unmodifiableList(joins);
    }

    public List<OrderSpecifier<?>> getOrderBy() {
        return Collections.unmodifiableList(orderBy);
    }

    public List<? extends Expr<?>> getProjection() {
        return Collections.unmodifiableList(projection);
    }

    public EBoolean getWhere() {
        return where.create();
    }
    
    public void addJoin(JoinExpression<JoinMeta> joinExpression) {
        if (!exprInJoins.contains(joinExpression.getTarget())){
            joins.add(joinExpression);
            exprInJoins.add(joinExpression.getTarget());
        }        
    }

    public void addJoin(JoinType joinType, Expr<?> expr) {
        if (!exprInJoins.contains(expr)){
            joins.add(new JoinExpression<JoinMeta>(joinType, expr));
            exprInJoins.add(expr);
        }                
    }

    public void addJoinCondition(EBoolean o) {
        if (!joins.isEmpty()){
            joins.get(joins.size()-1).setCondition(o);
        }        
    }

    public void addToFrom(Expr<?>[] o) {
        for (Expr<?> expr : o){
            if (!exprInJoins.contains(expr)){
                joins.add(new JoinExpression<JoinMeta>(JoinType.DEFAULT,expr));
                exprInJoins.add(expr);
            }            
        }        
    }

    public void addToGroupBy(Expr<?>[] o) {
        groupBy.addAll(Arrays.<Expr<?>>asList(o));        
    }

    public void addToHaving(EBoolean[] o) {
        for (EBoolean e : o) having.and(e);        
    }

    public void addToOrderBy(OrderSpecifier<?>[] o) {
        orderBy.addAll(Arrays.asList(o));        
    }

    public void addToProjection(Expr<?>[] o) {
        projection.addAll(Arrays.asList(o));        
    }

    public void addToWhere(EBoolean[] o) {
        for (EBoolean e : o) where.and(e);        
    }
      
}
