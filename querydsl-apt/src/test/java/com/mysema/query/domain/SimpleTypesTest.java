package com.mysema.query.domain;

import static org.junit.Assert.assertEquals;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QueryTransient;
import com.mysema.query.types.path.PComparable;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PSimple;

public class SimpleTypesTest {
    
    public static class CustomLiteral {

    }
    
    @SuppressWarnings("serial")
    public static class CustomNumber extends Number{

        @Override
        public double doubleValue() {
            return 0;
        }

        @Override
        public float floatValue() {
            return 0;
        }

        @Override
        public int intValue() {
            return 0;
        }

        @Override
        public long longValue() {
            return 0;
        }
        
    }
    
    @SuppressWarnings("serial")
    public static class CustomComparableNumber extends CustomNumber implements Comparable<CustomComparableNumber>{

        @Override
        public int compareTo(CustomComparableNumber o) {
            return 0;
        }
        
    }
    
    public static class CustomComparableLiteral implements Comparable<CustomComparableLiteral> {

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
        CustomLiteral customLiteral;
        CustomComparableLiteral customComparableLiteral;        
        CustomNumber customNumber;        
        CustomComparableNumber customComparableNumber;
        java.sql.Clob clob;
        java.sql.Blob blob;
        @QueryTransient
        String skipMe;
    }
        
    @Test
    public void customLiteral() throws SecurityException, NoSuchFieldException{
        assertEquals(PSimple.class, QSimpleTypes.class.getField("customLiteral").getType());
    }
    
    @Test
    public void customComparableLiteral() throws SecurityException, NoSuchFieldException{
        assertEquals(PComparable.class, QSimpleTypes.class.getField("customComparableLiteral").getType());
    }
    
    @Test
    public void customNumber() throws SecurityException, NoSuchFieldException{
        assertEquals(PSimple.class, QSimpleTypes.class.getField("customNumber").getType());
    }
    
    @Test
    public void customComparableNumber() throws SecurityException, NoSuchFieldException{
        assertEquals(PNumber.class, QSimpleTypes.class.getField("customComparableNumber").getType());
    }
    
    @Test(expected=NoSuchFieldException.class)
    public void skippedFields() throws SecurityException, NoSuchFieldException {
        QSimpleTypes.class.getField("skipMe");
    }

}
