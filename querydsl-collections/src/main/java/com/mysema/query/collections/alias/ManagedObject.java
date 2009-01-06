package com.mysema.query.collections.alias;


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
