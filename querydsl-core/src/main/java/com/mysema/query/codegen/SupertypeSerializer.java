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
    protected void constructors(BeanModel model, Writer writer) throws IOException {
        final String simpleName = model.getSimpleName();
        final String queryType = model.getPrefix() + simpleName;
        final String genericName = model.getGenericName();
        
        StringBuilder builder = new StringBuilder();
        
        if (model.getEntityProperties().isEmpty()){
            builder.append("    public "+queryType+"(PEntity<? extends "+ genericName+"> entity){\n");
            builder.append("        super(entity.getType(), entity.getEntityName(), entity.getMetadata());\n");
            builder.append("    }\n\n");
            
            builder.append("    public "+queryType+"(Class<? extends "+genericName+"> type, @NotEmpty String entityName, PathMetadata<?> metadata) {\n");
            builder.append("        super(type, entityName, metadata);\n");
        
        }else{
            builder.append("    public "+queryType+"(Class<? extends "+genericName+"> type, @NotEmpty String entityName, PathMetadata<?> metadata, PathInits inits) {\n");
            builder.append("        super(type, entityName, metadata);\n");
            if (!model.getEntityProperties().isEmpty()){
                initEntityFields(builder, model);
            }                
        }
        builder.append("    }\n");        
        writer.append(builder.toString());
    }
    
    @Override
    protected void introDefaultInstance(StringBuilder builder, BeanModel model) {
        // no default instance
    }
    
    @Override
    protected void introFactoryMethods(StringBuilder builder, BeanModel model) throws IOException {
        // no factory methods        
    }
        
    @Override
    protected void introImports(StringBuilder builder, BeanModel model) {
        builder.append("import com.mysema.query.util.*;\n");
        builder.append("import com.mysema.query.types.path.*;\n\n");
    }
    
    @Override
    protected void introInits(StringBuilder builder, BeanModel model) {
        // no PathInits instance
    }

}
