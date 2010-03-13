/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query._mysql;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import com.mysema.query.Connections;
import com.mysema.query.DeleteBaseTest;
import com.mysema.query.Target;
import com.mysema.query.sql.MySQLTemplates;
import com.mysema.testutil.FilteringTestRunner;
import com.mysema.testutil.Label;
import com.mysema.testutil.ResourceCheck;

@RunWith(FilteringTestRunner.class)
@ResourceCheck("/mysql.run")
@Label(Target.MYSQL)
public class DeleteMySQLTest extends DeleteBaseTest{
    
    @BeforeClass
    public static void setUp() throws Exception {
        Connections.initMySQL();
    }

    @Before
    public void setUpForTest() {
        dialect = new MySQLTemplates().newLineToSingleSpace();
    }

}
