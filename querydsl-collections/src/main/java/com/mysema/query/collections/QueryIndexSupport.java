/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import com.mysema.query.collections.support.DefaultIndexSupport;
import com.mysema.query.grammar.types.Expr.EBoolean;

/**
 * QueryIndexSupport enables the injection of indexed query source lookup into 
 * collection query instances
 *
 * @see DefaultIndexSupport
 *
 * @author tiwe
 * @version $Id$
 */
public interface QueryIndexSupport extends IteratorSource{
    
    /**
     * Update the local indices for the given query condition
     * 
     * @param orderedSources
     * @param condition
     */
    void updateFor(EBoolean condition);
    
}
