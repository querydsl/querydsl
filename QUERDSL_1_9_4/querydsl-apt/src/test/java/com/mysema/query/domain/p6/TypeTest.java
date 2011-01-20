package com.mysema.query.domain.p6;

import static org.junit.Assert.*;

import org.junit.Test;

public class TypeTest {

    @Test
    public void test(){
        QType1 type1 = QType1.type1;
        QType2 type2 = QType2.type2;
        assertEquals(type2.getType(), type1.property.getType());
        assertEquals(type2.getClass(), type1.property.getClass());
    }
}
