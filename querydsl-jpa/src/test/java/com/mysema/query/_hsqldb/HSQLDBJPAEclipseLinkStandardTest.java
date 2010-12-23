/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query._hsqldb;

import org.junit.runner.RunWith;

import com.mysema.query.AbstractJPATest;
import com.mysema.query.Target;
import com.mysema.query.jpa.EclipseLinkTemplates;
import com.mysema.query.jpa.JPQLTemplates;
import com.mysema.testutil.JPAConfig;
import com.mysema.testutil.JPATestRunner;

/**
 * @author tiwe
 *
 */
@RunWith(JPATestRunner.class)
@JPAConfig("hsqldb-eclipselink")
public abstract class HSQLDBJPAEclipseLinkStandardTest extends AbstractJPATest{

    @Override
    protected JPQLTemplates getTemplates(){
        return EclipseLinkTemplates.DEFAULT;
    }

    @Override
    protected Target getTarget() {
        return Target.HSQLDB;
    }

}
