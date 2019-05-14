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
package com.querydsl.jpa.hibernate;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.primitives.Ints;
import com.querydsl.core.types.ParamExpression;
import com.querydsl.core.types.ParamNotSetException;
import com.querydsl.core.types.dsl.Param;
import org.hibernate.Query;
import org.hibernate.type.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * {@code HibernateUtil} provides static utility methods for Hibernate
 *
 * @author tiwe
 */
public final class HibernateUtil {

    private static final Set<Class<?>> BUILT_IN = ImmutableSet.<Class<?>>of(Boolean.class, Byte.class,
            Character.class, Double.class, Float.class, Integer.class, Long.class, Short.class,
            String.class, BigDecimal.class, byte[].class, Byte[].class, java.util.Date.class,
            java.util.Calendar.class, java.sql.Date.class, java.sql.Time.class, java.sql.Timestamp.class,
            java.util.Locale.class, java.util.TimeZone.class, java.util.Currency.class, Class.class,
            java.io.Serializable.class, java.sql.Blob.class, java.sql.Clob.class);

    private static final Map<Class<?>, Type> TYPES;

    static {
        ImmutableMap.Builder<Class<?>, Type> builder = ImmutableMap.builder();
        builder.put(Byte.class, new ByteType());
        builder.put(Short.class, new ShortType());
        builder.put(Integer.class, new IntegerType());
        builder.put(Long.class, new LongType());
        builder.put(BigInteger.class, new BigIntegerType());
        builder.put(Double.class, new DoubleType());
        builder.put(Float.class, new FloatType());
        builder.put(BigDecimal.class, new BigDecimalType());
        builder.put(String.class, new StringType());
        builder.put(Character.class, new CharacterType());
        builder.put(Date.class, new DateType());
        builder.put(Boolean.class, new BooleanType());
        TYPES = builder.build();
    }

    private HibernateUtil() {
    }

    public static void setConstants(Query query, Map<Object, String> constants,
                                    Map<ParamExpression<?>, Object> params) {
        for (Map.Entry<Object, String> entry : constants.entrySet()) {
            String key = entry.getValue();
            Object val = entry.getKey();
            if (Param.class.isInstance(val)) {
                val = params.get(val);
                if (val == null) {
                    throw new ParamNotSetException((Param<?>) entry.getKey());
                }
            }
            setValue(query, key, val);
        }
    }

    private static void setValue(Query query, String key, Object val) {

        Integer intKey = Ints.tryParse(key);

        if (intKey == null) {
            if (val instanceof Collection<?>) {
                query.setParameterList(key, (Collection<?>) val);
            } else if (val instanceof Object[] && !BUILT_IN.contains(val.getClass())) {
                query.setParameterList(key, (Object[]) val);
            } else if (val instanceof Number && TYPES.containsKey(val.getClass())) {
                query.setParameter(key, val, getType(val.getClass()));
            } else {
                query.setParameter(key, val);
            }
        } else {
            if (val instanceof Collection<?>) {
                query.setParameterList(intKey, (Collection<?>) val);
            } else if (val instanceof Object[] && !BUILT_IN.contains(val.getClass())) {
                query.setParameterList(intKey, (Object[]) val);
            } else if (val instanceof Number && TYPES.containsKey(val.getClass())) {
                query.setParameter(intKey, val, getType(val.getClass()));
            } else {
                query.setParameter(intKey, val);
            }
        }
    }

    public static Type getType(Class<?> clazz) {
        return TYPES.get(clazz);
    }
}
