/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.lang.ObjectUtils;

import com.mysema.query.types.Expression;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Operation;
import com.mysema.query.types.Operator;
import com.mysema.query.types.PathType;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.BooleanExpression;

/**
 * BooleanBuilder is a cascading builder for {@link BooleanExpression} expressions.
 *
 * @author tiwe
 */
public final class BooleanBuilder extends BooleanExpression implements Cloneable, Operation<Boolean>{

    private static final long serialVersionUID = -4129485177345542519L;

    @Nullable
    private Predicate expr;

    public BooleanBuilder() {  }

    public BooleanBuilder(BooleanExpression initial){
        expr = initial;
    }

    @Override
    public <R,C> R accept(Visitor<R,C> v, C context) {
        if (expr != null){
            return expr.accept(v, context);
        }else{
            throw new QueryException("BooleanBuilder has no value");
        }
    }
    
    @Override
    public BooleanBuilder and(@Nullable Predicate right) {
        if (right != null){
            if (expr == null){
                expr = right;
            }else{
                expr = ExpressionUtils.and(expr, right);
            }
        }
        return this;
    }

    /**
     * Create the intersection of this and the union of the given args
     * <p>(this && (arg1 || arg2 ... || argN))</p>
     *
     * @param args
     * @return
     */
    public BooleanBuilder andAnyOf(Predicate... args) {
        if (args.length > 0){
            and(ExpressionUtils.anyOf(args));
        }
        return this;
    }

    public BooleanBuilder andNot(Predicate right) {
        return and(ExpressionUtils.not(right));
    }

    @Override
    public BooleanBuilder clone() throws CloneNotSupportedException{
        return (BooleanBuilder) super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this){
            return true;
        }else if (o instanceof BooleanBuilder){
            return ObjectUtils.equals(((BooleanBuilder)o).getValue(), expr);
        }else{
            return false;
        }
    }

    @Override
    public Expression<?> getArg(int index) {
        if (index == 0){
            return expr;
        }else{
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public List<Expression<?>> getArgs() {
        return Collections.<Expression<?>>singletonList(expr);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Operator<? super Boolean> getOperator() {
        return (Operator)PathType.DELEGATE;
    }

    @Nullable
    public Predicate getValue(){
        return expr;
    }

    @Override
    public int hashCode(){
        return expr != null ? expr.hashCode() : super.hashCode();
    }

    /**
     * Returns true if the value is set, and false, if not
     *
     * @return
     */
    public boolean hasValue(){
        return expr != null;
    }

    @Override
    public BooleanBuilder not(){
        if (expr != null){
            expr = ExpressionUtils.not(expr);
        }
        return this;
    }

    @Override
    public BooleanBuilder or(@Nullable Predicate right) {
        if (right != null){
            if (expr == null){
                expr = right;
            }else{
                expr = ExpressionUtils.or(expr, right);
            }
        }
        return this;
    }

    /**
     * Create the union of this and the intersection of the given args
     * <p>(this || (arg1 && arg2 ... && argN))</p>
     *
     * @param args
     * @return
     */
    public BooleanBuilder orAllOf(Predicate... args) {
        if (args.length > 0){
            or(ExpressionUtils.allOf(args));
        }
        return this;
    }

    public BooleanBuilder orNot(BooleanExpression right){
        return or(right.not());
    }
    
}
