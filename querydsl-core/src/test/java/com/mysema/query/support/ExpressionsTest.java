package com.mysema.query.support;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;

import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Ops;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.path.BooleanPath;
import com.mysema.query.types.path.StringPath;

public class ExpressionsTest {

    private static final StringPath str = new StringPath("str");
    
    private static final BooleanExpression a = new BooleanPath("a"), b = new BooleanPath("b");
    
    @Test
    public void AllOf() {
        assertEquals("a && b", Expressions.allOf(a, b).toString());
    }

    @Test
    public void AnyOf() {
        assertEquals("a || b", Expressions.anyOf(a, b).toString());
    }

    @Test
    public void Constant() {
        assertEquals("X", Expressions.constant("X").toString());
    }

    @Test
    public void Template() {
        assertEquals("a && b", Expressions.template(Object.class, "{0} && {1}", a, b).toString());
    }

    @Test
    public void ComparableTemplate() {
        assertEquals("a && b", Expressions.comparableTemplate(Boolean.class, "{0} && {1}", a, b).toString());
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
        assertEquals("variable.property", Expressions.path(String.class, Expressions.path(Object.class, "variable"), "property").toString());
    }

    @Test
    public void ComparablePathClassOfTString() {
        assertEquals("variable", Expressions.comparablePath(String.class, "variable").toString());
    }

    @Test
    public void ComparablePathClassOfTPathOfQString() {
        assertEquals("variable.property", Expressions.comparablePath(String.class, Expressions.path(Object.class, "variable"), "property").toString());
    }

    @Test
    public void DatePathClassOfTString() {
        assertEquals("variable", Expressions.datePath(Date.class, "variable").toString());   
    }

    @Test
    public void DatePathClassOfTPathOfQString() {
        assertEquals("variable.property", Expressions.datePath(Date.class, Expressions.path(Object.class, "variable"), "property").toString());
    }

    @Test
    public void DateTimePathClassOfTString() {
        assertEquals("variable", Expressions.dateTimePath(Date.class, "variable").toString());
    }

    @Test
    public void DateTimePathClassOfTPathOfQString() {
        assertEquals("variable.property", Expressions.dateTimePath(Date.class, Expressions.path(Object.class, "variable"), "property").toString());
    }

    @Test
    public void TimePathClassOfTString() {
        assertEquals("variable", Expressions.timePath(Date.class, "variable").toString());
    }

    @Test
    public void TimePathClassOfTPathOfQString() {
        assertEquals("variable.property", Expressions.timePath(Date.class, Expressions.path(Object.class, "variable"), "property").toString());
    }

    @Test
    public void NumberPathClassOfTString() {
        assertEquals("variable", Expressions.numberPath(Integer.class, "variable").toString());
    }

    @Test
    public void NumberPathClassOfTPathOfQString() {
        assertEquals("variable.property", Expressions.numberPath(Integer.class, Expressions.path(Object.class, "variable"), "property").toString());
    }

    @Test
    public void StringPathString() {
        assertEquals("variable", Expressions.stringPath("variable").toString());
    }

    @Test
    public void StringPathPathOfQString() {
        assertEquals("variable.property", Expressions.stringPath(Expressions.path(Object.class, "variable"), "property").toString());
    }

    @Test
    public void StringOperation() {
        assertEquals("substring(str,2)", Expressions.stringOperation(Ops.SUBSTR_1ARG, str, ConstantImpl.create(2)).toString());
    }
    
    @Test
    public void BooleanPathString() {
        assertEquals("variable", Expressions.booleanPath("variable").toString());
    }

    @Test
    public void BooleanPathPathOfQString() {
        assertEquals("variable.property", Expressions.booleanPath(Expressions.path(Object.class, "variable"), "property").toString());
    }

    @Test
    public void Cases() {
        // TODO
    }

}
