package com.querydsl.core.types.path;

import java.util.Collection;
import java.util.Map;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PathBuilderValidatorTest {

    public static class Customer {
        String name;
        Collection<Integer> collection;
        Map<String, Integer> map;
    }

    public static class ExtendedCustomer extends Customer {}

    public static class Project {
        public String getName() { return ""; }
        public Collection<Integer> getCollection() { return null; }
        public Map<String, Integer> getMap() { return null; }
    }

    public static class ExtendedProject extends Project {
        public boolean isStarted() { return true; }
    }

    @Test
    public void Default() {
        assertEquals(String.class, PathBuilderValidator.DEFAULT.validate(Customer.class, "name", String.class));
        assertEquals(String.class, PathBuilderValidator.DEFAULT.validate(ExtendedCustomer.class, "name", String.class));
        assertEquals(String.class, PathBuilderValidator.DEFAULT.validate(Project.class, "name", String.class));
        assertEquals(String.class, PathBuilderValidator.DEFAULT.validate(ExtendedProject.class, "name", String.class));
    }

    @Test
    public void Fields() {
        assertEquals(String.class, PathBuilderValidator.FIELDS.validate(Customer.class, "name", String.class));
        assertEquals(String.class, PathBuilderValidator.FIELDS.validate(ExtendedCustomer.class, "name", String.class));
        assertEquals(Integer.class, PathBuilderValidator.FIELDS.validate(Customer.class, "collection", Collection.class));
        assertEquals(Integer.class, PathBuilderValidator.FIELDS.validate(Customer.class, "map", Map.class));
        assertNull(PathBuilderValidator.FIELDS.validate(Project.class, "name", String.class));
        assertNull(PathBuilderValidator.FIELDS.validate(ExtendedProject.class, "name", String.class));
    }

    @Test
    public void Properties() {
        assertNull(PathBuilderValidator.PROPERTIES.validate(Customer.class, "name", String.class));
        assertNull(PathBuilderValidator.PROPERTIES.validate(ExtendedCustomer.class, "name", String.class));
        assertEquals(String.class, PathBuilderValidator.PROPERTIES.validate(Project.class, "name", String.class));
        assertEquals(String.class, PathBuilderValidator.PROPERTIES.validate(ExtendedProject.class, "name", String.class));
        assertEquals(Boolean.class, PathBuilderValidator.PROPERTIES.validate(ExtendedProject.class, "started", Boolean.class));
        assertEquals(Integer.class, PathBuilderValidator.PROPERTIES.validate(Project.class, "collection", Collection.class));
        assertEquals(Integer.class, PathBuilderValidator.PROPERTIES.validate(Project.class, "map", Map.class));
    }
}
