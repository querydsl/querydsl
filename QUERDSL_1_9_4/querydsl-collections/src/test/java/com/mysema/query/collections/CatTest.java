/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.collections;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.mysema.query.types.path.PSimple;

public class CatTest {

    @Test(expected=NoSuchFieldException.class)
    public void testSkippedField() throws SecurityException, NoSuchFieldException{
        QCat.class.getField("skippedField");
    }

    @Test
    public void stringAsSimple() throws SecurityException, NoSuchFieldException {
        assertTrue(QCat.cat.stringAsSimple.getClass().equals(PSimple.class));
    }

    @Test
    public void dateAsSimple(){
        assertTrue(QCat.cat.dateAsSimple.getClass().equals(PSimple.class));
    }
}
