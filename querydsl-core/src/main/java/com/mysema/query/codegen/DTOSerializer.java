/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.io.IOException;

import net.jcip.annotations.Immutable;

import org.apache.commons.collections15.Transformer;

import com.mysema.commons.lang.Assert;
import com.mysema.query.types.Expr;
import com.mysema.util.CodeWriter;

/**
 * DTOSerializer is a Serializer implementation for DTO types
 * 
 * @author tiwe
 *
 */
@Immutable
public final class DTOSerializer implements Serializer{
    
    private final TypeMappings typeMappings;

    public DTOSerializer(TypeMappings typeMappings){
        this.typeMappings = Assert.notNull(typeMappings,"typeMappings");
    }
    
    protected void intro(EntityType model, CodeWriter writer) throws IOException {
        String simpleName = model.getSimpleName();
        String queryType = typeMappings.getPathType(model, model, false);
        String localName = model.getLocalRawName();
                
        // package        
        writer.packageDecl(model.getPackageName());
        
        // imports
        writer.imports(Expr.class.getPackage());
        writer.nl();
        
        // javadoc
        writer.javadoc(queryType + " is a Querydsl DTO type for " + simpleName);
        
        // class header
//        writer.suppressWarnings("serial");        
        writer.beginClass(queryType, "EConstructor<" + localName + ">");
        writer.privateStaticFinal("long", "serialVersionUID", String.valueOf(model.hashCode()));
    }

    protected void outro(EntityType model, CodeWriter writer) throws IOException {
        writer.end();   
    }
    
    @Override
    public void serialize(final EntityType model, SerializerConfig serializerConfig, CodeWriter writer) throws IOException{
        // intro
        intro(model, writer);
        
        final String localName = model.getLocalRawName();
        
        for (Constructor c : model.getConstructors()){
            // begin
            writer.beginConstructor(c.getParameters(), new Transformer<Parameter,String>(){
                @Override
                public String transform(Parameter p) {
                    return typeMappings.getExprType(p.getType(), model, false, false, true) + " " + p.getName();
                }                
            });            
            
            // body
            writer.beginLine("super(" + localName + ".class");
            writer.append(", new Class[]{");
            boolean first = true;
            for (Parameter p : c.getParameters()){
                if (!first){
                    writer.append(", ");
                }
                if (p.getType().getPrimitiveName() != null){
                    writer.append(p.getType().getPrimitiveName()+".class");
                }else{
                    p.getType().appendLocalRawName(model, writer);
                    writer.append(".class");    
                }                
                first = false;
            }
            writer.append("}");
            
            for (Parameter p : c.getParameters()){
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
