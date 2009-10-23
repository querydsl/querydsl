/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.hql.domain.Animal;
import com.mysema.query.hql.domain.Cat;
import com.mysema.query.hql.domain.InheritedProperties;
import com.mysema.query.hql.domain.QAnimal;
import com.mysema.query.hql.domain.QCat;
import com.mysema.query.hql.domain.QInheritedProperties;
import com.mysema.query.hql.domain.QSuperclass;

public class TypeCastTest {

    @Test
    public void mappedSuperclass(){
        QInheritedProperties subClass = QInheritedProperties.inheritedProperties;
        QSuperclass superClass = subClass._super;
        
        assertEquals(InheritedProperties.class, superClass.getType());
        assertEquals(InheritedProperties.class.getSimpleName(), superClass.getEntityName());
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
    public void subClassToSuper(){
        QCat cat = QCat.cat;
        QAnimal animal = new QAnimal(cat);
        
        assertEquals(Cat.class, animal.getType());
        assertEquals(Cat.class.getSimpleName(), animal.getEntityName());
        assertEquals("cat", animal.toString());
    }
    
    @Test
    public void subClassToSuper2(){
        QCat cat = QCat.cat;
        QAnimal animal = new QAnimal(cat.getMetadata());
        
        assertEquals(Animal.class, animal.getType());
        assertEquals(Animal.class.getSimpleName(), animal.getEntityName());
        assertEquals("cat", animal.toString());
    }
    
    @Test
    public void superClassToSub(){        
        QAnimal animal = QAnimal.animal;
        QCat cat = new QCat(animal.getMetadata());
        
        assertEquals(Cat.class, cat.getType());
        assertEquals(Cat.class.getSimpleName(), cat.getEntityName());
        assertEquals("animal", cat.toString());
    }
    
}
