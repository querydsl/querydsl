package com.querydsl.sql.spatial.suites;

import org.junit.BeforeClass;

import com.querydsl.sql.*;
import com.querydsl.sql.spatial.GeoDBTemplates;
import com.querydsl.sql.spatial.SpatialBase;
import com.querydsl.sql.suites.AbstractSuite;

public class H2SuiteTest extends AbstractSuite {

    public static class Spatial extends SpatialBase {}

    @BeforeClass
    public static void setUp() throws Exception {
        Connections.initH2();
        Connections.initConfiguration(GeoDBTemplates.builder().newLineToSingleSpace().build());
    }

}
