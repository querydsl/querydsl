/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query._hsqldb;

import org.junit.runner.RunWith;

import com.mysema.query.AbstractJPATest;
import com.mysema.query.EclipseLinkTemplates;
import com.mysema.query.Target;
import com.mysema.query.hql.HQLTemplates;
import com.mysema.testutil.JPAConfig;
import com.mysema.testutil.JPATestRunner;



/**
 * @author tiwe
 *
 */
@RunWith(JPATestRunner.class)
@JPAConfig("hsqldb-eclipselink")
public abstract class HSQLDBJPAEclipseLinkStandardTest extends AbstractJPATest{
    
    protected HQLTemplates getTemplates(){
        return EclipseLinkTemplates.DEFAULT;
    }
    
    @Override
    protected Target getTarget() {
        return Target.HSQLDB;
    }
    
    
}
