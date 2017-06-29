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

import com.google.common.collect.ImmutableList;
import com.querydsl.core.QueryFlag;
import com.querydsl.core.QueryFlag.Position;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Ops;

/**
 * {@code MySQLTemplates} is an SQL dialect for MySQL
 *
 * <p>tested with MySQL CE 5.1 and 5.5</p>
 *
 * @author tiwe
 *
 */
public class MySQLTemplates extends SQLTemplates {

    protected static final Expression<?> LOCK_IN_SHARE_MODE = ExpressionUtils.operation(
        Object.class, SQLOps.LOCK_IN_SHARE_MODE, ImmutableList.<Expression<?>>of());

    @SuppressWarnings("FieldNameHidesFieldInSuperclass") //Intentional
    public static final MySQLTemplates DEFAULT = new MySQLTemplates();

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
        super(Keywords.MYSQL, "`", escape, quote, false);
        setArraysSupported(false);
        setParameterMetadataAvailable(false);
        setLimitRequired(true);
        setSupportsUnquotedReservedWordsAsIdentifier(true);
        setNullsFirst(null);
        setNullsLast(null);

        setForShareSupported(true);
        setForShareFlag(new QueryFlag(Position.END, LOCK_IN_SHARE_MODE));

        setPrecedence(Precedence.COMPARISON, Ops.EQ, Ops.EQ_IGNORE_CASE, Ops.NE);
        setPrecedence(Precedence.CASE, Ops.BETWEEN);

        add(SQLOps.LOCK_IN_SHARE_MODE, "\nlock in share mode");

        add(Ops.MOD, "{0} % {1}", Precedence.ARITH_HIGH);
        add(Ops.CONCAT, "concat({0}, {1})", -1);

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
        add(Ops.MathOps.COSH, "(exp({0}) + exp({0*'-1'})) / 2");
        add(Ops.MathOps.COTH, "(exp({0*'2'}) + 1) / (exp({0*'2'}) - 1)");
        add(Ops.MathOps.SINH, "(exp({0}) - exp({0*'-1'})) / 2");
        add(Ops.MathOps.TANH, "(exp({0*'2'}) - 1) / (exp({0*'2'}) + 1)");

        add(Ops.AggOps.BOOLEAN_ANY, "bit_or({0})", 0);
        add(Ops.AggOps.BOOLEAN_ALL, "bit_and({0})", 0);

        add(Ops.DateTimeOps.DAY_OF_WEEK, "dayofweek({0})");
        add(Ops.DateTimeOps.DAY_OF_YEAR, "dayofyear({0})");
        add(Ops.DateTimeOps.YEAR_MONTH, "extract(year_month from {0})");
        add(Ops.DateTimeOps.YEAR_WEEK, "yearweek({0})");

        add(Ops.DateTimeOps.ADD_YEARS, "date_add({0}, interval {1s} year)");
        add(Ops.DateTimeOps.ADD_MONTHS, "date_add({0}, interval {1s} month)");
        add(Ops.DateTimeOps.ADD_WEEKS, "date_add({0}, interval {1s} week)");
        add(Ops.DateTimeOps.ADD_DAYS, "date_add({0}, interval {1s} day)");
        add(Ops.DateTimeOps.ADD_HOURS, "date_add({0}, interval {1s} hour)");
        add(Ops.DateTimeOps.ADD_MINUTES, "date_add({0}, interval {1s} minute)");
        add(Ops.DateTimeOps.ADD_SECONDS, "date_add({0}, interval {1s} second)");

        add(Ops.DateTimeOps.DIFF_YEARS, "timestampdiff(year,{0},{1})");
        add(Ops.DateTimeOps.DIFF_MONTHS, "timestampdiff(month,{0},{1})");
        add(Ops.DateTimeOps.DIFF_WEEKS, "timestampdiff(week,{0},{1})");
        add(Ops.DateTimeOps.DIFF_DAYS, "timestampdiff(day,{0},{1})");
        add(Ops.DateTimeOps.DIFF_HOURS, "timestampdiff(hour,{0},{1})");
        add(Ops.DateTimeOps.DIFF_MINUTES, "timestampdiff(minute,{0},{1})");
        add(Ops.DateTimeOps.DIFF_SECONDS, "timestampdiff(second,{0},{1})");

