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

import java.math.BigDecimal;

import com.mysema.query.types.Ops;

/**
 * MySQLTemplates is an SQL dialect for MySQL
 *
 * <p>tested with MySQL CE 5.1 and 5.5</p>
 *
 * @author tiwe
 *
 */
public class MySQLTemplates extends SQLTemplates {

    public static Builder builder() {
        return new Builder() {
            @Override
            protected SQLTemplates build(char escape, boolean quote) {
                return new MySQLTemplates(escape, quote);
            }
        };
    }

    public MySQLTemplates() {
        this('\\', false);
    }

    public MySQLTemplates(boolean quote) {
        this('\\', quote);
    }

    public MySQLTemplates(char escape, boolean quote) {
        super("`", escape, quote);
        setParameterMetadataAvailable(false);
        setLimitRequired(true);
        setNullsFirst(null);
        setNullsLast(null);

        addClass2TypeMappings("bool", Boolean.class);
        addClass2TypeMappings("int", Integer.class);

        addClass2TypeMappings("decimal",
                Double.class,
                Float.class,
                BigDecimal.class);
        addClass2TypeMappings("char", String.class);

        add(Ops.CONCAT, "concat({0}, {1})",0);

        add(Ops.StringOps.LPAD, "lpad({0},{1},' ')");
        add(Ops.StringOps.RPAD, "rpad({0},{1},' ')");

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

        add(Ops.MathOps.LOG, "log({1},{0})");
        add(Ops.MathOps.COSH, "(exp({0}) + exp({0} * -1)) / 2");
        add(Ops.MathOps.COTH, "(exp({0} * 2) + 1) / (exp({0} * 2) - 1)");
        add(Ops.MathOps.SINH, "(exp({0}) - exp({0} * -1)) / 2");
        add(Ops.MathOps.TANH, "(exp({0} * 2) - 1) / (exp({0} * 2) + 1)");

        add(Ops.AggOps.BOOLEAN_ANY, "bit_or({0})", 0);
        add(Ops.AggOps.BOOLEAN_ALL, "bit_and({0})", 0);

        add(Ops.DateTimeOps.YEAR_MONTH, "extract(year_month from {0})");
        add(Ops.DateTimeOps.YEAR_WEEK, "yearweek({0})");

        add(Ops.DateTimeOps.ADD_YEARS, "date_add({0}, interval {1s} year)");
        add(Ops.DateTimeOps.ADD_MONTHS, "date_add({0}, interval {1s} month)");
        add(Ops.DateTimeOps.ADD_WEEKS, "date_add({0}, interval {1s} week)");
        add(Ops.DateTimeOps.ADD_DAYS, "date_add({0}, interval {1s} day)");
        add(Ops.DateTimeOps.ADD_HOURS, "date_add({0}, interval {1s} hour)");
        add(Ops.DateTimeOps.ADD_MINUTES, "date_add({0}, interval {1s} minute)");
        add(Ops.DateTimeOps.ADD_SECONDS, "date_add({0}, interval {1s} second)");

        add(Ops.DateTimeOps.DIFF_DAYS, "datediff({0}, {1})");

    }

}
