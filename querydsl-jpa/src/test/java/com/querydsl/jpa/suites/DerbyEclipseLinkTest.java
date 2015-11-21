package com.querydsl.jpa.suites;

import org.junit.BeforeClass;
import org.junit.experimental.categories.Category;

import com.querydsl.core.Target;
import com.querydsl.core.testutil.Derby;
import com.querydsl.jpa.*;

@Category(Derby.class)
public class DerbyEclipseLinkTest extends AbstractJPASuite {

    public static class JPA extends JPABase {
        @Override
        public void order_stringValue() {
            // not supported in MySQL/EclipseLink
        }
        @Override
        public void order_stringValue_to_integer() {
            // not supported in MySQL/EclipseLink
        }
        @Override
        public void order_stringValue_toLong() {
            // not supported in MySQL/EclipseLink
        }
        @Override
        public void order_stringValue_toBigInteger() {
            // not supported in MySQL/EclipseLink
        }
    }
    public static class JPASQL extends JPASQLBase { }
    public static class JPAIntegration extends JPAIntegrationBase { }
    public static class Serialization extends SerializationBase { }

    @BeforeClass
    public static void setUp() throws Exception {
        Mode.mode.set("derby-eclipselink");
        Mode.target.set(Target.DERBY);
    }
}
