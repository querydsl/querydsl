/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.io.IOException;
import java.io.Writer;

import net.jcip.annotations.Immutable;

@Immutable
public class DTOSerializer implements Serializer{
    
    @Override
    public void serialize(BeanModel model, Writer writer) throws IOException{
        // intro
        intro(model, writer);
        
        final String simpleName = model.getSimpleName();
        final String queryType = model.getPrefix() + simpleName;
        final String localName = model.getLocalName();
        
        StringBuilder builder = new StringBuilder();
        for (ConstructorModel c : model.getConstructors()){
            // begin
            builder.append("    public "+ queryType + "(");
            boolean first = true;
            for (ParameterModel p : c.getParameters()){
                if (!first) builder.append(", ");                
                builder.append("Expr<");
                if (!p.getType().isFinal()){
                    builder.append("? extends ");
                }
                builder.append(p.getType().getLocalGenericName(model) + "> " + p.getName());
                first = false;
            }
            builder.append("){\n");
            
            // body
            builder.append("        super(" + localName + ".class");
            builder.append(", new Class[]{");
            first = true;
            for (ParameterModel p : c.getParameters()){
                if (!first) builder.append(",");
                if (p.getType().getPrimitiveName() != null){
                    builder.append(p.getType().getPrimitiveName()+".class");
                }else{
                    builder.append(p.getType().getLocalRawName(model) + ".class");    
                }                
                first = false;
            }
            builder.append("}");
            
            for (ParameterModel p : c.getParameters()){
                builder.append(", " + p.getName());
            }
            
            // end
            builder.append(");\n");
            builder.append("    }\n\n");
        }
        writer.append(builder.toString());
                
        // outro
        outro(model, writer);
    }

    protected void intro(BeanModel model, Writer writer) throws IOException {
        final String simpleName = model.getSimpleName();
        final String queryType = model.getPrefix() + simpleName;
        final String localName = model.getLocalName();
        
        StringBuilder builder = new StringBuilder();        
        // package
        builder.append("package " + model.getPackageName() + ";\n\n");
        
        // imports
        builder.append("import com.mysema.query.types.expr.*;\n\n");
        
        // javadoc
        builder.append("/**\n");
        builder.append(" * " + queryType + " is a Querydsl DTO type for " + simpleName + "\n");
        builder.append(" * \n");
        builder.append(" */ \n");
        
        // class header
        //builder.append("@SuppressWarnings(\"all\")\n");
        builder.append("public class " + queryType + " extends EConstructor<" + localName + ">{\n\n");
        builder.append("    private static final long serialVersionUID = "+model.getConstructors().hashCode()+"L;\n\n");
        writer.append(builder.toString());
    }
    
    protected void outro(BeanModel model, Writer writer) throws IOException {
        writer.write("}\n");        
    }
    
}
