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
package com.querydsl.core.types;

import java.util.Map;

import com.google.common.collect.ImmutableList;

/**
 * Factory class for FactoryExpression instances
 * 
 * @author tiwe
 *
 */
public final class Projections {

    /**
     * Create a typed array projection for the given type and expressions
     * 
     * @param <T> 
     * @param type type of the projection
     * @param exprs arguments for the projection
     * @return
     */
    public static <T> ArrayConstructorExpression<T> array(Class<T[]> type, Expression<T>... exprs) {
        return new ArrayConstructorExpression<T>(type, exprs);
    }

    /**
     * Create an appending factory expression which serializes all the arguments but the uses
     * the base value as the return value
     *
     * @param base
     * @param rest
     * @param <T>
     * @return
     */
    public static <T> AppendingFactoryExpression<T> appending(Expression<T> base, Expression<?>... rest) {
        return new AppendingFactoryExpression<T>(base, rest);
    }
    
    /**
     * Create a Bean populating projection for the given type and expressions
     * 
     * <p>Example</p>
     * <pre>
     * UserDTO dto = query.singleResult(
     *     Projections.bean(UserDTO.class, user.firstName, user.lastName));
     * </pre>
     * 
     * @param <T>
     * @param type type of the projection
     * @param exprs arguments for the projection
     * @return
     */
    public static <T> QBean<T> bean(Class<T> type, Expression<?>... exprs) {
        return new QBean<T>(type, exprs);
    }
    
    /**
     * Create a Bean populating projection for the given type and expressions
     * 
     * @param <T>
     * @param type type of the projection
     * @param exprs arguments for the projection
     * @return
     */
    public static <T> QBean<T> bean(Path<T> type, Expression<?>... exprs) {
        return new QBean<T>(type, exprs);
    }

    /**
     * Create a Bean populating projection for the given type and bindings
     *
     * @param type
     * @param bindings
     */
    public static <T> QBean<T> bean(Path<T> type, Map<String, ? extends Expression<?>> bindings) {
        return new QBean<T>(type, bindings);
    }

    /**
     * Create a Bean populating projection for the given type and bindings
     *
     * @param type
     * @param bindings
     */
    public static <T> QBean<T> bean(Class<T> type, Map<String, ? extends Expression<?>> bindings) {
        return new QBean<T>(type, bindings);
    }


    /**
     * Create a constructor invocation projection for the given type and expressions
     * 
     * <p>Example</p>
     * <pre>
     * UserDTO dto = query.singleResult(
     *     Projections.constructor(UserDTO.class, user.firstName, user.lastName));
     * </pre>
     * 
     * @param <T>
     * @param type type of the projection
     * @param exprs arguments for the projection
     * @return
     */
    public static <T> ConstructorExpression<T> constructor(Class<T> type, Expression<?>... exprs) {
        return ConstructorExpression.create(type, exprs);
    }

    /**
     * Create a constructor invocation projection for given type, parameter types and expressions
     *
     * @param type
     * @param paramTypes
     * @param exprs
     * @param <T>
     * @return
     */
    public static <T> ConstructorExpression<T> constructor(Class<T> type, Class<?>[] paramTypes, Expression<?>... exprs) {
        return new ConstructorExpression<T>(type, paramTypes, exprs);
    }

    /**
     * Create a constructor invocation projection for given type, parameter types and expressions
     *
     * @param type
     * @param paramTypes
     * @param exprs
     * @param <T>
     * @return
     */
    public static <T> ConstructorExpression<T> constructor(Class<T> type, Class<?>[] paramTypes, ImmutableList<Expression<?>> exprs) {
        return new ConstructorExpression<T>(type, paramTypes, exprs);
    }

    /**
     * Create a field access based Bean populating projection for the given type and expressions
     * 
     * <p>Example</p>
     * <pre>
     * UserDTO dto = query.singleResult(
     *     Projections.fields(UserDTO.class, user.firstName, user.lastName));
     * </pre>
     * 
     * @param <T>
     * @param type type of the projection
     * @param exprs arguments for the projection
     * @return
     */
    public static <T> QBean<T> fields(Class<T> type, Expression<?>... exprs) {
        return new QBean<T>(type, true, exprs);
    }
    
    /**
     * Create a field access based Bean populating projection for the given type and expressions
     * 
     * @param <T>
     * @param type type of the projection
     * @param exprs arguments for the projection
     * @return
     */
    public static <T> QBean<T> fields(Path<T> type, Expression<?>... exprs) {
        return new QBean<T>(type, true, exprs);
    }

    /**
     * Create a field access based  Bean populating projection for the given type and bindings
     *
     * @param type
     * @param bindings
     */
    public static <T> QBean<T> fields(Path<T> type, Map<String, ? extends Expression<?>> bindings) {
        return new QBean<T>(type, true, bindings);
    }

    /**
     * Create a field access based Bean populating projection for the given type and bindings
     *
     * @param type
     * @param bindings
     */
    public static <T> QBean<T> fields(Class<T> type, Map<String, ? extends Expression<?>> bindings) {
        return new QBean<T>(type, true, bindings);
    }

    /**
     * Create a new List typed projection for the given expressions
     *
     * @param args
     * @return
     */
    public static QList list(Expression<?>... args) {
        return new QList(args);
    }

    /**
     * Create a new List typed projection for the given expressions
     *
     * @param args
     * @return
     */
    public static QList list(ImmutableList<Expression<?>> args) {
        return new QList(args);
    }

    /**
     * Create a new List typed projection for the given expressions
     *
     * @param args
     * @return
     */
    public static QList list(Expression<?>[]... args) {
        return new QList(args);
    }

    /**
     * Create a Map typed projection for the given expressions
     * 
     * <p>Example</p>
     * <pre>{@code
     * Map<Expression<?>, ?> map = query.singleResult(
     *      Projections.map(user.firstName, user.lastName));
     * }</pre>
     * 
     * @param exprs arguments for the projection
     * @return
     */
    public static QMap map(Expression<?>... exprs) {
        return new QMap(exprs);
    }
    
    /**
     * Create a Tuple typed projection for the given expressions
     * 
     * @param exprs arguments for the projection
     * @return
     */
    public static QTuple tuple(Expression<?>... exprs) {
        return new QTuple(exprs);
    }

    /**
     * Create a Tuple typed projection for the given expressions
     *
     * @param exprs arguments for the projection
     * @return
     */
    public static QTuple tuple(ImmutableList<Expression<?>> exprs) {
        return new QTuple(exprs);
    }

    /**
     * Create a Tuple typed projection for the given expressions
     *
     * @param exprs arguments for the projection
     * @return
     */
    public static QTuple tuple(Expression<?>[]... exprs) {
        return new QTuple(exprs);
    }
    
    private Projections() {}
}
