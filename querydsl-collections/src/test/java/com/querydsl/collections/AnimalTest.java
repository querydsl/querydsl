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
package com.querydsl.collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.querydsl.core.types.path.SimplePath;

public class AnimalTest {

    @Test
    public void Cast() {
        QCat cat = QAnimal.animal.as(QCat.class);
        assertEquals(QAnimal.animal, cat.getMetadata().getElement());
        assertEquals("animal", cat.toString());
    }

    @Test
    public void Date_As_Simple() {
        assertTrue(QAnimal.animal.dateAsSimple.getClass().equals(SimplePath.class));
    }

}
