/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query._hsqldb;

import org.junit.runner.RunWith;

import com.mysema.query.AbstractJPATest;
import com.mysema.query.Target;
import com.mysema.testutil.JPAConfig;
import com.mysema.testutil.JPATestRunner;

/**
 * @author tiwe
 *
 */
@RunWith(JPATestRunner.class)
@JPAConfig("hsqldb")
public class HSQLDBJPAStandardTest extends AbstractJPATest{

    @Override
    protected Target getTarget() {
        return Target.HSQLDB;
    }

}
