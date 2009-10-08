/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import org.junit.runner.RunWith;

import com.mysema.query.StandardTest.Target;

@RunWith(HibernateTestRunner.class)
@HibernateConfig("mysql.properties")
public abstract class MySQLJPAStandardTest extends AbstractHibernateTest{

    @Override
    protected Target getTarget() {
        return Target.MYSQL;
    }

}
