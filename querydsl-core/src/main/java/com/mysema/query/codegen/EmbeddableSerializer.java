/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.io.Writer;


public class EmbeddableSerializer extends EntitySerializer{
    
    @Override
    protected void constructorsForVariables(StringBuilder builder, ClassModel model) {
        // no root constructors
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
    protected void introDefaultInstance(StringBuilder builder, ClassModel model) {
        // no default instance
    }


}
