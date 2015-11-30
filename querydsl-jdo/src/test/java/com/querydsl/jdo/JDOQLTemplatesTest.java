package com.querydsl.jdo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Operator;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.TemplatesTestUtils;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;

public class JDOQLTemplatesTest {

    @Test
    public void precedence() {
//        Cast
//        Unary ("~") ("!")
        int p1 =  getPrecedence(Ops.NOT);
//        Unary ("+") ("-")
        int p2 = getPrecedence(Ops.NEGATE);
//        Multiplicative ("*") ("/") ("%")
        int p3 = getPrecedence(Ops.MULT, Ops.DIV, Ops.MOD);
//        Additive ("+") ("-")
        int p4 = getPrecedence(Ops.ADD, Ops.SUB);
//        Relational (">=") (">") ("<=") ("<") ("instanceof")
        int p5 = getPrecedence(Ops.GOE, Ops.GT, Ops.LOE, Ops.LT, Ops.INSTANCE_OF);
//        Equality ("==") ("!=")
        int p6 = getPrecedence(Ops.EQ, Ops.EQ_IGNORE_CASE, Ops.NE);
//        Boolean logical AND ("&")
//        Boolean logical OR ("|")
//        Conditional AND ("&&")
        int p7 = getPrecedence(Ops.AND);
//        Conditional OR ("||")
        int p8 = getPrecedence(Ops.OR);

        assertTrue(p1 < p2);
        assertTrue(p2 < p3);
        assertTrue(p3 < p4);
        assertTrue(p4 < p5);
        assertTrue(p5 < p6);
        assertTrue(p6 < p7);
        assertTrue(p7 < p8);
    }

    protected int getPrecedence(Operator... ops) {
        int precedence = JDOQLTemplates.DEFAULT.getPrecedence(ops[0]);
        for (int i = 1; i < ops.length; i++) {
            assertEquals(ops[i].name(), precedence, JDOQLTemplates.DEFAULT.getPrecedence(ops[i]));
        }
        return precedence;
    }

    @Test
    public void generic_precedence() {
        TemplatesTestUtils.testPrecedence(JDOQLTemplates.DEFAULT);
    }

    @Test
    public void concat() {
        StringPath a = Expressions.stringPath("a");
        StringPath b = Expressions.stringPath("b");
        StringPath c = Expressions.stringPath("c");
        Expression<?> expr = a.append(b).toLowerCase();
        String str = new JDOQLSerializer(JDOQLTemplates.DEFAULT, c).handle(expr).toString();
        assertEquals("(a + b).toLowerCase()", str);
    }
}
