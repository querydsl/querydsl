package com.querydsl.jpa.suites;

import org.junit.BeforeClass;
import org.junit.experimental.categories.Category;

import com.querydsl.core.Target;
import com.querydsl.core.testutil.Oracle;
import com.querydsl.jpa.*;

import static org.junit.Assume.assumeTrue;

@Category(Oracle.class)
public class OracleSuiteTest extends AbstractSuite {

    public static class JPA extends JPABase { }
    public static class JPASQL extends JPASQLBase { }
    public static class JPAIntegration extends JPAIntegrationBase { }
    public static class Serialization extends SerializationBase { }
    public static class Hibernate extends HibernateBase { }
    public static class HibernateSQL extends HibernateSQLBase { }

    @BeforeClass
    public static void setUp() throws Exception {
        assumeTrue("OracleSuiteTest is disabled (Maven Profile 'enable-jpa-oracle-test' is not enabled)",
                "true".equalsIgnoreCase(System.getProperty("querydsl-enable-jpa-oracle-test", "false")));
        Mode.mode.set("oracle");
        Mode.target.set(Target.ORACLE);
    }

}
