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
package com.querydsl.core.alias;

/**
 * TypeSystem defines an interface for detecting whether a given class confirms to certain Collection
 * contract
 * 
 * @author tiwe
 *
 */
public interface TypeSystem {
    
    /**
     * @param cl
     * @return
     */
    boolean isCollectionType(Class<?> cl);
    
    /**
     * @param cl
     * @return
     */
    boolean isSetType(Class<?> cl);
    
    /**
     * @param cl
     * @return
     */
    boolean isListType(Class<?> cl);
    
    /**
     * @param cl
     * @return
     */
    boolean isMapType(Class<?> cl);

}
