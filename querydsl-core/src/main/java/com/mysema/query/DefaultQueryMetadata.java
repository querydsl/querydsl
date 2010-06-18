/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import com.mysema.query.types.Expr;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Param;
import com.mysema.query.types.Path;
import com.mysema.query.types.expr.EBoolean;

import edu.umd.cs.findbugs.annotations.SuppressWarnings;

/**
 * DefaultQueryMetadata is the default implementation of the {@link QueryMetadata} interface
 * 
 * @author tiwe
 */
//TODO : rename to DefaultQueryModel in Querydsl 2.0
public class DefaultQueryMetadata implements QueryMetadata, Cloneable {
    
    private static final long serialVersionUID = 317736313966701232L;

    private boolean distinct;
    
    private Set<Expr<?>> exprInJoins = new HashSet<Expr<?>>();

    private List<Expr<?>> groupBy = new ArrayList<Expr<?>>();

    private BooleanBuilder having = new BooleanBuilder();

    private List<JoinExpression> joins = new ArrayList<JoinExpression>();

    @Nullable
    private QueryModifiers modifiers = new QueryModifiers();

    private List<OrderSpecifier<?>> orderBy = new ArrayList<OrderSpecifier<?>>();

    private List<Expr<?>> projection = new ArrayList<Expr<?>>();

    private Map<Param<?>,Object> params = new HashMap<Param<?>,Object>();
    
    private boolean unique;

    private BooleanBuilder where = new BooleanBuilder();
        
    @Override
    public void addGroupBy(Expr<?>... o) {
        groupBy.addAll(Arrays.<Expr<?>> asList(o));
    }
    
    @Override
    public void addHaving(EBoolean... o) {
        for (EBoolean e : o){
            if (!BooleanBuilder.class.isInstance(e) || ((BooleanBuilder)e).hasValue()){
                having.and(e);    
            }            
        }            
    }

    @java.lang.SuppressWarnings("unchecked")
    @SuppressWarnings("unchecked")
    @Override
    public void addJoin(JoinType joinType, Expr<?> expr) {
        if (!exprInJoins.contains(expr)) {
            if (expr instanceof Path && joinType == JoinType.DEFAULT){
                ensureRoot((Path<?>) expr);
            }
            joins.add(new JoinExpression(joinType, expr));
            exprInJoins.add(expr);
        }
    }


    @Override
    public void addJoinCondition(EBoolean o) {
        if (!joins.isEmpty()) {
            joins.get(joins.size() - 1).addCondition(o);
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
            if (!BooleanBuilder.class.isInstance(e) || ((BooleanBuilder)e).hasValue()){
                where.and(e);    
            }            
        }            
    }    

    public void clearOrderBy(){
        orderBy = new ArrayList<OrderSpecifier<?>>();
    }
    
    public void clearProjection(){
        projection = new ArrayList<Expr<?>>();
    }
    
    public void clearWhere(){
        where = new BooleanBuilder();
    }
    
    @Override
    public QueryMetadata clone(){
        try {
            DefaultQueryMetadata clone = (DefaultQueryMetadata) super.clone();
            clone.exprInJoins = new HashSet<Expr<?>>(exprInJoins);
            clone.groupBy = new ArrayList<Expr<?>>(groupBy);
            clone.having = having.clone();
            clone.joins = new ArrayList<JoinExpression>(joins); 
            clone.modifiers = new QueryModifiers(modifiers);
            clone.orderBy = new ArrayList<OrderSpecifier<?>>(orderBy);
            clone.projection = new ArrayList<Expr<?>>(projection);
            clone.params = new HashMap<Param<?>,Object>(params);
            clone.where = where.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new QueryException(e);
        }
        
    }

    private void ensureRoot(Path<?> path){
        if (path.getMetadata().getParent() != null){
            throw new IllegalArgumentException("Only root paths are allowed for joins : " + path);
        }
    }

    @Override
    public List<? extends Expr<?>> getGroupBy() {
        return Collections.unmodifiableList(groupBy);
    }

    @Override
    public EBoolean getHaving() {
        return having.hasValue() ? having.getValue() : null;
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

    public Map<Param<?>,Object> getParams(){
        return params;
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
        return where.hasValue() ? where.getValue() : null;
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
    public void reset() {
        clearProjection();
        params = new HashMap<Param<?>,Object>();
        modifiers = new QueryModifiers();
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

    @Override
    public <T> void setParam(Param<T> param, T value) {
        params.put(param, value);
    }
    
}
