package com.mysema.query.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QueryTransient;

public class SimpleTypesTest {
    
    public static class CustomLiteral {

    }
    
    public class CustomComparableLiteral implements Comparable<CustomComparableLiteral> {

        @Override
        public int compareTo(CustomComparableLiteral o) {
            return 0;
        }

    }
    
    @QueryEntity
    public static class SimpleTypes {

        transient int test;

        long id;

        BigDecimal bigDecimal;

        Byte bbyte;

        byte bbyte2;

        Character cchar;

        char cchar2;

        Double ddouble;

        double ddouble2;

        Float ffloat;

        float ffloat2;

        Integer iint;

        int iint2;

        Locale llocale;

        Long llong;

        long llong2;

        String sstring;

        Date date;

        java.sql.Time time;

        java.sql.Timestamp timestamp;

        Serializable serializable;

        Object object;

        Class<?> clazz;

        Package packageAsLiteral;

        CustomLiteral literal;

        CustomComparableLiteral literal2;

        java.sql.Clob clob;

        java.sql.Blob blob;

        @QueryTransient
        String skipMe;
    }
    
    @Test(expected=NoSuchFieldException.class)
    public void test() throws SecurityException, NoSuchFieldException {
        QSimpleTypes.class.getField("skipMe");
    }

}
