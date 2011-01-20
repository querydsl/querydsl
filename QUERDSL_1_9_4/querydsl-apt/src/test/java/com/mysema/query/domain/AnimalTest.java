/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.domain;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;

public class AnimalTest {

    @QueryEntity
    public static class Animal{

        public String name;

    }

    @QueryEntity
    public static class Cat extends Animal{

        public Cat mate;

    }

    @Test
    public void properties_are_copied_from_super(){
        assertTrue("direct copy of PString field failed",  QAnimalTest_Cat.cat.name == QAnimalTest_Cat.cat._super.name);

    }

}
