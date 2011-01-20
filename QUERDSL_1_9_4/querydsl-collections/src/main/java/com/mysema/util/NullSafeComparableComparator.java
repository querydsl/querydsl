package com.mysema.util;

import java.util.Comparator;

/**
 * @author tiwe
 *
 * @param <T>
 */
public class NullSafeComparableComparator<T extends Comparable<T>> implements Comparator<T>{
    
    @Override
    public int compare(T obj1, T obj2) {
        if (obj1 == null){
            return obj2 == null ? 0 : -1;
        }else if (obj2 == null){
            return 1;
        }else{
            return obj1.compareTo(obj2);
        }           
    }

}
