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
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> ColQuery innerJoin(Path<? extends Collection<P>> target, Path<P> alias);

}
