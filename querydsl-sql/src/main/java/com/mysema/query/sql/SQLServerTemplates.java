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
package com.mysema.query.sql;

import com.mysema.query.QueryFlag;
import com.mysema.query.QueryFlag.Position;
import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryModifiers;
import com.mysema.query.support.Expressions;
import com.mysema.query.types.Ops;

/**
 * SQLServerTemplates is an SQL dialect for Microsoft SQL Server
 *
 * @author tiwe
 *
 */
public class SQLServerTemplates extends SQLTemplates {

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
        addClass2TypeMappings("decimal", Double.class);
        setDummyTable("");
        setNullsFirst(null);
        setNullsLast(null);

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
        add(Ops.SUBSTR_2ARGS, "substring({0},{1}+1,{2})");
        add(Ops.TRIM, "ltrim(rtrim({0}))");

        add(Ops.StringOps.LOCATE, "charindex({0},{1})");
        add(Ops.StringOps.LOCATE2, "charindex({0},{1},{2})");

        add(SQLOps.NEXTVAL, "{0s}.nextval");

        add(Ops.MathOps.COSH, "(exp({0}) + exp({0} * -1)) / 2");
        add(Ops.MathOps.COTH, "(exp({0} * 2) + 1) / (exp({0} * 2) - 1)");
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
