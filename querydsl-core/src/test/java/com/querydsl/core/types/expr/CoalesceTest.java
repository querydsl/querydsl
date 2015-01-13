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
package com.querydsl.core.types.expr;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.querydsl.core.types.path.StringPath;

public class CoalesceTest {

    private final StringPath firstname = new StringPath("firstname");

    private final StringPath lastname = new StringPath("lastname");

    @Test
    public void Mutable() {
        Coalesce<String> c = new Coalesce<String>(firstname, lastname).add("xxx");
        assertEquals("coalesce(firstname, lastname, xxx)", c.toString());
        assertEquals("coalesce(firstname, lastname, xxx, yyy)", c.add("yyy").toString());
    }
    
    @Test
    public void WithList() {
        Coalesce<String> c = new Coalesce<String>(firstname, lastname).add("xxx");
        assertEquals("coalesce(firstname, lastname, xxx)", c.toString());
    }

    @Test
    public void WithSingleArg() {
        Coalesce<String> c = new Coalesce<String>().add("xxx");
        assertEquals("coalesce(xxx)", c.toString());
    }

    @Test
    public void AsComparable() {
        Coalesce<String> c = new Coalesce<String>(firstname, lastname);
        c.getValue().asc();
    }

    @Test
    public void AsString() {
        Coalesce<String> c = new Coalesce<String>(firstname, lastname);
        c.asString().lower();
    }

    @Test
    public void WithoutWarnings() {
        Coalesce<String> c = new Coalesce<String>(String.class).add(firstname).add(lastname);
        assertEquals("coalesce(firstname, lastname)", c.toString());
    }
    
    @Test
    public void Dsl() {
        assertEquals("coalesce(firstname, lastname)", firstname.coalesce(lastname).toString());
    }
    
    @Test
    public void Dsl2() {
        assertEquals("coalesce(firstname, lastname, xxx)", firstname.coalesce(lastname).add("xxx").toString());
    }
    
    @Test
    public void Dsl3() {
        assertEquals("coalesce(firstname, xxx)", firstname.coalesce("xxx").toString());   
    }
    
    @Test
    public void Asc() {
        assertEquals("coalesce(firstname, xxx) ASC", firstname.coalesce("xxx").asc().toString());
    }
    
    @Test
    public void Desc() {
        assertEquals("coalesce(firstname, xxx) DESC", firstname.coalesce("xxx").desc().toString());
    }
    
}
