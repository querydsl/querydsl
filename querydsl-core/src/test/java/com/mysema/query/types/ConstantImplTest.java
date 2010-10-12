package com.mysema.query.types;

import static org.junit.Assert.*;

import org.junit.Test;

public class ConstantImplTest {
    
    @Test
    public void Create(){
        assertNotNull(ConstantImpl.create(true));
        assertNotNull(ConstantImpl.create((byte)1));
        assertNotNull(ConstantImpl.create(ConstantImplTest.class));
        assertNotNull(ConstantImpl.create(1));
        assertNotNull(ConstantImpl.create(1l));
        assertNotNull(ConstantImpl.create((short)1));
        assertNotNull(ConstantImpl.create("x"));
        assertNotNull(ConstantImpl.create("x",true));
    }

}
