/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.io.IOException;

import net.jcip.annotations.Immutable;

import com.mysema.query.types.custom.Custom;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.Path;
import com.mysema.util.CodeWriter;

/**
 * SupertypeSerializer is a Serializer implementation for supertypes
 * 
 * @author tiwe
 *
 */
@Immutable
public final class SupertypeSerializer extends EntitySerializer{

    public SupertypeSerializer(TypeMappings typeMappings) {
        super(typeMappings);
    }

    @Override
    protected void constructorsForVariables(CodeWriter writer, EntityModel model) {
        // no constructors for variables
    }
    
    @Override
    protected void introDefaultInstance(CodeWriter writer, EntityModel model) {
        // no default instance
    }
    
    @Override
    protected void introFactoryMethods(CodeWriter writer, EntityModel model) throws IOException {
        // no factory methods        
    }
        
    @Override
    protected void introImports(CodeWriter writer, SerializerConfig config, EntityModel model) throws IOException {
        writer.imports(Path.class.getPackage());        
        if ((model.hasLists() && config.useListAccessors())
                || !model.getMethods().isEmpty()
                || (model.hasMaps() && config.useMapAccessors())){
            writer.imports(Expr.class.getPackage());
        }
        
        if (!model.getMethods().isEmpty()){
            writer.imports(Custom.class.getPackage());
        }
    }

}
