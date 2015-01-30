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

import javax.annotation.Nonnegative;

/**
 * PathMetadataFactory is a factory class for {@link Path} construction
 * 
 * @author tiwe
 *
 */
public final class PathMetadataFactory {

    /**
     * Create a new PathMetadata for indexed array access
     * 
     * @param parent
     * @param index
     * @return
     */
    public static PathMetadata forArrayAccess(Path<?> parent, Expression<Integer> index) {
        return new PathMetadata(parent, index, PathType.ARRAYVALUE);
    }

    /**
     * Create a new PathMetadata for indexed array access
     * 
     * @param parent
     * @param index
     * @return
     */
    public static PathMetadata forArrayAccess(Path<?> parent, @Nonnegative int index) {
        return new PathMetadata(parent, index, PathType.ARRAYVALUE_CONSTANT);
    }
    
    /**
     * Create a new PathMetadata for collection any access
     * 
     * @param parent
     * @return
     */
    public static PathMetadata forCollectionAny(Path<?> parent) {
        return new PathMetadata(parent, "", PathType.COLLECTION_ANY);
    }

    /**
     * Create a new PathMetadata for delegate access
     * 
     * @param delegate
     * @return
     */
    public static <T> PathMetadata forDelegate(Path<T> delegate) {
        return new PathMetadata(delegate, delegate, PathType.DELEGATE);
    }

    /**
     * Create a new PathMetadata for indexed list access
     * 
     * @param parent
     * @param index
     * @return
     */
    public static PathMetadata forListAccess(Path<?> parent, Expression<Integer> index) {
        return new PathMetadata(parent, index, PathType.LISTVALUE);
    }

    /**
     * Create a new PathMetadata for indexed list access
     * 
     * @param parent
     * @param index
     * @return
     */
    public static PathMetadata forListAccess(Path<?> parent, @Nonnegative int index) {
        return new PathMetadata(parent, index, PathType.LISTVALUE_CONSTANT);
    }

    /**
     * Create a new PathMetadata for key based map access
     * 
     * @param parent
     * @param key
     * @return
     */
    public static <KT> PathMetadata forMapAccess(Path<?> parent, Expression<KT> key) {
        return new PathMetadata(parent, key, PathType.MAPVALUE);
    }

    /**
     * Create a new PathMetadata for for key based map access
     * 
     * @param parent
     * @param key
     * @return
     */
    public static <KT> PathMetadata forMapAccess(Path<?> parent, KT key) {
        return new PathMetadata(parent, key, PathType.MAPVALUE_CONSTANT);
    }

    /**
     * Create a new PathMetadata for property access
     * 
     * @param parent
     * @param property
     * @return
     */
    public static PathMetadata forProperty(Path<?> parent, String property) {
        return new PathMetadata(parent, property, PathType.PROPERTY);
    }

    /**
     * Create a new PathMetadata for a variable
     * 
     * @param variable
     * @return
     */
    public static PathMetadata forVariable(String variable) {
        return new PathMetadata(null, variable, PathType.VARIABLE);
    }

    private PathMetadataFactory() {}

}
