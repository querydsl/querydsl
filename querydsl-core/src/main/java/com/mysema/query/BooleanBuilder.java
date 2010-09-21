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
import com.mysema.query.types.Ops;
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
    private Predicate predicate;

    public BooleanBuilder() {  }

    public BooleanBuilder(BooleanExpression initial){
        predicate = initial;
    }

    @Override
    public <R,C> R accept(Visitor<R,C> v, C context) {
        if (predicate != null){
            return predicate.accept(v, context);
        }else{
            throw new QueryException("BooleanBuilder has no value");
        }
    }
    
    @Override
    public BooleanBuilder and(@Nullable Predicate right) {
        if (right != null){
            if (predicate == null){
                predicate = right;
            }else{
                predicate = ExpressionUtils.and(predicate, right);
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
        return and(right.not());
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
            return ObjectUtils.equals(((BooleanBuilder)o).getValue(), predicate);
        }else{
            return false;
        }
    }

    @Override
    public Expression<?> getArg(int index) {
        if (index == 0){
            return predicate;
        }else{
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public List<Expression<?>> getArgs() {
        return Collections.<Expression<?>>singletonList(predicate);
    }

    @Override
    public Operator<? super Boolean> getOperator() {
        return Ops.DELEGATE;
    }

    @Nullable
    public Predicate getValue(){
        return predicate;
    }

    @Override
    public int hashCode(){
        return predicate != null ? predicate.hashCode() : super.hashCode();
    }

    /**
     * Returns true if the value is set, and false, if not
     *
     * @return
     */
    public boolean hasValue(){
        return predicate != null;
    }

    @Override
    public BooleanBuilder not(){
        if (predicate != null){
            predicate = predicate.not();
        }
        return this;
    }

    @Override
    public BooleanBuilder or(@Nullable Predicate right) {
        if (right != null){
            if (predicate == null){
                predicate = right;
            }else{
                predicate = ExpressionUtils.or(predicate, right);
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

    public BooleanBuilder orNot(Predicate right){
        return or(right.not());
    }
    
}
