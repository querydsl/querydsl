/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.oracle;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import com.mysema.query.Connections;
import com.mysema.query.Target;
import com.mysema.query.UpdateBaseTest;
import com.mysema.query.sql.OracleTemplates;
import com.mysema.testutil.FilteringTestRunner;
import com.mysema.testutil.Label;
import com.mysema.testutil.ResourceCheck;

@RunWith(FilteringTestRunner.class)
@ResourceCheck("/oracle.run")
@Label(Target.ORACLE)
public class UpdateOracleTest extends UpdateBaseTest{
    
    @BeforeClass
    public static void setUp() throws Exception {
        Connections.initOracle();
    }

    @Before
    public void setUpForTest() {
        dialect = new OracleTemplates().newLineToSingleSpace();
    }

}
