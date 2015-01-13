/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.core.testutil;

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
    public MethodRule rule = new MethodRule() {
        @Override
        public Statement apply(final Statement base, FrameworkMethod method, Object target) {
            return new Statement() {
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
    public static void beforeClass() {
        System.err.println("before class");
    }
    
    @Before
    public void before() {
        System.err.println("before");
    }    
    
    @AfterClass
    public static void afterClass() {
        System.err.println("after class");
    }
    
    @After
    public void after() {
        System.err.println("after");
    }
    
    @Test
    public void test() {
        System.err.println("test");
    }

}
