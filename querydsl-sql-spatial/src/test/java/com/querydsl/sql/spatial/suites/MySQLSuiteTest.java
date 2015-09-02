package com.querydsl.sql.spatial.suites;

import org.junit.BeforeClass;
import org.junit.experimental.categories.Category;

import com.querydsl.core.testutil.ExternalDB;
import com.querydsl.sql.Connections;
import com.querydsl.sql.spatial.MySQLSpatialTemplates;
import com.querydsl.sql.spatial.SpatialBase;
import com.querydsl.sql.suites.AbstractSuite;

@Category(ExternalDB.class)
public class MySQLSuiteTest extends AbstractSuite {

    public static class Spatial extends SpatialBase { }

    @BeforeClass
    public static void setUp() throws Exception {
        Connections.initMySQL();
        Connections.initConfiguration(MySQLSpatialTemplates.builder().newLineToSingleSpace().build());
    }

}
