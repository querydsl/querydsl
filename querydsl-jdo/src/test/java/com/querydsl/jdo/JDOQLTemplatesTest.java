package com.querydsl.jdo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.querydsl.core.types.Operator;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Template;

import junit.framework.Assert;

public class JDOQLTemplatesTest {

    @Test
    public void Precedence() {
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
    public void Generic_Precedence() {
        JDOQLTemplates templates = JDOQLTemplates.DEFAULT;
        int likePrecedence = templates.getPrecedence(Ops.LIKE);
        for (Operator op : Ops.values()) {
            Template template = templates.getTemplate(op);
            int precedence = templates.getPrecedence(op);
            if (template.toString().contains(" like ") && precedence != likePrecedence) {
                Assert.fail("Unexpected precedence for " + op + " with template " + template);
            } else if (!template.toString().contains("(") && precedence < 0) {
                Assert.fail("Unexpected precedence for " + op + " with template " + template);
            }

        }
    }

}
