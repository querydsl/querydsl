/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query._mssql;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.BeforeClass;

import com.mysema.query.Connections;
import com.mysema.query.InsertBaseTest;
import com.mysema.query.Target;
import com.mysema.query.sql.SQLServerTemplates;
import com.mysema.testutil.Label;
import com.mysema.testutil.ResourceCheck;

@ResourceCheck("/sqlserver.run")
@Label(Target.SQLSERVER)
public class InsertMSSQLTest extends InsertBaseTest{

    @BeforeClass
    public static void setUpClass() throws Exception {
        Connections.initSQLServer();
    }

    @Before
    public void setUp() throws SQLException {
        dialect = new SQLServerTemplates().newLineToSingleSpace();
        super.setUp();
    }

}
