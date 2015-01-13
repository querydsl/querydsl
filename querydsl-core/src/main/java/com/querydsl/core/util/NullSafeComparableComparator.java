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
package com.querydsl.core.util;

import java.io.Serializable;
import java.util.Comparator;

/**
 * NullSafeComparableComparator is a null safe comparator for Comparable objects
 * 
 * @author tiwe
 *
 * @param <T>
 */
public class NullSafeComparableComparator<T extends Comparable<T>> implements Comparator<T>, Serializable{
    
    private static final long serialVersionUID = 5681808684776488757L;

    @Override
    public int compare(T obj1, T obj2) {
        if (obj1 == null) {
            return obj2 == null ? 0 : -1;
        } else if (obj2 == null) {
            return 1;
        } else {
            return obj1.compareTo(obj2);
        }           
    }

}
