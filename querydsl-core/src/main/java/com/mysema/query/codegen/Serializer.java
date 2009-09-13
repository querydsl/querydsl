/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.List;

public interface Serializer {

    /**
     * Serialize the given ClassModel 
     * 
     * @param type ClassModel to serialize
     * @param writer serialization target
     * @throws IOException
     */
    void serialize(ClassModel type, Writer writer) throws IOException;

//    /**
//     * Serialize the given classes as Querydsl query types
//     * 
//     * @param targetFolder serialization target folder
//     * @param prefix name prefix ("Q" is the preferred one)
//     * @param entityAnnotations entity annotations
//     * @param entityTypes entity types
//     */
//    void serialize(String targetFolder, String prefix,
//            List<Class<? extends Annotation>> entityAnnotations,
//            Class<?>... types);

}