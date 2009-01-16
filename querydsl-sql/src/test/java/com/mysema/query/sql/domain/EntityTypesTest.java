/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


/**
 * EntityTypesTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public class EntityTypesTest {
    
    @Test
    public void testUsage(){
        QSURVEY survey = new QSURVEY("survey");
        QSURVEY survey2 = new QSURVEY("survey2");
        
        survey.id.eq(survey2.id);        
        assertEquals("survey", survey.getEntityName());
    }

}
