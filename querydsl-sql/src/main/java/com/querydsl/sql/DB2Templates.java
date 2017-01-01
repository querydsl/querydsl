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

import com.querydsl.core.QueryFlag;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.QueryModifiers;
import com.querydsl.core.types.*;

/**
 * {@code DB2Templates} is an SQL dialect for DB2 10.1.2
 *
 * @author tiwe
 *
 */
public class DB2Templates extends SQLTemplates {

    @SuppressWarnings("FieldNameHidesFieldInSuperclass") //Intentional
    public static final DB2Templates DEFAULT = new DB2Templates();

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
        super(Keywords.DB2, "\"", escape, quote, false);
        setDummyTable("sysibm.sysdummy1");
        setAutoIncrement(" generated always as identity");
        setFunctionJoinsWrapped(true);
        setDefaultValues("\nvalues (default)");
        setNullsFirst(null);
        setNullsLast(null);

        setPrecedence(Precedence.ARITH_HIGH, Ops.CONCAT);
        setPrecedence(Precedence.COMPARISON - 1, Ops.EQ, Ops.EQ_IGNORE_CASE, Ops.NE, Ops.LT, Ops.GT, Ops.LOE, Ops.GOE);
        setPrecedence(Precedence.COMPARISON, Ops.IS_NULL, Ops.IS_NOT_NULL, Ops.LIKE, Ops.LIKE_ESCAPE, Ops.BETWEEN,
                Ops.IN, Ops.NOT_IN, Ops.EXISTS);

        setPrecedence(Precedence.COMPARISON, OTHER_LIKE_CASES);

        add(SQLOps.NEXTVAL, "next value for {0s}");

        add(Ops.MathOps.RANDOM, "rand()");
        add(Ops.MathOps.LN, "log({0})");
        add(Ops.MathOps.LOG, "(log({0}) / log({1}))");
        add(Ops.MathOps.COTH, "(exp({0*'2'}) + 1) / (exp({0*'2'}) - 1)");

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

        add(Ops.DateTimeOps.DIFF_YEARS,   "timestampdiff(256, char(timestamp({1}) - timestamp({0})))");
        add(Ops.DateTimeOps.DIFF_MONTHS,  "timestampdiff(64, char(timestamp({1}) - timestamp({0})))");
        add(Ops.DateTimeOps.DIFF_WEEKS,   "timestampdiff(32, char(timestamp({1}) - timestamp({0})))");
        add(Ops.DateTimeOps.DIFF_DAYS,    "timestampdiff(16, char(timestamp({1}) - timestamp({0})))");
        add(Ops.DateTimeOps.DIFF_HOURS,   "timestampdiff(8, char(timestamp({1}) - timestamp({0})))");
        add(Ops.DateTimeOps.DIFF_MINUTES, "timestampdiff(4, char(timestamp({1}) - timestamp({0})))");
        add(Ops.DateTimeOps.DIFF_SECONDS, "timestampdiff(2, char(timestamp({1}) - timestamp({0})))");

        add(Ops.DateTimeOps.TRUNC_YEAR, "trunc_timestamp({0}, 'year')");
        add(Ops.DateTimeOps.TRUNC_MONTH, "trunc_timestamp({0}, 'month')");
        add(Ops.DateTimeOps.TRUNC_WEEK, "trunc_timestamp({0}, 'ww')");
        add(Ops.DateTimeOps.TRUNC_DAY, "trunc_timestamp({0}, 'dd')");
        add(Ops.DateTimeOps.TRUNC_HOUR, "trunc_timestamp({0}, 'hh')");
        add(Ops.DateTimeOps.TRUNC_MINUTE, "trunc_timestamp({0}, 'mi')");
        add(Ops.DateTimeOps.TRUNC_SECOND, "trunc_timestamp({0}, 'ss')");

        addTypeNameToCode("smallint", Types.BOOLEAN, true);
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
                FactoryExpression<?> pr = Projections.appending(metadata.getProjection(), rn.as("rn"));
                metadata.setProjection(FactoryExpressionUtils.wrap(pr));
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
