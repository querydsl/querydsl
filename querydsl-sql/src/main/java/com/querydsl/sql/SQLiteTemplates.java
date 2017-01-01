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

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.querydsl.core.types.Ops;
import com.querydsl.sql.types.BigDecimalAsDoubleType;
import com.querydsl.sql.types.BigIntegerAsLongType;

/**
 * {@code SQLiteTemplates} is a SQL dialect for SQLite
 *
 * @author tiwe
 *
 */
public class SQLiteTemplates extends SQLTemplates {

    private static final DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    private static final DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("HH:mm:ss");

    @SuppressWarnings("FieldNameHidesFieldInSuperclass") //Intentional
    public static final SQLiteTemplates DEFAULT = new SQLiteTemplates();

    public static Builder builder() {
        return new Builder() {
            @Override
            protected SQLTemplates build(char escape, boolean quote) {
                return new SQLiteTemplates(escape, quote);
            }
        };
    }

    public SQLiteTemplates() {
        this('\\', false);
    }

    public SQLiteTemplates(boolean quote) {
        this('\\', quote);
    }

    public SQLiteTemplates(char escape, boolean quote) {
        super(Keywords.SQLITE, "\"", escape, quote, false);
        setDummyTable(null);
        addCustomType(BigDecimalAsDoubleType.DEFAULT);
        addCustomType(BigIntegerAsLongType.DEFAULT);
        setUnionsWrapped(false);
        setLimitRequired(true);
        setNullsFirst(null);
        setNullsLast(null);
        setDefaultValues("\ndefault values");
        setArraysSupported(false);
        setBatchToBulkSupported(false);

        setPrecedence(Precedence.COMPARISON - 1, Ops.LT, Ops.GT, Ops.LOE, Ops.GOE);
        setPrecedence(Precedence.COMPARISON, Ops.EQ, Ops.EQ_IGNORE_CASE, Ops.NE);

        add(Ops.MOD, "{0} % {1}", Precedence.ARITH_HIGH);

        add(Ops.INDEX_OF, "charindex({1},{0},1)-1", Precedence.ARITH_LOW);
        add(Ops.INDEX_OF_2ARGS, "charindex({1},{0},{2s}+1)-1", Precedence.ARITH_LOW);

        add(Ops.StringOps.LOCATE, "charindex({0},{1})");
        add(Ops.StringOps.LOCATE2, "charindex({0},{1},{2s})");

        // TODO : optimize
        add(Ops.DateTimeOps.YEAR, "cast(strftime('%Y',{0} / 1000, 'unixepoch', 'localtime') as integer)");
        add(Ops.DateTimeOps.MONTH, "cast(strftime('%m',{0} / 1000, 'unixepoch', 'localtime') as integer)");
        add(Ops.DateTimeOps.WEEK, "cast(strftime('%W',{0} / 1000, 'unixepoch', 'localtime') as integer) + 1");
        add(Ops.DateTimeOps.DAY_OF_MONTH, "cast(strftime('%d',{0} / 1000, 'unixepoch', 'localtime') as integer)");
        add(Ops.DateTimeOps.DAY_OF_WEEK, "cast(strftime('%w',{0} / 1000, 'unixepoch', 'localtime') as integer) + 1");
        add(Ops.DateTimeOps.DAY_OF_YEAR, "cast(strftime('%j',{0} / 1000, 'unixepoch', 'localtime') as integer)");
        add(Ops.DateTimeOps.HOUR, "cast(strftime('%H',{0} / 1000, 'unixepoch', 'localtime') as integer)");
        add(Ops.DateTimeOps.MINUTE, "cast(strftime('%M',{0} / 1000, 'unixepoch', 'localtime') as integer)");
        add(Ops.DateTimeOps.SECOND, "cast(strftime('%S',{0} / 1000, 'unixepoch', 'localtime') as integer)");

        add(Ops.DateTimeOps.YEAR_MONTH, "cast(strftime('%Y',{0} / 1000, 'unixepoch', 'localtime') * 100 + strftime('%m',{0} / 1000, 'unixepoch', 'localtime') as integer)");
        add(Ops.DateTimeOps.YEAR_WEEK, "cast(strftime('%Y%W',{0} / 1000, 'unixepoch', 'localtime') as integer)");

        add(Ops.DateTimeOps.ADD_YEARS, "date({0}, '+{1s} year')");
        add(Ops.DateTimeOps.ADD_MONTHS, "date({0}, '+{1s} month')");
        add(Ops.DateTimeOps.ADD_WEEKS, "date({0}, '+{1s} week')");
        add(Ops.DateTimeOps.ADD_DAYS, "date({0}, '+{1s} day')");
        add(Ops.DateTimeOps.ADD_HOURS, "date({0}, '+{1s} hour')");
        add(Ops.DateTimeOps.ADD_MINUTES, "date({0}, '+{1s} minute')");
        add(Ops.DateTimeOps.ADD_SECONDS, "date({0}, '+{1s} second')");

        add(Ops.MathOps.RANDOM, "random()");
        add(Ops.MathOps.RANDOM2, "random({0})");
        add(Ops.MathOps.LN, "log({0})");
        add(Ops.MathOps.LOG, "log({0}) / log({1})", Precedence.ARITH_HIGH);

        add(SQLOps.GROUP_CONCAT2, "group_concat({0},{1})");

        addTypeNameToCode("text", Types.VARCHAR);
    }

    @Override
    public String serialize(String literal, int jdbcType) {
        // XXX doesn't work with LocalDate, LocalDateTime and LocalTime
        switch (jdbcType) {
            case Types.TIMESTAMP:
            case TIMESTAMP_WITH_TIMEZONE:
                return String.valueOf(dateTimeFormatter.parseDateTime(literal).getMillis());
            case Types.DATE:
                return String.valueOf(dateFormatter.parseDateTime(literal).getMillis());
            case Types.TIME:
            case TIME_WITH_TIMEZONE:
                return String.valueOf(timeFormatter.parseDateTime(literal).getMillis());
            default:
                return super.serialize(literal, jdbcType);
        }
    }

}
