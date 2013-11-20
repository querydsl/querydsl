package com.mysema.query;

import org.junit.BeforeClass;

public class ExportSQLiteTest extends ExportBaseTest{

    @BeforeClass
    public static void setUpClass() throws Exception {
        Connections.initSQLite();
    }

}
