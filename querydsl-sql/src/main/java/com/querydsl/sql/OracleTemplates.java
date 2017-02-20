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
import java.util.List;
import java.util.Map;

import com.querydsl.core.QueryFlag;
import com.querydsl.core.QueryFlag.Position;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.QueryModifiers;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Path;
import com.querydsl.sql.dml.SQLInsertBatch;

/**
 * {@code OracleTemplates} is an SQL dialect for Oracle
 *
 * <p>tested with Oracle 10g XE</p>
 *
 * @author tiwe
 */
public class OracleTemplates extends SQLTemplates {

    @SuppressWarnings("FieldNameHidesFieldInSuperclass") //Intentional
    public static final OracleTemplates DEFAULT = new OracleTemplates();

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

    private String bulkInsertTemplate = "insert all";

    private String bulkInsertSeparator = " into ";

    public OracleTemplates() {
        this('\\', false);
    }

    public OracleTemplates(boolean quote) {
        this('\\',quote);
    }

    public OracleTemplates(char escape, boolean quote) {
        super(Keywords.ORACLE, "\"", escape, quote, false);
        setParameterMetadataAvailable(false);
        setBatchCountViaGetUpdateCount(true);
        setWithRecursive("with ");
        setCountViaAnalytics(true);
        setListMaxSize(1000);

        setPrecedence(Precedence.COMPARISON, Ops.EQ, Ops.EQ_IGNORE_CASE, Ops.NE);
        setPrecedence(Precedence.COMPARISON + 1, Ops.IS_NULL, Ops.IS_NOT_NULL, Ops.LIKE, Ops.LIKE_ESCAPE, Ops.BETWEEN,
                Ops.IN, Ops.NOT_IN, Ops.EXISTS);

        setPrecedence(Precedence.COMPARISON + 1, OTHER_LIKE_CASES);

        add(Ops.ALIAS, "{0} {1}");
        add(SQLOps.NEXTVAL, "{0s}.nextval");

        // String
        add(Ops.INDEX_OF, "instrb({0},{1})-1", Precedence.ARITH_LOW);
        add(Ops.INDEX_OF_2ARGS, "instrb({0},{1},{2+'1'})-1", Precedence.ARITH_LOW);
        add(Ops.MATCHES, "regexp_like({0},{1})", -1);
        add(Ops.StringOps.LOCATE, "instr({1},{0})");
        add(Ops.StringOps.LOCATE2, "instr({1},{0},{2s})");
        add(Ops.StringOps.LEFT, "substr({0},1,{1})");
        add(Ops.StringOps.RIGHT, "substr({0},-{1s},length({0}))");
        add(SQLOps.GROUP_CONCAT, "listagg({0},',')");
        add(SQLOps.GROUP_CONCAT2, "listagg({0},{1})");

        // Number
        add(Ops.MathOps.CEIL, "ceil({0})");
        add(Ops.MathOps.RANDOM, "dbms_random.value");
        add(Ops.MathOps.LN, "ln({0})");
        add(Ops.MathOps.LOG, "log({1},{0})");
        add(Ops.MathOps.COT, "(cos({0}) / sin({0}))");
        add(Ops.MathOps.COTH, "(exp({0*'2'}) + 1) / (exp({0*'2'}) - 1)");
        add(Ops.MathOps.DEG, "({0*'180.0'} / " + Math.PI + ")");
        add(Ops.MathOps.RAD, "({0*'" + Math.PI + "'} / 180.0)");

        // Date / time
        add(Ops.DateTimeOps.DATE, "trunc({0})");

        add(Ops.DateTimeOps.WEEK, "to_number(to_char({0},'WW'))");
        add(Ops.DateTimeOps.DAY_OF_WEEK, "to_number(to_char({0},'D')) + 1");
        add(Ops.DateTimeOps.DAY_OF_YEAR, "to_number(to_char({0},'DDD'))");
        add(Ops.DateTimeOps.YEAR_WEEK, "to_number(to_char({0},'IYYY') || to_char({0},'IW'))");

        add(Ops.DateTimeOps.ADD_YEARS, "{0} + interval '{1s}' year");
        add(Ops.DateTimeOps.ADD_MONTHS, "{0} + interval '{1s}' month");
        add(Ops.DateTimeOps.ADD_WEEKS, "{0} + interval '{1s}' week");
        add(Ops.DateTimeOps.ADD_DAYS, "{0} + interval '{1s}' day");
        add(Ops.DateTimeOps.ADD_HOURS, "{0} + interval '{1s}' hour");
        add(Ops.DateTimeOps.ADD_MINUTES, "{0} + interval '{1s}' minute");
        add(Ops.DateTimeOps.ADD_SECONDS, "{0} + interval '{1s}' second");

        add(Ops.DateTimeOps.DIFF_YEARS, "trunc(months_between({1}, {0}) / 12)");
        add(Ops.DateTimeOps.DIFF_MONTHS, "trunc(months_between({1}, {0}))");
        add(Ops.DateTimeOps.DIFF_WEEKS, "round(({1} - {0}) / 7)");
        add(Ops.DateTimeOps.DIFF_DAYS, "round({1} - {0})");
        add(Ops.DateTimeOps.DIFF_HOURS, "round(({1} - {0}) * 24)");
        add(Ops.DateTimeOps.DIFF_MINUTES, "round(({1} - {0}) * 1440)");
        add(Ops.DateTimeOps.DIFF_SECONDS, "round(({1} - {0}) * 86400)");

        add(Ops.DateTimeOps.TRUNC_YEAR, "trunc({0}, 'year')");
        add(Ops.DateTimeOps.TRUNC_MONTH, "trunc({0}, 'month')");
        add(Ops.DateTimeOps.TRUNC_WEEK, "trunc({0}, 'w')");
        add(Ops.DateTimeOps.TRUNC_DAY, "trunc({0}, 'dd')");
        add(Ops.DateTimeOps.TRUNC_HOUR, "trunc({0}, 'hh')");
        add(Ops.DateTimeOps.TRUNC_MINUTE, "trunc({0}, 'mi')");
        add(Ops.DateTimeOps.TRUNC_SECOND, "{0}"); // not truncated

        addTypeNameToCode("intervalds", -104);
        addTypeNameToCode("intervalym", -103);
        addTypeNameToCode("timestamp with local time zone", -102);
        addTypeNameToCode("timestamp with time zone", -101);
        addTypeNameToCode("long raw", Types.LONGVARBINARY);
        addTypeNameToCode("raw", Types.VARBINARY);
        addTypeNameToCode("long", Types.LONGVARCHAR);
        addTypeNameToCode("varchar2", Types.VARCHAR);

        addTypeNameToCode("number(1,0)", Types.BOOLEAN, true);
        addTypeNameToCode("number(3,0)", Types.TINYINT, true);
        addTypeNameToCode("number(5,0)", Types.SMALLINT, true);
        addTypeNameToCode("number(10,0)", Types.INTEGER, true);
        addTypeNameToCode("number(19,0)", Types.BIGINT, true);
        addTypeNameToCode("binary_float", Types.FLOAT, true);
        addTypeNameToCode("binary_double", Types.DOUBLE, true);
    }

