package com.mysema.util;

import java.io.Serializable;
import java.util.Comparator;

/**
 * @author tiwe
 *
 * @param <T>
 */
public class NullSafeComparableComparator<T extends Comparable<T>> implements Comparator<T>, Serializable{
    
    private static final long serialVersionUID = 5681808684776488757L;

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
