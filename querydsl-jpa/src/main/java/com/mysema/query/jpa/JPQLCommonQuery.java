package com.mysema.query.jpa;

import java.util.Collection;

import com.mysema.query.Query;
import com.mysema.query.types.CollectionExpression;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.MapExpression;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;

/**
 * JPQLCommonQuery is a common interface for HQLQuery and HQLSubQuery
 * 
 * @author tiwe
 *
 * @param <Q>
 */
public interface JPQLCommonQuery<Q extends JPQLCommonQuery<Q>>  extends Query<Q> {
    

    /**
     * Set the sources of this query
     *
     * @param sources
     * @return
     */
    Q from(EntityPath<?>... sources);

    /**
     * Create an inner join with the given target.
     * Use fetch() to add the fetch parameter to this join.
     *
     *
     * @param <P>
     * @param target
     * @return
     */
    <P> Q innerJoin(EntityPath<P> target);

    /**
     * Create an inner join with the given target and alias.
     *
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> Q innerJoin(EntityPath<P> target, EntityPath<P> alias);

    /**
     * Create an inner join with the given target.
     * Use fetch() to add the fetch parameter to this join.
     *
     * @param <P>
     * @param target
     * @return
     */
    <P> Q innerJoin(CollectionExpression<?,?> target);

    /**
     * Create an inner join with the given target and alias.
     *
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> Q innerJoin(CollectionExpression<?,P> target, Path<P> alias);

    /**
     * Create an inner join with the given target.
     * Use fetch() to add the fetch parameter to this join.
     *
     * @param <P>
     * @param target
     * @return
     */
    <P> Q innerJoin(MapExpression<?, P> target);

    /**
     * Create an inner join with the given target and alias.
     *
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> Q innerJoin(MapExpression<?, P> target, Path<P> alias);

    /**
     * Create an join with the given target.
     * Use fetch() to add the fetch parameter to this join.
     *
     * @param <P>
     * @param target
     * @return
     */
    <P> Q join(EntityPath<P> target);

    /**
     * Create a join with the given target and alias.
     *
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> Q join(EntityPath<P> target, EntityPath<P> alias);

    /**
     * Create an join with the given target.
     * Use fetch() to add the fetch parameter to this join.
     *
     * @param <P>
     * @param target
     * @return
     */
    <P> Q join(CollectionExpression<?,P> target);

    /**
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> Q join(CollectionExpression<?,P> target, Path<P> alias);

    /**
     * Create an join with the given target.
     * Use fetch() to add the fetch parameter to this join.
     *
     * @param <P>
     * @param target
     * @return
     */
    <P> Q join(MapExpression<?, P> target);

    /**
     * Create a join with the given target and alias.
     *
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> Q join(MapExpression<?, P> target, Path<P> alias);

    /**
     * Create an left join with the given target.
     * Use fetch() to add the fetch parameter to this join.
     *
     * @param <P>
     * @param target
     * @return
     */
    <P> Q leftJoin(EntityPath<P> target);

    /**
     * Create a left join with the given target and alias.
     *
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> Q leftJoin(EntityPath<P> target, EntityPath<P> alias);

    /**
     * Create an left join with the given target.
     * Use fetch() to add the fetch parameter to this join.
     *
     * @param <P>
     * @param target
     * @return
     */
    <P> Q leftJoin(CollectionExpression<?,P> target);

    /**
     * Create a left join with the given target and alias.
     *
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> Q leftJoin(CollectionExpression<?,P> target, Path<P> alias);

    /**
     * Create an left join with the given target.
     * Use fetch() to add the fetch parameter to this join.
     *
     * @param <P>
     * @param target
     * @return
     */
    <P> Q leftJoin(MapExpression<?, P> target);

    /**
     * Create a left join with the given target and alias.
     *
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> Q leftJoin(MapExpression<?, P> target, Path<P> alias);

    /**
     * Create an full join with the given target.
     * Use fetch() to add the fetch parameter to this join.
     *
     * @param <P>
     * @param target
     * @return
     */
    <P> Q fullJoin(EntityPath<P> target);

    /**
     * Create a full join with the given target and alias.
     *
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> Q fullJoin(EntityPath<P> target, EntityPath<P> alias);

    /**
     * Create an full join with the given target.
     * Use fetch() to add the fetch parameter to this join.
     *
     * @param <P>
     * @param target
     * @return
     */
    <P> Q fullJoin(CollectionExpression<?,?> target);

    /**
     * Create a full join with the given target and alias.
     *
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> Q fullJoin(CollectionExpression<?,P> target, Path<P> alias);

    /**
     * Create an full join with the given target.
     * Use fetch() to add the fetch parameter to this join.
     *
     * @param <P>
     * @param target
     * @return
     */
    <P> Q fullJoin(MapExpression<?, P> target);

    /**
     * Create a full join with the given target and alias.
     *
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> Q fullJoin(MapExpression<?, P> target, Path<P> alias);

    /**
     * Add conditions to the with clause
     *
     * @param condition
     * @return
     */
    Q with(Predicate... condition);

}
