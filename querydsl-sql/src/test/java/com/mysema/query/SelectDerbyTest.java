/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import com.mysema.query.sql.DerbyTemplates;
import com.mysema.testutil.FilteringTestRunner;
import com.mysema.testutil.Label;

@RunWith(FilteringTestRunner.class)
@Label(Target.DERBY)
public class SelectDerbyTest extends SelectBaseTest {
    
    @BeforeClass
    public static void setUp() throws Exception {
        Connections.initDerby();
    }

    @Before
    public void setUpForTest() {
        dialect = new DerbyTemplates().newLineToSingleSpace();
    }

}
