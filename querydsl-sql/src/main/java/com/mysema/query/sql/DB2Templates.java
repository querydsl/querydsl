/*
 * Copyright 2014, Timo Westkämper
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

import java.sql.Types;

import com.mysema.query.QueryFlag;
import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryModifiers;
import com.mysema.query.types.Ops;
import com.mysema.query.types.OrderSpecifier;

/**
 * DB2Templates is an SQL dialect for DB2 10.1.2
 *
 * @author tiwe
 *
 */
public class DB2Templates extends SQLTemplates {

    private String limitTemplate = "\nfetch first {0s} rows only";

    private String outerQueryStart = "select * from (\n  ";

    private String outerQueryEnd = ") a where ";

    private String limitOffsetTemplate = "rn > {0} and rn <= {1}";

    private String offsetTemplate = "rn > {0}";

    private String outerQuerySuffix = " order by rn";

    public static Builder builder() {
        return new Builder() {
            @Override
            protected SQLTemplates build(char escape, boolean quote) {
                return new DB2Templates(escape, quote);
            }
        };
    }

    public DB2Templates() {
        this('\\',false);
    }

    public DB2Templates(boolean quote) {
        this('\\',quote);
    }

    public DB2Templates(char escape, boolean quote) {
        super("\"", escape, quote);
        setDummyTable("sysibm.sysdummy1");
        setAutoIncrement(" generated always as identity");
        setFunctionJoinsWrapped(true);
        setDefaultValues("\nvalues (default)");
        setNullsFirst(null);
        setNullsLast(null);

        add(SQLOps.NEXTVAL, "next value for {0s}");

        add(Ops.MathOps.RANDOM, "rand()");
        add(Ops.MathOps.LN, "log({0})");
        add(Ops.MathOps.LOG, "(log({0}) / log({1}))");
        add(Ops.MathOps.COTH, "(exp({0} * 2) + 1) / (exp({0} * 2) - 1)");

        // overrides of the SQL standard functions
        add(Ops.DateTimeOps.SECOND, "second({0})");
        add(Ops.DateTimeOps.MINUTE, "minute({0})");
        add(Ops.DateTimeOps.HOUR, "hour({0})");
        add(Ops.DateTimeOps.WEEK, "week({0})");
        add(Ops.DateTimeOps.MONTH, "month({0})");
        add(Ops.DateTimeOps.YEAR, "year({0})");
        add(Ops.DateTimeOps.YEAR_MONTH, "(year({0}) * 100 + month({0}))");
        add(Ops.DateTimeOps.YEAR_WEEK, "(year({0}) * 100 + week({0}))");
        add(Ops.DateTimeOps.DAY_OF_WEEK, "dayofweek({0})");
        add(Ops.DateTimeOps.DAY_OF_MONTH, "day({0})");
        add(Ops.DateTimeOps.DAY_OF_YEAR, "dayofyear({0})");

        add(Ops.DateTimeOps.ADD_YEARS, "{0} + {1} years");
        add(Ops.DateTimeOps.ADD_MONTHS, "{0} + {1} months");
        add(Ops.DateTimeOps.ADD_WEEKS, "{0} + {1} weeks");
        add(Ops.DateTimeOps.ADD_DAYS, "{0} + {1} days");
        add(Ops.DateTimeOps.ADD_HOURS, "{0} + {1} hours");
        add(Ops.DateTimeOps.ADD_MINUTES, "{0} + {1} minutes");
        add(Ops.DateTimeOps.ADD_SECONDS, "{0} + {1} seconds");

        // FIXME
        add(Ops.DateTimeOps.DIFF_YEARS, "timestampdiff(256, char({0} - {1}))");
        add(Ops.DateTimeOps.DIFF_MONTHS, "timestampdiff(64, char({0} - {1}))");
        add(Ops.DateTimeOps.DIFF_WEEKS, "timestampdiff(32, char({0} - {1}))");
        add(Ops.DateTimeOps.DIFF_DAYS, "timestampdiff(16, char({0} - {1}))");
        add(Ops.DateTimeOps.DIFF_HOURS, "timestampdiff(8, char({0} - {1}))");
        add(Ops.DateTimeOps.DIFF_MINUTES, "timestampdiff(4, char({0} - {1}))");
        add(Ops.DateTimeOps.DIFF_SECONDS, "timestampdiff(2, char({0} - {1}))");

        add(Ops.DateTimeOps.TRUNC_YEAR, "trunc_timestamp({0}, 'year')");
        add(Ops.DateTimeOps.TRUNC_MONTH, "trunc_timestamp({0}, 'month')");
        add(Ops.DateTimeOps.TRUNC_WEEK, "trunc_timestamp({0}, 'week')");
        add(Ops.DateTimeOps.TRUNC_DAY, "trunc_timestamp({0}, 'day')");
        add(Ops.DateTimeOps.TRUNC_HOUR, "trunc_timestamp({0}, 'hour')");
        add(Ops.DateTimeOps.TRUNC_MINUTE, "trunc_timestamp({0}, 'minute')");
        add(Ops.DateTimeOps.TRUNC_SECOND, "trunc_timestamp({0}, 'second')");

        addTypeNameToCode("smallint", Types.TINYINT, true);
        addTypeNameToCode("long varchar for bit data", Types.LONGVARBINARY);
        addTypeNameToCode("varchar () for bit data", Types.VARBINARY);
        addTypeNameToCode("char () for bit data", Types.BINARY);
        addTypeNameToCode("long varchar", Types.LONGVARCHAR, true);
        addTypeNameToCode("object", Types.JAVA_OBJECT, true);
        addTypeNameToCode("xml", Types.SQLXML,true);
    }

