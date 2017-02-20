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
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.querydsl.core.QueryFlag;
import com.querydsl.core.QueryFlag.Position;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.QueryModifiers;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.dsl.Expressions;

/**
 * {@code SQLServerTemplates} is an SQL dialect for Microsoft SQL Server
 *
 * @author tiwe
 *
 */
public class SQLServerTemplates extends SQLTemplates {

    protected static final Expression<?> WITH_REPEATABLE_READ = ExpressionUtils.operation(
        Object.class, SQLOps.WITH_REPEATABLE_READ, ImmutableList.<Expression<?>>of());

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
        this(Keywords.DEFAULT, escape, quote);
    }

    protected SQLServerTemplates(Set<String> keywords, char escape, boolean quote) {
        super(keywords, "\"", escape, quote, false);
        setDummyTable("");
        setNullsFirst(null);
        setNullsLast(null);
        setDefaultValues("\ndefault values");
        setArraysSupported(false);
        setForUpdateFlag(new QueryFlag(Position.BEFORE_FILTERS, FOR_UPDATE));

        setForShareSupported(true);
        setForShareFlag(new QueryFlag(Position.BEFORE_FILTERS, WITH_REPEATABLE_READ));

        setPrecedence(Precedence.ARITH_LOW, Ops.NEGATE);
        setPrecedence(Precedence.COMPARISON, Ops.EQ, Ops.EQ_IGNORE_CASE, Ops.NE);
        setPrecedence(Precedence.OR, Ops.BETWEEN, Ops.IN, Ops.NOT_IN, Ops.LIKE, Ops.LIKE_ESCAPE);
        setPrecedence(Precedence.OR, OTHER_LIKE_CASES);
        setPrecedence(Precedence.OR + 1, Ops.LIST, Ops.SET, Ops.SINGLETON);

        add(SQLOps.WITH_REPEATABLE_READ, "\nwith (repeatableread)");

        // String
        add(Ops.CONCAT, "{0} + {1}");
        add(Ops.CHAR_AT, "cast(substring({0},{1+'1'},1) as char)");
        add(Ops.INDEX_OF, "charindex({1},{0})-1", Precedence.ARITH_LOW);
        add(Ops.INDEX_OF_2ARGS, "charindex({1},{0},{2})-1", Precedence.ARITH_LOW);
        // NOTE : needs to be replaced with real regular expression
        add(Ops.MATCHES, "{0} like {1}", Precedence.OR);
        add(Ops.STRING_IS_EMPTY, "len({0}) = 0", Precedence.COMPARISON);
        add(Ops.STRING_LENGTH, "len({0})");
        add(Ops.SUBSTR_1ARG, "substring({0},{1+'1'},255)");
        add(Ops.SUBSTR_2ARGS, "substring({0},{1+'1'},{2-1s})");
        add(Ops.TRIM, "ltrim(rtrim({0}))");

        add(SQLOps.FOR_UPDATE, "\nwith (updlock)");

        add(Ops.StringOps.LOCATE, "charindex({0},{1})");
        add(Ops.StringOps.LOCATE2, "charindex({0},{1},{2})");
        add(Ops.StringOps.LPAD, "right(replicate(' ', {1}) + left({0}, {1}), {1})");
        add(Ops.StringOps.LPAD2, "right(replicate({2}, {1}) + left({0}, {1}), {1})");
        add(Ops.StringOps.RPAD, "left(left({0}, {1}) + replicate(' ', {1}), {1})");
        add(Ops.StringOps.RPAD2, "left(left({0}, {1}) + replicate({2}, {1}), {1})");

        add(SQLOps.NEXTVAL, "{0s}.nextval");

        add(Ops.MOD, "{0} % {1}", Precedence.ARITH_HIGH);
        add(Ops.MathOps.COSH, "(exp({0}) + exp({0*'-1'})) / 2");
        add(Ops.MathOps.COTH, "(exp({0*'2'}) + 1) / (exp({0*'2'}) - 1)");
        add(Ops.MathOps.LN, "log({0})");
        add(Ops.MathOps.LOG, "log({0}, {1})");
        add(Ops.MathOps.POWER, "power({0}, {1})");
        add(Ops.MathOps.ROUND, "round({0}, 0)");
        add(Ops.MathOps.SINH, "(exp({0}) - exp({0*'-1'})) / 2");
        add(Ops.MathOps.TANH, "(exp({0*'2'}) - 1) / (exp({0*'2'}) + 1)");

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

        // truncates timestamps by replacing suffix
        add(Ops.DateTimeOps.TRUNC_YEAR,   "CONVERT(DATETIME, CONVERT(VARCHAR(4), {0}, 120) + '-01-01')");
        add(Ops.DateTimeOps.TRUNC_MONTH,  "CONVERT(DATETIME, CONVERT(VARCHAR(7), {0}, 120) + '-01')");
        // TODO week
        add(Ops.DateTimeOps.TRUNC_DAY,    "CONVERT(DATETIME, CONVERT(VARCHAR(10), {0}, 120))");
        add(Ops.DateTimeOps.TRUNC_HOUR,   "CONVERT(DATETIME, CONVERT(VARCHAR(13), {0}, 120) + ':00:00')");
        add(Ops.DateTimeOps.TRUNC_MINUTE, "CONVERT(DATETIME, CONVERT(VARCHAR(16), {0}, 120) + ':00')");
        add(Ops.DateTimeOps.TRUNC_SECOND, "CONVERT(DATETIME, CONVERT(VARCHAR(19), {0}, 120))");

        add(Ops.DateTimeOps.DATE, "cast({0} as date)");
        add(Ops.DateTimeOps.CURRENT_DATE, "cast(getdate() as date)");

        addTypeNameToCode("bit", Types.BOOLEAN, true);
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
        switch (jdbcType) {
            case Types.TIMESTAMP:
            case TIMESTAMP_WITH_TIMEZONE:
                return "{ts '" + literal + "'}";
            case Types.DATE:
                return "{d '" + literal + "'}";
            case Types.TIME:
            case TIME_WITH_TIMEZONE:
                return "{t '" + literal + "'}";
            default:
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
