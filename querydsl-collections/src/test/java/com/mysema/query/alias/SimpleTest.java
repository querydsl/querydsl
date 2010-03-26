/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.alias;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.mysema.query.collections.MiniApi;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PathMetadataFactory;

public class SimpleTest {

    public static class TestClass {

        private int age;
        
        private String name;

        public TestClass(String name, int age){
            this.name = name;
            this.age = age;
        }
        
        public TestClass(){}
        
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
        
    }

    public static class QTestClass extends PEntity<TestClass> {

        public static final QTestClass testClass = new QTestClass("testClass");

        public final PString name = createString("name");
        
        public final PNumber<Integer> age = createNumber("age",Integer.class);
        
        public QTestClass(String variable) {
            super(TestClass.class, PathMetadataFactory.forVariable(variable));
        }

    }
    
    @Test
    public void test(){
        List<TestClass> testSource = new ArrayList<TestClass>();
        testSource.add(new TestClass("old timer", 89));
        testSource.add(new TestClass("bob", 22));
        testSource.add(new TestClass("joe", 24));
        
        QTestClass tc = QTestClass.testClass;
        for (TestClass t : MiniApi.from(tc, testSource).where(tc.name.eq("bob")).list(tc)){
            System.out.println(t.getName());  
        }
        
        for (TestClass t : MiniApi.from(tc, testSource).where(tc.name.in("bob")).list(tc)){
            System.out.println(t.getName());  
        }
    }

}
