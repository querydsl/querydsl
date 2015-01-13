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

import static org.junit.Assert.*;

import org.junit.Test;

import com.querydsl.core.types.ConstantImpl;

public class MapPathTest {

    private MapPath<String,String,StringPath> mapPath = new MapPath<String,String,StringPath>(
            String.class, String.class, StringPath.class, "p");
    
    @Test
    public void Get() {
        assertNotNull(mapPath.get("X"));
        assertNotNull(mapPath.get(ConstantImpl.create("X")));
    }
    
    @Test
    public void GetKeytType() {
        assertEquals(String.class, mapPath.getKeyType());
    }

    @Test
    public void GetValueType() {
        assertEquals(String.class, mapPath.getValueType());
    }
    
    @Test
    public void GetParameter() {
        assertEquals(String.class, mapPath.getParameter(0));
        assertEquals(String.class, mapPath.getParameter(1));
    }

}
