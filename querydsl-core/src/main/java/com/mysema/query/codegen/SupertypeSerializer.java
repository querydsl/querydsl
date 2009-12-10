/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.io.IOException;

import net.jcip.annotations.Immutable;

/**
 * SupertypeSerializer is a Serializer implementation for supertypes
 * 
 * @author tiwe
 *
 */
@Immutable
public class SupertypeSerializer extends EntitySerializer{

    @Override
    protected void constructorsForVariables(StringBuilder builder, EntityModel model) {
        // no constructors for variables
    }
    
    @Override
    protected void introDefaultInstance(StringBuilder builder, EntityModel model) {
        // no default instance
    }
    
    @Override
    protected void introFactoryMethods(StringBuilder builder, EntityModel model) throws IOException {
        // no factory methods        
    }
        
    @Override
    protected void introImports(StringBuilder builder, EntityModel model) {
        builder.append("import com.mysema.query.types.path.*;\n\n");
        if (hasOwnEntityProperties(model)){
            builder.append("import static com.mysema.query.types.path.PathMetadata.*;\n");    
        }                
        if (model.hasLists() || model.hasMaps()){
            builder.append("import com.mysema.query.types.expr.*;\n");
        }
    }

}
