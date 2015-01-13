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
package com.querydsl.core.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BeanUtilsTest {

    @Test
    public void Capitalize() {
        assertEquals("X", BeanUtils.capitalize("x"));
        assertEquals("Prop", BeanUtils.capitalize("prop"));
        assertEquals("URL",  BeanUtils.capitalize("URL"));
        assertEquals("cId",  BeanUtils.capitalize("cId"));
        assertEquals("sEPOrder",  BeanUtils.capitalize("sEPOrder"));
    }

    @Test
    public void Uncapitalize() {
        assertEquals("x",    BeanUtils.uncapitalize("X"));
        assertEquals("prop", BeanUtils.uncapitalize("Prop"));
        assertEquals("URL",  BeanUtils.uncapitalize("URL"));
        assertEquals("cId",  BeanUtils.uncapitalize("cId"));
        assertEquals("sEPOrder",  BeanUtils.capitalize("sEPOrder"));
    }


}
