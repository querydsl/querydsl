/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.query;

import com.mysema.query.QueryMetadata;
import com.mysema.query.types.expr.EBoolean;

/**
 * 
 * SubQuery is a sub query
 * 
 * @author tiwe
 * @version $Id$
 * 
 * @param <A>
 */
public interface SubQuery{

    QueryMetadata getMetadata();
 
    EBoolean exists();
    
    EBoolean notExists();
    
}