/*
 * Copyright 2011, Mysema Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.apt.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

import com.querydsl.core.annotations.Config;
import com.querydsl.core.annotations.PropertyType;
import com.querydsl.core.annotations.QueryEntity;
import com.querydsl.core.annotations.QueryTransient;
import com.querydsl.core.annotations.QueryType;
import com.querydsl.apt.domain.QSimpleTypesTest_SimpleTypes;
import com.querydsl.core.types.path.ComparablePath;
import com.querydsl.core.types.path.DateTimePath;
import com.querydsl.core.types.path.EnumPath;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.path.SimplePath;
import com.querydsl.core.types.path.StringPath;
import com.querydsl.core.types.path.TimePath;

public class SimpleTypesTest extends AbstractTest {

    public enum MyEnum {
        VAL1,
        VAL2
    }

    public static class CustomLiteral {

    }

    @SuppressWarnings("serial")
    public static class CustomNumber extends Number {

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

    public static class CustomComparableNumber extends CustomNumber implements Comparable<CustomComparableNumber> {

        private static final long serialVersionUID = 4398583038967396133L;

        @Override
        public int compareTo(CustomComparableNumber o) {
            return 0;
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof CustomComparableNumber;
        }
    }

    public static class CustomComparableLiteral implements Comparable<CustomComparableLiteral> {

        @Override
        public int compareTo(CustomComparableLiteral o) {
            return 0;
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof CustomComparableLiteral;
        }
    }

    public static class CustomGenericComparableLiteral<C> implements Comparable<CustomComparableLiteral> {

        @Override
        public int compareTo(CustomComparableLiteral o) {
            return 0;
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof CustomGenericComparableLiteral;
        }
    }

    @QueryEntity
    @Config(listAccessors=true)
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

        Short sshort;
        List<Short> sshortList;
        short sshort2;

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

        MyEnum myEnum;

        int[] intArray;
        byte[] byteArray;
        long[] longArray;
        float[] floatArray;
        double[] doubleArray;
        short[] shortArray;

        @QueryType(PropertyType.SIMPLE)
        byte[] byteArrayAsSimple;
    }

    @Test
    public void List_Access() {
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
    public void Simple_Types() throws IllegalAccessException, NoSuchFieldException {
        start(QSimpleTypesTest_SimpleTypes.class, QSimpleTypesTest_SimpleTypes.simpleTypes);
        match(NumberPath.class, "id");
        matchType(Long.class, "id");
        match(NumberPath.class, "bigDecimal");
        matchType(BigDecimal.class, "bigDecimal");
        match(NumberPath.class, "bigInteger");
        matchType(BigInteger.class, "bigInteger");
//        match(PNumber.class, "bbyte");
        match(NumberPath.class, "bbyte2");
        matchType(Byte.class, "bbyte");
        match(NumberPath.class, "ddouble");
        matchType(Double.class, "ddouble");
        match(NumberPath.class, "ddouble2");
        matchType(Double.class, "ddouble2");
        match(NumberPath.class, "ffloat");
        matchType(Float.class, "ffloat");
        match(NumberPath.class, "ffloat2");
        matchType(Float.class, "ffloat2");
//        match(PNumber.class, "iint");
        match(NumberPath.class, "iint2");
        matchType(Integer.class, "iint2");
        match(NumberPath.class, "llong");
        matchType(Long.class, "llong");
        match(NumberPath.class, "llong2");
        matchType(Long.class, "llong2");

        match(ComparablePath.class, "cchar");
        matchType(Character.class, "cchar");
        match(ComparablePath.class, "cchar2");
        matchType(Character.class, "cchar2");

        match(StringPath.class, "sstring");

        match(DateTimePath.class, "date");
        matchType(Date.class, "date");
        match(DateTimePath.class, "calendar");
        matchType(Calendar.class, "calendar");
//        match(PDateTime.class, "timestamp");

        match(TimePath.class, "time");
        matchType(Time.class, "time");

        match(SimplePath.class, "llocale");
        matchType(Locale.class, "llocale");
        match(SimplePath.class, "serializable");
        matchType(Serializable.class, "serializable");
        match(SimplePath.class, "object");
        matchType(Object.class, "object");
        match(SimplePath.class, "clazz");
        matchType(Class.class, "clazz");
        match(SimplePath.class, "packageAsLiteral");
        matchType(Package.class, "packageAsLiteral");

        match(SimplePath.class, "clob");
        matchType(Clob.class, "clob");
        match(SimplePath.class, "blob");
        matchType(Blob.class, "blob");

        match(EnumPath.class, "myEnum");
        matchType(MyEnum.class, "myEnum");
    }

    @Test
    public void Custom_Literal() throws IllegalAccessException, NoSuchFieldException {
        start(QSimpleTypesTest_SimpleTypes.class, QSimpleTypesTest_SimpleTypes.simpleTypes);
        match(SimplePath.class, "customLiteral");
        matchType(CustomLiteral.class, "customLiteral");
    }

    @Test
    public void Custom_ComparableLiteral() throws IllegalAccessException, NoSuchFieldException {
        start(QSimpleTypesTest_SimpleTypes.class, QSimpleTypesTest_SimpleTypes.simpleTypes);
        match(ComparablePath.class, "customComparableLiteral");
        matchType(CustomComparableLiteral.class, "customComparableLiteral");
    }

    @Test
    public void Custom_Number() throws IllegalAccessException, NoSuchFieldException {
        start(QSimpleTypesTest_SimpleTypes.class, QSimpleTypesTest_SimpleTypes.simpleTypes);
        match(SimplePath.class, "customNumber");
        matchType(CustomNumber.class, "customNumber");
    }

    @Test
    public void Custom_ComparableNumber() throws IllegalAccessException, NoSuchFieldException {
        start(QSimpleTypesTest_SimpleTypes.class, QSimpleTypesTest_SimpleTypes.simpleTypes);
        match(NumberPath.class, "customComparableNumber");
        matchType(CustomComparableNumber.class, "customComparableNumber");
    }

    @Test
    public void Skipped_Field1() {
        start(QSimpleTypesTest_SimpleTypes.class, QSimpleTypesTest_SimpleTypes.simpleTypes);
        assertMissing("skipMe");
    }

    @Test
    public void Skipped_Field2() {
        start(QSimpleTypesTest_SimpleTypes.class, QSimpleTypesTest_SimpleTypes.simpleTypes);
        assertMissing("test");
    }

}
