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
package com.querydsl.jpa;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.querydsl.jpa.domain.Animal;
import com.querydsl.jpa.domain.Cat;
import com.querydsl.jpa.domain.InheritedProperties;
import com.querydsl.jpa.domain.QAnimal;
import com.querydsl.jpa.domain.QCat;
import com.querydsl.jpa.domain.QInheritedProperties;
import com.querydsl.jpa.domain.QSuperclass;

public class TypeCastTest {

    @Test
    public void MappedSuperclass() {
        QInheritedProperties subClass = QInheritedProperties.inheritedProperties;
        QSuperclass superClass = subClass._super;

        assertEquals(InheritedProperties.class, superClass.getType());
//        assertEquals(InheritedProperties.class.getSimpleName(), superClass.getEntityName());
        assertEquals("inheritedProperties", superClass.toString());
    }

//    @Test
//    public void mappedSuperclass2() {
//        QInheritedProperties subClass = QInheritedProperties.inheritedProperties;
//        QSuperclass superClass = new QSuperclass(subClass.getMetadata());
//
//        assertEquals(Superclass.class, superClass.getType());
//        assertEquals(Superclass.class.getSimpleName(), superClass.getEntityName());
//        assertEquals("inheritedProperties", superClass.toString());
//    }

    @Test
    public void SubClassToSuper() {
        QCat cat = QCat.cat;
        QAnimal animal = new QAnimal(cat);

        assertEquals(Cat.class, animal.getType());
//        assertEquals(Cat.class.getSimpleName(), animal.getEntityName());
        assertEquals("cat", animal.toString());
    }

    @Test
    public void SubClassToSuper2() {
        QCat cat = QCat.cat;
        QAnimal animal = new QAnimal(cat.getMetadata());

        assertEquals(Animal.class, animal.getType());
//        assertEquals(Animal.class.getSimpleName(), animal.getEntityName());
        assertEquals("cat", animal.toString());
    }

    @Test
    public void SuperClassToSub() {
        QAnimal animal = QAnimal.animal;
        QCat cat = new QCat(animal.getMetadata());

        assertEquals(Cat.class, cat.getType());
//        assertEquals(Cat.class.getSimpleName(), cat.getEntityName());
        assertEquals("animal", cat.toString());
    }

}
