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

import com.querydsl.r2dbc.binding.BindMarker;
import com.querydsl.r2dbc.binding.BindTarget;
import io.r2dbc.spi.Row;

/**
 * Common abstract superclass for Type implementations
 *
 * @param <IN>
 * @param <OUT>
 * @author mc_fish
 */
public abstract class AbstractType<IN, OUT> implements Type<IN, OUT> {

    private final int type;

    public AbstractType(int type) {
        this.type = type;
    }

    @Override
    public final int[] getSQLTypes() {
        return new int[]{type};
    }

    @Override
    public String getLiteral(IN value) {
        return value.toString();
    }

    @Override
    public IN getValue(Row row, int startIndex) {
        OUT value = row.get(startIndex, getDatabaseClass());
        if (value == null) {
            return null;
        }

        return fromDbValue(value);
    }

    @Override
    public void setValue(BindMarker bindMarker, BindTarget bindTarget, IN value) {
        bindMarker.bind(bindTarget, toDbValue(value));
    }

    @Override
    public Class<OUT> getDatabaseClass() {
        return (Class<OUT>) getReturnedClass();
    }

    protected OUT toDbValue(IN value) {
        return (OUT) value;
    }

    protected IN fromDbValue(OUT value) {
        return (IN) value;
    }

}
