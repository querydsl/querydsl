/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.codegen;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import net.jcip.annotations.Immutable;

import org.apache.commons.collections15.Transformer;

import com.mysema.codegen.CodeWriter;
import com.mysema.codegen.model.ClassType;
import com.mysema.codegen.model.Constructor;
import com.mysema.codegen.model.Parameter;
import com.mysema.codegen.model.Type;
import com.mysema.codegen.model.TypeCategory;
import com.mysema.codegen.model.Types;
import com.mysema.commons.lang.Assert;
import com.mysema.query.types.EConstructor;
import com.mysema.query.types.Expr;
import com.mysema.query.types.expr.ENumber;

/**
 * ProjectionSerializer is a Serializer implementation for DTO types
 *
 * @author tiwe
 *
 */
@Immutable
public final class ProjectionSerializer implements Serializer{

    private final TypeMappings typeMappings;

    public ProjectionSerializer(TypeMappings typeMappings){
        this.typeMappings = Assert.notNull(typeMappings,"typeMappings");
    }

    protected void intro(EntityType model, CodeWriter writer) throws IOException {
        String simpleName = model.getSimpleName();
        Type queryType = typeMappings.getPathType(model, model, false);

        // package
        if (!model.getPackageName().isEmpty()){
            writer.packageDecl(model.getPackageName());
        }

        // imports
        writer.imports(Expr.class.getPackage(), ENumber.class.getPackage());

        // javadoc
        writer.javadoc(queryType + " is a Querydsl Projection type for " + simpleName);

        // class header
//        writer.suppressWarnings("serial");
        Type superType = new ClassType(TypeCategory.SIMPLE, EConstructor.class, model);
        writer.beginClass(queryType, superType);
        writer.privateStaticFinal(Types.LONG_P, "serialVersionUID", String.valueOf(model.hashCode()));
    }

    protected void outro(EntityType model, CodeWriter writer) throws IOException {
        writer.end();
    }

    @Override
    public void serialize(final EntityType model, SerializerConfig serializerConfig, CodeWriter writer) throws IOException{
        // intro
        intro(model, writer);

        final Set<String> packages = Collections.singleton(model.getPackageName());
        final String localName = model.getLocalRawName();

        for (Constructor c : model.getConstructors()){
            // begin
            writer.beginConstructor(c.getParameters(), new Transformer<Parameter,Parameter>(){
                @Override
                public Parameter transform(Parameter p) {
                    return new Parameter(p.getName(), typeMappings.getExprType(p.getType(), model, false, false, true));
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
                    writer.append(p.getType().getRawName(packages, Collections.<String>emptySet()));
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
