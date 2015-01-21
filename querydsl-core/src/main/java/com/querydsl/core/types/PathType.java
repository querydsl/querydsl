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
package com.querydsl.core.types;

/**
 * PathType represents the relation of a {@link Path} to its parent
 */
public enum PathType implements Operator<Object> {
    /**
     * Indexed array access (array[i])
     */
    ARRAYVALUE,
    
    /**
     * Indexed array access with constant (array[i])
     */
    ARRAYVALUE_CONSTANT,    
        
    /**
     * Access of any element in a collection
     */
    COLLECTION_ANY,
    
    /**
     * Delegate to an expression
     */
    DELEGATE,
    
    /**
     * Indexed list access (list.get(index))
     */
    LISTVALUE,
    
    /**
     * Indexed list access with constant (list.get(index))
     */
    LISTVALUE_CONSTANT,
    
    /**
     * Map value access (map.get(key))
     */
    MAPVALUE,
    
    /**
     * Map value access with constant (map.get(key))
     */
    MAPVALUE_CONSTANT,
    
    /**
     * Property of the parent
     */
    PROPERTY,
    
    /**
     * Root path
     */
    VARIABLE;
    
    @Override
    public String getId() {
        return name();
    }
    
}
