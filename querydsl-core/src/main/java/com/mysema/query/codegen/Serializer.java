/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.io.IOException;
import java.io.Writer;

/**
 * Serializer defines a common interface for EntityModel serializers
 * 
 * @author tiwe
 *
 */
public interface Serializer {

    /**
     * Serialize the given ClassModel 
     * 
     * @param type ClassModel to serialize
     * @param serializerConfig TODO
     * @param writer serialization target
     * @throws IOException
     */
    void serialize(EntityModel type, SerializerConfig serializerConfig, Writer writer) throws IOException;
    
    /**
     * Get a String representation of the Querydsl type for the given TypeModel
     * in tyhe given EntityModel context
     * 
     * @param type
     * @param model
     * @param raw
     * @return
     */
    public String getPathType(TypeModel type, EntityModel model, boolean raw);

}