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

import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.ComparableExpression;
import com.mysema.query.types.expr.DateExpression;
import com.mysema.query.types.expr.DateTimeExpression;
import com.mysema.query.types.expr.NumberExpression;
import com.mysema.query.types.expr.StringExpression;
import com.mysema.query.types.expr.TimeExpression;
import com.mysema.query.types.query.BooleanSubQuery;
import com.mysema.query.types.query.ComparableSubQuery;
import com.mysema.query.types.query.DateSubQuery;
import com.mysema.query.types.query.DateTimeSubQuery;
import com.mysema.query.types.query.ListSubQuery;
import com.mysema.query.types.query.NumberSubQuery;
import com.mysema.query.types.query.SimpleSubQuery;
import com.mysema.query.types.query.StringSubQuery;
import com.mysema.query.types.query.TimeSubQuery;

/**
 * Detachable defines methods for the construction of SubQuery instances
 *
 * @author tiwe
 *
 */
public interface Detachable {

    /**
     * Return the count of matched rows as a sub query
     *
     * <p>Usage</p>
     *
     * {@code query.where(subQuery.from(customer).where(...).count().gt(1)) }
     *
     * @return
     */
    NumberSubQuery<Long> count();

    /**
     * Create an exists(this) expression
     *
     * <p>Usage</p>
     *
     * {@code query.where(subQuery.from(customer).where(...).exists()) }
     *
     * @return
     */
    BooleanExpression exists();

    /**
     * Create a multi row subquery expression for the given projection
     *
     * <p>Usage</p>
     *
     * {@code subQuery.from(person).list(person.firstName, person.lastName).countDistinct() }
     *
     * @param args
     * @return a view of the subquery result as a list
     */
    ListSubQuery<Tuple> list(Expression<?>... args);
    
    /**
     * Create a multi row subquery expression for the given projection
     * <p>Non expression arguments are converted into constant expressions</p>
     *
     * <p>Usage</p>
     *
     * {@code subQuery.from(person).list(person.firstName, "M")}
     * 
     * @param args
     * @return a view of the subquery result as a list
     */
    ListSubQuery<Tuple> list(Object... args);

    /**
     * Create a multi row subquery expression for the given projection
     *
     * <p>Usage</p>
     *
     * {@code customer.name.in(subQuery.from(customer).where(...).list(customer.name))}
     *
     * @param <RT>
     *            generic type of the List
     * @param projection
     * @return a view of the subquery result as a list
     */
    <RT> ListSubQuery<RT> list(Expression<RT> projection);
    

    /**
     * Create an not exists(this) expression
     *
     * <p>Usage</p>
     *
     * {@code query.where(subQuery.from(customer).where(...).notExists()) }
     *
     * @return
     */
    BooleanExpression notExists();

    /**
     * Create a single row subquery expression for the given projection
     *
     * <p>Usage</p>
     *
     * {@code subQuery.from(person).unique(person.firstName, person.lastName)}
     *
     * @param args
     * @return
     */
    SimpleSubQuery<Tuple> unique(Expression<?>... args);
    
    /**
     * Create a single row subquery expression for the given projection
     * <p>Non expression arguments are converted into constant expressions</p>
     *
     * <p>Usage</p>
     *
     * {@code subQuery.from(person).unique(person.firstName, "M")}
     * 
     * @param args
     * @return a view of the subquery result as a single value
     */
    SimpleSubQuery<Tuple> unique(Object... args);

    /**
     * Create a single row subquery expression for the given projection
     *
     * <p>Usage</p>
     *
     * {@code person.age.eq(subQuery.from(person).unique(person.age.max())) }
     *
     * @param <RT>
     *            return type
     * @param projection
     * @return a view of the subquery result as a single value
     */
    <RT> SimpleSubQuery<RT> unique(Expression<RT> projection);

    /**
     * Create a single row subquery expression for the given projection
     *
     * @param projection
     * @return a view of the subquery result as a single value
     * @see #unique(com.mysema.query.types.Expression)
     */
    BooleanSubQuery unique(Predicate projection);

    /**
     * Create a single row subquery expression for the given projection
     *
     * @param projection
     * @return a view of the subquery result as a single value
     * @see #unique(com.mysema.query.types.Expression)
     */
    StringSubQuery unique(StringExpression projection);

    /**
     * Create a single row subquery expression for the given projection
     *
     * @param <RT>
     * @param projection
     * @return a view of the subquery result as a single value
     * @see #unique(com.mysema.query.types.Expression)
     */
    <RT extends Comparable<?>> ComparableSubQuery<RT> unique(ComparableExpression<RT> projection);

    /**
     * Create a single row subquery expression for the given projection
     *
     * @param <RT>
     * @param projection
     * @return a view of the subquery result as a single value
     * @see #unique(com.mysema.query.types.Expression)
     */
    <RT extends Comparable<?>> DateSubQuery<RT> unique(DateExpression<RT> projection);

    /**
     * Create a single row subquery expression for the given projection
     *
     * @param <RT>
     * @param projection
     * @return a view of the subquery result as a single value
     * @see #unique(com.mysema.query.types.Expression)
     */
    <RT extends Comparable<?>> DateTimeSubQuery<RT> unique(DateTimeExpression<RT> projection);

    /**
     * Create a single row subquery expression for the given projection
     *
     * @param <RT>
     * @param projection
     * @return a view of the subquery result as a single value
     * @see #unique(com.mysema.query.types.Expression)
     */
    <RT extends Comparable<?>> TimeSubQuery<RT> unique(TimeExpression<RT> projection);

    /**
     * Create a single row subquery expression for the given projection
     *
     * @param <RT>
     * @param projection
     * @return a view of the subquery result as a single value
     * @see #unique(com.mysema.query.types.Expression)
     */
    <RT extends Number & Comparable<?>> NumberSubQuery<RT> unique(NumberExpression<RT> projection);

}
