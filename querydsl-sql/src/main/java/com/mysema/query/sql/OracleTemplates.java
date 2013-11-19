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

import java.math.BigInteger;

import com.mysema.query.QueryFlag.Position;
import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryModifiers;
import com.mysema.query.types.Ops;

/**
 * OracleTemplates is an SQL dialect for Oracle
 *
 * <p>tested with Oracle 10g XE</p>
 *
 * @author tiwe
 */
public class OracleTemplates extends SQLTemplates {

    public static Builder builder() {
        return new Builder() {
            @Override
            protected SQLTemplates build(char escape, boolean quote) {
                return new OracleTemplates(escape, quote);
            }
        };
    }

    private String outerQueryStart = "select * from (\n select a.*, rownum rn from (\n  ";

    private String outerQueryEnd = "\n ) a) where ";

    private String limitQueryStart = "select * from (\n  ";

    private String limitQueryEnd = "\n) where rownum <= {0}";

    private String limitOffsetTemplate = "rn > {0s} and rownum <= {1s}";

    private String offsetTemplate = "rn > {0}";

    public OracleTemplates() {
        this('\\', false);
    }

    public OracleTemplates(boolean quote) {
        this('\\',quote);
    }

    public OracleTemplates(char escape, boolean quote) {
        super("\"", escape, quote);
        setParameterMetadataAvailable(false);
        setBatchCountViaGetUpdateCount(true);
        setWithRecursive("with ");
        // type mappings
        addClass2TypeMappings("number(3,0)", Byte.class);
        addClass2TypeMappings("number(1,0)", Boolean.class);
        addClass2TypeMappings("number(19,0)", BigInteger.class, Long.class);
        addClass2TypeMappings("number(5,0)", Short.class);
        addClass2TypeMappings("number(10,0)", Integer.class);
        addClass2TypeMappings("double precision", Double.class);
        addClass2TypeMappings("varchar(4000 char)", String.class);

        add(Ops.ALIAS, "{0} {1}");
        add(SQLOps.NEXTVAL, "{0s}.nextval");

        // String
        add(Ops.INDEX_OF, "instrb({0},{1})-1");
        add(Ops.INDEX_OF_2ARGS, "instrb({0},{1},{2}+1)-1");
        add(Ops.MATCHES, "regexp_like({0},{1})");
        add(Ops.StringOps.LOCATE, "instr({1},{0})");
        add(Ops.StringOps.LOCATE2, "instr({1},{0},{2s})");
        add(Ops.StringOps.LEFT, "substr({0},1,{1})");
        add(Ops.StringOps.RIGHT, "substr({0},-{1s},length({0}))");

        // Number
        add(Ops.MathOps.CEIL, "ceil({0})");
        add(Ops.MathOps.RANDOM, "dbms_random.value");
        add(Ops.MathOps.LN, "ln({0})");
        add(Ops.MathOps.LOG, "log({1},{0})");
        add(Ops.MathOps.COT, "(cos({0}) / sin({0}))");
        add(Ops.MathOps.COTH, "(exp({0} * 2) + 1) / (exp({0} * 2) - 1)");
        add(Ops.MathOps.DEG, "({0} * 180 / "+Math.PI+")");
        add(Ops.MathOps.RAD, "({0} * "+Math.PI+" / 180)");

        // Date / time
        add(Ops.DateTimeOps.DATE, "trunc({0})");
        add(Ops.DateTimeOps.YEAR, "extract(year from {0})");
        add(Ops.DateTimeOps.MONTH, "extract(month from {0})");
        add(Ops.DateTimeOps.WEEK, "to_number(to_char({0},'WW'))");
        add(Ops.DateTimeOps.DAY_OF_MONTH, "to_number(to_char({0},'DD'))");
        add(Ops.DateTimeOps.DAY_OF_WEEK, "to_number(to_char({0},'D')) + 1");
        add(Ops.DateTimeOps.DAY_OF_YEAR, "to_number(to_char({0},'DDD'))");
        add(Ops.DateTimeOps.HOUR, "to_number(to_char({0},'HH24'))");
        add(Ops.DateTimeOps.MINUTE, "to_number(to_char({0},'MI'))");
        add(Ops.DateTimeOps.SECOND, "to_number(to_char({0},'SS'))");

        add(Ops.DateTimeOps.YEAR_MONTH, "extract(year from {0}) * 100 + extract(month from {0})");
        add(Ops.DateTimeOps.YEAR_WEEK, "to_number(to_char({0},'IYYY') || to_char({0},'IW'))");

        add(Ops.DateTimeOps.ADD_YEARS, "{0} + interval '{1s}' year");
        add(Ops.DateTimeOps.ADD_MONTHS, "{0} + interval '{1s}' month");
        add(Ops.DateTimeOps.ADD_WEEKS, "{0} + interval '{1s}' week");
        add(Ops.DateTimeOps.ADD_DAYS, "{0} + interval '{1s}' day");
        add(Ops.DateTimeOps.ADD_HOURS, "{0} + interval '{1s}' hour");
        add(Ops.DateTimeOps.ADD_MINUTES, "{0} + interval '{1s}' minute");
        add(Ops.DateTimeOps.ADD_SECONDS, "{0} + interval '{1s}' second");

        add(Ops.DateTimeOps.DIFF_YEARS, "round(({1} - {0}) / 365)");
        add(Ops.DateTimeOps.DIFF_MONTHS, "round(({1} - {0}) / 30)");
        add(Ops.DateTimeOps.DIFF_WEEKS, "round(({1} - {0}) / 7)");
        add(Ops.DateTimeOps.DIFF_DAYS, "round({1} - {0})");
        add(Ops.DateTimeOps.DIFF_HOURS, "round(({1} - {0}) * 24)");
        add(Ops.DateTimeOps.DIFF_MINUTES, "round(({1} - {0}) * 1440)");
        add(Ops.DateTimeOps.DIFF_SECONDS, "round(({1} - {0}) * 86400)");

        add(Ops.DateTimeOps.TRUNC_YEAR, "trunc({0}, 'year')");
        add(Ops.DateTimeOps.TRUNC_MONTH, "trunc({0}, 'month')");
        add(Ops.DateTimeOps.TRUNC_WEEK, "trunc({0}, 'w')");
        add(Ops.DateTimeOps.TRUNC_DAY, "trunc({0}, 'day')");
        add(Ops.DateTimeOps.TRUNC_HOUR, "trunc({0}, 'hh')");
        add(Ops.DateTimeOps.TRUNC_MINUTE, "trunc({0}, 'mi')");
        add(Ops.DateTimeOps.TRUNC_SECOND, "{0}"); // not truncated
    }