    @Override
    public String getCastTypeNameForCode(int code) {
        switch (code) {
            case Types.VARCHAR:  return "varchar(4000)";
            default: return super.getCastTypeNameForCode(code);
        }
    }


    @Override
    public String serialize(String literal, int jdbcType) {
        if (jdbcType == Types.TIMESTAMP) {
            return "{ts '" + literal + "'}";
        } else if (jdbcType == Types.DATE) {
            return "{d '" + literal + "'}";
        } else if (jdbcType == Types.TIME) {
            return "{t '" + literal + "'}";
        } else {
            return super.serialize(literal, jdbcType);
        }
    }

    @Override
    public void serialize(QueryMetadata metadata, boolean forCountRow, SQLSerializer context) {
        if (!forCountRow && metadata.getModifiers().isRestricting() && !metadata.getJoins().isEmpty()) {
            QueryModifiers mod = metadata.getModifiers();
            if (mod.getOffset() == null) {
                context.serializeForQuery(metadata, forCountRow);
                context.handle(limitTemplate, mod.getLimit());
            } else {
                context.append(outerQueryStart);
                metadata = metadata.clone();
                WindowFunction<Long> rn = SQLExpressions.rowNumber().over();
                for (OrderSpecifier<?> os : metadata.getOrderBy()) {
                    rn.orderBy(os);
                }
                metadata.addProjection(rn.as("rn"));
                metadata.clearOrderBy();
                context.serializeForQuery(metadata, forCountRow);
                context.append(outerQueryEnd);
                if (mod.getLimit() == null) {
                    context.handle(offsetTemplate, mod.getOffset());
                } else {
                    context.handle(limitOffsetTemplate, mod.getOffset(), mod.getLimit() + mod.getOffset());
                }
                context.append(outerQuerySuffix);
            }

        } else {
            context.serializeForQuery(metadata, forCountRow);
        }

        if (!metadata.getFlags().isEmpty()) {
            context.serialize(QueryFlag.Position.END, metadata.getFlags());
        }
    }

    @Override
    protected void serializeModifiers(QueryMetadata metadata, SQLSerializer context) {
        // do nothing
    }


}
