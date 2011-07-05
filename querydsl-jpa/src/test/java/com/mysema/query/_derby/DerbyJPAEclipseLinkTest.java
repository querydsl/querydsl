/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query._derby;

import org.junit.Ignore;
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
@Ignore
@RunWith(JPATestRunner.class)
@JPAConfig("derby-eclipselink")
public class DerbyJPAEclipseLinkTest extends AbstractJPATest{

    @Override
    protected JPQLTemplates getTemplates(){
        return EclipseLinkTemplates.DEFAULT;
    }

    @Override
    protected Target getTarget() {
        return Target.DERBY;
    }

    @Override
    public void test(){
        // FIXME
    }
    
    @Override
    public void TupleProjection(){
        // FIXME : custom projections don't work
    }

    @Override
    public void ArrayProjection(){
        // FIXME : custom projections don't work
    }
    
}
