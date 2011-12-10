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
package com.mysema.query.types.expr;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.types.path.StringPath;

public class CoalesceTest {

    private final StringPath firstname = new StringPath("firstname");

    private final StringPath lastname = new StringPath("lastname");

    @Test
    public void withList(){
        Coalesce<String> c = new Coalesce<String>(firstname, lastname).add("xxx");
        assertEquals("coalesce(firstname, lastname, xxx)", c.toString());
    }

    @Test
    public void withSingleArg(){
        Coalesce<String> c = new Coalesce<String>().add("xxx");
        assertEquals("coalesce(xxx)", c.toString());
    }

    @Test
    public void asComparable(){
        Coalesce<String> c = new Coalesce<String>(firstname, lastname);
        c.asc();
    }

    @Test
    public void asString(){
        Coalesce<String> c = new Coalesce<String>(firstname, lastname);
        c.asString().lower();
    }

    @Test
    public void withoutWarnings() {
        Coalesce<String> c = new Coalesce<String>(String.class).add(firstname).add(lastname);
        assertEquals("coalesce(firstname, lastname)", c.toString());
    }
}
