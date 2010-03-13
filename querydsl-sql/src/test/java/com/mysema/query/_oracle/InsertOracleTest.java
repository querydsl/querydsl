/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query._oracle;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import com.mysema.query.Connections;
import com.mysema.query.InsertBaseTest;
import com.mysema.query.Target;
import com.mysema.query.sql.OracleTemplates;
import com.mysema.testutil.FilteringTestRunner;
import com.mysema.testutil.Label;
import com.mysema.testutil.ResourceCheck;

@RunWith(FilteringTestRunner.class)
@ResourceCheck("/oracle.run")
@Label(Target.ORACLE)
public class InsertOracleTest extends InsertBaseTest{
    
    @BeforeClass
    public static void setUp() throws Exception {
        Connections.initOracle();
    }

    @Before
    public void setUpForTest() {
        dialect = new OracleTemplates().newLineToSingleSpace();
    }

}
