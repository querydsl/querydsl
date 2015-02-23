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
package com.querydsl.jpa;

import com.querydsl.core.FetchableQuery;
import com.querydsl.core.Query;
import com.querydsl.core.support.ExtendedSubQuery;
import com.querydsl.core.types.*;

/**
 * Query interface for JPQL queries
 *
 * @author tiwe
 *
 */
public interface JPQLQuery<T> extends FetchableQuery<T, JPQLQuery<T>>, Query<JPQLQuery<T>>, ExtendedSubQuery<T> {

    /**
     * Set the sources of this query
     *
     * @param sources
     * @return
     */
    JPQLQuery<T> from(EntityPath<?>... sources);

    /**
     *
     * @param target
     * @param alias
     * @param <P>
     * @return
     */
    <P> JPQLQuery<T> from(CollectionExpression<?,P> target, Path<P> alias);

    /**
     * Create a inner join with the given target.
     * Use fetchJoin() to add the fetchJoin parameter to this join.
     *
     *
     * @param <P>
     * @param target
     * @return
     */
    <P> JPQLQuery<T> innerJoin(EntityPath<P> target);

    /**
     * Create a inner join with the given target and alias.
     *
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> JPQLQuery<T> innerJoin(EntityPath<P> target, Path<P> alias);

    /**
     * Create a inner join with the given target.
     * Use fetchJoin() to add the fetchJoin parameter to this join.
     *
     * @param <P>
     * @param target
     * @return
     */
    <P> JPQLQuery<T> innerJoin(CollectionExpression<?, P> target);

    /**
     * Create a inner join with the given target and alias.
     *
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> JPQLQuery<T> innerJoin(CollectionExpression<?,P> target, Path<P> alias);

    /**
     * Create a inner join with the given target.
     * Use fetchJoin() to add the fetchJoin parameter to this join.
     *
     * @param <P>
     * @param target
     * @return
     */
    <P> JPQLQuery<T> innerJoin(MapExpression<?, P> target);

    /**
     * Create a inner join with the given target and alias.
     *
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> JPQLQuery<T> innerJoin(MapExpression<?, P> target, Path<P> alias);

    /**
     * Create a join with the given target.
     * Use fetchJoin() to add the fetchJoin parameter to this join.
     *
     * @param <P>
     * @param target
     * @return
     */
    <P> JPQLQuery<T> join(EntityPath<P> target);

    /**
     * Create a join with the given target and alias.
     *
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> JPQLQuery<T> join(EntityPath<P> target, Path<P> alias);

    /**
     * Create a join with the given target.
     * Use fetchJoin() to add the fetchJoin parameter to this join.
     *
     * @param <P>
     * @param target
     * @return
     */
    <P> JPQLQuery<T> join(CollectionExpression<?,P> target);

    /**
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> JPQLQuery<T> join(CollectionExpression<?,P> target, Path<P> alias);

    /**
     * Create a join with the given target.
     * Use fetchJoin() to add the fetchJoin parameter to this join.
     *
     * @param <P>
     * @param target
     * @return
     */
    <P> JPQLQuery<T> join(MapExpression<?, P> target);

    /**
     * Create a join with the given target and alias.
     *
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> JPQLQuery<T> join(MapExpression<?, P> target, Path<P> alias);

    /**
     * Create a left join with the given target.
     * Use fetchJoin() to add the fetchJoin parameter to this join.
     *
     * @param <P>
     * @param target
     * @return
     */
    <P> JPQLQuery<T> leftJoin(EntityPath<P> target);

    /**
     * Create a left join with the given target and alias.
     *
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> JPQLQuery<T> leftJoin(EntityPath<P> target, Path<P> alias);

    /**
     * Create a left join with the given target.
     * Use fetchJoin() to add the fetchJoin parameter to this join.
     *
     * @param <P>
     * @param target
     * @return
     */
    <P> JPQLQuery<T> leftJoin(CollectionExpression<?,P> target);

    /**
     * Create a left join with the given target and alias.
     *
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> JPQLQuery<T> leftJoin(CollectionExpression<?,P> target, Path<P> alias);

    /**
     * Create a left join with the given target.
     * Use fetchJoin() to add the fetchJoin parameter to this join.
     *
     * @param <P>
     * @param target
     * @return
     */
    <P> JPQLQuery<T> leftJoin(MapExpression<?, P> target);

    /**
     * Create a left join with the given target and alias.
     *
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> JPQLQuery<T> leftJoin(MapExpression<?, P> target, Path<P> alias);

    /**
     * Create a right join with the given target.
     * Use fetchJoin() to add the fetchJoin parameter to this join.
     *
     * @param <P>
     * @param target
     * @return
     */
    <P> JPQLQuery<T> rightJoin(EntityPath<P> target);

    /**
     * Create a right join with the given target and alias.
     *
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> JPQLQuery<T> rightJoin(EntityPath<P> target, Path<P> alias);

    /**
     * Create a right join with the given target.
     * Use fetchJoin() to add the fetchJoin parameter to this join.
     *
     * @param <P>
     * @param target
     * @return
     */
    <P> JPQLQuery<T> rightJoin(CollectionExpression<?,P> target);

    /**
     * Create a right join with the given target and alias.
     *
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> JPQLQuery<T> rightJoin(CollectionExpression<?,P> target, Path<P> alias);

    /**
     * Create a right join with the given target.
     * Use fetchJoin() to add the fetchJoin parameter to this join.
     *
     * @param <P>
     * @param target
     * @return
     */
    <P> JPQLQuery<T> rightJoin(MapExpression<?, P> target);

    /**
     * Create a right join with the given target and alias.
     *
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> JPQLQuery<T> rightJoin(MapExpression<?, P> target, Path<P> alias);

    /**
     * Add join conditions to the last added join
     *
     * @param condition
     * @return
     */
    JPQLQuery<T> on(Predicate... condition);

    /**
     * Add the "fetchJoin" flag to the last defined join
     *
     * Mind that collection joins might result in duplicate rows and that "inner join fetchJoin"
     * will restrict your result set.
     *
     * @return
     */
    JPQLQuery<T> fetchJoin();

    /**
      * Add the "fetchJoin all properties" flag to the last defined join.
      * @return
       */
    JPQLQuery<T> fetchAll();

}
