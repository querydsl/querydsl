/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.impl;

import javax.annotation.Nullable;

import com.mysema.query.collections.IteratorSource;
import com.mysema.query.types.expr.EBoolean;

/**
 * QueryIndexSupport enables the injection of indexed query source lookup into
 * collection query instances
 * 
 * @see DefaultIndexSupport
 * 
 * @author tiwe
 * @version $Id$
 */
public interface QueryIndexSupport extends IteratorSource {

    /**
     * Get an IteratorSource optimized for the given query condition
     * 
     * @param orderedSources
     * @param condition
     */
    IteratorSource getChildFor(@Nullable EBoolean condition);

}
