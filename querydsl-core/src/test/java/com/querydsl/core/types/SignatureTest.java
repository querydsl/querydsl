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
package com.querydsl.core.types;

import static org.junit.Assert.assertFalse;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.querydsl.core.types.expr.Coalesce;
import com.querydsl.core.types.expr.DslExpression;
import com.querydsl.core.types.expr.SimpleExpression;

public class SignatureTest {
    
    private List<Class<?>> classes = new ArrayList<Class<?>>();
    
    @Before
    public void setUp() throws ClassNotFoundException{
        for (String folder : Arrays.asList(
                "com/mysema/query/types/expr",
                "com/mysema/query/types/path",
                "com/mysema/query/types/query",
                "com/mysema/query/types/template")) {
            for (String file : new File("src/main/java",folder).list()) {
                if (file.endsWith(".java") && !file.equals("package-info.java")) {
                    String className = (folder+"."+file.substring(0, file.length()-5)).replace('/', '.');
                    classes.add(Class.forName(className));
                }                
            }
        }
    }
    
    @Test
    public void ReturnType_extends_SimpleExpression() {
        assertFalse(classes.isEmpty());
        Set<String> skippedMethods = new HashSet<String>(Arrays.asList("getArg","getRoot","not"));
        List<String> errors = new ArrayList<String>();
        for (Class<?> cl : classes) {
            for (Method m : cl.getDeclaredMethods()) {
                if (!skippedMethods.contains(m.getName())
                 && Modifier.isPublic(m.getModifiers())
                 && Expression.class.isAssignableFrom(m.getReturnType())
                 && !Coalesce.class.isAssignableFrom(m.getReturnType())
                 && !DslExpression.class.isAssignableFrom(m.getReturnType())
                 && !SimpleExpression.class.isAssignableFrom(m.getReturnType())) {
                    errors.add(cl.getSimpleName()+"."+m.getName() + " has illegal return type");
                }
            }
        }        
        for (String error : errors) {
            System.err.println(error);            
        }
        if (!errors.isEmpty()) {
            Assert.fail("Got " + errors.size() +" errors");
        }
    }

}
