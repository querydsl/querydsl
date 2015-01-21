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
package com.querydsl.core.support;

import static org.junit.Assert.assertEquals;

import java.sql.Time;
import java.util.Date;

import org.junit.Test;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.expr.BooleanExpression;
import com.querydsl.core.types.path.BooleanPath;
import com.querydsl.core.types.path.StringPath;

public class ExpressionsTest {

    private static final StringPath str = new StringPath("str");
    
    private static final BooleanExpression a = new BooleanPath("a"), b = new BooleanPath("b");
    
    @Test
    public void As() {
        assertEquals("null as str", Expressions.as(null, str).toString());
        assertEquals("s as str", Expressions.as(new StringPath("s"), str).toString());
    }
    
    @Test
    public void AllOf() {
        assertEquals("a && b", Expressions.allOf(a, b).toString());
    }
    
    @Test
    public void AllOf_With_Nulls() {
        assertEquals("a && b", Expressions.allOf(a, b, null).toString());        
        assertEquals("a", Expressions.allOf(a, null).toString());
        assertEquals("a", Expressions.allOf(null, a).toString());
    }

    @Test
    public void AnyOf() {
        assertEquals("a || b", Expressions.anyOf(a, b).toString());
    }
    
    @Test
    public void AnyOf_With_Nulls() {
        assertEquals("a || b", Expressions.anyOf(a, b, null).toString());       
        assertEquals("a", Expressions.anyOf(a, null).toString());
        assertEquals("a", Expressions.anyOf(null, a).toString());
    }

    @Test
    public void Constant() {
        assertEquals("X", Expressions.constant("X").toString());
    }

    @Test
    public void Constant_As() {
        assertEquals("str as str", Expressions.constantAs("str", str).toString());
    }
    
    @Test
    public void Template() {
        assertEquals("a && b", Expressions.template(Object.class, "{0} && {1}", a, b).toString());
    }

    @Test
    public void ComparableTemplate() {
        assertEquals("a && b", 
                Expressions.comparableTemplate(Boolean.class, "{0} && {1}", a, b).toString());
    }

    @Test
    public void NumberTemplate() {
        assertEquals("1", Expressions.numberTemplate(Integer.class, "1").toString());
    }

    @Test
    public void StringTemplate() {
        assertEquals("X", Expressions.stringTemplate("X").toString());
    }

    @Test
    public void BooleanTemplate() {
        assertEquals("a && b", Expressions.booleanTemplate("{0} && {1}", a, b).toString());
    }

    @Test
    public void SubQuery() {
        // TODO
    }

    @Test
    public void Operation() {
        assertEquals("a && b", Expressions.operation(Boolean.class, Ops.AND, a, b).toString());
    }

    @Test
    public void Predicate() {
        assertEquals("a && b", Expressions.predicate(Ops.AND, a, b).toString());
    }
    
    @Test
    public void PathClassOfTString() {
        assertEquals("variable", Expressions.path(String.class, "variable").toString());
    }

    @Test
    public void PathClassOfTPathOfQString() {
        assertEquals("variable.property", Expressions.path(String.class, 
                Expressions.path(Object.class, "variable"), "property").toString());
    }

    @Test
    public void ComparablePathClassOfTString() {
        assertEquals("variable", Expressions.comparablePath(String.class, "variable").toString());
    }

    @Test
    public void ComparablePathClassOfTPathOfQString() {
        assertEquals("variable.property", Expressions.comparablePath(String.class, 
                Expressions.path(Object.class, "variable"), "property").toString());
    }

    @Test
    public void DatePathClassOfTString() {
        assertEquals("variable", Expressions.datePath(Date.class, "variable").toString());   
    }

    @Test
    public void DatePathClassOfTPathOfQString() {
        assertEquals("variable.property", Expressions.datePath(Date.class, 
                Expressions.path(Object.class, "variable"), "property").toString());
    }

    @Test
    public void DateTimePathClassOfTString() {
        assertEquals("variable", Expressions.dateTimePath(Date.class, "variable").toString());
    }

    @Test
    public void DateTimePathClassOfTPathOfQString() {
        assertEquals("variable.property", Expressions.dateTimePath(Date.class,
                Expressions.path(Object.class, "variable"), "property").toString());
    }

    @Test
    public void TimePathClassOfTString() {
        assertEquals("variable", Expressions.timePath(Date.class, "variable").toString());
    }

    @Test
    public void TimePathClassOfTPathOfQString() {
        assertEquals("variable.property", Expressions.timePath(Date.class, 
                Expressions.path(Object.class, "variable"), "property").toString());
    }

    @Test
    public void NumberPathClassOfTString() {
        assertEquals("variable", Expressions.numberPath(Integer.class, "variable").toString());
    }

    @Test
    public void NumberPathClassOfTPathOfQString() {
        assertEquals("variable.property", Expressions.numberPath(Integer.class, 
                Expressions.path(Object.class, "variable"), "property").toString());
    }

    @Test
    public void StringPathString() {
        assertEquals("variable", Expressions.stringPath("variable").toString());
    }

    @Test
    public void StringPathPathOfQString() {
        assertEquals("variable.property", 
                Expressions.stringPath(Expressions.path(Object.class, "variable"), "property").toString());
    }

    @Test
    public void StringOperation() {
        assertEquals("substring(str,2)", 
                Expressions.stringOperation(Ops.SUBSTR_1ARG, str, ConstantImpl.create(2)).toString());
    }
    
    @Test
    public void BooleanPathString() {
        assertEquals("variable", Expressions.booleanPath("variable").toString());
    }

    @Test
    public void BooleanPathPathOfQString() {
        assertEquals("variable.property", 
                Expressions.booleanPath(Expressions.path(Object.class, "variable"), "property").toString());
    }
    
    @Test
    public void BooleanOperation() {
        assertEquals("a && b", Expressions.booleanOperation(Ops.AND, a, b).toString());
    }
    
    @Test
    public void ComparableOperation() {
        assertEquals("a && b", Expressions.comparableOperation(Boolean.class, Ops.AND, a, b).toString());
    }
    
    @Test
    public void DateOperation() {
        assertEquals("current_date()", 
                Expressions.dateOperation(Date.class, Ops.DateTimeOps.CURRENT_DATE).toString());
    }
    
    @Test
    public void DateTimeOperation() {
        assertEquals("current_timestamp()", 
                Expressions.dateTimeOperation(Date.class, Ops.DateTimeOps.CURRENT_TIMESTAMP).toString());
    }
    
    @Test
    public void TimeOperation() {
        assertEquals("current_time()", 
                Expressions.timeOperation(Time.class, Ops.DateTimeOps.CURRENT_TIME).toString());
    }
    
    @Test
    public void Cases() {
        // TODO
    }

}
