/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.collections;

import com.mysema.query.Projectable;
import com.mysema.query.SimpleQuery;
import com.mysema.query.types.CollectionExpression;
import com.mysema.query.types.MapExpression;
import com.mysema.query.types.Path;

/**
 * Query interface for Collection queries
 *
 * @author tiwe
 * @version $Id$
 */
public interface ColQuery extends SimpleQuery<ColQuery>, Projectable {

    /**
     * Clone this ColQuery instance and return the clone
     *
     * @return
     */
    ColQuery clone();

    /**
     * Add a query source
     *
     * @param <A>
     * @param entity Path for the source
     * @param col content of the source
     * @return
     */
    <A> ColQuery from(Path<A> entity, Iterable<? extends A> col);

    /**
     * Define an inner join from the Collection typed path to the alias
     *
     * @param <P>
     * @param collectionPath
     * @param alias
     * @return
     */
    <P> ColQuery innerJoin(CollectionExpression<?, P> collectionPath, Path<P> alias);

    /**
     * Define an inner join from the Map typed path to the alias
     *
     * @param <P>
     * @param mapPath
     * @param alias
     * @return
     */
    <P> ColQuery innerJoin(MapExpression<?,P> mapPath, Path<P> alias);

}
