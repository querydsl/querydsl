/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.domain;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;

public class AnnotatedGettersTest {

    @QueryEntity
    public interface Entity{
        String getName();
    }

    @Test
    public void Annotated_Getter_Is_Supported(){
        assertNotNull(QAnnotatedGettersTest_Entity.entity.name);
    }

}
