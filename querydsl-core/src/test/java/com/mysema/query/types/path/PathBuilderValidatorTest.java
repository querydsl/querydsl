package com.mysema.query.types.path;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PathBuilderValidatorTest {

    public static class Customer {
        String name;
    }

    public static class ExtendedCustomer extends Customer {}

    public static class Project {
        public String getName() { return ""; }
    }

    public static class ExtendedProject extends Project {}

    @Test
    public void Default() {
        assertTrue(PathBuilderValidator.DEFAULT.validate(Customer.class, "name", String.class));
        assertTrue(PathBuilderValidator.DEFAULT.validate(ExtendedCustomer.class, "name", String.class));
        assertTrue(PathBuilderValidator.DEFAULT.validate(Project.class, "name", String.class));
        assertTrue(PathBuilderValidator.DEFAULT.validate(ExtendedProject.class, "name", String.class));
    }

    @Test
    public void Fields() {
        assertTrue(PathBuilderValidator.FIELDS.validate(Customer.class, "name", String.class));
        assertTrue(PathBuilderValidator.FIELDS.validate(ExtendedCustomer.class, "name", String.class));
        assertFalse(PathBuilderValidator.FIELDS.validate(Project.class, "name", String.class));
        assertFalse(PathBuilderValidator.FIELDS.validate(ExtendedProject.class, "name", String.class));
    }

    @Test
    public void Properties() {
        assertFalse(PathBuilderValidator.PROPERTIES.validate(Customer.class, "name", String.class));
        assertFalse(PathBuilderValidator.PROPERTIES.validate(ExtendedCustomer.class, "name", String.class));
        assertTrue(PathBuilderValidator.PROPERTIES.validate(Project.class, "name", String.class));
        assertTrue(PathBuilderValidator.PROPERTIES.validate(ExtendedProject.class, "name", String.class));
    }
}
