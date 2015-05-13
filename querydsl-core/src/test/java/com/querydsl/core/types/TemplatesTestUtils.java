package com.querydsl.core.types;

import org.junit.Assert;

public final class TemplatesTestUtils {

    public static void testPrecedence(Templates templates) {
        int likePrecedence = templates.getPrecedence(Ops.LIKE);
        int eqPrecedence = templates.getPrecedence(Ops.EQ);
        if (templates.getPrecedence(Ops.EQ_IGNORE_CASE) != eqPrecedence) {
            Assert.fail("Unexpected precedence for EQ_IGNORE_CASE "
                    + templates.getPrecedence(Ops.EQ_IGNORE_CASE));
        }
        for (Operator op : Ops.values()) {
            Template template = templates.getTemplate(op);
            String str = template.toString();
            int precedence = templates.getPrecedence(op);
            if (str.contains(" like ") && precedence != likePrecedence) {
                Assert.fail("Unexpected precedence for " + op + " with template " + template);
            } else if (!str.contains("(") && !str.contains(".") && precedence < 0) {
                Assert.fail("Unexpected precedence for " + op + " with template " + template);
            } else if (str.matches(".*[<>] ?\\-?\\d")) {
                if (precedence != Templates.Precedence.COMPARISON) {
                    Assert.fail("Unsafe pattern for " + op + " with template " + template);
                }
            } else if (str.matches(".*[\\+\\-] ?\\-?\\d")) {
                if (precedence != Templates.Precedence.ARITH_LOW && precedence != Templates.Precedence.ARITH_HIGH) {
                    Assert.fail("Unsafe pattern for " + op + " with template " + template);
                }
            }
        }
    }

    private TemplatesTestUtils() { }

}
