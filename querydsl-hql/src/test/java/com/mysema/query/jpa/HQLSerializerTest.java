/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.jpa.HQLSerializer;
import com.mysema.query.jpa.HQLTemplates;
import com.mysema.query.types.path.NumberPath;

public class HQLSerializerTest {

    @Test
    public void testNormalizeNumericArgs() {
        HQLSerializer serializer = new HQLSerializer(HQLTemplates.DEFAULT);
        NumberPath<Double> doublePath = new NumberPath<Double>(Double.class, "doublePath");
        serializer.handle(doublePath.add(1));
        serializer.handle(doublePath.between((float)1.0, 1l));
        serializer.handle(doublePath.lt((byte)1));
        for (Object constant : serializer.getConstantToLabel().keySet()){
            assertEquals(Double.class, constant.getClass());
        }
    }

}
