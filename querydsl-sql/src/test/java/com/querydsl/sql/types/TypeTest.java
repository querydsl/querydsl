/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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
package com.querydsl.sql.types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.util.*;

import org.easymock.EasyMock;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.junit.Test;

import com.mysema.commons.lang.Pair;

public class TypeTest implements InvocationHandler {

    public enum Gender {
        MALE, FEMALE
    }

    private Object value;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("wasNull")) {
            return value == null;
        } else if (method.getName().startsWith("get")) {
            if (method.getReturnType().isPrimitive() && value == null) {
                Class<?> rt = method.getReturnType();
                if (rt == Byte.TYPE) {
                    return (byte) 0;
                } else if (rt == Short.TYPE) {
                    return (short) 0;
                } else if (rt == Integer.TYPE) {
                    return 0;
                } else if (rt == Long.TYPE) {
                    return 0L;
                } else if (rt == Float.TYPE) {
                    return 0.0f;
                } else if (rt == Double.TYPE) {
                    return 0.0;
                } else if (rt == Boolean.TYPE) {
                    return Boolean.FALSE;
                } else if (rt == Character.TYPE) {
                    return (char) 0;
                }
            }
            return value;
        } else {
            value = args[1];
            return null;
        }
    }

    private final ResultSet resultSet = (ResultSet) Proxy.newProxyInstance(getClass().getClassLoader(), new Class<?>[]{ResultSet.class}, this);

    private final PreparedStatement statement = (PreparedStatement) Proxy.newProxyInstance(getClass().getClassLoader(), new Class<?>[]{PreparedStatement.class}, this);

    @SuppressWarnings("unchecked")
    @Test
    public void test() throws MalformedURLException, SQLException {
        List<Pair<?,?>> valueAndType = new ArrayList<Pair<?,?>>();
        valueAndType.add(Pair.of(new BigDecimal("1"), new BigDecimalType()));
        valueAndType.add(Pair.of(new BigInteger("2"), new BigIntegerType()));
        valueAndType.add(Pair.of(new BigDecimal("1.0"), new BigDecimalAsDoubleType()));
        valueAndType.add(Pair.of(new BigInteger("2"), new BigIntegerAsLongType()));
        //valueAndType.add(Pair.of(Boolean.TRUE,         new BooleanType()));
        valueAndType.add(Pair.of((byte) 1,   new ByteType()));
        valueAndType.add(Pair.of(new byte[0],         new BytesType()));
        valueAndType.add(Pair.of(Calendar.getInstance(), new CalendarType()));
        valueAndType.add(Pair.of('c',  new CharacterType()));
        valueAndType.add(Pair.of(Currency.getInstance("EUR"), new CurrencyType()));
        valueAndType.add(Pair.of(new java.sql.Date(0),new DateType()));
        valueAndType.add(Pair.of(1.0,       new DoubleType()));
        valueAndType.add(Pair.of(1.0f,        new FloatType()));
        valueAndType.add(Pair.of(1,      new IntegerType()));
        valueAndType.add(Pair.of(true,                    new NumericBooleanType()));
        valueAndType.add(Pair.of(1L,         new LongType()));
        valueAndType.add(Pair.of(new Object(),        new ObjectType()));
        valueAndType.add(Pair.of((short) 1, new ShortType()));
        valueAndType.add(Pair.of("",                   new StringType()));
        valueAndType.add(Pair.of(true,                 new TrueFalseType()));
        valueAndType.add(Pair.of(true,                 new YesNoType()));
        valueAndType.add(Pair.of(new Timestamp(0),    new TimestampType()));
        valueAndType.add(Pair.of(new Time(0),         new TimeType()));
        valueAndType.add(Pair.of(new URL("http://www.mysema.com"), new URLType()));
        valueAndType.add(Pair.of(new java.util.Date(),new UtilDateType()));

        valueAndType.add(Pair.of(new DateTime(),      new DateTimeType()));
        valueAndType.add(Pair.of(new LocalDateTime(), new LocalDateTimeType()));
        valueAndType.add(Pair.of(new LocalDate(),     new LocalDateType()));
        valueAndType.add(Pair.of(new LocalTime(),     new LocalTimeType()));

        valueAndType.add(Pair.of(Gender.MALE,         new EnumByNameType<Gender>(Gender.class)));
        valueAndType.add(Pair.of(Gender.MALE,         new EnumByOrdinalType<Gender>(Gender.class)));

        valueAndType.add(Pair.of(EasyMock.createNiceMock(Blob.class), new BlobType()));
        valueAndType.add(Pair.of(EasyMock.createNiceMock(Clob.class), new ClobType()));

        valueAndType.add(Pair.of(UUID.randomUUID(),   new UtilUUIDType(true)));
        valueAndType.add(Pair.of(UUID.randomUUID(),   new UtilUUIDType(false)));

        for (Pair pair : valueAndType) {
            value = null;
            Type type = (Type) pair.getSecond();
            assertNull(type.toString(),   type.getValue(resultSet, 0));
            type.setValue(statement, 0, pair.getFirst());
            assertEquals(type.toString(), pair.getFirst(), type.getValue(resultSet, 0));
        }
    }


}
