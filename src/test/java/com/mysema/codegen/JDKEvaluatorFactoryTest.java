/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.codegen;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JDKEvaluatorFactoryTest {

    public static class TestEntity {

        private final String name;

        public TestEntity(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

    }

    private EvaluatorFactory factory;

    private List<String> names = Arrays.asList("a", "b");

    private List<Class<?>> ints = Arrays.<Class<?>> asList(int.class, int.class);

    private List<Class<?>> strings = Arrays.<Class<?>> asList(String.class, String.class);

    private List<Class<?>> string_int = Arrays.<Class<?>> asList(String.class, int.class);

    @Before
    public void setUp() throws IOException {
        factory = new JDKEvaluatorFactory((URLClassLoader) getClass().getClassLoader());
    }

    @Test
    public void Simple() {
        for (String expr : Arrays.asList("a.equals(b)", "a.startsWith(b)", "a.equalsIgnoreCase(b)")) {
            long start = System.currentTimeMillis();
            evaluate(expr, boolean.class, names, strings, Arrays.asList("a", "b"),
                    Collections.<String, Object> emptyMap());
            long duration = System.currentTimeMillis() - start;
            System.err.println(expr + " took " + duration + "ms\n");
        }

        for (String expr : Arrays.asList("a != b", "a < b", "a > b", "a <= b", "a >= b")) {
            long start = System.currentTimeMillis();
            evaluate(expr, boolean.class, names, ints, Arrays.asList(0, 1),
                    Collections.<String, Object> emptyMap());
            long duration = System.currentTimeMillis() - start;
            System.err.println(expr + " took " + duration + "ms\n");
        }
    }

    @Test
    public void Results() {
        // String + String
        test("a + b", String.class, names, strings, Arrays.asList("Hello ", "World"), "Hello World");

        // String + int
        test("a.substring(b)", String.class, names, string_int,
                Arrays.<Object> asList("Hello World", 6), "World");

        // int + int
        test("a + b", int.class, names, ints, Arrays.asList(1, 2), 3);
    }

    @Test
    public void WithConstants() {
        Map<String, Object> constants = new HashMap<String, Object>();
        constants.put("x", "Hello World");
        List<Class<?>> types = Arrays.<Class<?>> asList(String.class);
        List<String> names = Arrays.asList("a");
        assertEquals(
                Boolean.TRUE,
                evaluate("a.equals(x)", boolean.class, names, types, Arrays.asList("Hello World"),
                        constants));
        assertEquals(
                Boolean.FALSE,
                evaluate("a.equals(x)", boolean.class, names, types, Arrays.asList("Hello"),
                        constants));
    }

    @Test
    public void CustomType() {
        test("a.getName()", String.class, Collections.singletonList("a"),
                Collections.<Class<?>> singletonList(TestEntity.class),
                Arrays.asList(new TestEntity("Hello World")), "Hello World");
    }

    private void test(String source, Class<?> projectionType, List<String> names,
            List<Class<?>> types, List<?> args, Object expectedResult) {
        Assert.assertEquals(
                expectedResult,
                evaluate(source, projectionType, names, types, args,
                        Collections.<String, Object> emptyMap()));
    }

    private Object evaluate(String source, Class<?> projectionType, List<String> names,
            List<Class<?>> types, List<?> args, Map<String, Object> constants) {
        Evaluator<?> evaluator = factory.createEvaluator("return " + source + ";", projectionType,
                names.toArray(new String[names.size()]), types.toArray(new Class<?>[types.size()]),
                constants);
        return evaluator.evaluate(args.toArray());
    }

}
