/*
 * 
 */
package com.mysema.query.sql;

import com.mysema.query.Query;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.SubQueryExpression;

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
    Q from(Expression<?>... o);

    /**
     * Adds a full join to the given target
     *
     * @param o
     * @return
     */
    Q fullJoin(RelationalPath<?> o);

    /**
     * Adds an inner join to the given target
     *
     * @param o
     * @return
     */
    Q innerJoin(RelationalPath<?> o);

    /**
     * Adds a join to the given target
     *
     * @param o
     * @return
     */
    Q join(RelationalPath<?> o);

    /**
     * Adds a left join to the given target
     *
     * @param o
     * @return
     */
    Q leftJoin(RelationalPath<?> o);

    /**
     * Adds a right join to the given target
     *
     * @param o
     * @return
     */
    Q rightJoin(RelationalPath<?> o);

    /**
     * Adds a full join to the given target
     *
     * @param o
     * @return
     */
    <E> Q fullJoin(ForeignKey<E> key, RelationalPath<E> entity);

    /**
     * Adds an inner join to the given target
     *
     * @param o
     * @return
     */
    <E> Q innerJoin(ForeignKey<E> foreign, RelationalPath<E> entity);

    /**
     * Adds a join to the given target
     *
     * @param o
     * @return
     */
    <E> Q join(ForeignKey<E> foreign, RelationalPath<E> entity);

    /**
     * Adds a left join to the given target
     *
     * @param o
     * @return
     */
    <E> Q leftJoin(ForeignKey<E> foreign, RelationalPath<E> entity);

    /**
     * Adds a right join to the given target
     *
     * @param o
     * @return
     */
    <E> Q rightJoin(ForeignKey<E> foreign, RelationalPath<E> entity);

    /**
     * Adds a full join to the given target
     *
     * @param o
     * @return
     */
    Q fullJoin(SubQueryExpression<?> o, Path<?> alias);

    /**
     * Adds an inner join to the given target
     *
     * @param o
     * @return
     */
    Q innerJoin(SubQueryExpression<?> o, Path<?> alias);

    /**
     * Adds a join to the given target
     *
     * @param o
     * @return
     */
    Q join(SubQueryExpression<?> o, Path<?> alias);

    /**
     * Adds a left join to the given target
     *
     * @param o
     * @return
     */
    Q leftJoin(SubQueryExpression<?> o, Path<?> alias);

    /**
     * Adds a right join to the given target
     *
     * @param o
     * @return
     */
    Q rightJoin(SubQueryExpression<?> o, Path<?> alias);

    /**
     * Defines a filter to the last added join
     *
     * @param conditions
     * @return
     */
    Q on(Predicate... conditions);


}
