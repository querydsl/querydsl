package com.mysema.query.hql;

import java.util.Collection;

import com.mysema.query.Query;
import com.mysema.query.types.Path;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PMap;

/**
 * HQLCommonQuery is a common interface for HQLQuery and HQLSubQuery
 * 
 * @author tiwe
 *
 * @param <Q>
 */
public interface HQLCommonQuery<Q extends HQLCommonQuery<Q>>  extends Query<Q> {
    

    /**
     * Set the sources of this query
     *
     * @param sources
     * @return
     */
    Q from(PEntity<?>... sources);

    /**
     * Create an inner join with the given target.
     * Use fetch() to add the fetch parameter to this join.
     *
     *
     * @param <P>
     * @param target
     * @return
     */
    <P> Q innerJoin(PEntity<P> target);

    /**
     * Create an inner join with the given target and alias.
     *
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> Q innerJoin(PEntity<P> target, PEntity<P> alias);

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
    <P> Q innerJoin(PMap<?, P, ?> target);

    /**
     * Create an inner join with the given target and alias.
     *
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> Q innerJoin(PMap<?, P, ?> target, Path<P> alias);

    /**
     * Create an join with the given target.
     * Use fetch() to add the fetch parameter to this join.
     *
     * @param <P>
     * @param target
     * @return
     */
    <P> Q join(PEntity<P> target);

    /**
     * Create a join with the given target and alias.
     *
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> Q join(PEntity<P> target, PEntity<P> alias);

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
    <P> Q join(PMap<?, P, ?> target);

    /**
     * Create a join with the given target and alias.
     *
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> Q join(PMap<?, P, ?> target, Path<P> alias);

    /**
     * Create an left join with the given target.
     * Use fetch() to add the fetch parameter to this join.
     *
     * @param <P>
     * @param target
     * @return
     */
    <P> Q leftJoin(PEntity<P> target);

    /**
     * Create a left join with the given target and alias.
     *
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> Q leftJoin(PEntity<P> target, PEntity<P> alias);

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
    <P> Q leftJoin(PMap<?, P, ?> target);

    /**
     * Create a left join with the given target and alias.
     *
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> Q leftJoin(PMap<?, P, ?> target, Path<P> alias);

    /**
     * Create an full join with the given target.
     * Use fetch() to add the fetch parameter to this join.
     *
     * @param <P>
     * @param target
     * @return
     */
    <P> Q fullJoin(PEntity<P> target);

    /**
     * Create a full join with the given target and alias.
     *
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> Q fullJoin(PEntity<P> target, PEntity<P> alias);

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
    <P> Q fullJoin(PMap<?, P, ?> target);

    /**
     * Create a full join with the given target and alias.
     *
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> Q fullJoin(PMap<?, P, ?> target, Path<P> alias);

    /**
     * Add conditions to the with clause
     *
     * @param condition
     * @return
     */
    Q with(EBoolean... condition);

}
