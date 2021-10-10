package com.querydsl.jpa.suites;

import org.junit.BeforeClass;
import org.junit.experimental.categories.Category;

import com.querydsl.core.Target;
import com.querydsl.core.testutil.MySQL;
import com.querydsl.jpa.*;

import static org.junit.Assume.assumeTrue;

@Category(MySQL.class)
public class MySQLSuiteTest extends AbstractSuite {

    public static class JPA extends JPABase {
        @Override
        public void order_stringValue_toLong() {
            // not supported
        }
    }
    public static class JPASQL extends JPASQLBase { }
    public static class JPAIntegration extends JPAIntegrationBase { }
    public static class Serialization extends SerializationBase { }
    public static class Hibernate extends HibernateBase {
        @Override
        public void order_stringValue_toLong() {
            // not supported
        }
    }
    public static class HibernateSQL extends HibernateSQLBase { }

    @BeforeClass
    public static void setUp() throws Exception {
        assumeTrue("MySQLSuiteTest is disabled (Maven Profile 'enable-jpa-mysql-test' is not enabled)",
                "true".equalsIgnoreCase(System.getProperty("querydsl-enable-jpa-mysql-test", "false")));
        Mode.mode.set("mysql");
        Mode.target.set(Target.MYSQL);
    }

}
