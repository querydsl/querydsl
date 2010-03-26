/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import org.junit.Test;

import com.mysema.query.animal.QCat;


public class ConstructorTest {
    
    @Test
    public void test(){
        QCat.create(QCat.cat.name, QCat.cat.id).getJavaConstructor();
    }

}
