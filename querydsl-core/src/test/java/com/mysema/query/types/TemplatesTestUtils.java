package com.mysema.query.types;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

public final class TemplatesTestUtils {

    private static final List<Operator<?>> OPS = new ArrayList<Operator<?>>();

    static {
        try {
            for (Field field : Ops.class.getFields()) {
                if (field.getType().equals(Operator.class)) {
                    OPS.add((Operator<?>)field.get(null));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void testPrecedence(Templates templates) {
        int likePrecedence = templates.getPrecedence(Ops.LIKE);
        int eqPrecedence = templates.getPrecedence(Ops.EQ);
        if (templates.getPrecedence(Ops.EQ_IGNORE_CASE) != eqPrecedence) {
            Assert.fail("Unexpected precedence for EQ_IGNORE_CASE "
                    + templates.getPrecedence(Ops.EQ_IGNORE_CASE));
        }
        for (Operator<?> op : OPS) {
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

    private TemplatesTestUtils() {
    }

}
