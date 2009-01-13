/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql;

import org.junit.Test;

import com.mysema.query.grammar.SqlOps;
import com.mysema.query.sql.domain.QSURVEY;
import com.mysema.query.sql.domain.QSYSTEM_ALLTYPEINFO;


/**
 * SqlQueryTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public class SqlQueryTest {
    
    private QSURVEY survey = new QSURVEY("survey");
    private QSURVEY survey2 = new QSURVEY("survey2");
    private QSYSTEM_ALLTYPEINFO allTypeInfo = new QSYSTEM_ALLTYPEINFO("systemAllTypeInfo");
    
    @Test
    public void testQuery(){               
        SqlQuery query = new SqlQuery(new SqlOps());
        query.from(survey, allTypeInfo).where(survey.id.eq(12)).list(survey.id);
        System.out.println(query);
        System.out.println();
                
        query = new SqlQuery(new SqlOps().toUpperCase());
        query.from(survey, allTypeInfo).where(survey.id.eq(12)).list(survey.id);
        System.out.println(query);
    }

}
