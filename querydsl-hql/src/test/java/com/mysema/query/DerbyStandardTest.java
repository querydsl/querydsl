/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import org.junit.runner.RunWith;

import com.mysema.query.StandardTest.Target;


/**
 * @author tiwe
 *
 */
@RunWith(HibernateTestRunner.class)
@HibernateConfig("derby.properties")
public class DerbyStandardTest extends AbstractHibernateTest{
    
    @Override
    protected Target getTarget() {
        return Target.DERBY;
    }
    
}
