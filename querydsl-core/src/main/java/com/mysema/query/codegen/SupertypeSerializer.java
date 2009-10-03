/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.io.IOException;
import java.io.Writer;

import net.jcip.annotations.Immutable;

/**
 * @author tiwe
 *
 */
@Immutable
public class SupertypeSerializer extends EntitySerializer{

    @Override
    protected void introDefaultInstance(StringBuilder builder, ClassModel model) {
        // no default instance
    }
    
    @Override
    protected void factoryMethods(ClassModel model, Writer writer) {
        // no factory methods        
    }
    
    @Override
    protected void introImports(StringBuilder builder) {
        builder.append("import com.mysema.query.util.*;\n");
        builder.append("import com.mysema.query.types.path.*;\n\n");
    }
    
    @Override
    protected void constructors(ClassModel model, Writer writer) throws IOException {
        final String simpleName = model.getSimpleName();
        final String queryType = model.getPrefix() + simpleName;
        final String localName = model.getLocalName();
        
        StringBuilder builder = new StringBuilder();
        builder.append("    public " + queryType + "(PEntity<? extends "+localName+"> entity) {\n");
        builder.append("        super(entity.getType(), entity.getEntityName(), entity.getMetadata());\n");
        builder.append("    }\n\n");
        writer.append(builder.toString());
    }

}
