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
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.querydsl.core.types.Ops;

/**
 * PostgresTemplates is an SQL dialect for PostgreSQL
 *
 * <p>tested with PostgreSQL 8.4 and 9.1</p>
 *
 * @author tiwe
 *
 */
public class PostgresTemplates extends SQLTemplates {

    protected static final Set<String> POSTGRES_RESERVED_WORDS
            = ImmutableSet.of(
                    "ALL", "ANALYSE", "ANALYZE", "AND", "ANY", "ARRAY", "AS",
                    "ASC", "ASYMMETRIC", "AUTHORIZATION", "BINARY", "BOTH",
                    "CASE", "CAST", "CHECK", "COLLATE", "COLLATION", "COLUMN",
                    "CONCURRENTLY", "CONSTRAINT", "CREATE", "CROSS",
                    "CURRENT_CATALOG", "CURRENT_DATE", "CURRENT_ROLE",
                    "CURRENT_SCHEMA", "CURRENT_TIME", "CURRENT_TIMESTAMP",
                    "CURRENT_USER", "DEFAULT", "DEFERRABLE", "DESC", "DISTINCT",
                    "DO", "ELSE", "END", "EXCEPT", "FALSE", "FETCH", "FOR",
                    "FOREIGN", "FREEZE", "FROM", "FULL", "GRANT", "GROUP",
                    "HAVING", "ILIKE", "IN", "INITIALLY", "INNER", "INTERSECT",
                    "INTO", "IS", "ISNULL", "JOIN", "LATERAL", "LEADING", "LEFT",
                    "LIKE", "LIMIT", "LOCALTIME", "LOCALTIMESTAMP", "NATURAL",
                    "NOT", "NOTNULL", "NULL", "OFFSET", "ON", "ONLY", "OR",
                    "ORDER", "OUTER", "OVER", "OVERLAPS", "PLACING", "PRIMARY",
                    "REFERENCES", "RETURNING", "RIGHT", "SELECT", "SESSION_USER",
                    "SIMILAR", "SOME", "SYMMETRIC", "TABLE", "THEN", "TO",
                    "TRAILING", "TRUE", "UNION", "UNIQUE", "USER", "USING",
                    "VARIADIC", "VERBOSE", "WHEN", "WHERE", "WINDOW", "WITH");

    @SuppressWarnings("FieldNameHidesFieldInSuperclass") //Intentional
    public static final PostgresTemplates DEFAULT = new PostgresTemplates();

    public static Builder builder() {
        return new Builder() {
            @Override
            protected SQLTemplates build(char escape, boolean quote) {
                return new PostgresTemplates(escape, quote);
            }
        };
    }

    public PostgresTemplates() {
        this('\\', false);
    }

    public PostgresTemplates(boolean quote) {
        this('\\', quote);
    }

    public PostgresTemplates(char escape, boolean quote) {
        super(POSTGRES_RESERVED_WORDS, "\"", escape, quote);
        setDummyTable(null);
        setCountDistinctMultipleColumns(true);
        setCountViaAnalytics(true);
        setDefaultValues("\ndefault values");
        setSupportsUnquotedReservedWordsAsIdentifier(true);

        // String
        add(Ops.MATCHES, "{0} ~ {1}");
        add(Ops.INDEX_OF, "strpos({0},{1})-1");
        add(Ops.INDEX_OF_2ARGS, "strpos({0},{1})-1"); //FIXME
        add(Ops.StringOps.LOCATE,  "strpos({1},{0})");
        add(Ops.StringOps.LOCATE2, "strpos(repeat('^',{2s}-1) || substr({1},{2s}),{0})");

        // like without escape
        if (escape == '\\') {
            add(Ops.LIKE, "{0} like {1}");
            add(Ops.ENDS_WITH, "{0} like {%1}");
            add(Ops.ENDS_WITH_IC, "{0l} like {%%1}");
            add(Ops.STARTS_WITH, "{0} like {1%}");
            add(Ops.STARTS_WITH_IC, "{0l} like {1%%}");
            add(Ops.STRING_CONTAINS, "{0} like {%1%}");
            add(Ops.STRING_CONTAINS_IC, "{0l} like {%%1%%}");
        }

        // Number
        add(Ops.MathOps.RANDOM, "random()");
        add(Ops.MathOps.LN, "ln({0})");
        add(Ops.MathOps.LOG, "log({1},{0})");
        add(Ops.MathOps.COSH, "(exp({0}) + exp({0} * -1)) / 2");
        add(Ops.MathOps.COTH, "(exp({0} * 2) + 1) / (exp({0} * 2) - 1)");
        add(Ops.MathOps.SINH, "(exp({0}) - exp({0} * -1)) / 2");
        add(Ops.MathOps.TANH, "(exp({0} * 2) - 1) / (exp({0} * 2) + 1)");

        // Date / time
        add(Ops.DateTimeOps.DAY_OF_WEEK, "extract(dow from {0}) + 1");
        add(Ops.DateTimeOps.DAY_OF_YEAR, "extract(doy from {0})");
        add(Ops.DateTimeOps.YEAR_WEEK, "(extract(isoyear from {0}) * 100 + extract(week from {0}))");

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
        String hoursDiff = "("+ daysDiff + " * 24 + date_part('hour', age({1}, {0})))";
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
