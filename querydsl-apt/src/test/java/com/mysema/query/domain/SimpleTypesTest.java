package com.mysema.query.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QueryTransient;
import com.mysema.query.types.path.PComparable;
import com.mysema.query.types.path.PDateTime;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PSimple;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PTime;

public class SimpleTypesTest extends AbstractTest{
    
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
    public void testSimpleTypes() throws SecurityException, NoSuchFieldException{
        cl = QSimpleTypes.class;
        match(PNumber.class, "id");
        match(PNumber.class, "bigDecimal");
        match(PNumber.class, "bbyte");
        match(PNumber.class, "bbyte2");
        match(PNumber.class, "ddouble");
        match(PNumber.class, "ddouble2");
        match(PNumber.class, "ffloat");
        match(PNumber.class, "ffloat2");
        match(PNumber.class, "iint");
        match(PNumber.class, "iint2");        
        match(PNumber.class, "llong");
        match(PNumber.class, "llong2");
        
        match(PComparable.class, "cchar");
        match(PComparable.class, "cchar2");
        
        match(PString.class, "sstring");
        
        match(PDateTime.class, "date");
        match(PDateTime.class, "timestamp");
        
        match(PTime.class, "time");
        
        match(PSimple.class, "llocale");
        match(PSimple.class, "serializable");
        match(PSimple.class, "object");
        match(PSimple.class, "clazz");
        match(PSimple.class, "packageAsLiteral");
        
//        CustomLiteral customLiteral;
//        CustomComparableLiteral customComparableLiteral;        
//        CustomNumber customNumber;        
//        CustomComparableNumber customComparableNumber;
        
        match(PSimple.class, "clob");
        match(PSimple.class, "blob");
    }
        
    @Test
    public void customLiteral() throws SecurityException, NoSuchFieldException{
        cl = QSimpleTypes.class;
        match(PSimple.class, "customLiteral");
    }
    
    @Test
    public void customComparableLiteral() throws SecurityException, NoSuchFieldException{
        cl = QSimpleTypes.class;
        match(PComparable.class, "customComparableLiteral");
    }
    
    @Test
    public void customNumber() throws SecurityException, NoSuchFieldException{
        cl = QSimpleTypes.class;
        match(PSimple.class, "customNumber");
    }
    
    @Test
    public void customComparableNumber() throws SecurityException, NoSuchFieldException{
        cl = QSimpleTypes.class;
        match(PNumber.class, "customComparableNumber");
    }
    
    @Test(expected=NoSuchFieldException.class)
    public void skippedField1() throws SecurityException, NoSuchFieldException {
        QSimpleTypes.class.getField("skipMe");
    }
    
    @Test(expected=NoSuchFieldException.class)
    public void skippedField2() throws SecurityException, NoSuchFieldException {
        QSimpleTypes.class.getField("test");
    }

}
