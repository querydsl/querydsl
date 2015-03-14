package com.mysema.query.sql;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.mysema.query.types.Ops;

public class HSQLDBTemplatesTest extends AbstractSQLTemplatesTest{

    @Override
    protected SQLTemplates createTemplates() {
        return new HSQLDBTemplates();
    }

    @Test
    public void Precedence() {
        // Evaluation from left to right. Parentheses group operations.
        // Multiplication and division take precedence over addition and subtraction.
        // AND takes precedence over OR.
        // NOT applies to the immediate term.
        // LIKE applies to the result of any string concatenation to the right.
        // Comparison ops are not combined without logical ops so there is no precedence issue.

        //int p1 = getPrecedence(Ops.NEGATE);
        int p2 = getPrecedence(Ops.MULT, Ops.DIV, Ops.CONCAT);
        int p3 = getPrecedence(Ops.ADD, Ops.SUB);
        int p4 = getPrecedence(Ops.NOT);
        int p5 = getPrecedence(Ops.EQ, Ops.NE, Ops.LT, Ops.GT, Ops.LOE, Ops.GOE);
        int p6 = getPrecedence(Ops.IS_NULL, Ops.IS_NOT_NULL, Ops.LIKE, Ops.LIKE_ESCAPE, Ops.BETWEEN, Ops.IN, Ops.NOT_IN, Ops.EXISTS);
        int p7 = getPrecedence(Ops.AND);
        int p8 = getPrecedence(Ops.OR);

        //assertTrue(p1 < p2);
        assertTrue(p2 < p3);
        assertTrue(p3 < p4);
        assertTrue(p4 < p5);
        assertTrue(p5 < p6);
        assertTrue(p6 < p7);
        assertTrue(p7 < p8);
    }

}