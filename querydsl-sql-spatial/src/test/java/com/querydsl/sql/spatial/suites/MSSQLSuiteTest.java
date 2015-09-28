package com.querydsl.sql.spatial.suites;

import org.junit.BeforeClass;
import org.junit.experimental.categories.Category;

import com.querydsl.core.testutil.SQLServer;
import com.querydsl.sql.Connections;
import com.querydsl.sql.spatial.SQLServer2008SpatialTemplates;
import com.querydsl.sql.spatial.SpatialBase;
import com.querydsl.sql.suites.AbstractSuite;

@Category(SQLServer.class)
public class MSSQLSuiteTest extends AbstractSuite {

    public static class Spatial extends SpatialBase { }

    @BeforeClass
    public static void setUp() throws Exception {
        Connections.initSQLServer();
        Connections.initConfiguration(SQLServer2008SpatialTemplates.builder().newLineToSingleSpace().build());
    }

}
