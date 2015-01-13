package com.querydsl.jpa.suites;

import org.junit.BeforeClass;

import com.querydsl.jpa.HibernateBase;
import com.querydsl.jpa.HibernateSQLBase;
import com.querydsl.jpa.JPABase;
import com.querydsl.jpa.JPASQLBase;
import com.querydsl.jpa.Mode;
import com.querydsl.core.Target;
import com.querydsl.jpa.JPAIntegrationBase;
import com.querydsl.jpa.SerializationBase;

public class HSQLDBSuiteTest extends AbstractSuite {

    public static class JPA extends JPABase {}
    public static class JPASQL extends JPASQLBase {}
    public static class JPAIntegration extends JPAIntegrationBase {}
    public static class Serialization extends SerializationBase {}
    public static class Hibernate extends HibernateBase {}
    public static class HibernateSQL extends HibernateSQLBase {}

    @BeforeClass
    public static void setUp() throws Exception {
        Mode.mode.set("hsqldb");
        Mode.target.set(Target.HSQLDB);
    }

}
