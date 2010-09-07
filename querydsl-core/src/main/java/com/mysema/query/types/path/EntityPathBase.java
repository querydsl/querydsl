/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.path;

import javax.annotation.Nullable;

import com.mysema.query.types.EntityPath;
import com.mysema.query.types.PathMetadata;

/**
 * @author tiwe
 *
 * @param <D>
 */
public class EntityPathBase<D> extends BeanPath<D> implements EntityPath<D>{
    
    private static final long serialVersionUID = -8610055828414880996L;
    
    public EntityPathBase(Class<? extends D> type, String variable) {
        super(type, variable);
    }

    public EntityPathBase(Class<? extends D> type, PathMetadata<?> metadata) {
        super(type, metadata);
    }

    public EntityPathBase(Class<? extends D> type, PathMetadata<?> metadata, @Nullable PathInits inits) {
        super(type, metadata, inits);
    }

}
