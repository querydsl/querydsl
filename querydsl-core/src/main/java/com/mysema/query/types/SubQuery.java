/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types;

import com.mysema.query.QueryMetadata;

/**
 * 
 * SubQuery represents a sub query. The actual construction of a subquery
 * is done via an Detachable instance.
 * 
 * @author tiwe
 * @version $Id$
 * 
 * @param <T>
 */
public interface SubQuery<T>{

    /**
     * Get an exists(this) expression for the subquery
     * 
     * @return
     */
    // Expr is in lower level package
    EBoolean exists();
 
    /**
     * Get the query metadata for the subquery
     * 
     * @return
     */
    QueryMetadata getMetadata();
    
    /**
     * Get a not exists(this) expression for the subquery
     * 
     * @return
     */
    // Expr is in lower level package
    EBoolean notExists();
    
    /**
     * @return
     */
    // Expr is in lower level package
    Expr<T> asExpr();
    
    /**
     * Create an alias for the query
     * 
     * @param alias
     * @return
     */
    Expr<T> as(Path<T> alias);
    
}