        add(Ops.DateTimeOps.TRUNC_YEAR,   "str_to_date(concat(date_format({0},'%Y'),'-1-1'),'%Y-%m-%d')");
        add(Ops.DateTimeOps.TRUNC_MONTH,  "str_to_date(concat(date_format({0},'%Y-%m'),'-1'),'%Y-%m-%d')");
        add(Ops.DateTimeOps.TRUNC_WEEK,   "str_to_date(concat(date_format({0},'%Y-%u'),'-2'),'%Y-%u-%w')");
        add(Ops.DateTimeOps.TRUNC_DAY,    "str_to_date(date_format({0},'%Y-%m-%d'),'%Y-%m-%d')");
        add(Ops.DateTimeOps.TRUNC_HOUR,   "str_to_date(date_format({0},'%Y-%m-%d %k'),'%Y-%m-%d %k')");
        add(Ops.DateTimeOps.TRUNC_MINUTE, "str_to_date(date_format({0},'%Y-%m-%d %k:%i'),'%Y-%m-%d %k:%i')");
        add(Ops.DateTimeOps.TRUNC_SECOND, "str_to_date(date_format({0},'%Y-%m-%d %k:%i:%s'),'%Y-%m-%d %k:%i:%s')");

        addTypeNameToCode("bool", Types.BIT, true);
        addTypeNameToCode("tinyint unsigned", Types.TINYINT);
        addTypeNameToCode("bigint unsigned", Types.BIGINT);
        addTypeNameToCode("long varbinary", Types.LONGVARBINARY, true);
        addTypeNameToCode("mediumblob", Types.LONGVARBINARY);
        addTypeNameToCode("longblob", Types.LONGVARBINARY);
        addTypeNameToCode("blob", Types.LONGVARBINARY);
        addTypeNameToCode("tinyblob", Types.LONGVARBINARY);
        addTypeNameToCode("long varchar", Types.LONGVARCHAR, true);
        addTypeNameToCode("mediumtext", Types.LONGVARCHAR);
        addTypeNameToCode("longtext", Types.LONGVARCHAR);
        addTypeNameToCode("text", Types.LONGVARCHAR);
        addTypeNameToCode("tinytext", Types.LONGVARCHAR);
        addTypeNameToCode("integer unsigned", Types.INTEGER);
        addTypeNameToCode("int", Types.INTEGER);
        addTypeNameToCode("int unsigned", Types.INTEGER);
        addTypeNameToCode("mediumint", Types.INTEGER);
        addTypeNameToCode("mediumint unsigned", Types.INTEGER);
        addTypeNameToCode("smallint unsigned", Types.SMALLINT);
        addTypeNameToCode("float", Types.REAL, true);
        addTypeNameToCode("double precision", Types.DOUBLE, true);
        addTypeNameToCode("real", Types.DOUBLE);
        addTypeNameToCode("enum", Types.VARCHAR);
        addTypeNameToCode("set", Types.VARCHAR);
        addTypeNameToCode("datetime", Types.TIMESTAMP, true);
    }

    @Override
    public String escapeLiteral(String str) {
        StringBuilder builder = new StringBuilder();
        for (char ch : super.escapeLiteral(str).toCharArray()) {
            if (ch == '\\') {
                builder.append("\\");
            }
            builder.append(ch);
        }
        return builder.toString();
    }

    @Override
    public String getCastTypeNameForCode(int code) {
        switch (code) {
            case Types.TINYINT:
            case Types.SMALLINT:
            case Types.INTEGER:
            case Types.BIGINT: return "signed";
            case Types.FLOAT:
            case Types.DOUBLE:
            case Types.REAL:
            case Types.DECIMAL: return "decimal";
            case Types.VARCHAR: return "char";
            default: return super.getCastTypeNameForCode(code);
        }
    }

}
