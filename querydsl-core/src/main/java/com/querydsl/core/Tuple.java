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
package com.querydsl.core;

import javax.annotation.Nullable;

import com.querydsl.core.types.Expression;

/**
 * Tuple defines an interface for generic querydsl result projection
 * 
 * <p>Usage example:</p>
 * <pre>
 * {@code 
 * List<Tuple> result = querydsl.from(employee).list(new QTuple(employee.firstName, employee.lastName));
 * for (Tuple row : result) {
 *     System.out.println("firstName " + row.get(employee.firstName));
 *     System.out.println("lastName " + row.get(employee.lastName)); 
 * }
 * } 
 * </pre>
 *
 * @author tiwe
 *
 */
public interface Tuple {

    /**
     * Get a Tuple element by index
     *
     * @param <T>
     * @param index
     * @param type
     * @return
     */
    @Nullable
    <T> T get(int index, Class<T> type);

    /**
     * Get a tuple element by expression
     *
     * @param <T>
     * @param expr
     * @return
     */
    @Nullable
    <T> T get(Expression<T> expr);

    /**
     * Get the size of the Tuple
     * 
     * @return 
     */
    int size();
    
    /**
     * Get the content as an Object array
     *
     * @return
     */
    Object[] toArray();
    
   /**
    * All Tuples should override equals and hashCode. For compatibility
    * across different Tuple implementations, equality check should use
    * {@link java.util.Arrays#equals(Object[], Object[])} with {@link #toArray()} as parameters.
    *
    * @see Object#equals(Object)
    */
   boolean equals(Object o);
        
   /**
    * All Tuples should override equals and hashCode. For compatibility
    * across different Tuple implementations, hashCode should use
    * {@link java.util.Arrays#hashCode(Object[])} with {@link #toArray()} as parameter.
    *
    * @see Object#hashCode()
    */
   int hashCode();
    

}
