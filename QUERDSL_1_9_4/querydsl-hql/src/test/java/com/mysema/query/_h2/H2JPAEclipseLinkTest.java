/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query._h2;

import org.junit.Ignore;
import org.junit.runner.RunWith;

import com.mysema.query.AbstractJPATest;
import com.mysema.query.Target;
import com.mysema.query.hql.EclipseLinkTemplates;
import com.mysema.query.hql.JPQLTemplates;
import com.mysema.testutil.JPAConfig;
import com.mysema.testutil.JPATestRunner;

/**
 * @author tiwe
 *
 */
@Ignore
@RunWith(JPATestRunner.class)
@JPAConfig("h2-eclipselink")
public class H2JPAEclipseLinkTest extends AbstractJPATest{

    protected JPQLTemplates getTemplates(){
        return EclipseLinkTemplates.DEFAULT;
    }

    @Override
    protected Target getTarget() {
        return Target.H2;
    }

}
