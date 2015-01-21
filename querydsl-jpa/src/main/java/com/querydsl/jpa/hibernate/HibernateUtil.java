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
package com.querydsl.jpa.hibernate;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.BigIntegerType;
import org.hibernate.type.ByteType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.FloatType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.ShortType;
import org.hibernate.type.Type;

import com.querydsl.core.types.ParamExpression;
import com.querydsl.core.types.ParamNotSetException;
import com.querydsl.core.types.expr.Param;

/**
 * HibernateUtil provides static utility methods for Hibernate
 *
 * @author tiwe
 *
 */
public final class HibernateUtil {

    private static final Map<Class<?>,Type> TYPES = new HashMap<Class<?>,Type>();

    static{
        TYPES.put(Byte.class, new ByteType());
        TYPES.put(Short.class, new ShortType());
        TYPES.put(Integer.class, new IntegerType());
        TYPES.put(Long.class, new LongType());
        TYPES.put(BigInteger.class, new BigIntegerType());
        TYPES.put(Double.class, new DoubleType());
        TYPES.put(Float.class, new FloatType());
        TYPES.put(BigDecimal.class, new BigDecimalType());
    }

    private HibernateUtil() {}

    public static void setConstants(Query query, Map<Object,String> constants, 
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
        if (val instanceof Collection<?>) {
            query.setParameterList(key, (Collection<?>) val);
        } else if (val.getClass().isArray()) {
            query.setParameterList(key, (Object[]) val);
        } else if (TYPES.containsKey(val.getClass())) {
            query.setParameter(key, val, TYPES.get(val.getClass()));
        } else {
            query.setParameter(key, val);
        }
    }
}
