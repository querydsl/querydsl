/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import com.mysema.query.annotations.QuerydslConfig;

/**
 * @author tiwe
 *
 */
public class SimpleSerializerConfig implements SerializerConfig{
    
    public static final SerializerConfig DEFAULT = new SimpleSerializerConfig(false, false, false);

    public static SerializerConfig getConfig(QuerydslConfig annotation){
        return new SimpleSerializerConfig(
                annotation.entityAccessors(), 
                annotation.listAccessors(), 
                annotation.mapAccessors());
    }
    
    private final boolean entityAccessors, listAccessors, mapAccessors;
    
    private SimpleSerializerConfig(boolean entityAccessors, boolean listAccessors, boolean mapAccessors){
        this.entityAccessors = entityAccessors;
        this.listAccessors = listAccessors;
        this.mapAccessors = mapAccessors;
    }

    @Override
    public boolean useEntityAccessors() {
        return entityAccessors;
    }

    @Override
    public boolean useListAccessors() {
        return listAccessors;
    }
    
    @Override
    public boolean useMapAccessors() {
        return mapAccessors;
    }

}
