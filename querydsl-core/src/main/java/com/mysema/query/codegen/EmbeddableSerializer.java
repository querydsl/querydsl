/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.io.IOException;


public class EmbeddableSerializer extends EntitySerializer{
    
    @Override
    protected void constructorsForVariables(StringBuilder builder, EntityModel model) {
        // no root constructors
    }
    
    @Override
    protected void introFactoryMethods(StringBuilder builder, EntityModel model) throws IOException {
        // no factory methods        
    }
    
    @Override
    protected void introImports(StringBuilder builder, EntityModel model) {
        if (model.hasEntityFields()){
            builder.append("import com.mysema.query.util.*;\n");    
        }        
        builder.append("import com.mysema.query.types.path.*;\n\n");
        if (!model.getProperties().isEmpty()){
            builder.append("import static com.mysema.query.types.path.PathMetadata.*;\n");    
        }        
        if (model.hasLists() || model.hasMaps()){
            builder.append("import com.mysema.query.types.expr.*;\n");
        }
    }
    
    @Override
    protected void introDefaultInstance(StringBuilder builder, EntityModel model) {
        // no default instance
    }


}
