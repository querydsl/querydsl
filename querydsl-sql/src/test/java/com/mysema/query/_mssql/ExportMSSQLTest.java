/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query._mssql;

import org.junit.BeforeClass;

import com.mysema.query.Connections;
import com.mysema.query.ExportBaseTest;
import com.mysema.testutil.ResourceCheck;

@ResourceCheck("/sqlserver.run")
public class ExportMSSQLTest extends ExportBaseTest{

    @BeforeClass
    public static void setUpClass() throws Exception {
        Connections.initSQLServer();
    }

}
