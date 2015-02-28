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
            if (str.matches("\\w+\\([^\\(\\)]+\\)") && precedence > -1) {
                Assert.fail("Unexpected precedence for " + op + " with template " + template);
            } else if (str.contains(" like ") && precedence != likePrecedence) {
                Assert.fail("Unexpected precedence for " + op + " with template " + template);
            } else if (!str.contains("(") && precedence < 0) {
                Assert.fail("Unexpected precedence for " + op + " with template " + template);
            } else if (str.endsWith(" + 1") || str.endsWith("+1") || str.endsWith(" - 1") || str.endsWith("-1")) {
                Assert.fail("Unsafe pattern for " + op + " with template " + template);
            }
        }
    }

    private TemplatesTestUtils() {}

}
