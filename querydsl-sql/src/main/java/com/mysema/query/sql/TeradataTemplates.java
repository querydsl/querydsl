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
package com.mysema.query.sql;

import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryModifiers;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Ops.MathOps;


/**
 * TeradataTemplates is a SQL dialect for Teradata
 *
 * @author tiwe
 *
 */
public class TeradataTemplates extends SQLTemplates {

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
        addClass2TypeMappings("double precision", Double.class);
        addClass2TypeMappings("byteint", Byte.class);
        addClass2TypeMappings("byteint", Byte.class);
        addClass2TypeMappings("varchar(4000)", String.class);

        add(Ops.NE, "{0} <> {1}");

        // Math
        add(Ops.MOD, "{0} mod {1}");
        add(Ops.MathOps.LOG, "(log({0}) / log({1}))");
        add(MathOps.RANDOM, "(cast(random(0, 10000) as float) / 10000.0)");

        // String
        add(Ops.INDEX_OF, "position({1} in {0})-1");
        //add(Ops.INDEX_OF_2ARGS, // TODO
        add(Ops.STRING_CAST, "cast({0} as varchar(255))");
        add(Ops.STRING_LENGTH, "character_length({0})");
        add(Ops.StringOps.LOCATE, "position({0} in {1})");
        add(Ops.StringOps.LOCATE2, "(position({0} in substr({1}, {2})) + {2})");
        add(Ops.StringOps.LEFT, "substr({0}, 1, {1})");
        add(Ops.StringOps.RIGHT, "substr({0}, (character_length({0})-{1s}) + 1, {1})");
        add(Ops.MATCHES, "regex_instr({0}, {1}) = 1");
        // add(Ops.MATCHES_IC, "regex_instr({0}, {1}) = 1"); TODO

        // Number
        add(Ops.MathOps.COT, "(cos({0}) / sin({0}))");
        add(Ops.MathOps.COTH, "(exp({0} * 2) + 1) / (exp({0} * 2) - 1)");

        // Date / time
        add(Ops.DateTimeOps.DATE, "cast({0} as date)");
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
        add(Ops.DateTimeOps.YEAR_WEEK, "(extract(year from {0}) * 100 + extract(week from {0}))");
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
