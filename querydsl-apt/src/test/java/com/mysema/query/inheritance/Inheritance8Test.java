/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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
package com.mysema.query.inheritance;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.domain.CommonIdentifiable;
import com.mysema.query.domain.CommonPersistence;
import com.mysema.query.types.path.NumberPath;

public class Inheritance8Test {

    @QueryEntity
    public static class SimpleSubclass extends CommonPersistence {
    }

    @QueryEntity
    public static class GenericSubclass extends CommonIdentifiable<Long> {
    }

    @Test
    public void Simple_subclass_should_contain_fields_from_external_superclass() {
        assertEquals(NumberPath.class, QInheritance8Test_SimpleSubclass.simpleSubclass.version.getClass());
    }

    @Test
    public void Generic_subclass_should_contain_fields_from_external_superclass() {
        assertEquals(NumberPath.class, QInheritance8Test_GenericSubclass.genericSubclass.version.getClass());
    }

}
