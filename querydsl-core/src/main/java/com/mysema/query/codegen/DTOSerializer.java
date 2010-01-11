/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.io.IOException;
import java.io.Writer;

import net.jcip.annotations.Immutable;

import org.apache.commons.collections15.Transformer;

import com.mysema.commons.lang.Assert;
import com.mysema.query.types.expr.Expr;
import com.mysema.util.CodeWriter;
import com.mysema.util.JavaWriter;

/**
 * DTOSerializer is a Serializer implementation for DTO types
 * 
 * @author tiwe
 *
 */
@Immutable
public class DTOSerializer implements Serializer{
    
    private final TypeMappings typeMappings;
    
    public DTOSerializer(TypeMappings typeMappings){
        this.typeMappings = Assert.notNull(typeMappings);
    }
    
    protected void intro(EntityModel model, CodeWriter writer) throws IOException {
        final String simpleName = model.getSimpleName();
        final String queryType = typeMappings.getPathType(model, model, false);
        final String localName = model.getLocalRawName();
                
        // package        
        writer.packageDecl(model.getPackageName());
        
        // imports
        writer.imports(Expr.class.getPackage());
        writer.nl();
        
        // javadoc
        writer.javadoc(queryType + " is a Querydsl DTO type for " + simpleName);
        
        // class header
        writer.suppressWarnings("serial");        
        writer.beginClass(queryType, "EConstructor<" + localName + ">");
    }

    protected void outro(EntityModel model, CodeWriter writer) throws IOException {
        writer.end();   
    }
    
    @Override
    public void serialize(final EntityModel model, SerializerConfig serializerConfig, Writer w) throws IOException{
        JavaWriter writer = new JavaWriter(w);
        // intro
        intro(model, writer);
        
        final String localName = model.getLocalRawName();
        
        for (ConstructorModel c : model.getConstructors()){
            // begin
            writer.beginConstructor(c.getParameters(), new Transformer<ParameterModel,String>(){
                @Override
                public String transform(ParameterModel p) {
                    return typeMappings.getExprType(p.getType(), model, false, false, true) + " " + p.getName();
                }                
            });            
            
            // body
            writer.append("        super(" + localName + ".class");
            writer.append(", new Class[]{");
            boolean first = true;
            for (ParameterModel p : c.getParameters()){
                if (!first) writer.append(", ");
                if (p.getType().getPrimitiveName() != null){
                    writer.append(p.getType().getPrimitiveName()+".class");
                }else{
                    writer = p.getType().getLocalRawName(model, writer);
                    writer.append(".class");    
                }                
                first = false;
            }
            writer.append("}");
            
            for (ParameterModel p : c.getParameters()){
                writer.append(", " + p.getName());
            }
            
            // end
            writer.append(");\n");
            writer.end();
        }
                
        // outro
        outro(model, writer);
    }
    
}
