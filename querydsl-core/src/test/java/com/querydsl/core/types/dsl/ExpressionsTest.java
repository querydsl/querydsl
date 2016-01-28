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
package com.querydsl.core.types.dsl;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;
import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.querydsl.core.types.*;
import com.querydsl.core.util.BeanUtils;

public class ExpressionsTest {

    private static final StringPath str = new StringPath("str");

    private static final BooleanExpression a = new BooleanPath("a"), b = new BooleanPath("b");

    private enum testEnum {
        TEST;
    }

    @Before
    public void before() {
        System.setProperty("user.timezone", "UTC");
        TimeZone.setDefault(null);
    }

    @Test
    public void  Signature() throws NoSuchMethodException {
        List<String> types = ImmutableList.of("boolean", "comparable", "date", "dsl", "dateTime",
                "enum", "number", "simple", "string", "time");
        for (String type : types) {
            if (type.equals("boolean") || type.equals("string")) {
                assertReturnType(Expressions.class.getMethod(type + "Path", String.class));
                assertReturnType(Expressions.class.getMethod(type + "Path", Path.class, String.class));
                assertReturnType(Expressions.class.getMethod(type + "Path", PathMetadata.class));
                assertReturnType(Expressions.class.getMethod(type + "Operation", Operator.class, Expression[].class));
                assertReturnType(Expressions.class.getMethod(type + "Template", String.class, Object[].class));
                assertReturnType(Expressions.class.getMethod(type + "Template", String.class, ImmutableList.class));
                assertReturnType(Expressions.class.getMethod(type + "Template", Template.class, Object[].class));
                assertReturnType(Expressions.class.getMethod(type + "Template", Template.class, ImmutableList.class));
            } else {
                assertReturnType(Expressions.class.getMethod(type + "Path", Class.class, String.class));
                assertReturnType(Expressions.class.getMethod(type + "Path", Class.class, Path.class, String.class));
                assertReturnType(Expressions.class.getMethod(type + "Path", Class.class, PathMetadata.class));
                assertReturnType(Expressions.class.getMethod(type + "Operation", Class.class, Operator.class, Expression[].class));
                assertReturnType(Expressions.class.getMethod(type + "Template", Class.class, String.class, Object[].class));
                assertReturnType(Expressions.class.getMethod(type + "Template", Class.class, String.class, ImmutableList.class));
                assertReturnType(Expressions.class.getMethod(type + "Template", Class.class, Template.class, Object[].class));
                assertReturnType(Expressions.class.getMethod(type + "Template", Class.class, Template.class, ImmutableList.class));
            }
        }

        // arrays
        assertReturnType(Expressions.class.getMethod("arrayPath", Class.class, String.class));
        assertReturnType(Expressions.class.getMethod("arrayPath", Class.class, Path.class, String.class));
        assertReturnType(Expressions.class.getMethod("arrayPath", Class.class, PathMetadata.class));
    }

    private void assertReturnType(Method method) {
        assertEquals(BeanUtils.capitalize(method.getName()), method.getReturnType().getSimpleName());
    }

    @Test
    public void as() {
        assertEquals("null as str", Expressions.as(null, str).toString());
        assertEquals("s as str", Expressions.as(new StringPath("s"), str).toString());
    }

    @Test
    public void allOf() {
        assertEquals("a && b", Expressions.allOf(a, b).toString());
    }

    @Test
    public void allOf_with_nulls() {
        assertEquals("a && b", Expressions.allOf(a, b, null).toString());
        assertEquals("a", Expressions.allOf(a, null).toString());
        assertEquals("a", Expressions.allOf(null, a).toString());
    }

    @Test
    public void anyOf() {
        assertEquals("a || b", Expressions.anyOf(a, b).toString());
    }

    @Test
    public void anyOf_with_nulls() {
        assertEquals("a || b", Expressions.anyOf(a, b, null).toString());
        assertEquals("a", Expressions.anyOf(a, null).toString());
        assertEquals("a", Expressions.anyOf(null, a).toString());
    }

    @Test
    public void constant() {
        assertEquals("X", Expressions.constant("X").toString());
    }

    @Test
    public void constant_as() {
        assertEquals("str as str", Expressions.constantAs("str", str).toString());
    }

    @Test
    public void template() {
        assertEquals("a && b", Expressions.template(Object.class, "{0} && {1}", a, b).toString());
    }

