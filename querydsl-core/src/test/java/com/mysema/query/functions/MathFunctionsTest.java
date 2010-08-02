/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.functions;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.types.path.PNumber;

public class MathFunctionsTest {

    @Test
    public void test(){
        PNumber<Double> num = new PNumber<Double>(Double.class, "num");
        assertEquals("acos(num)",   MathFunctions.acos(num).toString());
        assertEquals("asin(num)",   MathFunctions.asin(num).toString());
        assertEquals("atan(num)",   MathFunctions.atan(num).toString());
        assertEquals("cos(num)",    MathFunctions.cos(num).toString());
        assertEquals("exp(num)",    MathFunctions.exp(num).toString());
        assertEquals("log(num)",    MathFunctions.log(num).toString());
        assertEquals("log10(num)",  MathFunctions.log10(num).toString());
        assertEquals("pow(num,num)",  MathFunctions.pow(num,num).toString());
        assertEquals("sin(num)",    MathFunctions.sin(num).toString());
        assertEquals("tan(num)",    MathFunctions.tan(num).toString());

    }

}
