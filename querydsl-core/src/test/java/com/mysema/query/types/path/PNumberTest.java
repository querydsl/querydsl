/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.path;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.mysema.query.types.Constant;
import com.mysema.query.types.Operation;

public class PNumberTest {

    private PNumber<Byte> bytePath = new PNumber<Byte>(Byte.class, "bytePath");

    @SuppressWarnings("unchecked")
    @Test
    public void bytePath_in(){
        Operation<?> operation = (Operation<?>) bytePath.in(1, 2, 3);

        List<Byte> numbers = (List<Byte>) ((Constant)operation.getArg(1)).getConstant();
        assertEquals(Byte.valueOf((byte)1), numbers.get(0));
        assertEquals(Byte.valueOf((byte)2), numbers.get(1));
        assertEquals(Byte.valueOf((byte)3), numbers.get(2));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void bytePath_notIn(){
        Operation<?> operation = (Operation<?>) bytePath.notIn(1, 2, 3);
        // unwrap negation
        operation = (Operation<?>) operation.getArg(0);

        List<Byte> numbers = (List<Byte>) ((Constant)operation.getArg(1)).getConstant();
        assertEquals(Byte.valueOf((byte)1), numbers.get(0));
        assertEquals(Byte.valueOf((byte)2), numbers.get(1));
        assertEquals(Byte.valueOf((byte)3), numbers.get(2));
    }
}
