package com.querydsl.jpa.suites;

import org.junit.BeforeClass;
import org.junit.experimental.categories.Category;

import com.querydsl.jpa.JPABase;
import com.querydsl.jpa.JPASQLBase;
import com.querydsl.jpa.Mode;
import com.querydsl.core.Target;
import com.querydsl.jpa.JPAIntegrationBase;
import com.querydsl.jpa.SerializationBase;
import com.querydsl.core.testutil.ExternalDB;

@Category(ExternalDB.class)
public class MySQLEclipseLinkTest extends AbstractJPASuite {

    public static class JPA extends JPABase {
        @Override
        public void Cast() {
            // not supported in MySQL/EclipseLink
        }
        @Override
        public void Enum_StartsWith() {
            // not supported in MySQL/EclipseLink
        }
        @Override
        public void Order_StringValue() {
            // not supported in MySQL/EclipseLink
        }
        @Override
        public void Order_StringValue_To_Integer() {
            // not supported in MySQL/EclipseLink
        }
        @Override
        public void Order_StringValue_ToLong() {
            // not supported in MySQL/EclipseLink
        }
        @Override
        public void Order_StringValue_ToBigInteger() {
            // not supported in MySQL/EclipseLink
        }
        @Override
        public void Order_NullsFirst() {
            // not supported in MySQL/EclipseLink
        }
        @Override
        public void Order_NullsLast() {
            // not supported in MySQL/EclipseLink
        }
    }

    public static class JPASQL extends JPASQLBase {}
    public static class JPAIntegration extends JPAIntegrationBase {}
    public static class Serialization extends SerializationBase {}

    @BeforeClass
    public static void setUp() throws Exception {
        Mode.mode.set("mysql-eclipselink");
        Mode.target.set(Target.MYSQL);
    }

}
