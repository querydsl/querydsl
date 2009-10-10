package com.mysema.query.collections.impl;

import org.junit.Test;

import com.mysema.query.domain.animal.QCat;


public class ConstructorTest {
    
    @Test
    public void test(){
        QCat.create(QCat.cat.name, QCat.cat.id).getJavaConstructor();
    }

}
