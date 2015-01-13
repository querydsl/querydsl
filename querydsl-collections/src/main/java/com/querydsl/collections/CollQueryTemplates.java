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
package com.querydsl.collections;

import com.querydsl.core.types.JavaTemplates;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.PathType;

/**
 * CollQueryTemplates extends {@link JavaTemplates} to add module specific operation
 * templates.
 *
 * @author tiwe
 */
public class CollQueryTemplates extends JavaTemplates {

    @SuppressWarnings("FieldNameHidesFieldInSuperclass") //intentional
    public static final CollQueryTemplates DEFAULT = new CollQueryTemplates();

    protected CollQueryTemplates() {
        String functions = CollQueryFunctions.class.getName();
        add(Ops.EQ, functions + ".equals({0}, {1})");
        add(Ops.NE, "!" + functions + ".equals({0}, {1})");
        add(Ops.INSTANCE_OF, "{1}.isInstance({0})");

        // Comparable
        add(Ops.GT, functions + ".compareTo({0}, {1}) > 0");
        add(Ops.LT, functions + ".compareTo({0}, {1}) < 0");
        add(Ops.GOE, functions + ".compareTo({0}, {1}) >= 0");
        add(Ops.LOE, functions + ".compareTo({0}, {1}) <= 0");
        add(Ops.BETWEEN, functions + ".between({0}, {1}, {2})");
        add(Ops.STRING_CAST, "String.valueOf({0})");

        // Number
        add(Ops.MathOps.COT,  functions + ".cot({0})");
        add(Ops.MathOps.COTH, functions + ".coth({0})");
        add(Ops.MathOps.DEG,  functions + ".degrees({0})");
        add(Ops.MathOps.LN,   "Math.log({0})");
        add(Ops.MathOps.LOG,  functions + ".log({0},{1})");
        add(Ops.MathOps.RAD,  functions + ".radians({0})");
        add(Ops.MathOps.SIGN, "{0} > 0 ? 1 : -1");

        add(Ops.ADD, "{0}.add({1})");
        add(Ops.SUB, "{0}.subtract({1})");
        add(Ops.MULT, "{0}.multiply({1})");
        add(Ops.DIV, "{0}.divide({1})");

        // Date and Time
        add(Ops.DateTimeOps.YEAR,         functions + ".getYear({0})");
        add(Ops.DateTimeOps.MONTH,        functions + ".getMonth({0})");
        add(Ops.DateTimeOps.WEEK,         functions + ".getWeek({0})");
        add(Ops.DateTimeOps.DAY_OF_WEEK,  functions + ".getDayOfWeek({0})");
        add(Ops.DateTimeOps.DAY_OF_MONTH, functions + ".getDayOfMonth({0})");
        add(Ops.DateTimeOps.DAY_OF_YEAR,  functions + ".getDayOfYear({0})");
        add(Ops.DateTimeOps.HOUR,         functions + ".getHour({0})");
        add(Ops.DateTimeOps.MINUTE,       functions + ".getMinute({0})");
        add(Ops.DateTimeOps.SECOND,       functions + ".getSecond({0})");
        add(Ops.DateTimeOps.MILLISECOND,  functions + ".getMilliSecond({0})");

        add(Ops.DateTimeOps.YEAR_MONTH,   functions + ".getYearMonth({0})");
        add(Ops.DateTimeOps.YEAR_WEEK,    functions + ".getYearWeek({0})");

        // String
        add(Ops.LIKE, functions + ".like({0},{1})");
        add(Ops.LIKE_ESCAPE, functions + ".like({0},{1},{2})");

        // Path types
        for (PathType type : new PathType[] {
                PathType.LISTVALUE,
                PathType.MAPVALUE,
                PathType.MAPVALUE_CONSTANT }) {
            add(type, "{0}.get({1})");
        }
        add(PathType.LISTVALUE_CONSTANT, "{0}.get({1})");
        add(PathType.ARRAYVALUE, "{0}[{1}]");
        add(PathType.ARRAYVALUE_CONSTANT, "{0}[{1}]");
        add(PathType.COLLECTION_ANY, "{0}_any");

        // coalesce
        add(Ops.COALESCE, functions + ".coalesce({0})");

        add(Ops.NULLIF, functions + ".nullif({0}, {1})");
    }

}
