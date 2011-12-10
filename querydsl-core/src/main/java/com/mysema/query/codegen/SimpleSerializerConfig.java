/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mysema.query.codegen;

import net.jcip.annotations.Immutable;

import com.mysema.query.annotations.Config;

/**
 * SimpleSerializerConfig is the default implementation of the SerializerConfig interface
 * 
 * @author tiwe
 *
 */
@Immutable
public final class SimpleSerializerConfig implements SerializerConfig{

    public static final SerializerConfig DEFAULT = new SimpleSerializerConfig(false, false, false, true);

    public static SerializerConfig getConfig(Config annotation) {
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
            boolean createDefaultVariable) {
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
