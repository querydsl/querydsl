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

import com.querydsl.core.QueryMetadata;
import com.querydsl.core.QueryModifiers;
import com.querydsl.core.types.Ops;

/**
 * {@code FirebirdTemplates} is an SQL dialect for Firebird
 */
public class FirebirdTemplates extends SQLTemplates {

    @SuppressWarnings("FieldNameHidesFieldInSuperclass") //Intentional
    public static final FirebirdTemplates DEFAULT = new FirebirdTemplates();

    private String limitOffsetTemplate = "\nrows {0} to {1}";

    private String limitTemplate = "\nrows {0}";

    private String offsetTemplate = "\nrows {0} to " + Integer.MAX_VALUE;

    public static Builder builder() {
        return new Builder() {
            @Override
            protected SQLTemplates build(char escape, boolean quote) {
                return new FirebirdTemplates(escape, quote);
            }
        };
    }

    public FirebirdTemplates() {
        this('\\', false);
    }

    public FirebirdTemplates(boolean quote) {
        this('\\', quote);
    }

    public FirebirdTemplates(char escape, boolean quote) {
        super(Keywords.FIREBIRD, "\"", escape, quote, false);
        setDummyTable("RDB$DATABASE");
        setUnionsWrapped(false);
        setWrapSelectParameters(true);
        setArraysSupported(false);
        setBatchToBulkSupported(false);

        setPrecedence(Precedence.COMPARISON, Ops.EQ, Ops.EQ_IGNORE_CASE, Ops.NE);

        add(Ops.CONCAT, "{0} || {1}", Precedence.NEGATE - 1);

        // string
        add(Ops.CHAR_AT, "cast(substring({0} from {1+'1's} for 1) as char)");
        add(Ops.SUBSTR_1ARG, "substring({0} from {1+'1's})");
        add(Ops.SUBSTR_2ARGS, "substring({0} from {1+'1's} for {2-1s})");
        add(Ops.INDEX_OF, "position({1},{0})-1", Precedence.ARITH_LOW);
        add(Ops.INDEX_OF_2ARGS, "position({1},{0},{2+'1's})-1", Precedence.ARITH_LOW);
        add(Ops.StringOps.LOCATE, "position({0},{1})");
        add(Ops.StringOps.LOCATE2, "position({0},{1},{2})");
        add(Ops.STRING_LENGTH, "char_length({0})");
        add(Ops.STRING_IS_EMPTY, "char_length({0}) = 0");

        add(Ops.AggOps.BOOLEAN_ANY, "any({0})");
        add(Ops.AggOps.BOOLEAN_ALL, "all({0})");

        // math
        add(Ops.MathOps.LOG, "log({1},{0})");
        add(Ops.MathOps.COSH, "(exp({0}) + exp({0*'-1'})) / 2");
        add(Ops.MathOps.COTH, "(exp({0*'2'}) + 1) / (exp({0*'2'}) - 1)");
        add(Ops.MathOps.SINH, "(exp({0}) - exp({0} * -1)) / 2");
        add(Ops.MathOps.TANH, "(exp({0*'2'}) - 1) / (exp({0*'2'}) + 1)");
        add(Ops.MathOps.DEG, "{0*'180.0'} / pi()", Precedence.ARITH_HIGH);
        add(Ops.MathOps.RAD, "{0}*pi() / 180.0 ", Precedence.ARITH_HIGH);

        //
        add(Ops.DateTimeOps.DATE, "cast({0} as date)");

        add(Ops.DateTimeOps.MILLISECOND, "extract(millisecond from {0})");
        add(Ops.DateTimeOps.YEAR_MONTH, "extract(year from {0}) * 100 + extract(month from {0})", Precedence.ARITH_LOW);
        add(Ops.DateTimeOps.YEAR_WEEK, "extract(year from {0}) * 100 + extract(week from {0})", Precedence.ARITH_LOW);
        add(Ops.DateTimeOps.DAY_OF_WEEK, "extract(weekday from {0})");
        add(Ops.DateTimeOps.DAY_OF_MONTH, "extract(day from {0})");
        //add(Ops.DateTimeOps.DAY_OF_YEAR, "extract(day_of_year from {0})");

        add(Ops.DateTimeOps.ADD_YEARS, "dateadd(year,{1},{0})");
        add(Ops.DateTimeOps.ADD_MONTHS, "dateadd(month,{1},{0})");
        add(Ops.DateTimeOps.ADD_WEEKS, "dateadd(week,{1},{0})");
        add(Ops.DateTimeOps.ADD_DAYS, "dateadd(day,{1},{0})");
        add(Ops.DateTimeOps.ADD_HOURS, "dateadd(hour,{1},{0})");
        add(Ops.DateTimeOps.ADD_MINUTES, "dateadd(minute,{1},{0})");
        add(Ops.DateTimeOps.ADD_SECONDS, "dateadd(second,{1},{0})");

        add(Ops.DateTimeOps.DIFF_YEARS, "datediff(year,{0},{1})");
        add(Ops.DateTimeOps.DIFF_MONTHS, "datediff(month,{0},{1})");
        add(Ops.DateTimeOps.DIFF_WEEKS, "datediff(week,{0},{1})");
        add(Ops.DateTimeOps.DIFF_DAYS, "datediff(day,{0},{1})");
        add(Ops.DateTimeOps.DIFF_HOURS, "datediff(hour,{0},{1})");
        add(Ops.DateTimeOps.DIFF_MINUTES, "datediff(minute,{0},{1})");
        add(Ops.DateTimeOps.DIFF_SECONDS, "datediff(second,{0},{1})");

        add(Ops.DateTimeOps.TRUNC_YEAR,   "cast(extract(year from {0}) || '-1-1' as date)");
        add(Ops.DateTimeOps.TRUNC_MONTH,  "cast(substring(cast({0} as char(100)) from 1 for 7) || '-1' as date)");
        // TODO weeks
        add(Ops.DateTimeOps.TRUNC_DAY,    "cast(substring(cast({0} as char(100)) from 1 for 10) as date)");
        add(Ops.DateTimeOps.TRUNC_HOUR,   "cast(substring(cast({0} as char(100)) from 1 for 13) || ':00:00' as timestamp)");
        add(Ops.DateTimeOps.TRUNC_MINUTE, "cast(substring(cast({0} as char(100)) from 1 for 16) || ':00' as timestamp)");
        add(Ops.DateTimeOps.TRUNC_SECOND, "cast(substring(cast({0} as char(100)) from 1 for 19) as timestamp)");

        add(SQLOps.GROUP_CONCAT, "list({0},',')");
        add(SQLOps.GROUP_CONCAT2, "list({0},{1})");

        addTypeNameToCode("smallint", Types.BOOLEAN, true);
        addTypeNameToCode("smallint", Types.BIT, true);
        addTypeNameToCode("smallint", Types.TINYINT, true);
        addTypeNameToCode("decimal", Types.DOUBLE, true);
        addTypeNameToCode("blob sub_type 0", Types.LONGVARBINARY);
        addTypeNameToCode("blob sub_type 1", Types.LONGVARCHAR);
        addTypeNameToCode("double precision", Types.DOUBLE);
        addTypeNameToCode("array", Types.OTHER);
        addTypeNameToCode("blob sub_type 0 ", Types.BLOB);
    }

    @Override
    public String getCastTypeNameForCode(int code) {
        if (code == Types.VARCHAR) {
            return "varchar(256)";
        } else {
            return super.getCastTypeNameForCode(code);
        }
    }

    @Override
    protected void serializeModifiers(QueryMetadata metadata, SQLSerializer context) {
        QueryModifiers mod = metadata.getModifiers();
        if (mod.isRestricting()) {
            if (mod.getLimit() != null) {
                if (mod.getOffset() != null) {
                    context.handle(limitOffsetTemplate, mod.getOffset() + 1, mod.getOffset() + mod.getLimit());
                } else {
                    context.handle(limitTemplate, mod.getLimit());
                }
            } else {
                context.handle(offsetTemplate, mod.getOffset() + 1);
            }
        }
    }
}
