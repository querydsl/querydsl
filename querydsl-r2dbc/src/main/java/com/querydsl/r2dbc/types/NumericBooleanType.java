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

import javax.annotation.Nullable;
import java.sql.Types;

/**
 * {@code NumericBooleanType} maps Boolean to 1/0 (Integer) on the JDBC level
 *
 * @author mc_fish
 */
public class NumericBooleanType extends AbstractType<Boolean, Integer> {

    public static final NumericBooleanType DEFAULT = new NumericBooleanType();

    public NumericBooleanType() {
        super(Types.INTEGER);
    }

    public NumericBooleanType(int type) {
        super(type);
    }

    @Override
    public Class<Boolean> getReturnedClass() {
        return Boolean.class;
    }

    @Override
    public String getLiteral(Boolean value) {
        return value ? "1" : "0";
    }

    @Override
    @Nullable
    public Boolean getValue(Row row, int startIndex) {
        Number num = row.get(startIndex, Number.class);
        return num != null ? num.intValue() == 1 : null;
    }

    @Override
    protected Integer toDbValue(Boolean value) {
        return value ? 1 : 0;
    }

    @Override
    public Class<Integer> getDatabaseClass() {
        return Integer.class;
    }

}
