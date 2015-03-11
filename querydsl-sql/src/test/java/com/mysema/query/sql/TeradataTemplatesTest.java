package com.mysema.query.sql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.mysema.query.types.Ops;

public class TeradataTemplatesTest extends AbstractSQLTemplatesTest {

    @Override
    protected SQLTemplates createTemplates() {
        return new TeradataTemplates();
    }

    @Test
    public void Limit() {
        query.from(survey1).limit(5);
        assertEquals("from SURVEY survey1 " +
                "qualify row_number() over (order by 1) <= ?", query.toString());
    }

    @Test
    public void Offset() {
        query.from(survey1).offset(5);
        assertEquals("from SURVEY survey1 " +
                "qualify row_number() over (order by 1) > ?", query.toString());
    }

    @Test
    public void Limit_Offset() {
        query.from(survey1).limit(5).offset(10);
        assertEquals("from SURVEY survey1 " +
                "qualify row_number() over (order by 1) between ? and ?", query.toString());
    }

    @Test
    public void OrderBy_Limit() {
        query.from(survey1).orderBy(survey1.name.asc()).limit(5);
        assertEquals("from SURVEY survey1 " +
                "order by survey1.NAME asc " +
                "qualify row_number() over (order by survey1.NAME asc) <= ?", query.toString());
    }

    @Test
    public void Precedence() {
        // +, - (unary)
        int p1 = getPrecedence(Ops.NEGATE);
        // ** (exponentation)
        // * / MOD
        int p2 = getPrecedence(Ops.MULT, Ops.DIV, Ops.MOD);
        // +, - (binary)
        int p3 = getPrecedence(Ops.ADD, Ops.SUB);
        // concat
        int p4 = getPrecedence(Ops.CONCAT);
        // EQ, NE, GT, LE, LT, GE, IN, NOT IN, BEWEEN, LIKE
        int p5 = getPrecedence(Ops.EQ, Ops.NE, Ops.GT, Ops.LT, Ops.GOE, Ops.LOE, Ops.IN, Ops.NOT_IN,
                Ops.BETWEEN, Ops.LIKE, Ops.LIKE_ESCAPE);
        // NOT
        int p6 = getPrecedence(Ops.NOT);
        // AND
        int p7 = getPrecedence(Ops.AND);
        // OR
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