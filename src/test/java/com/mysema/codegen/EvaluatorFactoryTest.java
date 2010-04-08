/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.codegen;

import java.io.IOException;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class EvaluatorFactoryTest {
    
    public static class TestEntity {

        private final String name;

        public TestEntity(String name){
            this.name = name;
        }
        
        public String getName() {
            return name;
        }
        
    }
    
    private EvaluatorFactory factory;
    
    private List<String> names = Arrays.asList("a","b");
    
    private List<Class<?>> ints = Arrays.<Class<?>>asList(int.class, int.class);
    
    private List<Class<?>> strings = Arrays.<Class<?>>asList(String.class, String.class);
    
    private List<Class<?>> string_int = Arrays.<Class<?>>asList(String.class, int.class);
    
    @Before
    public void setUp() throws IOException{
        factory = new EvaluatorFactory((URLClassLoader) getClass().getClassLoader());
    }
    
    @Test
    public void testSimple(){               
        for (String expr : Arrays.asList("a.equals(b)", "a.startsWith(b)", "a.equalsIgnoreCase(b)")){
            test(expr, boolean.class, names, strings, Arrays.asList("a","b"));    
        }
        
        for (String expr : Arrays.asList("a != b", "a < b", "a > b", "a <= b", "a >= b")){
            test(expr, boolean.class, names, ints, Arrays.asList(0,1));    
        }        
    }
    
    @Test
    public void testResults(){
        // String + String
        test("a + b", String.class, names, strings, Arrays.asList("Hello ", "World"), "Hello World");
        
        // String + int
        test("a.substring(b)", String.class, names, string_int, Arrays.<Object>asList("Hello World", 6), "World");
        
        // int + int
        test("a + b", int.class, names, ints, Arrays.asList(1,2), 3);
    }
    
    @Test
    public void testCustomType(){
        test("a.getName()", String.class, 
                Collections.singletonList("a"), Collections.singletonList(TestEntity.class),
                Arrays.asList(new TestEntity("Hello World")), "Hello World");
                
    }
    
    private void test(String source, Class<?> projectionType, List<String> names, List<? extends Class<?>> types, List<?> args, Object expectedResult){
        Assert.assertEquals(expectedResult, test(source, projectionType, names, types, args));
    }
    
    private Object test(String source, Class<?> projectionType, List<String> names, List<? extends Class<?>> types, List<?> args) {
        Evaluator<?> evaluator = factory.createEvaluator(
                source, 
                projectionType, 
                names.toArray(new String[names.size()]), 
                types.toArray(new Class[types.size()]));
        return evaluator.evaluate(args.toArray());
    }

}
