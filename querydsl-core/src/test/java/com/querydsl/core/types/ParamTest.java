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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import com.querydsl.core.types.expr.Param;

public class ParamTest {

    Param<String> param11 = new Param<String>(String.class, "param1");
    Param<String> param12 = new Param<String>(String.class, "param1");
    Param<String> param2 = new Param<String>(String.class, "param2");
    Param<Object> param3 = new Param<Object>(Object.class, "param1");
    Param<String> param4 = new Param<String>(String.class);

    @Test
    public void Identity() {
        assertEquals(param11, param12);
        assertFalse(param11.equals(param2));
        assertFalse(param11.equals(param3));
        assertFalse(param11.equals(param4));
    }
    
    @Test
    public void Anon() {
        System.out.println(param4.getName());
    }

    @Test
    public void GetNotSetMessage() {
        assertEquals("The parameter param1 needs to be set", param11.getNotSetMessage());
        assertEquals("A parameter of type java.lang.String was not set", param4.getNotSetMessage());
    }
}
