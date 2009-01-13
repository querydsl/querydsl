package com.mysema.query.sql.domain;

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
        QSYSTEM_ALLTYPEINFO allTypeInfo = new QSYSTEM_ALLTYPEINFO("systemAllTypeInfo");
        
        survey.id.eq(survey2.id);
        
        allTypeInfo.asProcCol.eq(true);
        allTypeInfo.createParams.lower().eq("ES");
    }

}
