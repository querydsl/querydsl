/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import com.mysema.query.Projectable;
import com.mysema.query.Query;
import com.mysema.query.types.path.Path;

/**
 * Query interface for Collection queries
 * 
 * @author tiwe
 * @version $Id$
 */
public interface ColQuery extends Query<ColQuery>, Projectable {

    /**
     * Add a query source
     * 
     * @param <A>
     * @param entity Path for the source
     * @param col content of the source
     * @return
     */
    <A> ColQuery from(Path<A> entity, Iterable<? extends A> col);

}
