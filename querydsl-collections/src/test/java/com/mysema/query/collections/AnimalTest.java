/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.mysema.query.types.path.SimplePath;

public class AnimalTest {

    @Test
    public void Cast(){
        QCat cat = QAnimal.animal.as(QCat.class);
        assertEquals(QAnimal.animal.getMetadata(), cat.getMetadata());
        assertEquals("animal", cat.toString());
    }

    @Test
    public void Date_As_Simple(){
        assertTrue(QAnimal.animal.dateAsSimple.getClass().equals(SimplePath.class));
    }

}
