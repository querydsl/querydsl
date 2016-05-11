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
 * {@code PostgreSQLTemplates} is an SQL dialect for PostgreSQL
 *
 * <p>tested with PostgreSQL 8.4 and 9.1</p>
 *
 * @author tiwe
 *
 */
public class PostgreSQLTemplates extends SQLTemplates {

    @SuppressWarnings("FieldNameHidesFieldInSuperclass") //Intentional
    public static final PostgreSQLTemplates DEFAULT = new PostgreSQLTemplates();

    public static Builder builder() {
        return new Builder() {
            @Override
            protected SQLTemplates build(char escape, boolean quote) {
                return new PostgreSQLTemplates(escape, quote);
            }
        };
    }

    public PostgreSQLTemplates() {
        this('\\', false);
    }

    public PostgreSQLTemplates(boolean quote) {
        this('\\', quote);
    }

    public PostgreSQLTemplates(char escape, boolean quote) {
        super(Keywords.POSTGRESQL, "\"", escape, quote);
        setDummyTable(null);
        setCountDistinctMultipleColumns(true);
        setCountViaAnalytics(true);
        setDefaultValues("\ndefault values");
        setSupportsUnquotedReservedWordsAsIdentifier(true);

        setForShareSupported(true);

        setPrecedence(Precedence.COMPARISON - 3, Ops.IS_NULL, Ops.IS_NOT_NULL);
        setPrecedence(Precedence.COMPARISON - 2, Ops.CONCAT, Ops.MATCHES);
        setPrecedence(Precedence.COMPARISON - 1, Ops.IN);
        setPrecedence(Precedence.COMPARISON, Ops.BETWEEN);
        setPrecedence(Precedence.COMPARISON + 1, Ops.LIKE, Ops.LIKE_ESCAPE);
        setPrecedence(Precedence.COMPARISON + 2, Ops.LT, Ops.GT, Ops.LOE, Ops.GOE);
        setPrecedence(Precedence.COMPARISON + 3, Ops.EQ, Ops.EQ_IGNORE_CASE);

        setPrecedence(Precedence.COMPARISON + 1, OTHER_LIKE_CASES);

        add(Ops.MOD, "{0} % {1}", Precedence.ARITH_HIGH);

        // String
        add(Ops.MATCHES, "{0} ~ {1}");
        add(Ops.INDEX_OF, "strpos({0},{1})-1", Precedence.ARITH_LOW);
        add(Ops.INDEX_OF_2ARGS, "strpos({0},{1})-1", Precedence.ARITH_LOW); //FIXME
        add(Ops.StringOps.LOCATE,  "strpos({1},{0})");
        add(Ops.StringOps.LOCATE2, "strpos(repeat('^',{2-'1's}) || substr({1},{2s}),{0})");
        add(SQLOps.GROUP_CONCAT, "string_agg({0},',')");
        add(SQLOps.GROUP_CONCAT2, "string_agg({0},{1})");

        add(Ops.LIKE_ESCAPE_IC, "{0} ilike {1} escape '{2s}'");
        // like without escape
        if (escape == '\\') {
            add(Ops.LIKE, "{0} like {1}");
            add(Ops.LIKE_IC, "{0} ilike {1}");
            add(Ops.ENDS_WITH, "{0} like {%1}");
            add(Ops.ENDS_WITH_IC, "{0} ilike {%1}");
            add(Ops.STARTS_WITH, "{0} like {1%}");
            add(Ops.STARTS_WITH_IC, "{0} ilike {1%}");
            add(Ops.STRING_CONTAINS, "{0} like {%1%}");
            add(Ops.STRING_CONTAINS_IC, "{0} ilike {%1%}");
        } else {
            // remap case insensitive operations under 'ilike'
            add(Ops.LIKE_IC, "{0} ilike {1} escape '" + escape + "'");
            add(Ops.ENDS_WITH_IC, "{0} ilike {%1} escape '" + escape + "'");
            add(Ops.STARTS_WITH_IC, "{0} ilike {1%} escape '" + escape + "'");
            add(Ops.STRING_CONTAINS_IC, "{0} ilike {%1%} escape '" + escape + "'");
        }

        // Number
        add(Ops.MathOps.RANDOM, "random()");
        add(Ops.MathOps.LN, "ln({0})");
        add(Ops.MathOps.LOG, "log({1s},{0s})");
        add(Ops.MathOps.COSH, "(exp({0}) + exp({0*'-1'})) / 2");
        add(Ops.MathOps.COTH, "(exp({0*'2'}) + 1) / (exp({0*'2'}) - 1)");
        add(Ops.MathOps.SINH, "(exp({0}) - exp({0} * -1)) / 2");
        add(Ops.MathOps.TANH, "(exp({0*'2'}) - 1) / (exp({0*'2'}) + 1)");

        // Date / time
        add(Ops.DateTimeOps.DAY_OF_MONTH, "cast(extract(day from {0}) as integer)");
        add(Ops.DateTimeOps.DAY_OF_WEEK, "cast(extract(dow from {0}) + 1 as integer)");
        add(Ops.DateTimeOps.DAY_OF_YEAR, "cast(extract(doy from {0}) as integer)");
        add(Ops.DateTimeOps.HOUR, "cast(extract(hour from {0}) as integer)");
        add(Ops.DateTimeOps.MINUTE, "cast(extract(minute from {0}) as integer)");
        add(Ops.DateTimeOps.MONTH, "cast(extract(month from {0}) as integer)");
        add(Ops.DateTimeOps.SECOND, "cast(extract(second from {0}) as integer)");
        add(Ops.DateTimeOps.WEEK, "cast(extract(week from {0}) as integer)");
        add(Ops.DateTimeOps.YEAR, "cast(extract(year from {0}) as integer)");
        add(Ops.DateTimeOps.YEAR_MONTH, "cast(extract(year from {0}) * 100 + extract(month from {0}) as integer)");
        add(Ops.DateTimeOps.YEAR_WEEK, "cast(extract(isoyear from {0}) * 100 + extract(week from {0}) as integer)");

        add(Ops.AggOps.BOOLEAN_ANY, "bool_or({0})", 0);
        add(Ops.AggOps.BOOLEAN_ALL, "bool_and({0})", 0);

        add(Ops.DateTimeOps.ADD_YEARS, "{0} + interval '{1s} years'");
        add(Ops.DateTimeOps.ADD_MONTHS, "{0} + interval '{1s} months'");
        add(Ops.DateTimeOps.ADD_WEEKS, "{0} + interval '{1s} weeks'");
        add(Ops.DateTimeOps.ADD_DAYS, "{0} + interval '{1s} days'");
        add(Ops.DateTimeOps.ADD_HOURS, "{0} + interval '{1s} hours'");
        add(Ops.DateTimeOps.ADD_MINUTES, "{0} + interval '{1s} minutes'");
        add(Ops.DateTimeOps.ADD_SECONDS, "{0} + interval '{1s} seconds'");

        String yearsDiff = "date_part('year', age({1}, {0}))";
        String monthsDiff = "(" + yearsDiff + " * 12 + date_part('month', age({1}, {0})))";
        String weeksDiff =  "trunc((cast({1} as date) - cast({0} as date))/7)";
        String daysDiff = "(cast({1} as date) - cast({0} as date))";
        String hoursDiff = "(" + daysDiff + " * 24 + date_part('hour', age({1}, {0})))";
        String minutesDiff = "(" + hoursDiff + " * 60 + date_part('minute', age({1}, {0})))";
        String secondsDiff =  "(" +  minutesDiff + " * 60 + date_part('second', age({1}, {0})))";

        add(Ops.DateTimeOps.DIFF_YEARS,   yearsDiff);
        add(Ops.DateTimeOps.DIFF_MONTHS,  monthsDiff);
        add(Ops.DateTimeOps.DIFF_WEEKS,   weeksDiff);
        add(Ops.DateTimeOps.DIFF_DAYS,    daysDiff);
        add(Ops.DateTimeOps.DIFF_HOURS,   hoursDiff);
        add(Ops.DateTimeOps.DIFF_MINUTES, minutesDiff);
        add(Ops.DateTimeOps.DIFF_SECONDS, secondsDiff);

        addTypeNameToCode("bool", Types.BIT, true);
        addTypeNameToCode("bytea", Types.BINARY);
        addTypeNameToCode("name", Types.VARCHAR);
        addTypeNameToCode("int8", Types.BIGINT, true);
        addTypeNameToCode("bigserial", Types.BIGINT);
        addTypeNameToCode("int2", Types.SMALLINT, true);
        addTypeNameToCode("int2", Types.TINYINT, true); // secondary mapping
        addTypeNameToCode("int4", Types.INTEGER, true);
        addTypeNameToCode("serial", Types.INTEGER);
        addTypeNameToCode("text", Types.VARCHAR);
        addTypeNameToCode("oid", Types.BIGINT);
        addTypeNameToCode("xml", Types.SQLXML, true);
        addTypeNameToCode("float4", Types.REAL, true);
        addTypeNameToCode("float8", Types.DOUBLE, true);
        addTypeNameToCode("bpchar", Types.CHAR);
        addTypeNameToCode("timestamptz", Types.TIMESTAMP);
    }

    @Override
    public String serialize(String literal, int jdbcType) {
        if (jdbcType == Types.BOOLEAN) {
            return "1".equals(literal) ? "true" : "false";
        } else {
            return super.serialize(literal, jdbcType);
        }
    }

}
