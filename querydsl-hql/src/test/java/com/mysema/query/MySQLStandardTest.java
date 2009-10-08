/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import org.junit.runner.RunWith;


@RunWith(HibernateTestRunner.class)
@HibernateConfig("mysql.properties")
public abstract class MySQLStandardTest extends AbstractHibernateTest{

    @Override
    protected Target getTarget() {
        return Target.MYSQL;
    }

}
