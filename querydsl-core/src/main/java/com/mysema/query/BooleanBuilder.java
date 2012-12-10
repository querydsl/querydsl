/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mysema.query;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Objects;
import com.mysema.query.types.Expression;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.ExtractorVisitor;
import com.mysema.query.types.Operation;
import com.mysema.query.types.Operator;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.Visitor;

/**
 * BooleanBuilder is a cascading builder for {@link Predicate} expressions. BooleanBuilder is a mutable
 * Expression implementation.
 * 
 * <p>Usage example:</p>
 * <pre>
 * QEmployee employee = QEmployee.employee;
 * BooleanBuilder builder = new BooleanBuilder();
 * for (String name : names) {
 *     builder.or(employee.name.equalsIgnoreCase(name));      
 * }
 * </pre>
 *
 * @author tiwe
 */
public final class BooleanBuilder implements Predicate, Cloneable, Operation<Boolean> {

    private static final long serialVersionUID = -4129485177345542519L;

    @Nullable
    private Predicate predicate;

    /**
     * Create an empty BooleanBuilder
     */
    public BooleanBuilder() {  }

    /**
     * Create a BooleanBuilder with the given initial value
     * 
     * @param initial
     */
    public BooleanBuilder(Predicate initial) {
        predicate = (Predicate) initial.accept(ExtractorVisitor.DEFAULT, null);
    }

    @Override
    public <R,C> R accept(Visitor<R,C> v, C context) {
        if (predicate != null) {
            return predicate.accept(v, context);
        } else {
            return null;
        }
    }

    /**
     * Create the insertion of this and the given predicate
     *
     * @param right
     * @return
     */
    public BooleanBuilder and(@Nullable Predicate right) {
        if (right != null) {
            if (predicate == null) {
                predicate = right;
            } else {
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
        if (args.length > 0) {
            and(ExpressionUtils.anyOf(args));
        }
        return this;
    }

    /**
     * Create the insertion of this and the negation of the given predicate
     *
     * @param right
     * @return
     */
    public BooleanBuilder andNot(Predicate right) {
        return and(right.not());
    }

    @Override
    public BooleanBuilder clone() throws CloneNotSupportedException{
        return (BooleanBuilder) super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof BooleanBuilder) {
            return Objects.equal(((BooleanBuilder)o).getValue(), predicate);
        } else {
            return false;
        }
    }

    @Override
    public Expression<?> getArg(int index) {
        if (index == 0) {
            return predicate;
        } else {
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
        return predicate != null ? predicate.hashCode() : 0;
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
        if (predicate != null) {
            predicate = predicate.not();
        }
        return this;
    }

    /**
     * Create the union of this and the given predicate
     *
     * @param right
     * @return
     */
    public BooleanBuilder or(@Nullable Predicate right) {
        if (right != null) {
            if (predicate == null) {
                predicate = right;
            } else {
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
        if (args.length > 0) {
            or(ExpressionUtils.allOf(args));
        }
        return this;
    }

    /**
     * Create the union of this and the negation of the given predicate
     *
     * @param right
     * @return
     */
    public BooleanBuilder orNot(Predicate right){
        return or(right.not());
    }

    @Override
    public Class<? extends Boolean> getType() {
        return Boolean.class;
    }

    @Override
    public String toString(){
        return predicate != null ? predicate.toString() : super.toString();
    }

}
