package com.mysema.testutil;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

public class StatementTest {
    
    @Rule
    public MethodRule rule = new MethodRule(){
        @Override
        public Statement apply(final Statement base, FrameworkMethod method, Object target) {
            return new Statement(){
                @Override
                public void evaluate() throws Throwable {
                    System.err.println("in rule");
                    base.evaluate();
                    System.err.println("out of rule");
                }
                
            };
        }
        
    };
    
    @BeforeClass
    public static void beforeClass(){
        System.err.println("before class");
    }
    
    @Before
    public void before(){
        System.err.println("before");
    }    
    
    @AfterClass
    public static void afterClass(){
        System.err.println("after class");
    }
    
    @After
    public void after(){
        System.err.println("after");
    }
    
    @Test
    public void test(){
        System.err.println("test");
    }

}
