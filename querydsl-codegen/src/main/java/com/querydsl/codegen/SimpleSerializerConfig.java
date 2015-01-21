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
package com.querydsl.codegen;

import com.querydsl.core.annotations.Config;

/**
 * SimpleSerializerConfig is the default implementation of the {@link SerializerConfig} interface
 * 
 * @author tiwe
 *
 */
public final class SimpleSerializerConfig implements SerializerConfig{

    public static final SerializerConfig DEFAULT = new SimpleSerializerConfig(false, false, false, true, "");

    public static SerializerConfig getConfig(Config annotation) {
        return new SimpleSerializerConfig(
                annotation.entityAccessors(),
                annotation.listAccessors(),
                annotation.mapAccessors(),
                annotation.createDefaultVariable(),
                annotation.defaultVariableName());
    }

    private final boolean entityAccessors, listAccessors, mapAccessors, createDefaultVariable;
    private final String defaultVariableName;

    public SimpleSerializerConfig(
            boolean entityAccessors,
            boolean listAccessors,
            boolean mapAccessors,
            boolean createDefaultVariable,
            String defaultVariableName) {
        this.entityAccessors = entityAccessors;
        this.listAccessors = listAccessors;
        this.mapAccessors = mapAccessors;
        this.createDefaultVariable = createDefaultVariable;
        this.defaultVariableName = defaultVariableName;
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

    @Override
    public String defaultVariableName() {
        return defaultVariableName;
    }
}
