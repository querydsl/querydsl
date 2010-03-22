/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.io.IOException;

import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.custom.CSimple;
import com.mysema.query.types.expr.EComparable;
import com.mysema.query.types.path.PSimple;
import com.mysema.util.CodeWriter;


/**
 * EmbeddableSerializer is a Serializer implementation for embeddable types
 * 
 * @author tiwe
 *
 */
public final class EmbeddableSerializer extends EntitySerializer{

    public EmbeddableSerializer(TypeMappings typeMappings) {
        super(typeMappings);
    }

    @Override
    protected void constructorsForVariables(CodeWriter writer, EntityType model) {
        // no root constructors
    }
    
    @Override
    protected void introDefaultInstance(CodeWriter writer, EntityType model) {
        // no default instance
    }
    
    @Override
    protected void introFactoryMethods(CodeWriter writer, EntityType model) throws IOException {
        // no factory methods        
    }
    
    @Override
    protected void introImports(CodeWriter writer, SerializerConfig config, EntityType model) throws IOException {
        writer.imports(PathMetadata.class.getPackage());
        writer.imports(PSimple.class.getPackage());  
        
        if ((model.hasLists() && config.useListAccessors())
                || !model.getMethods().isEmpty()
                || (model.hasMaps() && config.useMapAccessors())){
            writer.imports(EComparable.class.getPackage());
        }
        
        if (!model.getMethods().isEmpty()){
            writer.imports(CSimple.class.getPackage());
        }
    }


}
