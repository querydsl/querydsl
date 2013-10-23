/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mysema.query.codegen;

import java.io.IOException;

import javax.annotation.Generated;
import javax.inject.Inject;

import com.google.common.base.Function;
import com.mysema.codegen.CodeWriter;
import com.mysema.codegen.model.ClassType;
import com.mysema.codegen.model.Constructor;
import com.mysema.codegen.model.Parameter;
import com.mysema.codegen.model.Type;
import com.mysema.codegen.model.TypeCategory;
import com.mysema.codegen.model.Types;
import com.mysema.query.types.ConstructorExpression;
import com.mysema.query.types.expr.NumberExpression;

/**
 * ProjectionSerializer is a {@link Serializer} implementation for projection types
 *
 * @author tiwe
 *
 */
public final class ProjectionSerializer implements Serializer{

    private final TypeMappings typeMappings;

    /**
     * Create a new ProjectionSerializer instance
     * 
     * @param typeMappings
     */
    @Inject
    public ProjectionSerializer(TypeMappings typeMappings) {
        this.typeMappings = typeMappings;
    }

    protected void intro(EntityType model, CodeWriter writer) throws IOException {
        String simpleName = model.getSimpleName();
        Type queryType = typeMappings.getPathType(model, model, false);

        // package
        if (!queryType.getPackageName().isEmpty()) {
            writer.packageDecl(queryType.getPackageName());
        }

        // imports
        writer.imports(NumberExpression.class.getPackage());
        writer.imports(ConstructorExpression.class, Generated.class);

        // javadoc
        writer.javadoc(queryType + " is a Querydsl Projection type for " + simpleName);

        writer.line("@Generated(\"", getClass().getName(), "\")");
        
        // class header
//        writer.suppressWarnings("serial");
        Type superType = new ClassType(TypeCategory.SIMPLE, ConstructorExpression.class, model);
        writer.beginClass(queryType, superType);
        writer.privateStaticFinal(Types.LONG_P, "serialVersionUID", String.valueOf(model.hashCode()) + "L");
    }

    protected void outro(EntityType model, CodeWriter writer) throws IOException {
        writer.end();
    }

    @Override
    public void serialize(final EntityType model, SerializerConfig serializerConfig, 
            CodeWriter writer) throws IOException{
        // intro
        intro(model, writer);

        String localName = writer.getRawName(model);
        
        for (Constructor c : model.getConstructors()) {
            // begin
            writer.beginConstructor(c.getParameters(), new Function<Parameter,Parameter>() {
                @Override
                public Parameter apply(Parameter p) {
                    return new Parameter(p.getName(), typeMappings.getExprType(p.getType(), 
                            model, false, false, true));
                }
            });

            // body
            writer.beginLine("super(" + writer.getClassConstant(localName));
            // TODO: Fix for Scala (Array[Class])
            writer.append(", new Class[]{");
            boolean first = true;
            
            for (Parameter p : c.getParameters()) {
                if (!first) {
                    writer.append(", ");
                }
                if (Types.PRIMITIVES.containsKey(p.getType())) {
                    Type primitive = Types.PRIMITIVES.get(p.getType());
                    writer.append(writer.getClassConstant(primitive.getFullName()));
                } else {
                    writer.append(writer.getClassConstant(writer.getRawName(p.getType())));
                }
                first = false;
            }
            writer.append("}");

            for (Parameter p : c.getParameters()) {
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
