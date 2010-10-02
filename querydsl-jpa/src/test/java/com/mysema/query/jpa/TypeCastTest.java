/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.jpa.domain.Animal;
import com.mysema.query.jpa.domain.Cat;
import com.mysema.query.jpa.domain.InheritedProperties;
import com.mysema.query.jpa.domain.QAnimal;
import com.mysema.query.jpa.domain.QCat;
import com.mysema.query.jpa.domain.QInheritedProperties;
import com.mysema.query.jpa.domain.QSuperclass;

public class TypeCastTest {

    @Test
    public void MappedSuperclass(){
        QInheritedProperties subClass = QInheritedProperties.inheritedProperties;
        QSuperclass superClass = subClass._super;

        assertEquals(InheritedProperties.class, superClass.getType());
//        assertEquals(InheritedProperties.class.getSimpleName(), superClass.getEntityName());
        assertEquals("inheritedProperties", superClass.toString());
    }

//    @Test
//    public void mappedSuperclass2(){
//        QInheritedProperties subClass = QInheritedProperties.inheritedProperties;
//        QSuperclass superClass = new QSuperclass(subClass.getMetadata());
//
//        assertEquals(Superclass.class, superClass.getType());
//        assertEquals(Superclass.class.getSimpleName(), superClass.getEntityName());
//        assertEquals("inheritedProperties", superClass.toString());
//    }

    @Test
    public void SubClassToSuper(){
        QCat cat = QCat.cat;
        QAnimal animal = new QAnimal(cat);

        assertEquals(Cat.class, animal.getType());
//        assertEquals(Cat.class.getSimpleName(), animal.getEntityName());
        assertEquals("cat", animal.toString());
    }

    @Test
    public void SubClassToSuper2(){
        QCat cat = QCat.cat;
        QAnimal animal = new QAnimal(cat.getMetadata());

        assertEquals(Animal.class, animal.getType());
//        assertEquals(Animal.class.getSimpleName(), animal.getEntityName());
        assertEquals("cat", animal.toString());
    }

    @Test
    public void SuperClassToSub(){
        QAnimal animal = QAnimal.animal;
        QCat cat = new QCat(animal.getMetadata());

        assertEquals(Cat.class, cat.getType());
//        assertEquals(Cat.class.getSimpleName(), cat.getEntityName());
        assertEquals("animal", cat.toString());
    }

}
