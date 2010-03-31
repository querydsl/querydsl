/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query._mysql;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import com.mysema.query.Connections;
import com.mysema.query.Target;
import com.mysema.query.UpdateBaseTest;
import com.mysema.query.sql.MySQLTemplates;
import com.mysema.testutil.FilteringTestRunner;
import com.mysema.testutil.Label;
import com.mysema.testutil.ResourceCheck;

@RunWith(FilteringTestRunner.class)
@ResourceCheck("/mysql.run")
@Label(Target.MYSQL)
public class UpdateMySQLTest extends UpdateBaseTest{
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        Connections.initMySQL();
    }

    @Before
    public void setUp() throws SQLException {
        dialect = new MySQLTemplates().newLineToSingleSpace();
        super.setUp();
    }

}
