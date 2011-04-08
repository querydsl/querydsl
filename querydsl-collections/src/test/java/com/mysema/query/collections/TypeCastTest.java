/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.collections;

import java.util.Collections;

import org.junit.Test;

import com.mysema.query.types.path.PathInits;

public class TypeCastTest {

    @Test(expected=IllegalStateException.class)
    public void Cast() {
        QAnimal animal = QAnimal.animal;
        QCat cat = new QCat(animal.getMetadata(), new PathInits("*"));
        System.out.println(cat);
        MiniApi.from(animal, Collections.<Animal> emptyList()).from(cat, Collections.<Cat> emptyList());
    }

}
