package com.querydsl.sql.spatial.suites;

import org.junit.BeforeClass;
import org.junit.experimental.categories.Category;

import com.querydsl.core.testutil.ExternalDB;
import com.querydsl.sql.Connections;
import com.querydsl.sql.spatial.PostGISTemplates;
import com.querydsl.sql.spatial.SpatialBase;
import com.querydsl.sql.suites.AbstractSuite;

@Category(ExternalDB.class)
public class PostgreSQLLiteralsSuiteTest extends AbstractSuite {

    public static class Spatial extends SpatialBase { }

    @BeforeClass
    public static void setUp() throws Exception {
        Connections.initPostgreSQL();
        Connections.initConfiguration(PostGISTemplates.builder().quote().newLineToSingleSpace().build());
        Connections.getConfiguration().setUseLiterals(true);
    }

}
