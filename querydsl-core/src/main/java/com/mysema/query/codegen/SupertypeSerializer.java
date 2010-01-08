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
    protected void introImports(StringBuilder builder, SerializerConfig config, EntityModel model) {
        builder.append("import com.mysema.query.types.path.*;\n\n");
        if (hasOwnEntityProperties(model)){
            builder.append("import static com.mysema.query.types.path.PathMetadataFactory.*;\n");
        }                
        
        if ((model.hasLists() && config.useListAccessors())
                || !model.getMethods().isEmpty()
                || (model.hasMaps() && config.useMapAccessors())){
            builder.append("import com.mysema.query.types.expr.*;\n");
        }
        
        if (!model.getMethods().isEmpty()){
            builder.append("import com.mysema.query.types.custom.*;\n");
        }
    }

}
