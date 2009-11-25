/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;

/**
 * Path represents a path expression
 * 
 * @author tiwe
 * @version $Id$
 */
public interface Path<C> {
    /**
     * @return
     */
    Expr<C> asExpr();

    /**
     * Get the metadata for this path
     * 
     * @return
     */
    PathMetadata<?> getMetadata();

    /**
     * Get the root for this path
     * 
     * @return
     */
    Path<?> getRoot();

    /**
     * Get the type of this path
     * 
     * @return
     */
    Class<? extends C> getType();

    /**
     * Create a <code>this is not null</code> expression
     * 
     * @return
     */
    EBoolean isNotNull();
    
    /**
     * Create a <code>this is null</code> expression
     * 
     * 
     * @return
     */
    EBoolean isNull();

}