    @Test
    public void comparableTemplate() {
        assertEquals("a && b",
                Expressions.comparableTemplate(Boolean.class, "{0} && {1}", a, b).toString());
    }

    @Test
    public void numberTemplate() {
        assertEquals("1", Expressions.numberTemplate(Integer.class, "1").toString());
    }

    @Test
    public void stringTemplate() {
        assertEquals("X", Expressions.stringTemplate("X").toString());
    }

    @Test
    public void booleanTemplate() {
        assertEquals("a && b", Expressions.booleanTemplate("{0} && {1}", a, b).toString());
    }

    @Test
    public void subQuery() {
        // TODO
    }

    @Test
    public void operation() {
        assertEquals("a && b", Expressions.operation(Boolean.class, Ops.AND, a, b).toString());
    }

    @Test
    public void predicate() {
        assertEquals("a && b", Expressions.predicate(Ops.AND, a, b).toString());
    }

    @Test
    public void pathClassOfTString() {
        assertEquals("variable", Expressions.path(String.class, "variable").toString());
    }

    @Test
    public void pathClassOfTPathOfQString() {
        assertEquals("variable.property", Expressions.path(String.class,
                Expressions.path(Object.class, "variable"), "property").toString());
    }

    @Test
    public void comparablePathClassOfTString() {
        assertEquals("variable", Expressions.comparablePath(String.class, "variable").toString());
    }

    @Test
    public void comparablePathClassOfTPathOfQString() {
        assertEquals("variable.property", Expressions.comparablePath(String.class,
                Expressions.path(Object.class, "variable"), "property").toString());
    }

    @Test
    public void datePathClassOfTString() {
        assertEquals("variable", Expressions.datePath(Date.class, "variable").toString());
    }

    @Test
    public void datePathClassOfTPathOfQString() {
        assertEquals("variable.property", Expressions.datePath(Date.class,
                Expressions.path(Object.class, "variable"), "property").toString());
    }

    @Test
    public void dateTimePathClassOfTString() {
        assertEquals("variable", Expressions.dateTimePath(Date.class, "variable").toString());
    }

    @Test
    public void dateTimePathClassOfTPathOfQString() {
        assertEquals("variable.property", Expressions.dateTimePath(Date.class,
                Expressions.path(Object.class, "variable"), "property").toString());
    }

    @Test
    public void timePathClassOfTString() {
        assertEquals("variable", Expressions.timePath(Date.class, "variable").toString());
    }

    @Test
    public void timePathClassOfTPathOfQString() {
        assertEquals("variable.property", Expressions.timePath(Date.class,
                Expressions.path(Object.class, "variable"), "property").toString());
    }

    @Test
    public void numberPathClassOfTString() {
        assertEquals("variable", Expressions.numberPath(Integer.class, "variable").toString());
    }

    @Test
    public void numberPathClassOfTPathOfQString() {
        assertEquals("variable.property", Expressions.numberPath(Integer.class,
                Expressions.path(Object.class, "variable"), "property").toString());
    }

    @Test
    public void stringPathString() {
        assertEquals("variable", Expressions.stringPath("variable").toString());
    }

    @Test
    public void stringPathPathOfQString() {
        assertEquals("variable.property",
                Expressions.stringPath(Expressions.path(Object.class, "variable"), "property").toString());
    }

    @Test
    public void stringOperation() {
        assertEquals("substring(str,2)",
                Expressions.stringOperation(Ops.SUBSTR_1ARG, str, ConstantImpl.create(2)).toString());
    }

    @Test
    public void booleanPathString() {
        assertEquals("variable", Expressions.booleanPath("variable").toString());
    }

    @Test
    public void booleanPathPathOfQString() {
        assertEquals("variable.property",
                Expressions.booleanPath(Expressions.path(Object.class, "variable"), "property").toString());
    }

    @Test
    public void booleanOperation() {
        assertEquals("a && b", Expressions.booleanOperation(Ops.AND, a, b).toString());
    }

    @Test
    public void comparableOperation() {
        assertEquals("a && b", Expressions.comparableOperation(Boolean.class, Ops.AND, a, b).toString());
    }

    @Test
    public void dateOperation() {
        assertEquals("current_date()",
                Expressions.dateOperation(Date.class, Ops.DateTimeOps.CURRENT_DATE).toString());
    }

