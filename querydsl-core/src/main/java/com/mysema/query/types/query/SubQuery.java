/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.query;

import com.mysema.query.QueryMetadata;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.Path;

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
    EBoolean notExists();
    
    /**
     * @return
     */
    Expr<T> asExpr();
    
    /**
     * Create an alias for the query
     * 
     * @param alias
     * @return
     */
    Expr<T> as(Path<T> alias);
    
}