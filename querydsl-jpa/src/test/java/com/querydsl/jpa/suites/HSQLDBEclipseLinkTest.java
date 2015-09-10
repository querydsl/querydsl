package com.querydsl.jpa.suites;

import org.junit.BeforeClass;
import org.junit.experimental.categories.Category;

import com.querydsl.core.Target;
import com.querydsl.core.testutil.HSQLDB;
import com.querydsl.jpa.*;

@Category(HSQLDB.class)
public class HSQLDBEclipseLinkTest extends AbstractJPASuite {

    public static class JPA extends JPABase { }
    public static class JPASQL extends JPASQLBase { }
    public static class JPAIntegration extends JPAIntegrationBase { }
    public static class Serialization extends SerializationBase { }

    @BeforeClass
    public static void setUp() throws Exception {
        Mode.mode.set("hsqldb-eclipselink");
        Mode.target.set(Target.HSQLDB);
    }

}
