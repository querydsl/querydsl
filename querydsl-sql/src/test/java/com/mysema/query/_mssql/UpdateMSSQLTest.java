/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query._mssql;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import com.mysema.query.Connections;
import com.mysema.query.Target;
import com.mysema.query.UpdateBaseTest;
import com.mysema.query.sql.SQLServerTemplates;
import com.mysema.testutil.FilteringTestRunner;
import com.mysema.testutil.Label;
import com.mysema.testutil.ResourceCheck;

@RunWith(FilteringTestRunner.class)
@ResourceCheck("/sqlserver.run")
@Label(Target.SQLSERVER)
public class UpdateMSSQLTest extends UpdateBaseTest{
    
    @BeforeClass
    public static void setUp() throws Exception {
        Connections.initSQLServer();
    }

    @Before
    public void setUpForTest() {
        dialect = new SQLServerTemplates().newLineToSingleSpace();
    }

}
