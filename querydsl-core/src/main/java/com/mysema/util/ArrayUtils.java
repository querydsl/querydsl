/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.util;

/**
 * @author tiwe
 *
 */
public final class ArrayUtils {

    public static Object[] combine(int size, Object[]... arrays) {
        int offset = 0;
        Object[] target = new Object[size];
        for (Object[] arr : arrays) {
            System.arraycopy(arr, 0, target, offset, arr.length);
            offset += arr.length;
        }
        return target;
    }

    private ArrayUtils() {}

}
