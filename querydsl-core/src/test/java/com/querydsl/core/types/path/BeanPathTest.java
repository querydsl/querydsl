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
package com.querydsl.core.types.path;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.annotation.Nullable;

import org.junit.Ignore;
import org.junit.Test;

import com.querydsl.core.annotations.PropertyType;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.PathMetadataFactory;
import com.querydsl.core.types.expr.BooleanExpression;

public class BeanPathTest {
    
    public static class SubClass extends BeanPathTest {
        
    }
    
    public static class MyBeanPath extends BeanPath<BeanPathTest>{

        private static final long serialVersionUID = 6225684967115368814L;

        public MyBeanPath(PathMetadata<?> metadata) {
            super(BeanPathTest.class, metadata);
        }
        
        public MyBeanPath(PathMetadata<?> metadata, @Nullable PathInits inits) {
            super(BeanPathTest.class, metadata);
        }
        
    }
    
    private BeanPath<BeanPathTest> beanPath = new BeanPath<BeanPathTest>(BeanPathTest.class, "p");
    
    @Test
    public void As_Path() {        
        SimplePath<BeanPathTest> simplePath = new SimplePath<BeanPathTest>(BeanPathTest.class, "p");
        assertNotNull(beanPath.as(simplePath));
    }
    
    @Test
    @Ignore
    public void As_Class() {       
        MyBeanPath otherPath = beanPath.as(MyBeanPath.class);
        assertEquals(beanPath, otherPath);
        assertTrue(otherPath.getMetadata().isRoot());
    }
    
    @Test
    public void As_Class_Cached() {       
        MyBeanPath otherPath = beanPath.as(MyBeanPath.class);
//        assertEquals(beanPath, otherPath);
        assertTrue(otherPath == beanPath.as(MyBeanPath.class));
    }
    
    @Test
    @Ignore
    public void As_Class_with_Inits() {       
        beanPath = new BeanPath<BeanPathTest>(BeanPathTest.class, 
                PathMetadataFactory.forVariable("p"), PathInits.DEFAULT);
        MyBeanPath otherPath = beanPath.as(MyBeanPath.class);
        assertEquals(beanPath, otherPath);
    }
    
    @Test
    public void As_Class_with_Inits_Cached() {       
        beanPath = new BeanPath<BeanPathTest>(BeanPathTest.class, 
                PathMetadataFactory.forVariable("p"), PathInits.DEFAULT);
        MyBeanPath otherPath = beanPath.as(MyBeanPath.class);
//        assertEquals(beanPath, otherPath);
        assertTrue(otherPath == beanPath.as(MyBeanPath.class));
    }
    
    @Test
    public void CreateEnum() {
        assertNotNull(beanPath.createEnum("property", PropertyType.class));
    }
    
    @Test
    public void InstanceOf() {
        assertNotNull(beanPath.instanceOf(BeanPathTest.class));
    }
    
    @Test
    public void InstanceOfAny() {
        BooleanExpression pred1 = beanPath.instanceOf(BeanPathTest.class).or(beanPath.instanceOf(SubClass.class));
        BooleanExpression pred2 = beanPath.instanceOfAny(BeanPathTest.class, SubClass.class);
        assertEquals(pred1, pred2);
        assertEquals("p.class = class com.querydsl.core.types.path.BeanPathTest || " +
        	     "p.class = class com.querydsl.core.types.path.BeanPathTest$SubClass", pred2.toString());
    }

}
