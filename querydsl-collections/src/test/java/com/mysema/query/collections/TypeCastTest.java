/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.junit.Test;

import com.mysema.query.animal.QAnimal;
import com.mysema.query.animal.QCat;
import com.mysema.query.collections.ColQuery;
import com.mysema.query.collections.ColQueryImpl;
import com.mysema.query.collections.MiniApi;
import com.mysema.query.types.path.PathInits;

public class TypeCastTest {

    @Test
    public void cast() {
        QAnimal animal = QAnimal.animal;
        QCat cat = new QCat(animal.getMetadata(), new PathInits("*"));
        System.out.println(cat);

        ColQuery query = MiniApi.from(animal, Collections.<Animal> emptyList()).from(cat, Collections.<Cat> emptyList());
        assertEquals(1, ((ColQueryImpl) query).getMetadata().getJoins().size());
    }

}
