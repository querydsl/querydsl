/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.io.IOException;
import java.io.Writer;

/**
 * @author tiwe
 *
 */
public interface Serializer {

    /**
     * Serialize the given ClassModel 
     * 
     * @param type ClassModel to serialize
     * @param writer serialization target
     * @throws IOException
     */
    void serialize(BeanModel type, Writer writer) throws IOException;

}