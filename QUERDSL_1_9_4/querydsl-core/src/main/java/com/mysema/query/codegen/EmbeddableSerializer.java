/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.codegen;

import static com.mysema.codegen.Symbols.UNCHECKED;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.mysema.codegen.CodeWriter;
import com.mysema.codegen.model.ClassType;
import com.mysema.codegen.model.Type;
import com.mysema.codegen.model.TypeCategory;
import com.mysema.codegen.model.Types;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.custom.CSimple;
import com.mysema.query.types.expr.EComparable;
import com.mysema.query.types.path.*;

/**
 * EmbeddableSerializer is a Serializer implementation for embeddable types
 *
 * @author tiwe
 *
 */
public final class EmbeddableSerializer extends EntitySerializer{

    public EmbeddableSerializer(TypeMappings typeMappings, Collection<String> keywords) {
        super(typeMappings, keywords);
    }
    
    @Override
    @SuppressWarnings(UNCHECKED)
    protected void introClassHeader(CodeWriter writer, EntityType model) throws IOException {
        Type queryType = typeMappings.getPathType(model, model, true);

        TypeCategory category = model.getOriginalCategory();
        Class<? extends Path> pathType;
        if (model.getProperties().isEmpty()){
            switch(category){
                case COMPARABLE : pathType = PComparable.class; break;
                case ENUM: pathType = PEnum.class; break;
                case DATE: pathType = PDate.class; break;
                case DATETIME: pathType = PDateTime.class; break;
                case TIME: pathType = PTime.class; break;
                case NUMERIC: pathType = PNumber.class; break;
                case STRING: pathType = PString.class; break;
                case BOOLEAN: pathType = PBoolean.class; break;
                default : pathType = BeanPath.class;
            }
        }else{
            pathType = BeanPath.class;
        }

        for (Annotation annotation : model.getAnnotations()){
            writer.annotation(annotation);
        }
        
        if (category == TypeCategory.BOOLEAN || category == TypeCategory.STRING){
            writer.beginClass(queryType, new ClassType(pathType));
        }else{
            writer.beginClass(queryType, new ClassType(category,pathType, model));    
        }
        
        // TODO : generate proper serialVersionUID here
        writer.privateStaticFinal(Types.LONG_P, "serialVersionUID", String.valueOf(model.hashCode()));
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
        introDelegatePackages(writer, model);

        List<Package> packages = new ArrayList<Package>();
        packages.add(PathMetadata.class.getPackage());
        packages.add(PSimple.class.getPackage());
        if ((model.hasLists() && config.useListAccessors())
                || !model.getMethods().isEmpty()
                || !model.getDelegates().isEmpty()
                || (model.hasMaps() && config.useMapAccessors())){
            packages.add(EComparable.class.getPackage());
        }
        if (!model.getMethods().isEmpty()){
            packages.add(CSimple.class.getPackage());
        }
        writer.imports(packages.toArray(new Package[packages.size()]));
    }

}
