package com.mysema.query.types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JavaTemplatesTest {

    private Templates templates = JavaTemplates.DEFAULT;

    @Test
    public void Precedence() {
        // postfix	expr++ expr--
        // unary	++expr --expr +expr -expr ~ !
        // multiplicative	* / %
        // additive	+ -
        // shift	<< >> >>>
        // relational	< > <= >= instanceof
        // equality	== !=
        // bitwise AND	&
        // bitwise exclusive OR	^
        // bitwise inclusive OR	|
        // logical AND	&&
        // logical OR	||
        // ternary	? :
        // assignment	= += -= *= /= %= &= ^= |= <<= >>= >>>=

        int p1 = getPrecedence(Ops.NOT);
        int p2 = getPrecedence(Ops.MULT, Ops.DIV, Ops.MOD);
        int p3 = getPrecedence(Ops.ADD, Ops.SUB);
        int p4 = getPrecedence(Ops.LT, Ops.GT, Ops.GOE, Ops.LOE, Ops.BETWEEN, Ops.INSTANCE_OF);
        int p5 = getPrecedence(Ops.EQ, Ops.NE);
        int p6 = getPrecedence(Ops.AND);
        int p7 = getPrecedence(Ops.OR);

        assertTrue(p1 < p2);
        assertTrue(p2 < p3);
        assertTrue(p3 < p4);
        assertTrue(p4 < p5);
        assertTrue(p5 < p6);
        assertTrue(p6 < p7);
    }

    @Test
    public void Generic_Precedence() {
        TemplatesTestUtils.testPrecedence(JavaTemplates.DEFAULT);
    }

    protected int getPrecedence(Operator<?>... ops) {
        int precedence = templates.getPrecedence(ops[0]);
        for (int i = 1; i < ops.length; i++) {
            assertEquals(ops[i].getId(), precedence, templates.getPrecedence(ops[i]));
        }
        return precedence;
    }


}