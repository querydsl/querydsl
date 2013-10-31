package com.mysema.query.suites;

import org.junit.BeforeClass;

import com.mysema.query.HibernateBase;
import com.mysema.query.HibernateSQLBase;
import com.mysema.query.JPABase;
import com.mysema.query.JPASQLBase;
import com.mysema.query.Mode;
import com.mysema.query.Target;
import com.mysema.query.jpa.JPAIntegrationBase;
import com.mysema.query.jpa.SerializationBase;

public class DerbySuiteTest extends AbstractSuite {

    public static class JPA extends JPABase {}
    public static class JPASQL extends JPASQLBase {}
    public static class JPAIntegration extends JPAIntegrationBase {}
    public static class Serialization extends SerializationBase {}
    public static class Hibernate extends HibernateBase {}
    public static class HibernateSQL extends HibernateSQLBase {}

    @BeforeClass
    public static void setUp() throws Exception {
        Mode.mode.set("derby");
        Mode.target.set(Target.DERBY);
    }

}
