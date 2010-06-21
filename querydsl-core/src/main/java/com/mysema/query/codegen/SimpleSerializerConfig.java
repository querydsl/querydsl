/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.codegen;

import com.mysema.query.annotations.QuerydslConfig;

/**
 * @author tiwe
 *
 */
public final class SimpleSerializerConfig implements SerializerConfig{

    public static final SerializerConfig DEFAULT = new SimpleSerializerConfig(false, false, false, true);

    public static SerializerConfig getConfig(QuerydslConfig annotation){
        return new SimpleSerializerConfig(
                annotation.entityAccessors(),
                annotation.listAccessors(),
                annotation.mapAccessors(),
                annotation.createDefaultVariable());
    }

    private final boolean entityAccessors, listAccessors, mapAccessors, createDefaultVariable;

    public SimpleSerializerConfig(
            boolean entityAccessors,
            boolean listAccessors,
            boolean mapAccessors,
            boolean createDefaultVariable){
        this.entityAccessors = entityAccessors;
        this.listAccessors = listAccessors;
        this.mapAccessors = mapAccessors;
        this.createDefaultVariable = createDefaultVariable;
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

    @Override
    public boolean createDefaultVariable() {
        return createDefaultVariable;
    }

}
