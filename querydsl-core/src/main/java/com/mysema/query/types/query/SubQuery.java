/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.query;

import com.mysema.query.QueryMetadata;

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

    public QueryMetadata getMetadata();
    
}