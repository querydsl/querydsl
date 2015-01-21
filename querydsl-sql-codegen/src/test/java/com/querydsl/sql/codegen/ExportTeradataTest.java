package com.querydsl.sql.codegen;

import org.junit.BeforeClass;
import org.junit.experimental.categories.Category;

import com.querydsl.core.testutil.ExternalDB;
import com.querydsl.sql.Connections;

//@Ignore
@Category(ExternalDB.class)
public class ExportTeradataTest extends ExportBaseTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        Connections.initTeradata();
    }

}
