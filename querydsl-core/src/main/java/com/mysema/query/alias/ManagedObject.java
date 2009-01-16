/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.alias;


/**
 * MagagedObject provides
 *
 * @author tiwe
 * @version $Id$
 */
public interface ManagedObject {
    
    void setElementType(Class<?> type);
    
    void setKeyType(Class<?> type);
    
    void setValueType(Class<?> type);
        
}
