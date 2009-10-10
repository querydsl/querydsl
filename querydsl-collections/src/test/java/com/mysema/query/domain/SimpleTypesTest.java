package com.mysema.query.domain;

import org.junit.Test;

public class SimpleTypesTest {
    
    @Test(expected=NoSuchFieldException.class)
    public void test() throws SecurityException, NoSuchFieldException {
        QSimpleTypes.class.getField("skipMe");
    }

}
