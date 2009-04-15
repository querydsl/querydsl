/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.alias;

import com.mysema.query.grammar.types.Path.PEntity;


/**
 * MagagedObject is a tagging interface for CGLIB alias proxies
 * 
 * @version $Id$
 */
public interface ManagedObject {
    
    PEntity<?> __mappedPath();
    
}
