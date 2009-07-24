/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.impl;

import org.junit.Test;

import com.mysema.query.collections.domain.QGenericType;
import com.mysema.query.collections.domain.QItemType;
import com.mysema.query.collections.domain.QRelationType;
import com.mysema.query.collections.domain.QRelationType2;
import com.mysema.query.collections.domain.QSimpleTypes;

/**
 * Domain provides
 * 
 * @author tiwe
 * @version $Id$
 */
public class DomainTest {

    @Test
    @SuppressWarnings("unused")
    public void test() {
        QSimpleTypes st = new QSimpleTypes("st");
        QRelationType rt = new QRelationType("rt");

        QRelationType2 rt2 = new QRelationType2("rt2");
        rt2 = rt2.list(0);

        QGenericType gt = new QGenericType("qt");
        QItemType it = gt.itemType;
        it = new QItemType("it");
    }

}
