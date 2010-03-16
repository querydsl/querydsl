/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.alias;

import com.mysema.query.types.path.PEntity;

/**
 * MagagedObject is a tagging interface for CGLIB alias proxies
 * 
 * @version $Id$
 */
public interface ManagedObject {

    /**
     * @return
     */
    PEntity<?> __mappedPath();

}
