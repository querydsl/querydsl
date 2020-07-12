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

import java.sql.Types;

/**
 * {@code EnumByOrdinalType} maps Enum types to their Integer ordinals on the JDBC level
 *
 * @param <T>
 * @author mc_fish
 */
public class EnumByOrdinalType<T extends Enum<T>> extends AbstractType<T, Integer> {

    private final Class<T> type;

    public EnumByOrdinalType(Class<T> type) {
        this(Types.INTEGER, type);
    }

    public EnumByOrdinalType(int jdbcType, Class<T> type) {
        super(jdbcType);
        this.type = type;
    }

    @Override
    public Class<T> getReturnedClass() {
        return type;
    }

    @Override
    public T getValue(Row row, int startIndex) {
        Integer val = row.get(startIndex, Integer.class);
        return val != null ? type.getEnumConstants()[val] : null;
    }

    @Override
    protected Integer toDbValue(T value) {
        return value.ordinal();
    }

    @Override
    public Class<Integer> getDatabaseClass() {
        return Integer.class;
    }

}
