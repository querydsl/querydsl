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
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.querydsl.core.types.Ops;

/**
 * MySQLTemplates is an SQL dialect for MySQL
 *
 * <p>tested with MySQL CE 5.1 and 5.5</p>
 *
 * @author tiwe
 *
 */
public class MySQLTemplates extends SQLTemplates {

    protected static final Set<String> MYSQL_RESERVED_WORDS
            = ImmutableSet.of(
                    "ACCESSIBLE", "ADD", "ALL", "ALTER", "ANALYZE", "AND", "AS",
                    "ASC", "ASENSITIVE", "BEFORE", "BETWEEN", "BIGINT", "BINARY",
                    "BLOB", "BOTH", "BY", "CALL", "CASCADE", "CASE", "CHANGE",
                    "CHAR", "CHARACTER", "CHECK", "COLLATE", "COLUMN",
                    "CONDITION", "CONSTRAINT", "CONTINUE", "CONVERT", "CREATE",
                    "CROSS", "CURRENT_DATE", "CURRENT_TIME", "CURRENT_TIMESTAMP",
                    "CURRENT_USER", "CURSOR", "DATABASE", "DATABASES", "DAY_HOUR",
                    "DAY_MICROSECOND", "DAY_MINUTE", "DAY_SECOND", "DEC",
                    "DECIMAL", "DECLARE", "DEFAULT", "DELAYED", "DELETE",
                    "DESC", "DESCRIBE", "DETERMINISTIC", "DISTINCT",
                    "DISTINCTROW", "DIV", "DOUBLE", "DROP", "DUAL", "EACH",
                    "ELSE", "ELSEIF", "ENCLOSED", "ESCAPED", "EXISTS", "EXIT",
                    "EXPLAIN", "FALSE", "FETCH", "FLOAT", "FLOAT4", "FLOAT8",
                    "FOR", "FORCE", "FOREIGN", "FROM", "FULLTEXT", "GRANT",
                    "GROUP", "HAVING", "HIGH_PRIORITY", "HOUR_MICROSECOND",
                    "HOUR_MINUTE", "HOUR_SECOND", "IF", "IGNORE", "IN", "INDEX",
                    "INFILE", "INNER", "INOUT", "INSENSITIVE", "INSERT", "INT",
                    "INT1", "INT2", "INT3", "INT4", "INT8", "INTEGER", "INTERVAL",
                    "INTO", "IS", "ITERATE", "JOIN", "KEY", "KEYS", "KILL",
                    "LEADING", "LEAVE", "LEFT", "LIKE", "LIMIT", "LINEAR",
                    "LINES", "LOAD", "LOCALTIME", "LOCALTIMESTAMP", "LOCK",
                    "LONG", "LONGBLOB", "LONGTEXT", "LOOP", "LOW_PRIORITY",
                    "MASTER_SSL_VERIFY_SERVER_CERT", "MATCH", "MAXVALUE",
                    "MEDIUMBLOB", "MEDIUMINT", "MEDIUMTEXT", "MIDDLEINT",
                    "MINUTE_MICROSECOND", "MINUTE_SECOND", "MOD", "MODIFIES",
                    "NATURAL", "NOT", "NO_WRITE_TO_BINLOG", "NULL", "NUMERIC",
                    "ON", "OPTIMIZE", "OPTION", "OPTIONALLY", "OR", "ORDER",
                    "OUT", "OUTER", "OUTFILE", "PRECISION", "PRIMARY", "PROCEDURE",
                    "PURGE", "RANGE", "READ", "READS", "READ_WRITE", "REAL",
                    "REFERENCES", "REGEXP", "RELEASE", "RENAME", "REPEAT", "REPLACE",
                    "REQUIRE", "RESIGNAL", "RESTRICT", "RETURN", "REVOKE", "RIGHT",
                    "RLIKE", "SCHEMA", "SCHEMAS", "SECOND_MICROSECOND", "SELECT",
                    "SENSITIVE", "SEPARATOR", "SET", "SHOW", "SIGNAL", "SMALLINT",
                    "SPATIAL", "SPECIFIC", "SQL", "SQLEXCEPTION", "SQLSTATE",
                    "SQLWARNING", "SQL_BIG_RESULT", "SQL_CALC_FOUND_ROWS",
                    "SQL_SMALL_RESULT", "SSL", "STARTING", "STRAIGHT_JOIN", "TABLE",
                    "TERMINATED", "THEN", "TINYBLOB", "TINYINT", "TINYTEXT", "TO",
                    "TRAILING", "TRIGGER", "TRUE", "UNDO", "UNION", "UNIQUE",
                    "UNLOCK", "UNSIGNED", "UPDATE", "USAGE", "USE", "USING",
                    "UTC_DATE", "UTC_TIME", "UTC_TIMESTAMP", "VALUES", "VARBINARY",
                    "VARCHAR", "VARCHARACTER", "VARYING", "WHEN", "WHERE", "WHILE",
                    "WITH", "WRITE", "XOR", "YEAR_MONTH", "ZEROFILL");

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
        super(MYSQL_RESERVED_WORDS, "`", escape, quote);
        setArraysSupported(false);
        setParameterMetadataAvailable(false);
        setLimitRequired(true);
        setSupportsUnquotedReservedWordsAsIdentifier(true);
        setNullsFirst(null);
        setNullsLast(null);

        add(Ops.CONCAT, "concat({0}, {1})",0);

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
        add(Ops.MathOps.COSH, "(exp({0}) + exp({0} * -1)) / 2");
        add(Ops.MathOps.COTH, "(exp({0} * 2) + 1) / (exp({0} * 2) - 1)");
        add(Ops.MathOps.SINH, "(exp({0}) - exp({0} * -1)) / 2");
        add(Ops.MathOps.TANH, "(exp({0} * 2) - 1) / (exp({0} * 2) + 1)");

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
