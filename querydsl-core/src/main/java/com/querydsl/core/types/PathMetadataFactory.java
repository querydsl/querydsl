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
package com.querydsl.core.types;

import javax.annotation.Nonnegative;

/**
 * {@code PathMetadataFactory} is a factory class for {@link Path} construction
 * 
 * @author tiwe
 *
 */
public final class PathMetadataFactory {

    /**
     * Create a new PathMetadata instance for indexed array access
     * 
     * @param parent parent path
     * @param index index of element
     * @return array access path
     */
    public static PathMetadata forArrayAccess(Path<?> parent, Expression<Integer> index) {
        return new PathMetadata(parent, index, PathType.ARRAYVALUE);
    }

    /**
     * Create a new PathMetadata instance for indexed array access
     * 
     * @param parent parent path
     * @param index index of element
     * @return array access path
     */
    public static PathMetadata forArrayAccess(Path<?> parent, @Nonnegative int index) {
        return new PathMetadata(parent, index, PathType.ARRAYVALUE_CONSTANT);
    }
    
    /**
     * Create a new PathMetadata instance for collection any access
     * 
     * @param parent parent path
     * @return collection any path
     */
    public static PathMetadata forCollectionAny(Path<?> parent) {
        return new PathMetadata(parent, "", PathType.COLLECTION_ANY);
    }

    /**
     * Create a new PathMetadata instance for delegate access
     * 
     * @param delegate delegate path
     * @return wrapped path
     */
    public static <T> PathMetadata forDelegate(Path<T> delegate) {
        return new PathMetadata(delegate, delegate, PathType.DELEGATE);
    }

    /**
     * Create a new PathMetadata instance for indexed list access
     * 
     * @param parent parent path
     * @param index index of element
     * @return list access path
     */
    public static PathMetadata forListAccess(Path<?> parent, Expression<Integer> index) {
        return new PathMetadata(parent, index, PathType.LISTVALUE);
    }

    /**
     * Create a new PathMetadata instance for indexed list access
     * 
     * @param parent parent path
     * @param index index of element
     * @return list access path
     */
    public static PathMetadata forListAccess(Path<?> parent, @Nonnegative int index) {
        return new PathMetadata(parent, index, PathType.LISTVALUE_CONSTANT);
    }

    /**
     * Create a new PathMetadata instance for key based map access
     * 
     * @param parent parent path
     * @param key key for map access
     * @return map access path
     */
    public static <KT> PathMetadata forMapAccess(Path<?> parent, Expression<KT> key) {
        return new PathMetadata(parent, key, PathType.MAPVALUE);
    }

    /**
     * Create a new PathMetadata instance for for key based map access
     * 
     * @param parent parent path
     * @param key key for map access
     * @return map access path
     */
    public static <KT> PathMetadata forMapAccess(Path<?> parent, KT key) {
        return new PathMetadata(parent, key, PathType.MAPVALUE_CONSTANT);
    }

    /**
     * Create a new PathMetadata instance for property access
     * 
     * @param parent parent path
     * @param property property name
     * @return property path
     */
    public static PathMetadata forProperty(Path<?> parent, String property) {
        return new PathMetadata(parent, property, PathType.PROPERTY);
    }

    /**
     * Create a new PathMetadata instance for a variable
     * 
     * @param variable variable name
     * @return variable path
     */
    public static PathMetadata forVariable(String variable) {
        return new PathMetadata(null, variable, PathType.VARIABLE);
    }

    private PathMetadataFactory() {}

}
