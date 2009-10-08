/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import org.junit.runner.RunWith;



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
