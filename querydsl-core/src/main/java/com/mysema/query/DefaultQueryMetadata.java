/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.Path;

/**
 * DefaultQueryMetadata is the default implementation of the QueryMetadata interface
 * 
 * @author tiwe
 * @version $Id$
 */
public class DefaultQueryMetadata implements QueryMetadata {

    private boolean distinct;
    
    private final Set<Expr<?>> exprInJoins = new HashSet<Expr<?>>();

    private final List<Expr<?>> groupBy = new ArrayList<Expr<?>>();

    private final CascadingBoolean having = new CascadingBoolean();

    private final List<JoinExpression> joins = new ArrayList<JoinExpression>();

    @Nullable
    private QueryModifiers modifiers = new QueryModifiers();

    private final List<OrderSpecifier<?>> orderBy = new ArrayList<OrderSpecifier<?>>();

    private final List<Expr<?>> projection = new ArrayList<Expr<?>>();

    private boolean unique;

    private final CascadingBoolean where = new CascadingBoolean();

    @Override
    public void addFrom(Expr<?>... args) {
        for (Expr<?> arg : args) {
            addJoinElement(arg);
        }
    }
    
    @SuppressWarnings("unchecked")
    private void addJoinElement(Expr<?> expr){
        if (expr instanceof Path){
            Path<?> path = (Path<?>)expr;
            if (path.getMetadata().getParent() != null){
                throw new IllegalArgumentException("Only root paths are allowed for from : " + path);
            }
        }
        if (!exprInJoins.contains(expr)) {
            joins.add(new JoinExpression(JoinType.DEFAULT, expr));
            exprInJoins.add(expr);
        }    
    }

    @Override
    public void addGroupBy(Expr<?>... o) {
        groupBy.addAll(Arrays.<Expr<?>> asList(o));
    }

    @Override
    public void addHaving(EBoolean... o) {
        for (EBoolean e : o){
            having.and(e);
        }            
    }

    @Override
    public void addJoin(JoinExpression joinExpression) {
        if (!exprInJoins.contains(joinExpression.getTarget())) {
            joins.add(joinExpression);
            exprInJoins.add(joinExpression.getTarget());
        }
    }

    @Override
    public void addJoin(JoinType joinType, Expr<?> expr) {
        if (!exprInJoins.contains(expr)) {
            joins.add(new JoinExpression(joinType, expr));
            exprInJoins.add(expr);
        }
    }

    @Override
    public void addJoinCondition(EBoolean o) {
        if (!joins.isEmpty()) {
            joins.get(joins.size() - 1).setCondition(o);
        }
    }

    @Override
    public void addOrderBy(OrderSpecifier<?>... o) {
        orderBy.addAll(Arrays.asList(o));
    }

    @Override
    public void addProjection(Expr<?>... o) {
        projection.addAll(Arrays.asList(o));
    }

    @Override
    public void addWhere(EBoolean... o) {
        for (EBoolean e : o){
            where.and(e);
        }            
    }

    @Override
    public List<? extends Expr<?>> getGroupBy() {
        return Collections.unmodifiableList(groupBy);
    }

    @Override
    public EBoolean getHaving() {
        return having.hasValue() ? having : null;
    }

    @Override
    public List<JoinExpression> getJoins() {
        return Collections.unmodifiableList(joins);
    }

    @Override
    @Nullable
    public QueryModifiers getModifiers() {
        return modifiers;
    }

    @Override
    public List<OrderSpecifier<?>> getOrderBy() {
        return Collections.unmodifiableList(orderBy);
    }

    @Override
    public List<? extends Expr<?>> getProjection() {
        return Collections.unmodifiableList(projection);
    }

    @Override
    public EBoolean getWhere() {
        return where.hasValue() ? where : null;
    }

    @Override
    public boolean isDistinct() {
        return distinct;
    }

    @Override
    public boolean isUnique() {
        return unique;
    }

    @Override
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public void setLimit(Long limit) {
        if (modifiers == null || modifiers.getOffset() == null){
            modifiers = QueryModifiers.limit(limit);
        }else{
            modifiers = new QueryModifiers(limit, modifiers.getOffset());
        }        
    }

    @Override
    public void setModifiers(@Nullable QueryModifiers restriction) {
        this.modifiers = restriction;
    }

    @Override
    public void setOffset(Long offset) {
        if (modifiers == null || modifiers.getLimit() == null){
            modifiers = QueryModifiers.offset(offset);
        }else{
            modifiers = new QueryModifiers(modifiers.getLimit(), offset);
        }        
    }

    @Override
    public void setUnique(boolean unique) {
        this.unique = unique;
    }
    
}
