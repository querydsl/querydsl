/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query._mssql;

import org.junit.runner.RunWith;

import com.mysema.query.AbstractHibernateTest;
import com.mysema.query.Target;
import com.mysema.testutil.HibernateConfig;
import com.mysema.testutil.HibernateTestRunner;

/**
 * @author tiwe
 *
 */
@RunWith(HibernateTestRunner.class)
@HibernateConfig("mssql.properties")
public abstract class MSSQLStandardTest extends AbstractHibernateTest{
    
    @Override
    protected Target getTarget() {
        return Target.SQLSERVER;
    }
    
}
