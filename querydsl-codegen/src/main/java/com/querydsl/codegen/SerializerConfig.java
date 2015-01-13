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
package com.querydsl.codegen;

/**
 * SerializerConfig defines serialization options to be used in the {@link Serializer}
 * 
 * @author tiwe
 *
 */
public interface SerializerConfig {

    /**
     * @return if accessors are used for entity fields
     */
    boolean useEntityAccessors();

    /**
     * @return if indexed list accessors are used 
     */
    boolean useListAccessors();

    /**
     * @return if keyed map accessors are used
     */
    boolean useMapAccessors();

    /**
     * @return if the default variable is created
     */
    boolean createDefaultVariable();

    /**
     * @return the name of the default variable
     */
    String defaultVariableName();
}
