/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.hibernate;

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

/**
 * Utility methods for Hibernate
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
    
    private HibernateUtil(){}

    public static void setConstants(Query query, Map<Object,String> constants) {
        for (Map.Entry<Object, String> entry : constants.entrySet()){
            String key = entry.getValue();
            Object val = entry.getKey();
            
            if (val instanceof Collection<?>) {
                // NOTE : parameter types should be given explicitly
                query.setParameterList(key, (Collection<?>) val);
                
            } else if (val.getClass().isArray()) {
                // NOTE : parameter types should be given explicitly
                query.setParameterList(key, (Object[]) val);
                
            }else if (TYPES.containsKey(val.getClass())){
                query.setParameter(key, val, TYPES.get(val.getClass()));
                
            } else {
                // NOTE : parameter types should be given explicitly
                query.setParameter(key, val);
            }
        }
    }
}
