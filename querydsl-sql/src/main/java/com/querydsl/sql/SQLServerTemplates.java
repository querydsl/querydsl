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

import com.querydsl.core.QueryFlag;
import com.querydsl.core.QueryFlag.Position;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.QueryModifiers;
import com.querydsl.core.support.Expressions;
import com.querydsl.core.types.Ops;

/**
 * SQLServerTemplates is an SQL dialect for Microsoft SQL Server
 *
 * @author tiwe
 *
 */
public class SQLServerTemplates extends SQLTemplates {

    @SuppressWarnings("FieldNameHidesFieldInSuperclass") //Intentional
    public static final SQLServerTemplates DEFAULT = new SQLServerTemplates();

    public static Builder builder() {
        return new Builder() {
            @Override
            protected SQLTemplates build(char escape, boolean quote) {
                return new SQLServerTemplates(escape, quote);
            }
        };
    }

    private String topTemplate = "top {0s} ";

    public SQLServerTemplates() {
        this('\\',false);
    }

    public SQLServerTemplates(boolean quote) {
        this('\\',quote);
    }

    public SQLServerTemplates(char escape, boolean quote) {
        super("\"", escape, quote);
        setDummyTable("");
        setNullsFirst(null);
        setNullsLast(null);
        setDefaultValues("\ndefault values");
        setArraysSupported(false);

        // String
        add(Ops.CONCAT, "{0} + {1}", 13);
        add(Ops.CHAR_AT, "cast(substring({0},{1}+1,1) as char)");
        add(Ops.INDEX_OF, "charindex({1},{0})-1");
        add(Ops.INDEX_OF_2ARGS, "charindex({1},{0},{2})-1");
        // NOTE : needs to be replaced with real regular expression
        add(Ops.MATCHES, "{0} like {1}");
        add(Ops.STRING_IS_EMPTY, "len({0}) = 0");
        add(Ops.STRING_LENGTH, "len({0})");
        add(Ops.SUBSTR_1ARG, "substring({0},{1}+1,255)");
        add(Ops.SUBSTR_2ARGS, "substring({0},{1}+1,{2s}-{1s})", 1);
        add(Ops.TRIM, "ltrim(rtrim({0}))");

        add(Ops.StringOps.LOCATE, "charindex({0},{1})");
        add(Ops.StringOps.LOCATE2, "charindex({0},{1},{2})");
        add(Ops.StringOps.LPAD, "right(replicate(' ', {1}) + left({0}, {1}), {1})");
        add(Ops.StringOps.LPAD2, "right(replicate({2}, {1}) + left({0}, {1}), {1})");
        add(Ops.StringOps.RPAD, "left(left({0}, {1}) + replicate(' ', {1}), {1})");
        add(Ops.StringOps.RPAD2, "left(left({0}, {1}) + replicate({2}, {1}), {1})");

        add(SQLOps.NEXTVAL, "{0s}.nextval");

        add(Ops.MOD, "{0} % {1}", 10);
        add(Ops.MathOps.COSH, "(exp({0}) + exp({0} * -1)) / 2");
        add(Ops.MathOps.COTH, "(exp({0} * 2) + 1) / (exp({0} * 2) - 1)");
        add(Ops.MathOps.LN, "log({0})");
        add(Ops.MathOps.LOG, "log({0}, {1})");
        add(Ops.MathOps.POWER, "power({0}, {1})");
        add(Ops.MathOps.ROUND, "round({0}, 0)");
        add(Ops.MathOps.SINH, "(exp({0}) - exp({0} * -1)) / 2");
        add(Ops.MathOps.TANH, "(exp({0} * 2) - 1) / (exp({0} * 2) + 1)");

        // Date / time
        add(Ops.DateTimeOps.YEAR, "datepart(year, {0})");
        add(Ops.DateTimeOps.MONTH, "datepart(month, {0})");
        add(Ops.DateTimeOps.WEEK, "datepart(week, {0})");
        add(Ops.DateTimeOps.DAY_OF_MONTH, "datepart(day, {0})");
        add(Ops.DateTimeOps.DAY_OF_WEEK, "datepart(weekday, {0})");
        add(Ops.DateTimeOps.DAY_OF_YEAR, "datepart(dayofyear, {0})");
        add(Ops.DateTimeOps.HOUR, "datepart(hour, {0})");
        add(Ops.DateTimeOps.MINUTE, "datepart(minute, {0})");
        add(Ops.DateTimeOps.SECOND, "datepart(second, {0})");
        add(Ops.DateTimeOps.MILLISECOND, "datepart(millisecond, {0})");

        add(Ops.DateTimeOps.YEAR_MONTH, "(datepart(year, {0}) * 100 + datepart(month, {0}))");
        add(Ops.DateTimeOps.YEAR_WEEK, "(datepart(year, {0}) * 100 + datepart(isowk, {0}))");

        add(Ops.DateTimeOps.ADD_YEARS, "dateadd(year, {1s}, {0})");
        add(Ops.DateTimeOps.ADD_MONTHS, "dateadd(month, {1s}, {0})");
        add(Ops.DateTimeOps.ADD_WEEKS, "dateadd(week, {1s}, {0})");
        add(Ops.DateTimeOps.ADD_DAYS, "dateadd(day, {1s}, {0})");
        add(Ops.DateTimeOps.ADD_HOURS, "dateadd(hour, {1s}, {0})");
        add(Ops.DateTimeOps.ADD_MINUTES, "dateadd(minute, {1s}, {0})");
        add(Ops.DateTimeOps.ADD_SECONDS, "dateadd(second, {1s}, {0})");

        add(Ops.DateTimeOps.DIFF_YEARS, "datediff(year,{0},{1})");
        add(Ops.DateTimeOps.DIFF_MONTHS, "datediff(month,{0},{1})");
        add(Ops.DateTimeOps.DIFF_WEEKS, "datediff(week,{0},{1})");
        add(Ops.DateTimeOps.DIFF_DAYS, "datediff(day,{0},{1})");
        add(Ops.DateTimeOps.DIFF_HOURS, "datediff(hour,{0},{1})");
        add(Ops.DateTimeOps.DIFF_MINUTES, "datediff(minute,{0},{1})");
        add(Ops.DateTimeOps.DIFF_SECONDS, "datediff(second,{0},{1})");

        add(Ops.DateTimeOps.DATE, "cast({0} as date)");
        add(Ops.DateTimeOps.CURRENT_DATE, "cast(getdate() as date)");

        addTypeNameToCode("decimal", Types.DOUBLE, true);
        addTypeNameToCode("tinyint identity", Types.TINYINT);
        addTypeNameToCode("bigint identity", Types.BIGINT);
        addTypeNameToCode("timestamp", Types.BINARY);
        addTypeNameToCode("nchar", Types.CHAR);
        addTypeNameToCode("uniqueidentifier", Types.CHAR);
        addTypeNameToCode("numeric() identity", Types.NUMERIC);
        addTypeNameToCode("money", Types.DECIMAL);
        addTypeNameToCode("smallmoney", Types.DECIMAL);
        addTypeNameToCode("decimal() identity", Types.DECIMAL);
        addTypeNameToCode("int", Types.INTEGER);
        addTypeNameToCode("int identity", Types.INTEGER);
        addTypeNameToCode("smallint identity", Types.SMALLINT);
        addTypeNameToCode("float", Types.DOUBLE);
        addTypeNameToCode("nvarchar", Types.VARCHAR);
        addTypeNameToCode("date", Types.VARCHAR);
        addTypeNameToCode("time", Types.VARCHAR);
        addTypeNameToCode("datetime2", Types.VARCHAR);
        addTypeNameToCode("datetimeoffset", Types.VARCHAR);
        addTypeNameToCode("sysname", Types.VARCHAR);
        addTypeNameToCode("sql_variant", Types.VARCHAR);
        addTypeNameToCode("datetime", Types.TIMESTAMP);
        addTypeNameToCode("smalldatetime", Types.TIMESTAMP);
        addTypeNameToCode("image", Types.BLOB);
        addTypeNameToCode("ntext", Types.CLOB);
        addTypeNameToCode("xml", Types.CLOB);
        addTypeNameToCode("text", Types.CLOB);
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
    protected String escapeForLike(String str) {
        final StringBuilder rv = new StringBuilder(str.length() + 3);
        for (char ch : str.toCharArray()) {
            if (ch == getEscapeChar() || ch == '%' || ch == '_' || ch == '[') {
                rv.append(getEscapeChar());
            }
            rv.append(ch);
        }
        return rv.toString();
    }

    @Override
    public void serialize(QueryMetadata metadata, boolean forCountRow, SQLSerializer context) {
        if (!forCountRow && metadata.getModifiers().isRestricting() && !metadata.getJoins().isEmpty()) {
            QueryModifiers mod = metadata.getModifiers();
            if (mod.getOffset() == null) {
                // select top ...
                metadata = metadata.clone();
                metadata.addFlag(new QueryFlag(QueryFlag.Position.AFTER_SELECT,
                        Expressions.template(Integer.class, topTemplate, mod.getLimit())));
                context.serializeForQuery(metadata, forCountRow);
            } else {
                throw new IllegalStateException("offset not supported");
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
