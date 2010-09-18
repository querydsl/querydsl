package com.mysema.query.jpa;

import java.util.Collection;

import com.mysema.query.Query;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.Path;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.path.MapPath;

/**
 * HQLCommonQuery is a common interface for HQLQuery and HQLSubQuery
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
    <P> Q innerJoin(Path<? extends Collection<P>> target);

    /**
     * Create an inner join with the given target and alias.
     *
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> Q innerJoin(Path<? extends Collection<P>> target, Path<P> alias);

    /**
     * Create an inner join with the given target.
     * Use fetch() to add the fetch parameter to this join.
     *
     * @param <P>
     * @param target
     * @return
     */
    <P> Q innerJoin(MapPath<?, P, ?> target);

    /**
     * Create an inner join with the given target and alias.
     *
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> Q innerJoin(MapPath<?, P, ?> target, Path<P> alias);

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
    <P> Q join(Path<? extends Collection<P>> target);

    /**
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> Q join(Path<? extends Collection<P>> target, Path<P> alias);

    /**
     * Create an join with the given target.
     * Use fetch() to add the fetch parameter to this join.
     *
     * @param <P>
     * @param target
     * @return
     */
    <P> Q join(MapPath<?, P, ?> target);

    /**
     * Create a join with the given target and alias.
     *
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> Q join(MapPath<?, P, ?> target, Path<P> alias);

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
    <P> Q leftJoin(Path<? extends Collection<P>> target);

    /**
     * Create a left join with the given target and alias.
     *
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> Q leftJoin(Path<? extends Collection<P>> target, Path<P> alias);

    /**
     * Create an left join with the given target.
     * Use fetch() to add the fetch parameter to this join.
     *
     * @param <P>
     * @param target
     * @return
     */
    <P> Q leftJoin(MapPath<?, P, ?> target);

    /**
     * Create a left join with the given target and alias.
     *
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> Q leftJoin(MapPath<?, P, ?> target, Path<P> alias);

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
    <P> Q fullJoin(Path<? extends Collection<P>> target);

    /**
     * Create a full join with the given target and alias.
     *
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> Q fullJoin(Path<? extends Collection<P>> target, Path<P> alias);

    /**
     * Create an full join with the given target.
     * Use fetch() to add the fetch parameter to this join.
     *
     * @param <P>
     * @param target
     * @return
     */
    <P> Q fullJoin(MapPath<?, P, ?> target);

    /**
     * Create a full join with the given target and alias.
     *
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> Q fullJoin(MapPath<?, P, ?> target, Path<P> alias);

    /**
     * Add conditions to the with clause
     *
     * @param condition
     * @return
     */
    Q with(BooleanExpression... condition);

}
