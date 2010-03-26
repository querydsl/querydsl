/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;

import org.junit.Test;

import com.mysema.query.animal.Cat;
import com.mysema.query.animal.QCat;
import com.mysema.query.collections.ColQueryImpl;

public class QueryMutabilityTest {

    @Test
    public void test() throws SecurityException, IllegalArgumentException,
            NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, IOException {
        QCat cat = QCat.cat;
        ColQueryImpl query = new ColQueryImpl();
        query.from(cat, Collections.<Cat> emptyList());
        new QueryMutability(query).test(cat.id, cat.name);

    }

}