    @Override
    public String asLiteral(DateTimeType type, String literal) {
        if (type == DateTimeType.TIME) {
            // Oracle doesn't support the time type
            return "(timestamp '1970-01-01 " + literal + "')";
        } else {
            return super.asLiteral(type, literal);
        }
    }

    @Override
    public void serialize(QueryMetadata metadata, boolean forCountRow, SQLSerializer context) {
        if (!forCountRow && metadata.getModifiers().isRestricting() && !metadata.getJoins().isEmpty()) {
            QueryModifiers mod = metadata.getModifiers();

            if (mod.getOffset() == null) {
                context.append(limitQueryStart);
                context.serializeForQuery(metadata, forCountRow);
                context.handle(limitQueryEnd, mod.getLimit());
            } else {
                context.append(outerQueryStart);
                context.serializeForQuery(metadata, forCountRow);
                context.append(outerQueryEnd);

                if (mod.getLimit() == null) {
                    context.handle(offsetTemplate, mod.getOffset());
                } else {
                    context.handle(limitOffsetTemplate, mod.getOffset(), mod.getLimit());
                }
            }

        } else {
            context.serializeForQuery(metadata, forCountRow);
        }

        if (!metadata.getFlags().isEmpty()) {
            context.serialize(Position.END, metadata.getFlags());
        }
    }

    @Override
    protected void serializeModifiers(QueryMetadata metadata, SQLSerializer context) {
        // do nothing
    }

}
