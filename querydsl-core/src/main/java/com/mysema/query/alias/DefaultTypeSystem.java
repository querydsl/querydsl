/*
 * Copyright (c) 2011 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.alias;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author tiwe
 *
 */
public class DefaultTypeSystem implements TypeSystem{
    
    @Override
    public boolean isCollectionType(Class<?> cl) {
        return Collection.class.isAssignableFrom(cl);
    }

    @Override
    public boolean isListType(Class<?> cl) {
        return List.class.isAssignableFrom(cl);
    }
    
    @Override
    public boolean isSetType(Class<?> cl) {
        return Set.class.isAssignableFrom(cl);
    }

    @Override
    public boolean isMapType(Class<?> cl) {
        return Map.class.isAssignableFrom(cl);
    }

}
