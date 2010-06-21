/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QueryTransient;
import com.mysema.query.annotations.QuerydslConfig;
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

    @SuppressWarnings("all")
    public static class CustomComparableNumber extends CustomNumber implements Comparable<CustomComparableNumber>{

        @Override
        public int compareTo(CustomComparableNumber o) {
            return 0;
        }

        @Override
        public int hashCode(){
            return super.hashCode();
        }

        @Override
        public boolean equals(Object o){
            return o instanceof CustomComparableNumber;
        }
    }

    @SuppressWarnings("all")
    public static class CustomComparableLiteral implements Comparable<CustomComparableLiteral> {

        @Override
        public int compareTo(CustomComparableLiteral o) {
            return 0;
        }

        @Override
        public int hashCode(){
            return super.hashCode();
        }

        @Override
        public boolean equals(Object o){
            return o instanceof CustomComparableLiteral;
        }
    }

    @SuppressWarnings("all")
    public static class CustomGenericComparableLiteral<A> implements Comparable<CustomComparableLiteral> {

        @Override
        public int compareTo(CustomComparableLiteral o) {
            return 0;
        }

        @Override
        public int hashCode(){
            return super.hashCode();
        }

        @Override
        public boolean equals(Object o){
            return o instanceof CustomGenericComparableLiteral;
        }
    }

    @QueryEntity
    @QuerydslConfig(listAccessors=true)
    public static class SimpleTypes {
        transient int test;
        List<Integer> testList;

        Calendar calendar;
        List<Calendar> calendarList;

        long id;
        List<Long> idList;

        BigDecimal bigDecimal;
        List<BigDecimal> bigDecimalList;

        Byte bbyte;
        List<Byte> bbyteList;

        byte bbyte2;

        Character cchar;
        List<Character> ccharList;

        char cchar2;

        Double ddouble;
        List<Double> ddoubleList;

        double ddouble2;

        Float ffloat;
        List<Float> ffloatList;

        float ffloat2;

        Integer iint;
        List<Integer> iintList;

        int iint2;

        Locale llocale;
        List<Locale> llocaleList;

        Long llong;
        List<Long> llongList;

        long llong2;

        BigInteger bigInteger;

        String sstring;
        List<String> sstringList;

        Date date;
        List<Date> dateList;

        java.sql.Time time;
        List<java.sql.Time> timeList;

        java.sql.Timestamp timestamp;
        List<java.sql.Timestamp> timestampList;

        Serializable serializable;
        List<Serializable> serializableList;

        Object object;
        List<Object> objectList;

        Class<?> clazz;
        List<Class> classList2;
        List<Class<?>> classList3;
        List<Class<Package>> classList4;
        List<Class<? extends Date>> classList5;

        Package packageAsLiteral;
        List<Package> packageAsLiteralList;

        CustomLiteral customLiteral;
        List<CustomLiteral> customLiteralList;

        CustomComparableLiteral customComparableLiteral;
        List<CustomComparableLiteral> customComparableLiteralList;

        CustomNumber customNumber;
        List<CustomNumber> customNumberList;

        CustomComparableNumber customComparableNumber;
        List<CustomComparableNumber> customComparableNumber2;

        CustomGenericComparableLiteral customComparableLiteral2;
        List<CustomGenericComparableLiteral> customComparableLiteral2List;

        CustomGenericComparableLiteral<Number> customComparableLiteral3;
        List<CustomGenericComparableLiteral<Number>> customComparableLiteral3List;

        java.sql.Clob clob;
        List<java.sql.Clob> clobList;

        java.sql.Blob blob;
        List<java.sql.Blob> blobList;

        @QueryTransient
        String skipMe;
    }

    @Test
    public void listAccess(){
        // date / time
        QSimpleTypesTest_SimpleTypes.simpleTypes.dateList.get(0).after(new Date());
        QSimpleTypesTest_SimpleTypes.simpleTypes.timeList.get(0).after(new Time(0l));
        QSimpleTypesTest_SimpleTypes.simpleTypes.calendarList.get(0).before(Calendar.getInstance());

        // numeric
        QSimpleTypesTest_SimpleTypes.simpleTypes.bbyteList.get(0).abs();

        // string
        QSimpleTypesTest_SimpleTypes.simpleTypes.sstringList.get(0).toLowerCase();

        // boolean
//        QSimpleTypes.simpleTypes.b

    }

    @Test
    public void testSimpleTypes() throws SecurityException, NoSuchFieldException{
        cl = QSimpleTypesTest_SimpleTypes.class;
        match(PNumber.class, "id");
        match(PNumber.class, "bigDecimal");
        match(PNumber.class, "bigInteger");
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
        match(PDateTime.class, "calendar");
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
        cl = QSimpleTypesTest_SimpleTypes.class;
        match(PSimple.class, "customLiteral");
    }

    @Test
    public void customComparableLiteral() throws SecurityException, NoSuchFieldException{
        cl = QSimpleTypesTest_SimpleTypes.class;
        match(PComparable.class, "customComparableLiteral");
    }

    @Test
    public void customNumber() throws SecurityException, NoSuchFieldException{
        cl = QSimpleTypesTest_SimpleTypes.class;
        match(PSimple.class, "customNumber");
    }

    @Test
    public void customComparableNumber() throws SecurityException, NoSuchFieldException{
        cl = QSimpleTypesTest_SimpleTypes.class;
        match(PNumber.class, "customComparableNumber");
    }

    @Test(expected=NoSuchFieldException.class)
    public void skippedField1() throws SecurityException, NoSuchFieldException {
        QSimpleTypesTest_SimpleTypes.class.getField("skipMe");
    }

    @Test(expected=NoSuchFieldException.class)
    public void skippedField2() throws SecurityException, NoSuchFieldException {
        QSimpleTypesTest_SimpleTypes.class.getField("test");
    }

}
