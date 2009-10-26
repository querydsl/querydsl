/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.io.IOException;


public class EmbeddableSerializer extends EntitySerializer{
    
    @Override
    protected void constructorsForVariables(StringBuilder builder, BeanModel model) {
        // no root constructors
    }
    
    @Override
    protected void introFactoryMethods(StringBuilder builder, BeanModel model) throws IOException {
        // no factory methods        
    }
    
    @Override
    protected void introImports(StringBuilder builder, BeanModel model) {
        if (model.hasEntityFields()){
            builder.append("import com.mysema.query.util.*;\n");    
        }        
        builder.append("import com.mysema.query.types.path.*;\n\n");
        if (model.hasLists() || model.hasMaps()){
            builder.append("import com.mysema.query.types.expr.*;\n");
        }
    }
    
    @Override
    protected void introDefaultInstance(StringBuilder builder, BeanModel model) {
        // no default instance
    }


}
