package com.mysema.query.types.path;

import org.junit.Test;
import static org.junit.Assert.*;

public class PathBuilderValidatorTest {

    public static class Customer {
        String name;
    }

    public static class ExtendedCustomer extends Customer {}

    public static class Project {
        public String getName() { return ""; }
    }

    public static class ExtendedProject extends Project {
        public boolean isStarted() { return true; }
    }

    @Test
    public void Default() {
        assertNotNull(PathBuilderValidator.DEFAULT.validate(Customer.class, "name", String.class));
        assertNotNull(PathBuilderValidator.DEFAULT.validate(ExtendedCustomer.class, "name", String.class));
        assertNotNull(PathBuilderValidator.DEFAULT.validate(Project.class, "name", String.class));
        assertNotNull(PathBuilderValidator.DEFAULT.validate(ExtendedProject.class, "name", String.class));
    }

    @Test
    public void Fields() {
        assertNotNull(PathBuilderValidator.FIELDS.validate(Customer.class, "name", String.class));
        assertNotNull(PathBuilderValidator.FIELDS.validate(ExtendedCustomer.class, "name", String.class));
        assertNull(PathBuilderValidator.FIELDS.validate(Project.class, "name", String.class));
        assertNull(PathBuilderValidator.FIELDS.validate(ExtendedProject.class, "name", String.class));
    }

    @Test
    public void Properties() {
        assertNull(PathBuilderValidator.PROPERTIES.validate(Customer.class, "name", String.class));
        assertNull(PathBuilderValidator.PROPERTIES.validate(ExtendedCustomer.class, "name", String.class));
        assertNotNull(PathBuilderValidator.PROPERTIES.validate(Project.class, "name", String.class));
        assertNotNull(PathBuilderValidator.PROPERTIES.validate(ExtendedProject.class, "name", String.class));
        assertNotNull(PathBuilderValidator.PROPERTIES.validate(ExtendedProject.class, "started", Boolean.class));
    }
}
