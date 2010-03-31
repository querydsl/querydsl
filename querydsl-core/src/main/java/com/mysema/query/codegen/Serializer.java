/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.io.IOException;

import com.mysema.util.codegen.CodeWriter;

/**
 * Serializer defines a common interface for EntityType serializers
 * 
 * @author tiwe
 *
 */
public interface Serializer {

    /**
     * Serialize the given EntityType 
     * 
     * @param type EntityType to serialize
     * @param serializerConfig TODO
     * @param writer serialization target
     * @throws IOException
     */
    void serialize(EntityType type, SerializerConfig serializerConfig, CodeWriter writer) throws IOException;
    

}