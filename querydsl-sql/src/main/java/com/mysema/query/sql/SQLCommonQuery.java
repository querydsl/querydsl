/*
 * 
 */
package com.mysema.query.sql;

import com.mysema.query.Query;
import com.mysema.query.types.Expr;
import com.mysema.query.types.Path;
import com.mysema.query.types.SubQuery;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.path.PEntity;

/**
 * SQLCommonQuery is a common interface for SQLQuery and SQLSubQuery
 * 
 * @author tiwe
 *
 * @param <Q>
 */
public interface SQLCommonQuery<Q extends SQLCommonQuery<Q>> extends Query<Q> {
    
    /**
     * Defines the sources of the query
     *
     * @param o
     * @return
     */
    Q from(Expr<?>... o);

    /**
     * Adds a full join to the given target
     *
     * @param o
     * @return
     */
    Q fullJoin(PEntity<?> o);

    /**
     * Adds an inner join to the given target
     *
     * @param o
     * @return
     */
    Q innerJoin(PEntity<?> o);

    /**
     * Adds a join to the given target
     *
     * @param o
     * @return
     */
    Q join(PEntity<?> o);

    /**
     * Adds a left join to the given target
     *
     * @param o
     * @return
     */
    Q leftJoin(PEntity<?> o);

    /**
     * Adds a right join to the given target
     *
     * @param o
     * @return
     */
    Q rightJoin(PEntity<?> o);

    /**
     * Adds a full join to the given target
     *
     * @param o
     * @return
     */
    <E> Q fullJoin(ForeignKey<E> key, PEntity<E> entity);

    /**
     * Adds an inner join to the given target
     *
     * @param o
     * @return
     */
    <E> Q innerJoin(ForeignKey<E> foreign, PEntity<E> entity);

    /**
     * Adds a join to the given target
     *
     * @param o
     * @return
     */
    <E> Q join(ForeignKey<E> foreign, PEntity<E> entity);

    /**
     * Adds a left join to the given target
     *
     * @param o
     * @return
     */
    <E> Q leftJoin(ForeignKey<E> foreign, PEntity<E> entity);

    /**
     * Adds a right join to the given target
     *
     * @param o
     * @return
     */
    <E> Q rightJoin(ForeignKey<E> foreign, PEntity<E> entity);

    /**
     * Adds a full join to the given target
     *
     * @param o
     * @return
     */
    Q fullJoin(SubQuery<?> o, Path<?> alias);

    /**
     * Adds an inner join to the given target
     *
     * @param o
     * @return
     */
    Q innerJoin(SubQuery<?> o, Path<?> alias);

    /**
     * Adds a join to the given target
     *
     * @param o
     * @return
     */
    Q join(SubQuery<?> o, Path<?> alias);

    /**
     * Adds a left join to the given target
     *
     * @param o
     * @return
     */
    Q leftJoin(SubQuery<?> o, Path<?> alias);

    /**
     * Adds a right join to the given target
     *
     * @param o
     * @return
     */
    Q rightJoin(SubQuery<?> o, Path<?> alias);

    /**
     * Defines a filter to the last added join
     *
     * @param conditions
     * @return
     */
    Q on(EBoolean... conditions);


}
