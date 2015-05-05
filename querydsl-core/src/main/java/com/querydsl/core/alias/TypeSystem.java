/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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
package com.querydsl.core.alias;

/**
 * {@code TypeSystem} defines an interface for detecting whether a given class confirms to certain Collection
 * contract
 * 
 * @author tiwe
 *
 */
public interface TypeSystem {
    
    /**
     * Return whether the given class is a collection class
     *
     * @param cl class to check
     * @return true, if argument is a collection type
     */
    boolean isCollectionType(Class<?> cl);
    
    /**
     * Return whether the given class is a set class
     *
     * @param cl class to check
     * @return true, if argument is a set type
     */
    boolean isSetType(Class<?> cl);
    
    /**
     * Return whether the given class is a list class
     *
     * @param cl class to check
     * @return true, if argument is a list type
     */
    boolean isListType(Class<?> cl);
    
    /**
     * Return whether the given class is a map class
     *
     * @param cl class to check
     * @return true, if argument is a map type
     */
    boolean isMapType(Class<?> cl);

}
