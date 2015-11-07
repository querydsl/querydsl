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
package com.querydsl.sql;

import java.sql.Types;

import com.querydsl.core.types.Ops;

/**
 * {@code H2Templates} is an SQL dialect for H2
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
        super(Keywords.H2, "\"", escape, quote);
        setNativeMerge(true);
        setMaxLimit(2 ^ 31);
        setLimitRequired(true);
        setCountDistinctMultipleColumns(true);

        setPrecedence(Precedence.ARITH_LOW + 1, Ops.CONCAT);
        setPrecedence(Precedence.COMPARISON, Ops.EQ, Ops.EQ_IGNORE_CASE, Ops.NE);

        add(Ops.MOD, "{0} % {1}", Precedence.ARITH_HIGH);

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

        add(Ops.DateTimeOps.TRUNC_YEAR,   "parsedatetime(formatdatetime({0},'yyyy'),'yyyy')");
        add(Ops.DateTimeOps.TRUNC_MONTH,  "parsedatetime(formatdatetime({0},'yyyy-MM'),'yyyy-MM')");
        add(Ops.DateTimeOps.TRUNC_WEEK,   "parsedatetime(formatdatetime({0},'YYYY-ww'),'YYYY-ww')");
        add(Ops.DateTimeOps.TRUNC_DAY,    "parsedatetime(formatdatetime({0},'yyyy-MM-dd'),'yyyy-MM-dd')");
        add(Ops.DateTimeOps.TRUNC_HOUR,   "parsedatetime(formatdatetime({0},'yyyy-MM-dd HH'),'yyyy-MM-dd HH')");
        add(Ops.DateTimeOps.TRUNC_MINUTE, "parsedatetime(formatdatetime({0},'yyyy-MM-dd HH:mm'),'yyyy-MM-dd HH:mm')");
        add(Ops.DateTimeOps.TRUNC_SECOND, "parsedatetime(formatdatetime({0},'yyyy-MM-dd HH:mm:ss'),'yyyy-MM-dd HH:mm:ss')");
    }

}
