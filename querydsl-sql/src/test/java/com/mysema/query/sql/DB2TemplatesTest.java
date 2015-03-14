package com.mysema.query.sql;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.mysema.query.types.Ops;

public class DB2TemplatesTest extends AbstractSQLTemplatesTest{

    @Override
    protected SQLTemplates createTemplates() {
        return new DB2Templates();
    }

    @Test
    public void Precedence() {
        // Expressions within parentheses are evaluated first. When the order of evaluation is not
        // specified by parentheses, prefix operators are applied before multiplication and division,
        // and multiplication, division, and concatenation are applied before addition and subtraction.
        // Operators at the same precedence level are applied from left to right.

        int p1 = getPrecedence(Ops.NEGATE);
        int p2 = getPrecedence(Ops.MULT, Ops.DIV, Ops.CONCAT);
        int p3 = getPrecedence(Ops.ADD, Ops.SUB);
        int p4 = getPrecedence(Ops.EQ, Ops.NE, Ops.LT, Ops.GT, Ops.LOE, Ops.GOE);
        int p5 = getPrecedence(Ops.IS_NULL, Ops.IS_NOT_NULL, Ops.LIKE, Ops.LIKE_ESCAPE, Ops.BETWEEN, Ops.IN, Ops.NOT_IN, Ops.EXISTS);
        int p6 = getPrecedence(Ops.NOT);
        int p7 = getPrecedence(Ops.AND);
        int p8 = getPrecedence(Ops.OR);

        assertTrue(p1 < p2);
        assertTrue(p2 < p3);
        assertTrue(p3 < p4);
        assertTrue(p4 < p5);
        assertTrue(p5 < p6);
        assertTrue(p6 < p7);
        assertTrue(p7 < p8);
    }

}