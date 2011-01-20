/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.codegen;

/**
 * SerializerConfig defines serialization options to be used in the Serializer
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

}
