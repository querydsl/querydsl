/*
 * Copyright 2013, Mysema Ltd
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

import com.querydsl.core.QueryMetadata;
import com.querydsl.core.QueryModifiers;
import com.querydsl.core.types.Ops;


/**
 * TeradataTemplates is a SQL dialect for Teradata
 *
 * @author tiwe
 *
 */
public class TeradataTemplates extends SQLTemplates {

    @SuppressWarnings("FieldNameHidesFieldInSuperclass") //Intentional
    public static final TeradataTemplates DEFAULT = new TeradataTemplates();

    public static Builder builder() {
        return new Builder() {
            @Override
            protected SQLTemplates build(char escape, boolean quote) {
                return new TeradataTemplates(escape, quote);
            }
        };
    }

    private String limitOffsetStart = "\nqualify row_number() over (order by ";

    private String limitTemplate = " <= {0}";

    private String limitOffsetTemplate = " between {0} and {1}";

    private String offsetTemplate = " > {0}";

    public TeradataTemplates() {
        this('\\', false);
    }

    public TeradataTemplates(boolean quote) {
        this('\\', quote);
    }

    public TeradataTemplates(char escape, boolean quote) {
        super("\"", escape, quote);
        setNullsFirst(null);
        setNullsLast(null);
        setDummyTable(null);
        setCountViaAnalytics(true);
        setDefaultValues("\ndefault values");

        add(Ops.NE, "{0} <> {1}");

        // String
        add(Ops.STRING_LENGTH, "character_length({0})");
        add(Ops.INDEX_OF, "(instr({0},{1})-1)");
        add(Ops.INDEX_OF_2ARGS, "(instr({0},{1},{2}+1)-1)");
        add(Ops.STRING_CAST, "cast({0} as varchar(255))");
        add(Ops.StringOps.LOCATE, "instr({1},{0})");
        add(Ops.StringOps.LOCATE2, "instr({1},{0},{2s})");
        add(Ops.StringOps.LEFT, "substr({0}, 1, {1})");
        add(Ops.StringOps.RIGHT, "substr({0}, (character_length({0})-{1s}) + 1, {1})");
        add(Ops.MATCHES, "(regexp_instr({0}, {1}) = 1)");
        add(Ops.MATCHES_IC, "(regex_instr({0l}, {1}) = 1)");

        // Number
        add(Ops.MOD, "{0} mod {1}");
        add(Ops.MathOps.LOG, "(ln({0}) / ln({1}))");
        add(Ops.MathOps.RANDOM, "cast(random(0, 1000000000) as numeric(20,10))/1000000000");
        add(Ops.MathOps.COT, "(cos({0}) / sin({0}))");
        add(Ops.MathOps.COTH, "(exp({0} * 2) + 1) / (exp({0} * 2) - 1)");

        // Date / time
        add(Ops.DateTimeOps.DATE, "cast({0} as date)");

        add(Ops.DateTimeOps.WEEK, "(td_week_of_year({0}) + 1)"); // non-standard
        add(Ops.DateTimeOps.DAY_OF_WEEK, "td_day_of_week({0})"); // non-standard
        add(Ops.DateTimeOps.DAY_OF_YEAR, "td_day_of_year({0})"); // non-standard
        add(Ops.DateTimeOps.YEAR_WEEK, "(extract (year from {0}) * 100 + td_week_of_year({0}))");

        add(Ops.DateTimeOps.ADD_YEARS, "{0} + interval '{1s}' year");
        add(Ops.DateTimeOps.ADD_MONTHS, "{0} + interval '{1s}' month");
        add(Ops.DateTimeOps.ADD_DAYS, "{0} + interval '{1s}' day");

        add(Ops.DateTimeOps.DIFF_YEARS, "cast((({1} - {0}) year) as integer)");
        add(Ops.DateTimeOps.DIFF_MONTHS, "cast((({1} - {0}) month) as integer)"); // FIXME
        add(Ops.DateTimeOps.DIFF_DAYS, "({1} - {0})");

        add(Ops.DateTimeOps.TRUNC_YEAR, "trunc({0}, 'year')");
        add(Ops.DateTimeOps.TRUNC_MONTH, "trunc({0}, 'month')");
        add(Ops.DateTimeOps.TRUNC_WEEK, "trunc({0}, 'w')");
        add(Ops.DateTimeOps.TRUNC_DAY, "trunc({0}, 'day')");
        add(Ops.DateTimeOps.TRUNC_HOUR, "trunc({0}, 'hh')");
        add(Ops.DateTimeOps.TRUNC_MINUTE, "trunc({0}, 'mi')");
        add(Ops.DateTimeOps.TRUNC_SECOND, "{0}"); // not truncated
    }

    @Override
    protected void serializeModifiers(QueryMetadata metadata, SQLSerializer context) {
        QueryModifiers mod = metadata.getModifiers();
        context.append(limitOffsetStart);
        if (!metadata.getOrderBy().isEmpty()) {
            context.handleOrderBy(metadata.getOrderBy());
        } else {
            context.append("1");
        }
        context.append(")");
        if (mod.getLimit() == null) {
            context.handle(offsetTemplate, mod.getOffset());
        } else if (mod.getOffset() == null) {
            context.handle(limitTemplate, mod.getLimit());
        } else {
            context.handle(limitOffsetTemplate,  mod.getOffset() + 1, mod.getOffset() + mod.getLimit());
        }
    }

}
