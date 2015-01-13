package com.querydsl.sql.codegen;

import org.junit.BeforeClass;

import com.querydsl.sql.Connections;

public class ExportSQLiteTest extends ExportBaseTest{

    @BeforeClass
    public static void setUpClass() throws Exception {
        Connections.initSQLite();
    }

}
