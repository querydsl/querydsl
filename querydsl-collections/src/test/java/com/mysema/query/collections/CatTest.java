/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.collections;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.mysema.query.types.path.SimplePath;

public class CatTest {

    @Test(expected=NoSuchFieldException.class)
    public void SkippedField() throws SecurityException, NoSuchFieldException{
        QCat.class.getField("skippedField");
    }

    @Test
    public void StringAsSimple() throws SecurityException, NoSuchFieldException {
        assertTrue(QCat.cat.stringAsSimple.getClass().equals(SimplePath.class));
    }

    @Test
    public void DateAsSimple(){
        assertTrue(QCat.cat.dateAsSimple.getClass().equals(SimplePath.class));
    }
}
