package com.querydsl.jpa.suites;

import org.junit.BeforeClass;

import com.querydsl.core.Target;
import com.querydsl.jpa.*;

public class H2SuiteTest extends AbstractSuite {

    public static class JPA extends JPABase { }
    public static class JPASQL extends JPASQLBase { }
    public static class JPAIntegration extends JPAIntegrationBase { }
    public static class Serialization extends SerializationBase { }
    public static class Hibernate extends HibernateBase { }
    public static class HibernateSQL extends HibernateSQLBase { }

    @BeforeClass
    public static void setUp() throws Exception {
        Mode.mode.set("h2");
        Mode.target.set(Target.H2);
    }

}
