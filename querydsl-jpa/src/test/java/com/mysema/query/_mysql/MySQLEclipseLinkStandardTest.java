/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query._mysql;

import org.junit.runner.RunWith;

import com.mysema.query.AbstractHibernateTest;
import com.mysema.query.Target;
import com.mysema.testutil.HibernateConfig;
import com.mysema.testutil.HibernateTestRunner;

@RunWith(HibernateTestRunner.class)
@HibernateConfig("mysql.properties")
public abstract class MySQLEclipseLinkStandardTest extends AbstractHibernateTest{

    @Override
    protected Target getTarget() {
        return Target.MYSQL;
    }

}
