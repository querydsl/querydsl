/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.util;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * NumberUtil provides
 *
 * @author tiwe
 * @version $Id$
 */
public class NumberUtil {

    @SuppressWarnings("unchecked")
    public static <A extends Number> A castTo(Number number, Class<A> type){
        if (type.equals(number.getClass())){
            return (A)number;
        }else if (Byte.class.equals(type)){
            return (A)Byte.valueOf(number.byteValue());
        }else if (Double.class.equals(type)){
            return (A)Double.valueOf(number.doubleValue());
        }else if (Float.class.equals(type)){
            return (A)Float.valueOf(number.floatValue());
        }else if (Integer.class.equals(type)){
            return (A)Integer.valueOf(number.intValue());
        }else if (Long.class.equals(type)){
            return (A)Long.valueOf(number.longValue());
        }else if (Short.class.equals(type)){
            return (A)Short.valueOf(number.shortValue());
        }else if (BigInteger.class.equals(type)){
            return (A)new BigInteger(String.valueOf(number.longValue()));
        }else if (BigDecimal.class.equals(type)){
            return (A)new BigDecimal(number.toString());
        }else{
            throw new IllegalArgumentException("Unsupported target type : " + type.getName());
        }
    }
}
