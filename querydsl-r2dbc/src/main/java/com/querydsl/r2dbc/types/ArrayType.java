/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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
package com.querydsl.r2dbc.types;

import com.google.common.primitives.Primitives;
import com.querydsl.r2dbc.binding.BindMarker;
import com.querydsl.r2dbc.binding.BindTarget;
import io.r2dbc.spi.Row;

import javax.annotation.Nullable;
import java.sql.Types;

/**
 * {@code ArrayType} maps Java arrays to JDBC arrays
 *
 * @param <T>
 */
public class ArrayType<T> extends AbstractType<T, T> {

    private static void copy(Object source, Object target, int length) {
        // Note: System.arrayCopy doesn't handle copying from/to primitive arrays properly
        for (int i = 0; i < length; i++) {
            Object val = java.lang.reflect.Array.get(source, i);
            java.lang.reflect.Array.set(target, i, val);
        }
    }

    private final Class<T> type;

    private final String typeName;

    private final boolean convertPrimitives;

    public ArrayType(Class<T> type, String typeName) {
        super(Types.ARRAY);
        this.type = type;
        this.typeName = typeName;
        this.convertPrimitives = type.getComponentType().isPrimitive();
    }

    @Override
    public Class<T> getReturnedClass() {
        return type;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public T getValue(Row row, int startIndex) {
        T[] value = (T[]) row.get(startIndex);
        if (value == null) {
            return null;
        }

        if (convertPrimitives) {
            // primitives out
            Object rv2 = java.lang.reflect.Array.newInstance(type.getComponentType(), value.length);
            copy(value, rv2, value.length);
            return (T) rv2;
        }
        return (T) value;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setValue(BindMarker bindMarker, BindTarget bindTarget, T value) {
        if (convertPrimitives) {
            // primitives in
            int length = java.lang.reflect.Array.getLength(value);
            Object value2 = java.lang.reflect.Array.newInstance(Primitives.wrap(type.getComponentType()), length);
            copy(value, value2, length);
            value = (T) value2;
        }

        super.setValue(bindMarker, bindTarget, value);
    }
}
