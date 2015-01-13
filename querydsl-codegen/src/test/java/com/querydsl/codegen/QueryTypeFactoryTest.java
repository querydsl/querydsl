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
package com.querydsl.codegen;

import static org.junit.Assert.*;

import org.junit.Test;

import com.mysema.codegen.model.ClassType;
import com.mysema.codegen.model.Type;

public class QueryTypeFactoryTest {

    private Type type = new ClassType(Point.class);
    
    @Test
    public void Prefix_Only() {
        QueryTypeFactory factory = new QueryTypeFactoryImpl("Q", "", "");
        assertEquals("com.querydsl.codegen.QPoint", factory.create(type).getFullName());
    }
    
    @Test
    public void Prefix_And_Suffix() {
        QueryTypeFactory factory = new QueryTypeFactoryImpl("Q", "Type", "");
        assertEquals("com.querydsl.codegen.QPointType", factory.create(type).getFullName());
    }
    
    @Test
    public void Suffix_Only() {
        QueryTypeFactory factory = new QueryTypeFactoryImpl("", "Type", "");
        assertEquals("com.querydsl.codegen.PointType", factory.create(type).getFullName());
    }
    
    @Test
    public void Prefix_And_Package_Suffix() {
        QueryTypeFactory factory = new QueryTypeFactoryImpl("Q", "", ".querydsl");
        assertEquals("com.querydsl.codegen.querydsl.QPoint", factory.create(type).getFullName());
    }
    
}
