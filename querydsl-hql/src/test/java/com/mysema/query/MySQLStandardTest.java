/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import org.junit.runner.RunWith;

import com.mysema.testutil.HibernateConfig;
import com.mysema.testutil.HibernateTestRunner;


@RunWith(HibernateTestRunner.class)
@HibernateConfig("mysql.properties")
public abstract class MySQLStandardTest extends AbstractHibernateTest{

    @Override
    protected Target getTarget() {
        return Target.MYSQL;
    }

}
