package com.mysema.query._mysql;

import org.junit.BeforeClass;

import com.mysema.query.Connections;
import com.mysema.query.ExportBaseTest;
import com.mysema.testutil.ResourceCheck;

@ResourceCheck("/mysql.run")
public class ExportMySQLTest extends ExportBaseTest{

    @BeforeClass
    public static void setUpClass() throws Exception {
        Connections.initMySQL();
    }

}
