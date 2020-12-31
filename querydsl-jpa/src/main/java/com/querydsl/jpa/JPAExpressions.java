/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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
package com.querydsl.jpa;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.CollectionExpression;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionException;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.PathType;
import com.querydsl.core.types.dsl.BeanPath;
import com.querydsl.core.types.dsl.ComparableExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;

import javax.persistence.Entity;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;

/**
 * {@code JPAExpressions} provides factory methods for JPQL specific operations
 * elements.
 *
 * @author tiwe
 */
@SuppressWarnings("unchecked")
public final class JPAExpressions {

    /**
     * Create a new detached JPQLQuery instance with the given projection
     *
     * @param expr projection
     * @param <T>
     * @return select(expr)
     */
    public static <T> JPQLQuery<T> select(Expression<T> expr) {
        return new JPASubQuery<Void>().select(expr);
    }

    /**
     * Create a new detached JPQLQuery instance with the given projection
     *
     * @param exprs projection
     * @return select(exprs)
     */
    public static JPQLQuery<Tuple> select(Expression<?>... exprs) {
        return new JPASubQuery<Void>().select(exprs);
    }

    /**
     * Create a new detached JPQLQuery instance with the given projection
     *
     * @param expr projection
     * @param <T>
     * @return select(distinct expr)
     */
    public static <T> JPQLQuery<T> selectDistinct(Expression<T> expr) {
        return new JPASubQuery<Void>().select(expr).distinct();
    }

    /**
     * Create a new detached JPQLQuery instance with the given projection
     *
     * @param exprs projection
     * @return select(distinct expr)
     */
    public static JPQLQuery<Tuple> selectDistinct(Expression<?>... exprs) {
        return new JPASubQuery<Void>().select(exprs).distinct();
    }

    /**
     * Create a new detached JPQLQuery instance with the projection zero
     *
     * @return select(0)
     */
    public static JPQLQuery<Integer> selectZero() {
        return select(Expressions.ZERO);
    }

    /**
     * Create a new detached JPQLQuery instance with the projection one
     *
     * @return select(1)
     */
    public static JPQLQuery<Integer> selectOne() {
        return select(Expressions.ONE);
    }

    /**
     * Create a new detached JPQLQuery instance with the given projection
     *
     * @param expr projection and source
     * @param <T>
     * @return select(expr).from(expr)
     */
    public static <T> JPQLQuery<T> selectFrom(EntityPath<T> expr) {
        return select(expr).from(expr);
    }

    /**
     * Create a JPA 2.1 treated path.
     *
     * @param path The path to apply the treat operation on
     * @param subtype subtype class
     * @param <U> the subtype class
     * @param <T> the expression type
     * @return subtype instance with the same identity
     */
    public static <U extends BeanPath<? extends T>, T> U treat(BeanPath<? extends T> path, Class<U> subtype)  {
        try {
            Class<? extends T> entitySubType = getConcreteEntitySubType(subtype);
            PathMetadata pathMetadata = new PathMetadata(path, getEntityName(entitySubType), PathType.TREATED_PATH);
            return subtype.getConstructor(PathMetadata.class).newInstance(pathMetadata);
        } catch (InstantiationException e) {
            throw new ExpressionException(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            throw new ExpressionException(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            throw new ExpressionException(e.getMessage(), e);
        } catch (NoSuchMethodException e) {
            throw new ExpressionException(e.getMessage(), e);
        }
    }

    /**
     * Create a avg(col) expression
     *
     * @param col collection
     * @return avg(col)
     */
    public static <A extends Comparable<? super A>> ComparableExpression<A> avg(CollectionExpression<?,A> col) {
        return Expressions.comparableOperation((Class) col.getParameter(0), Ops.QuantOps.AVG_IN_COL, (Expression<?>) col);
    }

    /**
     * Create a max(col) expression
     *
     * @param left collection
     * @return max(col)
     */
    public static <A extends Comparable<? super A>> ComparableExpression<A> max(CollectionExpression<?,A> left) {
        return Expressions.comparableOperation((Class) left.getParameter(0), Ops.QuantOps.MAX_IN_COL, (Expression<?>) left);
    }

    /**
     * Create a min(col) expression
     *
     * @param left collection
     * @return min(col)
     */
    public static <A extends Comparable<? super A>> ComparableExpression<A> min(CollectionExpression<?,A> left) {
        return Expressions.comparableOperation((Class) left.getParameter(0), Ops.QuantOps.MIN_IN_COL, (Expression<?>) left);
    }

    /**
     * Create a type(path) expression
     *
     * @param path entity
     * @return type(path)
     */
    public static StringExpression type(EntityPath<?> path) {
        return Expressions.stringOperation(JPQLOps.TYPE, path);
    }

    private static String getEntityName(Class<?> clazz) {
        final Entity entityAnnotation = clazz.getAnnotation(Entity.class);
        if (entityAnnotation != null && entityAnnotation.name().length() > 0) {
            return entityAnnotation.name();
        } else if (clazz.getPackage() != null) {
            String pn = clazz.getPackage().getName();
            return clazz.getName().substring(pn.length() + 1);
        } else {
            return clazz.getName();
        }
    }

    private static <U extends BeanPath<? extends T>, T> Class<? extends T> getConcreteEntitySubType(Class<U> subtype) {
        return (Class<? extends T>) ((ParameterizedType) subtype.getGenericSuperclass()).getActualTypeArguments()[0];
    }

    private JPAExpressions() { }

}
