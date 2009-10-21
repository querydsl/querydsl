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
        
        StringBuilder builder = new StringBuilder();
        builder.append("    public "+queryType+"(Class<? extends T> type, @NotEmpty String entityName, PathMetadata<?> metadata) {\n");
        builder.append("        super(type, entityName, metadata);\n");
        builder.append("    }\n");
        writer.append(builder.toString());
    }
    
    @Override
    protected void factoryMethods(StringBuilder builder, BeanModel model) throws IOException {
        // no factory methods        
    }
    
    @Override
    protected void introClassHeader(StringBuilder builder, BeanModel model) {
        final String queryType = model.getPrefix() + model.getSimpleName();
        final String localName = model.getLocalName();
        builder.append("@SuppressWarnings(\"serial\")\n");        
        if (model.getSuperModel() != null){
            BeanModel superModel = model.getSuperModel();
            String superQueryType = superModel.getPrefix() + superModel.getSimpleName();
            if (!superModel.getPackageName().equals(model.getPackageName())){
                superQueryType = superModel.getPackageName() + "." + superQueryType;
            }
            builder.append("public abstract class " + queryType + "<T extends "+localName+"> extends "+superQueryType+"<T> {\n\n");
        }else{
            builder.append("public abstract class " + queryType + "<T extends "+localName+"> extends PEntity<T> {\n\n");    
        }        
    }
    
    @Override
    protected void introDefaultInstance(StringBuilder builder, BeanModel model) {
        // no default instance
    }
    
    @Override
    protected void introImports(StringBuilder builder, BeanModel model) {
        builder.append("import com.mysema.query.util.*;\n");
        builder.append("import com.mysema.query.types.path.*;\n\n");
    }
    
    @Override
    protected void introSuper(StringBuilder builder, BeanModel model) {
        BeanModel superModel = model.getSuperModel();
        String superQueryType = superModel.getPrefix() + superModel.getSimpleName();
        if (!superModel.getPackageName().equals(model.getPackageName())){
            superQueryType = superModel.getPackageName() + "." + superQueryType;
        }            
        builder.append("    public final "+superQueryType+"<T> _super = this;\n\n");
    }

}
