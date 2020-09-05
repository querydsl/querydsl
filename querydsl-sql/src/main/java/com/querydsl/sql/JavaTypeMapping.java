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
package com.querydsl.sql;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import com.querydsl.core.util.PrimitiveUtils;
import com.querydsl.core.util.ReflectionUtils;
import com.querydsl.sql.types.*;

/**
 * {@code JavaTypeMapping} provides a mapping from Class to Type instances
 *
 * @author tiwe
 *
 */
class JavaTypeMapping {

    private static final Type<Object> DEFAULT = new ObjectType();

    private static final Map<Class<?>,Type<?>> defaultTypes = new HashMap<Class<?>,Type<?>>();

    static {
        registerDefault(new BigIntegerType());
        registerDefault(new BigDecimalType());
        registerDefault(new BlobType());
        registerDefault(new BooleanType());
        registerDefault(new BytesType());
        registerDefault(new ByteType());
        registerDefault(new CharacterType());
        registerDefault(new CalendarType());
        registerDefault(new ClobType());
        registerDefault(new CurrencyType());
        registerDefault(new DateType());
        registerDefault(new DoubleType());
        registerDefault(new FloatType());
        registerDefault(new IntegerType());
        registerDefault(new LocaleType());
        registerDefault(new LongType());
        registerDefault(new ObjectType());
        registerDefault(new ShortType());
        registerDefault(new StringType());
        registerDefault(new TimestampType());
        registerDefault(new TimeType());
        registerDefault(new URLType());
        registerDefault(new UtilDateType());
        registerDefault(new UtilUUIDType(false));

        // initialize Joda-Time converters only if Joda-Time is available
        try {
            Class.forName("org.joda.time.Instant");
            registerDefault((Type<?>) Class.forName("com.querydsl.sql.types.DateTimeType").newInstance());
            registerDefault((Type<?>) Class.forName("com.querydsl.sql.types.LocalDateTimeType").newInstance());
            registerDefault((Type<?>) Class.forName("com.querydsl.sql.types.LocalDateType").newInstance());
            registerDefault((Type<?>) Class.forName("com.querydsl.sql.types.LocalTimeType").newInstance());
        } catch (ClassNotFoundException e) {
            // converters for Joda-Time are not loaded
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        // initialize java time api (JSR 310) converters only if java 8 is available
        try {
            Class.forName("java.time.Instant");
            registerDefault((Type<?>) Class.forName("com.querydsl.sql.types.JSR310InstantType").newInstance());
            registerDefault((Type<?>) Class.forName("com.querydsl.sql.types.JSR310LocalDateTimeType").newInstance());
            registerDefault((Type<?>) Class.forName("com.querydsl.sql.types.JSR310LocalDateType").newInstance());
            registerDefault((Type<?>) Class.forName("com.querydsl.sql.types.JSR310LocalTimeType").newInstance());
            registerDefault((Type<?>) Class.forName("com.querydsl.sql.types.JSR310OffsetDateTimeType").newInstance());
            registerDefault((Type<?>) Class.forName("com.querydsl.sql.types.JSR310OffsetTimeType").newInstance());
            registerDefault((Type<?>) Class.forName("com.querydsl.sql.types.JSR310ZonedDateTimeType").newInstance());
        } catch (ClassNotFoundException e) {
            // converters for JSR 310 are not loaded
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static void registerDefault(Type<?> type) {
        defaultTypes.put(type.getReturnedClass(), type);
        Class<?> primitive = PrimitiveUtils.unwrap(type.getReturnedClass());
        if (primitive != null) {
            defaultTypes.put(primitive, type);
        }
    }

    private final Map<Class<?>,Type<?>> typeByClass = new HashMap<Class<?>,Type<?>>();

    private final Map<Class<?>,Type<?>> resolvedTypesByClass = new HashMap<Class<?>,Type<?>>();

    private final Map<String, Map<String,Type<?>>> typeByColumn = new HashMap<String,Map<String,Type<?>>>();

    @Nullable
    public Type<?> getType(String table, String column) {
        Map<String,Type<?>> columns = typeByColumn.get(table);
        if (columns != null) {
            return columns.get(column);
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> Type<T> getType(Class<T> clazz) {
        Type<?> resolvedType = resolvedTypesByClass.get(clazz);
        if (resolvedType == null) {
            resolvedType = findType(clazz);
            if (resolvedType != null) {
                resolvedTypesByClass.put(clazz, resolvedType);
            } else {
                return (Type) DEFAULT;
            }
        }
        return (Type<T>) resolvedType;
    }

    @Nullable
    private Type<?> findType(Class<?> clazz) {
        //Look for a registered type in the class hierarchy
        Class<?> cl = clazz;
        do {
            if (typeByClass.containsKey(cl)) {
                return typeByClass.get(cl);
            } else if (defaultTypes.containsKey(cl)) {
                return defaultTypes.get(cl);
            }
            cl = cl.getSuperclass();
        } while (!cl.equals(Object.class));

        //Look for a registered type in any implemented interfaces
        Set<Class<?>> interfaces = ReflectionUtils.getImplementedInterfaces(clazz);
        for (Class<?> itf : interfaces) {
            if (typeByClass.containsKey(itf)) {
                return typeByClass.get(itf);
            } else if (defaultTypes.containsKey(itf)) {
                return defaultTypes.get(itf);
            }
        }
        return null;
    }

    public void register(Type<?> type) {
        typeByClass.put(type.getReturnedClass(), type);
        Class<?> primitive = PrimitiveUtils.unwrap(type.getReturnedClass());
        if (primitive != null) {
            typeByClass.put(primitive, type);
        }
        // Clear previous resolved types, so they won't impact future lookups
        resolvedTypesByClass.clear();
    }

    public void setType(String table, String column, Type<?> type) {
        Map<String, Type<?>> columns = typeByColumn.computeIfAbsent(table, k -> new HashMap<String, Type<?>>());
        columns.put(column, type);
    }

}