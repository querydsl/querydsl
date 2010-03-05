/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.domain;

import javax.persistence.Entity;
import javax.persistence.Transient;

import org.junit.Test;

import com.mysema.query.domain.JDOTest.JDOEntity;
import com.mysema.query.types.path.PString;

public class JPATest extends AbstractTest{
    
    @Entity
    public static class JPAEntity{
        
        String prop;
        
        @Transient
        String skipped;
        
        @Transient
        JDOEntity skippedEntity;
    }

    @Test
    public void test() throws SecurityException, NoSuchFieldException{
        cl = QJPATest_JPAEntity.class;
        match(PString.class, "prop");
        assertMissing("skipped");
        assertMissing("skippedEntity");
    }
}
