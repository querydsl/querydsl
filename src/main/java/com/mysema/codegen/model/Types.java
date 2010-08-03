/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.codegen.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * @author tiwe
 *
 */
@SuppressWarnings("unchecked")
public final class Types {

    public static final ClassType<Object> OBJECT = new ClassType<Object>(TypeCategory.SIMPLE,Object.class);
    
    public static final ClassType<Object[]> OBJECTS = new ClassType<Object[]>(TypeCategory.ARRAY,Object[].class);
        
    public static final ClassType<BigDecimal> BIG_DECIMAL = new ClassType<BigDecimal>(TypeCategory.NUMERIC,BigDecimal.class);
    
    public static final ClassType<BigInteger> BIG_INTEGER = new ClassType<BigInteger>(TypeCategory.NUMERIC,BigInteger.class);
    
    public static final ClassType<Boolean> BOOLEAN = new ClassType<Boolean>(TypeCategory.BOOLEAN,Boolean.class, boolean.class);

    public static final ClassType<Byte> BYTE = new ClassType<Byte>(TypeCategory.NUMERIC,Byte.class, byte.class);
    
    public static final ClassType<Character> CHAR = new ClassType<Character>(TypeCategory.COMPARABLE,Character.class, char.class);
    
    public static final ClassType<Collection> COLLECTION = new ClassType<Collection>(TypeCategory.COLLECTION, Collection.class, OBJECT);

    public static final ClassType<Double> DOUBLE = new ClassType<Double>(TypeCategory.NUMERIC,Double.class, double.class);

    public static final ClassType<Float> FLOAT = new ClassType<Float>(TypeCategory.NUMERIC,Float.class, float.class);

    public static final ClassType<Integer> INT = new ClassType<Integer>(TypeCategory.NUMERIC,Integer.class, int.class);

    public static final ClassType<Iterable> ITERABLE = new ClassType<Iterable>(TypeCategory.SIMPLE, Iterable.class, OBJECT);

    public static final ClassType<List> LIST = new ClassType<List>(TypeCategory.LIST, List.class, OBJECT);

    public static final ClassType<Locale> LOCALE = new ClassType<Locale>(TypeCategory.SIMPLE, Locale.class);

    public static final ClassType<Long> LONG = new ClassType<Long>(TypeCategory.NUMERIC,Long.class, long.class);

    public static final ClassType<Map> MAP = new ClassType<Map>(TypeCategory.MAP, Map.class, OBJECT, OBJECT);
            
    public static final ClassType<Set> SET = new ClassType<Set>(TypeCategory.SET, Set.class, OBJECT);
    
    public static final ClassType<Short> SHORT = new ClassType<Short>(TypeCategory.NUMERIC,Short.class, short.class);
    
    public static final ClassType<String> STRING = new ClassType<String>(TypeCategory.STRING,String.class);
    
    public static final ClassType<URI> URI = new ClassType<URI>(TypeCategory.COMPARABLE,URI.class);
    
    private Types(){}
    
}
