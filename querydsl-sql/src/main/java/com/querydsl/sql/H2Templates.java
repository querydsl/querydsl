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
package com.querydsl.sql;

import java.sql.Types;

import com.querydsl.core.types.Ops;

/**
 * H2Templates is an SQL dialect for H2
 *
 * @author tiwe
 *
 */
public class H2Templates extends SQLTemplates {

    @SuppressWarnings("FieldNameHidesFieldInSuperclass") //Intentional
    public static final H2Templates DEFAULT = new H2Templates();

    public static Builder builder() {
        return new Builder() {
            @Override
            protected SQLTemplates build(char escape, boolean quote) {
                return new H2Templates(escape, quote);
            }
        };
    }

    public H2Templates() {
        this('\\', false);
    }

    public H2Templates(boolean quote) {
        this('\\', quote);
    }

    public H2Templates(char escape, boolean quote) {
        super("\"", escape, quote);
        setNativeMerge(true);
        setLimitRequired(true);
        setCountDistinctMultipleColumns(true);

        add(Ops.MathOps.ROUND, "round({0},0)");
        add(Ops.TRIM, "trim(both from {0})");

        add(Ops.DateTimeOps.DAY_OF_WEEK, "day_of_week({0})");

        add(Ops.MathOps.LN, "log({0})");
        add(Ops.MathOps.LOG, "(log({0}) / log({1}))");
        add(Ops.MathOps.COTH, "(cosh({0}) / sinh({0}))");

        add(Ops.DateTimeOps.DATE, "convert({0}, date)");

        addTypeNameToCode("result_set", -10);
        addTypeNameToCode("identity", Types.BIGINT);
        addTypeNameToCode("uuid", Types.BINARY);
        addTypeNameToCode("serial", Types.INTEGER);
        addTypeNameToCode("varchar_ignorecase", Types.VARCHAR);
    }

}
