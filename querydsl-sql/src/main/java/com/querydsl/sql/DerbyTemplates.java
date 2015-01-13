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

import com.querydsl.core.QueryMetadata;
import com.querydsl.core.QueryModifiers;
import com.querydsl.core.types.Ops;

/**
 * DerbyTemplates is an SQL dialect for Derby
 *
 * @author tiwe
 *
 */
public class DerbyTemplates extends SQLTemplates {

    @SuppressWarnings("FieldNameHidesFieldInSuperclass") //Intentional
    public static final DerbyTemplates DEFAULT = new DerbyTemplates();

    private String limitOffsetTemplate = "\noffset {1s} rows fetch next {0s} rows only";

    private String limitTemplate = "\nfetch first {0s} rows only";

    private String offsetTemplate = "\noffset {0s} rows";

    public static Builder builder() {
        return new Builder() {
            @Override
            protected SQLTemplates build(char escape, boolean quote) {
                return new DerbyTemplates(escape, quote);
            }
        };
    }

    public DerbyTemplates() {
        this('\\',false);
    }

    public DerbyTemplates(boolean quote) {
        this('\\',quote);
    }

    public DerbyTemplates(char escape, boolean quote) {
        super("\"", escape, quote);
        setDummyTable("sysibm.sysdummy1");
        setAutoIncrement(" generated always as identity");
        setFunctionJoinsWrapped(true);
        setDefaultValues("\nvalues (default)");

        add(Ops.CONCAT, "varchar({0} || {1})");

        add(SQLOps.NEXTVAL, "next value for {0s}");

        // case for eq
        add(Ops.CASE_EQ, "case {1} end");
        add(Ops.CASE_EQ_WHEN,  "when {0} = {1} then {2} {3}");
        add(Ops.CASE_EQ_ELSE,  "else {0}");

        add(Ops.MathOps.RANDOM, "random()");
        add(Ops.MathOps.ROUND, "floor({0})"); // FIXME
        add(Ops.MathOps.POWER, "exp({1} * log({0}))");
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

        add(Ops.DateTimeOps.ADD_YEARS, "{fn timestampadd(SQL_TSI_YEAR, {1}, {0})}");
        add(Ops.DateTimeOps.ADD_MONTHS, "{fn timestampadd(SQL_TSI_MONTH, {1}, {0})}");
        add(Ops.DateTimeOps.ADD_WEEKS, "{fn timestampadd(SQL_TSI_WEEK, {1}, {0})}");
        add(Ops.DateTimeOps.ADD_DAYS, "{fn timestampadd(SQL_TSI_DAY, {1}, {0})}");
        add(Ops.DateTimeOps.ADD_HOURS, "{fn timestampadd(SQL_TSI_HOUR, {1}, {0})}");
        add(Ops.DateTimeOps.ADD_MINUTES, "{fn timestampadd(SQL_TSI_MINUTE, {1}, {0})}");
        add(Ops.DateTimeOps.ADD_SECONDS, "{fn timestampadd(SQL_TSI_SECOND, {1}, {0})}");

        add(Ops.DateTimeOps.DIFF_YEARS, "{fn timestampdiff(SQL_TSI_YEAR, {0}, {1})}");
        add(Ops.DateTimeOps.DIFF_MONTHS, "{fn timestampdiff(SQL_TSI_MONTH, {0}, {1})}");
        add(Ops.DateTimeOps.DIFF_WEEKS, "{fn timestampdiff(SQL_TSI_WEEK, {0}, {1})}");
        add(Ops.DateTimeOps.DIFF_DAYS, "{fn timestampdiff(SQL_TSI_DAY, {0}, {1})}");
        add(Ops.DateTimeOps.DIFF_HOURS, "{fn timestampdiff(SQL_TSI_HOUR, {0}, {1})}");
        add(Ops.DateTimeOps.DIFF_MINUTES, "{fn timestampdiff(SQL_TSI_MINUTE, {0}, {1})}");
        add(Ops.DateTimeOps.DIFF_SECONDS, "{fn timestampdiff(SQL_TSI_SECOND, {0}, {1})}");

        // left via substr
        add(Ops.StringOps.LEFT, "substr({0},1,{1})");

        addTypeNameToCode("smallint", Types.TINYINT, true);
        addTypeNameToCode("long varchar for bit data", Types.LONGVARBINARY);
        addTypeNameToCode("varchar () for bit data", Types.VARBINARY);
        addTypeNameToCode("char () for bit data", Types.BINARY);
        addTypeNameToCode("long varchar", Types.LONGVARCHAR, true);
        addTypeNameToCode("object", Types.JAVA_OBJECT, true);
        addTypeNameToCode("xml", Types.SQLXML,true);
    }

    @Override
    public String serialize(String literal, int jdbcType) {
        if (jdbcType == Types.BOOLEAN) {
            return "1".equals(literal) ? "true" : "false";
        } else if (jdbcType == Types.TIMESTAMP) {
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
    protected void serializeModifiers(QueryMetadata metadata, SQLSerializer context) {
        QueryModifiers mod = metadata.getModifiers();
        if (mod.getLimit() == null) {
            context.handle(offsetTemplate, mod.getOffset());
        } else if (mod.getOffset() == null) {
            context.handle(limitTemplate, mod.getLimit());
        } else {
            context.handle(limitOffsetTemplate, mod.getLimit(), mod.getOffset());
        }
    }

}
