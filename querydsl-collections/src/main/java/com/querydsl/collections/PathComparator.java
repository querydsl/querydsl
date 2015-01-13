/*
 * Copyright 2012, Mysema Ltd
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
package com.querydsl.collections;

import java.util.Comparator;

import com.google.common.base.Function;
import com.querydsl.core.types.Path;

/**
 * Compares two beans based on the values at a specific path.
 *
 * @author Jeroen van Schagen
 * @author tiwe
 *
 * @param <T> type of the path root
 * @param <V> type of value being matched
 */
public class PathComparator<T, V extends Comparable<V>> implements Comparator<T> {
    
    private final Function<T,V> accessor;
    
    public PathComparator(Path<V> comparingPath) {
        this(comparingPath, GuavaHelpers.<T,V>wrap(comparingPath));
    }
    
    public PathComparator(Path<V> comparingPath, Function<T,V> accessor) {
        this.accessor = accessor;
    }
    
    public static <T, V extends Comparable<V>> PathComparator<T, V> pathComparator(Path<V> comparingPath) {
        return new PathComparator<T,V>(comparingPath);
    }

    @Override
    public int compare(T leftBean, T rightBean) {
        if(leftBean == rightBean) {
            return 0; // Reference to the seme object should always result in '0'
        } else if (leftBean == null) {
            return -1; // Whenever the reference varies and left is null, right is not null
        } else if (rightBean == null) {
            return 1; // Whenever the reference varies and right is null, left is not null
        } else if (leftBean.equals(rightBean)) {
            return 0; // Equal beans should always result in '0'
        }
        return comparePathValues(leftBean, rightBean);
    }
    
    private int comparePathValues(T leftBean, T rightBean) {
        V left = accessor.apply(leftBean);
        V right = accessor.apply(rightBean);
        if (left == null) {
            return -1;
        } else if (right == null) {
            return 1;
        } else {
            return left.compareTo(right);
        }
    }

}
