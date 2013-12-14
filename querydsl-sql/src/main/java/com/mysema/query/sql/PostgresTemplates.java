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
package com.mysema.query.sql;

import com.mysema.query.types.Ops;

/**
 * PostgresTemplates is an SQL dialect for PostgreSQL
 *
 * <p>tested with PostgreSQL 8.4 and 9.1</p>
 *
 * @author tiwe
 *
 */
public class PostgresTemplates extends SQLTemplates {

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
        super("\"", escape, quote);
        setDummyTable(null);
        setCountDistinctMultipleColumns(true);
        // type mappings
        addClass2TypeMappings("numeric(3,0)", Byte.class);
        addClass2TypeMappings("double precision", Double.class);

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
        add(Ops.DateTimeOps.YEAR, "extract(year from {0})");
        add(Ops.DateTimeOps.MONTH, "extract(month from {0})");
        add(Ops.DateTimeOps.WEEK, "extract(week from {0})");
        add(Ops.DateTimeOps.DAY_OF_MONTH, "extract(day from {0})");
        add(Ops.DateTimeOps.DAY_OF_WEEK, "extract(dow from {0}) + 1");
        add(Ops.DateTimeOps.DAY_OF_YEAR, "extract(doy from {0})");
        add(Ops.DateTimeOps.HOUR, "extract(hour from {0})");
        add(Ops.DateTimeOps.MINUTE, "extract(minute from {0})");
        add(Ops.DateTimeOps.SECOND, "extract(second from {0})");

        add(Ops.DateTimeOps.YEAR_MONTH, "extract(year from {0}) * 100 + extract(month from {0})");
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
        String secondsDiff =  "(" +  minutesDiff + " * 60 + date_part('minute', age({1}, {0})))";

        add(Ops.DateTimeOps.DIFF_YEARS,   yearsDiff);
        add(Ops.DateTimeOps.DIFF_MONTHS,  monthsDiff);
        add(Ops.DateTimeOps.DIFF_WEEKS,   weeksDiff);
        add(Ops.DateTimeOps.DIFF_DAYS,    daysDiff);
        add(Ops.DateTimeOps.DIFF_HOURS,   hoursDiff);
        add(Ops.DateTimeOps.DIFF_MINUTES, minutesDiff);
        add(Ops.DateTimeOps.DIFF_SECONDS, secondsDiff);
    }

}