    @Test
    public void dateTimeOperation() {
        assertEquals("current_timestamp()",
                Expressions.dateTimeOperation(Date.class, Ops.DateTimeOps.CURRENT_TIMESTAMP).toString());
    }

    @Test
    public void timeOperation() {
        assertEquals("current_time()",
                Expressions.timeOperation(Time.class, Ops.DateTimeOps.CURRENT_TIME).toString());
    }

    @Test
    public void cases() {
        // TODO
    }

    @Test
    public void asBoolean_returns_a_corresponding_BooleanExpression_for_a_given_Expression() {
        assertEquals("true = true", Expressions.asBoolean(Expressions.constant(true)).isTrue().toString());
    }

    @Test
    public void asBoolean_returns_a_corresponding_BooleanExpression_for_a_given_Constant() {
        assertEquals("true = true", Expressions.asBoolean(true).isTrue().toString());
    }

    @Test
    public void asComparable_returns_a_corresponding_ComparableExpression_for_a_given_Expression() {
        assertEquals("1 = 1",
                Expressions.asComparable(Expressions.constant(1L)).eq(Expressions.constant(1L)).toString());
    }

    @Test
    public void asComparable_returns_a_corresponding_ComparableExpression_for_a_given_Constant() {
        assertEquals("1 = 1", Expressions.asComparable(1L).eq(1L).toString());
    }

    @Test
    public void asDate_returns_a_corresponding_DateExpression_for_a_given_Expression() {
        assertEquals("year(Thu Jan 01 00:00:00 UTC 1970)",
                Expressions.asDate(Expressions.constant(new Date(1L))).year().toString());
    }

    @Test
    public void asDate_returns_a_corresponding_DateExpression_for_a_given_Constant() {
        assertEquals("year(Thu Jan 01 00:00:00 UTC 1970)", Expressions.asDate(new Date(1L)).year().toString());
    }

    @Test
    public void asDateTime_returns_a_corresponding_DateTimeExpression_for_a_given_Expression() {
        assertEquals("min(Thu Jan 01 00:00:00 UTC 1970)",
                Expressions.asDateTime(Expressions.constant(new Date(1L))).min().toString());
    }

    @Test
    public void asDateTime_returns_a_corresponding_DateTimeExpression_for_a_given_Constant() {
        assertEquals("min(Thu Jan 01 00:00:00 UTC 1970)", Expressions.asDateTime(new Date(1L)).min().toString());
    }

    @Test
    public void asTime_returns_a_corresponding_TimeExpression_for_a_given_Expression() {
        assertEquals("hour(Thu Jan 01 00:00:00 UTC 1970)",
                Expressions.asTime(Expressions.constant(new Date(1L))).hour().toString());
    }

    @Test
    public void asTime_returns_a_corresponding_TimeExpression_for_a_given_Constant() {
        assertEquals("hour(Thu Jan 01 00:00:00 UTC 1970)", Expressions.asTime(new Date(1L)).hour().toString());
    }

    @Test
    public void asEnum_returns_a_corresponding_EnumExpression_for_a_given_Expression() {
        assertEquals("ordinal(TEST)", Expressions.asEnum(Expressions.constant(testEnum.TEST)).ordinal().toString());
    }

    @Test
    public void asEnum_returns_a_corresponding_EnumExpression_for_a_given_Constant() {
        assertEquals("ordinal(TEST)", Expressions.asEnum(testEnum.TEST).ordinal().toString());
    }

    @Test
    public void asNumber_returns_a_corresponding_NumberExpression_for_a_given_Expression() {
        assertEquals("1 + 1", Expressions.asNumber(Expressions.constant(1L)).add(Expressions.constant(1L)).toString());
    }

    @Test
    public void asNumber_returns_a_corresponding_NumberExpression_for_a_given_Constant() {
        assertEquals("1 + 1", Expressions.asNumber(1L).add(Expressions.constant(1L)).toString());
    }

    @Test
    public void asString_returns_a_corresponding_StringExpression_for_a_given_Expression() {
        assertEquals("left + right",
                Expressions.asString(Expressions.constant("left")).append(Expressions.constant("right")).toString());
    }

    @Test
    public void asString_returns_a_corresponding_StringExpression_for_a_given_Constant() {
        assertEquals("left + right", Expressions.asString("left").append(Expressions.constant("right")).toString());
    }

}
