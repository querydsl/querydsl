/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.alias;

import com.mysema.query.types.EntityPath;

/**
 * MagagedObject is a tagging interface for CGLIB alias proxies
 *
 */
public interface ManagedObject {

    /**
     * @return
     */
    EntityPath<?> __mappedPath();

}
