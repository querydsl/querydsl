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
package com.querydsl.core;

import static org.junit.Assert.assertTrue;
import static com.querydsl.core.alias.Alias.$;

import java.lang.reflect.Field;
import java.util.*;

import org.junit.Test;

import com.querydsl.core.alias.Alias;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Operation;
import com.querydsl.core.types.Operator;
import com.querydsl.core.types.Ops;

/**
 * The Class CoverageTest.
 */
public class CoverageTest {

    private MatchingFiltersFactory matchers = new MatchingFiltersFactory(Module.COLLECTIONS, Target.MEM);

    private ProjectionsFactory projections = new ProjectionsFactory(Module.COLLECTIONS, Target.MEM);

    private FilterFactory filters = new FilterFactory(projections, Module.COLLECTIONS, Target.MEM);

    @SuppressWarnings("unchecked")
    @Test
    public void test() throws IllegalArgumentException, IllegalAccessException {
        // make sure all Operators are covered in expression factory methods
        Set<Operator> usedOperators = new HashSet<Operator>();
        List<Expression<?>> exprs = new ArrayList<Expression<?>>();

        Entity entity = Alias.alias(Entity.class, "entity");
        // numeric
        exprs.addAll(projections.numeric($(entity.getNum()), $(entity.getNum()), 1, false));
        exprs.addAll(matchers.numeric($(entity.getNum()), $(entity.getNum()), 1));
        exprs.addAll(filters.numeric($(entity.getNum()), $(entity.getNum()), 1));
        exprs.addAll(projections.numericCasts($(entity.getNum()), $(entity.getNum()), 1));
        // string
        exprs.addAll(projections.string($(entity.getStr()), $(entity.getStr()), "abc"));
        exprs.addAll(matchers.string($(entity.getStr()), $(entity.getStr()), "abc"));
        exprs.addAll(filters.string($(entity.getStr()), $(entity.getStr()), "abc"));

        // date
        exprs.addAll(projections.date($(entity.getDate()), $(entity.getDate()), new java.sql.Date(0)));
        exprs.addAll(matchers.date($(entity.getDate()), $(entity.getDate()), new java.sql.Date(0)));
        exprs.addAll(filters.date($(entity.getDate()), $(entity.getDate()), new java.sql.Date(0)));
        // dateTime
        exprs.addAll(projections.dateTime($(entity.getDateTime()), $(entity.getDateTime()), new java.util.Date(0)));
        exprs.addAll(matchers.dateTime($(entity.getDateTime()), $(entity.getDateTime()), new java.util.Date(0)));
        exprs.addAll(filters.dateTime($(entity.getDateTime()), $(entity.getDateTime()), new java.util.Date(0)));
        // time
        exprs.addAll(projections.time($(entity.getTime()), $(entity.getTime()), new java.sql.Time(0)));
        exprs.addAll(matchers.time($(entity.getTime()), $(entity.getTime()), new java.sql.Time(0)));
        exprs.addAll(filters.time($(entity.getTime()), $(entity.getTime()), new java.sql.Time(0)));

        // boolean
        exprs.addAll(filters.booleanFilters($(entity.isBool()), $(entity.isBool())));
        // collection
        exprs.addAll(projections.list($(entity.getList()), $(entity.getList()), ""));
        exprs.addAll(filters.list($(entity.getList()), $(entity.getList()), ""));
        // array
        exprs.addAll(projections.array($(entity.getArray()), $(entity.getArray()), ""));
        exprs.addAll(filters.array($(entity.getArray()), $(entity.getArray()), ""));
        // map
        exprs.addAll(projections.map($(entity.getMap()), $(entity.getMap()), "", ""));
        exprs.addAll(filters.map($(entity.getMap()), $(entity.getMap()), "", ""));

        for (Expression<?> e : exprs) {
            if (e instanceof Operation) {
                Operation<?> op = (Operation<?>) e;
                if (op.getArg(0) instanceof Operation) {
                    usedOperators.add(((Operation<?>) op.getArg(0)).getOperator());
                } else if (op.getArgs().size() > 1 && op.getArg(1) instanceof Operation) {
                    usedOperators.add(((Operation<?>) op.getArg(1)).getOperator());
                }
                usedOperators.add(op.getOperator());
            }

        }

        // missing mappings
        usedOperators.addAll(Arrays.<Operator>asList(
                Ops.INSTANCE_OF,
                Ops.ALIAS,
                Ops.ARRAY_SIZE,
                Ops.MOD,
                Ops.STRING_CAST,
//                Ops.DELEGATE,
                Ops.WRAPPED,
                Ops.ORDER,

                Ops.XOR,
                Ops.XNOR,

                Ops.CASE_WHEN,
                Ops.CASE_ELSE,

                Ops.CASE_EQ_WHEN,
                Ops.CASE_EQ_ELSE,

                Ops.LIST,
                Ops.SET,
                Ops.SINGLETON,
                Ops.COALESCE,
                Ops.ORDINAL, // TODO: add support
                Ops.MATCHES_IC,

                // aggregation
                Ops.AggOps.AVG_AGG,
                Ops.AggOps.MAX_AGG,
                Ops.AggOps.MIN_AGG,
                Ops.AggOps.SUM_AGG,
                Ops.AggOps.COUNT_AGG,
                Ops.AggOps.COUNT_ALL_AGG,
                Ops.EXISTS
        ));

        List<Operator> notContained = new ArrayList<Operator>();
        for (Field field : Ops.class.getFields()) {
            if (Operator.class.isAssignableFrom(field.getType())) {
                Operator val = (Operator) field.get(null);
                if (!usedOperators.contains(val)) {
                    System.err.println(field.getName() + " was not contained");
                    notContained.add(val);
                }
            }
        }

        assertTrue(notContained.size() + " errors in processing, see log for details", notContained.isEmpty());
    }

}
