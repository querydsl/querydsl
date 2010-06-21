/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.hql;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.types.path.PNumber;

public class HQLSerializerTest {

    @Test
    public void testNormalizeNumericArgs() {
        HQLSerializer serializer = new HQLSerializer(HQLTemplates.DEFAULT);
        PNumber<Double> doublePath = new PNumber<Double>(Double.class, "doublePath");
        serializer.handle(doublePath.add(1));
        serializer.handle(doublePath.between((float)1.0, 1l));
        serializer.handle(doublePath.lt((byte)1));
        for (Object constant : serializer.getConstantToLabel().keySet()){
            assertEquals(Double.class, constant.getClass());
        }
    }

}
