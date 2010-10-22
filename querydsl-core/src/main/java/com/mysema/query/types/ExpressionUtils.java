/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import java.util.Collection;

import javax.annotation.Nullable;


/**
 * ExpressionUtils provides utilities for constructing common operation instances
 * 
 * @author tiwe
 *
 */
public final class ExpressionUtils {
    
    @Nullable
    public static Predicate allOf(Predicate... exprs){
        Predicate rv = null;
        for (Predicate b : exprs){
            rv = rv == null ? b : ExpressionUtils.and(rv,b);
        }
        return rv;
    }

    public static Predicate and(Predicate left, Predicate right){
        return new PredicateOperation(Ops.AND, left, right);
    }
    
    @Nullable
    public static Predicate anyOf(Predicate... exprs){
        Predicate rv = null;
        for (Predicate b : exprs){
            rv = rv == null ? b : ExpressionUtils.or(rv,b);
        }
        return rv;
    }
    
    public static <D> Expression<D> as(Expression<D> source, Path<D> alias) {
        return new OperationImpl<D>(alias.getType(), Ops.ALIAS, source, alias);
    }
    
    public static <D> Expression<D> as(Expression<D> source, String alias) {
        return as(source, new PathImpl<D>(source.getType(), alias));
    }

    public static <D> Predicate eqConst(Expression<D> left, D constant) {
        return eq(left, new ConstantImpl<D>(constant));
    }
    
    public static <D> Predicate eq(Expression<D> left, Expression<? extends D> right) {
        if (isPrimitive(left.getType())) {
            return new PredicateOperation(Ops.EQ_PRIMITIVE, left, right);
        } else {
            return new PredicateOperation(Ops.EQ_OBJECT, left, right);
        }
    }
    
    public static <D> Predicate in(Expression<D> left, CollectionExpression<?,? extends D> right) {
        return new PredicateOperation(Ops.IN, left, right);
    }
    
    public static <D> Predicate in(Expression<D> left, Collection<? extends D> right) {
        if (right.size() == 1){
            return eqConst(left, right.iterator().next());
        }else{
            return new PredicateOperation(Ops.IN, left, new ConstantImpl<Collection<?>>(right));
        }
    }
    
    public static Predicate isNull(Expression<?> left) {
        return new PredicateOperation(Ops.IS_NULL, left);
    }
    
    public static Predicate isNotNull(Expression<?> left) {
        return new PredicateOperation(Ops.IS_NOT_NULL, left);
    }
    
    private static boolean isPrimitive(Class<?> type){
        return type.isPrimitive()
            || Number.class.isAssignableFrom(type)
            || Boolean.class.equals(type)
            || Character.class.equals(type);
    }
    
    public static <D> Predicate neConst(Expression<D> left, D constant) {
        return ne(left, new ConstantImpl<D>(constant));
    }
    
    public static <D> Predicate ne(Expression<D> left, Expression<? super D> right) {
        if (isPrimitive(left.getType())) {
            return new PredicateOperation(Ops.NE_PRIMITIVE, left, right);
        } else {
            return new PredicateOperation(Ops.NE_OBJECT, left, right);
        }
    }
    
    public static Predicate or(Predicate left, Predicate right){
        return new PredicateOperation(Ops.OR, left, right);
    }
    
    private ExpressionUtils(){}
    
}
