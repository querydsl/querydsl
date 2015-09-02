package com.querydsl.jpa.suites;

import org.junit.BeforeClass;
import org.junit.experimental.categories.Category;

import com.querydsl.core.Target;
import com.querydsl.core.testutil.ExternalDB;
import com.querydsl.jpa.*;

@Category(ExternalDB.class)
public class PostgreSQLEclipseLinkSuiteTest extends AbstractJPASuite {

    public static class JPA extends JPABase { }
    public static class JPASQL extends JPASQLBase { }
    public static class JPAIntegration extends JPAIntegrationBase { }
    public static class Serialization extends SerializationBase { }

    @BeforeClass
    public static void setUp() throws Exception {
        Mode.mode.set("postgresql-eclipselink");
        Mode.target.set(Target.POSTGRESQL);
    }

}
