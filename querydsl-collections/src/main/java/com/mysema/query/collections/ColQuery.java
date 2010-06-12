/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import java.util.Collection;

import com.mysema.query.Projectable;
import com.mysema.query.Query;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PMap;

/**
 * Query interface for Collection queries
 * 
 * @author tiwe
 * @version $Id$
 */
public interface ColQuery extends Query<ColQuery>, Projectable {
    
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
    <P> ColQuery innerJoin(Path<? extends Collection<P>> collectionPath, Path<P> alias);

    /**
     * Define an inner join from the Map typed path to the alias
     * 
     * @param <P>
     * @param mapPath
     * @param alias
     * @return
     */
    <P> ColQuery innerJoin(PMap<?,P,?> mapPath, Path<P> alias);
    
}