    @Override
    public String getCastTypeNameForCode(int code) {
        switch (code) {
            case Types.DOUBLE:   return "double precision";
            case Types.VARCHAR:  return "varchar(4000 char)";
            default: return super.getCastTypeNameForCode(code);
        }
    }

    @Override
    public String serialize(String literal, int jdbcType) {
        switch (jdbcType) {
            case Types.TIMESTAMP:
            case TIMESTAMP_WITH_TIMEZONE:
                return "timestamp '" + literal + "'";
            case Types.DATE:
                return "date '" + literal + "'";
            case Types.TIME:
            case TIME_WITH_TIMEZONE:
                return "timestamp '1970-01-01 " + literal + "'";
            default:
                return super.serialize(literal, jdbcType);
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
    public void serializeDelete(QueryMetadata metadata, RelationalPath<?> entity, SQLSerializer context) {
        context.serializeForDelete(metadata, entity);

        // limit
        if (metadata.getModifiers().isRestricting()) {
            serializeModifiersForDML(metadata, context);
        }

        if (!metadata.getFlags().isEmpty()) {
            context.serialize(Position.END, metadata.getFlags());
        }
    }

    @Override
    public void serializeInsert(QueryMetadata metadata, RelationalPath<?> entity, List<SQLInsertBatch> batches, SQLSerializer context) {
        context.append(bulkInsertTemplate);
        metadata.addFlag(new QueryFlag(Position.START_OVERRIDE, bulkInsertSeparator));
        for (SQLInsertBatch batch : batches) {
            serializeInsert(metadata, entity, batch.getColumns(), batch.getValues(), batch.getSubQuery(), context);
        }
        context.append(" select * from dual");
    }

    @Override
    public void serializeUpdate(QueryMetadata metadata, RelationalPath<?> entity,
                                Map<Path<?>, Expression<?>> updates, SQLSerializer context) {
        context.serializeForUpdate(metadata, entity, updates);

        // limit
        if (metadata.getModifiers().isRestricting()) {
            serializeModifiersForDML(metadata, context);
        }

        if (!metadata.getFlags().isEmpty()) {
            context.serialize(Position.END, metadata.getFlags());
        }
    }

    private void serializeModifiersForDML(QueryMetadata metadata, SQLSerializer context) {
        if (metadata.getWhere() != null) {
            context.append(" and ");
        } else {
            context.append(getWhere());
        }
        context.append("rownum <= ");
        context.visitConstant(metadata.getModifiers().getLimit());
    }

    @Override
    protected void serializeModifiers(QueryMetadata metadata, SQLSerializer context) {
        // do nothing
    }

}
