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
 * EntityPathBase provides a base class for EntityPath implementations
 *
 * @author tiwe
 *
 * @param <T> entity type
 */
public class EntityPathBase<T> extends BeanPath<T> implements EntityPath<T>{

    private static final long serialVersionUID = -8610055828414880996L;

    public EntityPathBase(Class<? extends T> type, String variable) {
        super(type, variable);
    }

    public EntityPathBase(Class<? extends T> type, PathMetadata<?> metadata) {
        super(type, metadata);
    }

    public EntityPathBase(Class<? extends T> type, PathMetadata<?> metadata, @Nullable PathInits inits) {
        super(type, metadata, inits);
    }

}
