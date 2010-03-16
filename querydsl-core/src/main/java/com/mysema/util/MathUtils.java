/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.util;

import java.math.BigDecimal;
import java.math.BigInteger;

public final class MathUtils {
    
    private MathUtils(){}

    @SuppressWarnings("unchecked")
    public static <D extends Number & Comparable<?>> D sum(D num1, Number num2){
        BigDecimal res = new BigDecimal(num1.toString()).add(new BigDecimal(num2.toString()));
        return MathUtils.<D>cast(res, (Class<D>)num1.getClass());
    }
    
    @SuppressWarnings("unchecked")
    public static <D extends Number & Comparable<?>> D difference(D num1, Number num2){
        BigDecimal res = new BigDecimal(num1.toString()).subtract(new BigDecimal(num2.toString()));
        return MathUtils.<D>cast(res, (Class<D>)num1.getClass());
    }
    
    @SuppressWarnings("unchecked")
    private static <D extends Number & Comparable<?>> D cast(BigDecimal num, Class<D> type){
        Number rv;
        if (type.equals(Double.class)){
            rv = num.byteValue();
        }else if (type.equals(Double.class)){
            rv = num.doubleValue();
        }else if (type.equals(Float.class)){
            rv = num.floatValue();        
        }else if (type.equals(Integer.class)){
            rv = num.intValue();
        }else if (type.equals(Long.class)){
            rv = num.longValue();
        }else if (type.equals(Short.class)){
            rv = num.shortValue();
        }else if (type.equals(BigDecimal.class)){
            rv = num;
        }else if (type.equals(BigInteger.class)){
            rv = num.toBigInteger();
        }else{
            throw new IllegalArgumentException(String.format("Illegal type : %s", type.getSimpleName()));
        }
        return (D) rv;        
    }
}
