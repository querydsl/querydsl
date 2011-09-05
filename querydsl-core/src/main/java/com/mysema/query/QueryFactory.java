package com.mysema.query;

import com.mysema.query.types.query.Detachable;

/**
 * Common interface for QueryFactory implementations
 * 
 * @author tiwe
 * 
 * @param <Q>
 * @param <SQ>
 */
public interface QueryFactory<Q extends Query<?>, SQ extends Detachable> {
    /**
     * Create a new Query
     * 
     * @return
     */
    Q query();

    /**
     * Create a new Sub query
     * 
     * @return
     */
    SQ subQuery();
}