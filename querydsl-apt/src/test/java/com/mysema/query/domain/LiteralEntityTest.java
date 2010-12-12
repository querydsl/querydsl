/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.types.path.EnumPath;

public class LiteralEntityTest {

    @QueryEntity
    enum EnumEntity{

    }

    @Test
    public void test(){
        assertNotNull(QLiteralEntityTest_EnumEntity.enumEntity);
        assertEquals(EnumPath.class, QLiteralEntityTest_EnumEntity.class.getSuperclass());
    }

}
