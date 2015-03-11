package com.mysema.query.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.mysema.query.types.Operator;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Templates;
import com.mysema.query.types.TemplatesTestUtils;

public class JPQLTemplatesTest {

    @Test
    public void Escape() {
        List<Templates> templates = Arrays.<Templates>asList(
                new JPQLTemplates(), new HQLTemplates(),
                new EclipseLinkTemplates(), new OpenJPATemplates()
        );

        for (Templates t : templates) {
            assertEquals("{0} like {1} escape '!'", t.getTemplate(Ops.LIKE).toString());
        }
    }

    @Test
    public void Custom_Escape() {
        List<Templates> templates = Arrays.<Templates>asList(
                new JPQLTemplates('X'), new HQLTemplates('X'),
                new EclipseLinkTemplates('X'), new OpenJPATemplates('X')
        );

        for (Templates t : templates) {
            assertEquals("{0} like {1} escape 'X'", t.getTemplate(Ops.LIKE).toString());
        }
    }

    @Test
    public void Precedence() {
        // Navigation operator (.)
        // +, - unary *,
        int p1 = getPrecedence(Ops.NEGATE);
        // / multiplication and division
        int p2 = getPrecedence(Ops.MULT, Ops.DIV);
        // +, - addition and subtraction
        int p3 = getPrecedence(Ops.ADD, Ops.SUB);
        // Comparison operators : =, >, >=, <, <=, <> (not equal), [NOT] BETWEEN, [NOT] LIKE, [NOT] IN, IS [NOT] NULL, IS [NOT] EMPTY, [NOT] MEMBER [OF]
        int p4 = getPrecedence(Ops.EQ, Ops.GT, Ops.GOE, Ops.LT, Ops.LOE, Ops.NE, Ops.BETWEEN, Ops.LIKE, Ops.LIKE_ESCAPE, Ops.IN,
                Ops.IS_NULL, Ops.IS_NOT_NULL, JPQLOps.MEMBER_OF, JPQLOps.NOT_MEMBER_OF);
        // NOT
        int p5 = getPrecedence(Ops.NOT);
        // AND
        int p6 = getPrecedence(Ops.AND);
        // OR
        int p7 = getPrecedence(Ops.OR);

        assertTrue(p1 < p2);
        assertTrue(p2 < p3);
        assertTrue(p3 < p4);
        assertTrue(p4 < p5);
        assertTrue(p5 < p6);
        assertTrue(p6 < p7);
    }

    protected int getPrecedence(Operator<?>... ops) {
        int precedence = JPQLTemplates.DEFAULT.getPrecedence(ops[0]);
        for (int i = 1; i < ops.length; i++) {
            assertEquals(ops[i].getId(), precedence, JPQLTemplates.DEFAULT.getPrecedence(ops[i]));
        }
        return precedence;
    }

    @Test
    public void Generic_Precedence() {
        for (JPQLTemplates templates : ImmutableList.of(
                JPQLTemplates.DEFAULT, HQLTemplates.DEFAULT, EclipseLinkTemplates.DEFAULT)) {
            TemplatesTestUtils.testPrecedence(templates);
        }
    }

}
