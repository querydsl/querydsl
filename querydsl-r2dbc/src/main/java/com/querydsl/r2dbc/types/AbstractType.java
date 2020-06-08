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

import io.r2dbc.spi.Row;
import io.r2dbc.spi.Statement;

/**
 * Common abstract superclass for Type implementations
 *
 * @param <T>
 * @author tiwe
 */
public abstract class AbstractType<T> implements Type<T> {

    private final int type;

    public AbstractType(int type) {
        this.type = type;
    }

    @Override
    public final int[] getSQLTypes() {
        return new int[]{type};
    }

    @Override
    public String getLiteral(T value) {
        return value.toString();
    }

    @Override
    public T getValue(Row row, int startIndex) {
        return row.get(startIndex, getReturnedClass());
    }

    @Override
    public void setValue(Statement st, int startIndex, T value) {
        if (value == null) {
            st.bindNull(startIndex, getReturnedClass());
        } else {
            st.bind(startIndex, toDbValue(value));
        }
    }

    protected Object toDbValue(T value) {
        return value;
    }

}
