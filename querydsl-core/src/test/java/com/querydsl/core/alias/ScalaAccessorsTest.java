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
package com.querydsl.core.alias;

import static com.querydsl.core.alias.Alias.$;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ScalaAccessorsTest {
    
    @Test
    public void ScalaAccessors() {
        ScalaAccessors scalaAccessors = Alias.alias(ScalaAccessors.class);
        assertEquals("scalaAccessors.firstName", $(scalaAccessors.firstName()).toString());
        assertEquals("scalaAccessors.lastName",  $(scalaAccessors.lastName()).toString());
    }

}
