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

import com.querydsl.core.QueryMetadata;
import com.querydsl.core.QueryModifiers;
import com.querydsl.core.types.Ops;
import com.querydsl.sql.types.NumericBooleanType;

/**
 * {@code CUBRIDTemplates} is a SQL dialect for CUBRID
 *
 * @author tiwe
 *
 */
public class CUBRIDTemplates extends SQLTemplates {

    @SuppressWarnings("FieldNameHidesFieldInSuperclass") //Intentional
    public static final CUBRIDTemplates DEFAULT = new CUBRIDTemplates();

    public static Builder builder() {
        return new Builder() {
            @Override
            protected SQLTemplates build(char escape, boolean quote) {
                return new CUBRIDTemplates(escape, quote);
            }
        };
    }

    private String limitTemplate = "\nlimit {0}";

    private String offsetLimitTemplate = "\nlimit {0}, {1}";

    public CUBRIDTemplates() {
        this('\\', false);
    }

    public CUBRIDTemplates(boolean quote) {
        this('\\',quote);
    }

    public CUBRIDTemplates(char escape, boolean quote) {
        super(Keywords.CUBRID, "\"", escape, quote);
        setDummyTable(null);
        addCustomType(NumericBooleanType.DEFAULT);
        setParameterMetadataAvailable(false);
        setNullsFirst(null);
        setNullsLast(null);
        setDefaultValues("\ndefault values");
        setArraysSupported(false);

        add(Ops.DateTimeOps.DATE, "trunc({0})");

        add(Ops.DateTimeOps.DAY_OF_YEAR, "dayofyear({0})");
        add(Ops.DateTimeOps.DAY_OF_WEEK, "dayofweek({0})");
        add(Ops.DateTimeOps.YEAR_WEEK, "(year({0}) * 100 + week({0}))");

        add(Ops.DateTimeOps.ADD_YEARS, "date_add({0}, interval {1s} year)");
        add(Ops.DateTimeOps.ADD_MONTHS, "date_add({0}, interval {1s} month)");
        add(Ops.DateTimeOps.ADD_WEEKS, "date_add({0}, interval {1s} week)");
        add(Ops.DateTimeOps.ADD_DAYS, "date_add({0}, interval {1s} day)");
        add(Ops.DateTimeOps.ADD_HOURS, "date_add({0}, interval {1s} hour)");
        add(Ops.DateTimeOps.ADD_MINUTES, "date_add({0}, interval {1s} minute)");
        add(Ops.DateTimeOps.ADD_SECONDS, "date_add({0}, interval {1s} second)");

        String diffSeconds = "(unix_timestamp({1}) - unix_timestamp({0}))";
        add(Ops.DateTimeOps.DIFF_YEARS,   "(year({1}) - year({0}))");
        add(Ops.DateTimeOps.DIFF_MONTHS,  "months_between({1}, {0})");
        add(Ops.DateTimeOps.DIFF_WEEKS,   "ceil(({1}-{0}) / 7)");
        add(Ops.DateTimeOps.DIFF_DAYS,    "({1}-{0})");
        add(Ops.DateTimeOps.DIFF_HOURS,   "ceil(" + diffSeconds + " / 3600)");
        add(Ops.DateTimeOps.DIFF_MINUTES, "ceil(" + diffSeconds + " / 60)");
        add(Ops.DateTimeOps.DIFF_SECONDS, diffSeconds);

        add(Ops.DateTimeOps.TRUNC_YEAR,   "trunc({0},'yyyy')");
        add(Ops.DateTimeOps.TRUNC_MONTH,  "trunc({0},'mm')");
        add(Ops.DateTimeOps.TRUNC_WEEK,   "trunc({0},'day')");
        add(Ops.DateTimeOps.TRUNC_DAY,    "trunc({0},'dd')");
        // trunc works only with date arguments
        // timestamp(datepart, timepart) reconstructs a datetime
        add(Ops.DateTimeOps.TRUNC_HOUR,   "timestamp(date({0}),concat(hour({0}),':00:00'))");
        add(Ops.DateTimeOps.TRUNC_MINUTE, "timestamp(date({0}),concat(hour({0}),':',minute({0}),':00'))");
        add(Ops.DateTimeOps.TRUNC_SECOND, "timestamp(date({0}),concat(hour({0}),':',minute({0}),':',second({0})))");

        add(Ops.MathOps.LN, "ln({0})");
        add(Ops.MathOps.LOG, "(ln({0}) / ln({1}))");
        add(Ops.MathOps.COSH, "(exp({0}) + exp({0*'-1'})) / 2");
        add(Ops.MathOps.COTH, "(exp({0*'2'}) + 1) / (exp({0*'2'}) - 1)");
        add(Ops.MathOps.SINH, "(exp({0}) - exp({0*'-1'})) / 2");
        add(Ops.MathOps.TANH, "(exp({0*'2'}) - 1) / (exp({0*'2'}) + 1)");

        add(SQLOps.GROUP_CONCAT2, "group_concat({0} separator '{1s}')");

        addTypeNameToCode("numeric(1,0)", Types.BOOLEAN, true);
        addTypeNameToCode("numeric(3,0)", Types.TINYINT, true);
        addTypeNameToCode("numeric(38,0)", Types.BIGINT, true);
        addTypeNameToCode("bit varying", Types.LONGVARBINARY);
        addTypeNameToCode("bit varying", Types.VARBINARY);
        addTypeNameToCode("bit", Types.BINARY, true);
        addTypeNameToCode("varchar", Types.LONGVARCHAR, true);
        addTypeNameToCode("double", Types.FLOAT, true);
        addTypeNameToCode("float", Types.REAL, true);
    }


    @Override
    public String serialize(String literal, int jdbcType) {
        switch (jdbcType) {
            case Types.TIMESTAMP:
                return "timestamp'" + literal + "'";
            case Types.DATE:
                return "date'" + literal + "'";
            case Types.TIME:
                return "time'" + literal + "'";
            default:
                return super.serialize(literal, jdbcType);
        }
    }

    @Override
    protected void serializeModifiers(QueryMetadata metadata, SQLSerializer context) {
        QueryModifiers mod = metadata.getModifiers();
        if (mod.getLimit() != null) {
            if (mod.getOffset() != null) {
                context.handle(offsetLimitTemplate, mod.getOffset(), mod.getLimit());
            } else {
                context.handle(limitTemplate, mod.getLimit());
            }
        } else if (mod.getOffset() != null) {
            context.handle(offsetLimitTemplate, mod.getOffset(), Integer.MAX_VALUE);
        }
    }

}
