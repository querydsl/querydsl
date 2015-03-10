package com.mysema.query.types;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;

import org.junit.Test;

public class OpsTest {

    @Test
    public void Naming() throws IllegalAccessException {
        for (Field field : Ops.class.getFields()) {
            validate(field);
        }
        for (Field field : Ops.AggOps.class.getFields()) {
            validate(field);
        }
        for (Field field : Ops.QuantOps.class.getFields()) {
            validate(field);
        }
        for (Field field : Ops.DateTimeOps.class.getFields()) {
            validate(field);
        }
        for (Field field : Ops.MathOps.class.getFields()) {
            validate(field);
        }
        for (Field field : Ops.StringOps.class.getFields()) {
            validate(field);
        }
    }

    public static void validate(Field field) throws IllegalAccessException {
        if (field.getType().equals(Operator.class)) {
            assertEquals(field.getDeclaringClass().getName() + "#" + field.getName(),
                    ((Operator<?>)field.get(null)).getId());
        }

    }
}
