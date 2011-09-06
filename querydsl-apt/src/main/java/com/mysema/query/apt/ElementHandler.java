/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.apt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;

import net.jcip.annotations.Immutable;

import com.mysema.codegen.model.Constructor;
import com.mysema.codegen.model.Parameter;
import com.mysema.codegen.model.Type;
import com.mysema.codegen.model.TypeCategory;
import com.mysema.query.annotations.PropertyType;
import com.mysema.query.annotations.QueryInit;
import com.mysema.query.annotations.QueryType;
import com.mysema.query.codegen.EntityType;
import com.mysema.query.codegen.Property;
import com.mysema.query.codegen.QueryTypeFactory;
import com.mysema.query.codegen.TypeMappings;
import com.mysema.util.BeanUtils;

/**
 * ElementHandler is a an APT visitor for entity types
 *
 * @author tiwe
 *
 */
@Immutable
// TODO : rename
public final class ElementHandler{

    private final TypeMappings typeMappings;

    private final QueryTypeFactory queryTypeFactory;

    private final Configuration configuration;

    private final ExtendedTypeFactory typeFactory;

    public ElementHandler(Configuration configuration, ExtendedTypeFactory typeFactory,
            TypeMappings typeMappings, QueryTypeFactory queryTypeFactory){
        this.configuration = configuration;
        this.typeFactory = typeFactory;
        this.typeMappings = typeMappings;
        this.queryTypeFactory = queryTypeFactory;
    }
    

    public EntityType handleEntityType(TypeElement e) {
        EntityType entityType = typeFactory.getEntityType(e.asType(), true);
        List<? extends Element> elements = e.getEnclosedElements();
        VisitorConfig config = configuration.getConfig(e, elements);
        Set<String> blockedProperties = new HashSet<String>();
        Map<String,Property> properties = new HashMap<String,Property>();
        Map<String,TypeCategory> types = new HashMap<String,TypeCategory>();

        // constructors
        if (config.visitConstructors()){
            handleConstructors(entityType, elements);
        }

        // fields
        if (config.visitFieldProperties()){
            for (VariableElement field : ElementFilter.fieldsIn(elements)){
                String name = field.getSimpleName().toString();

                if (configuration.isBlockedField(field)){
                    blockedProperties.add(name);
                } else if (configuration.isValidField(field)){
                    handleFieldProperty(entityType, field, properties, blockedProperties, types);
                }
            }
        }

        // methods
        if (config.visitMethodProperties()){
            for (ExecutableElement method : ElementFilter.methodsIn(elements)){            
                String name = method.getSimpleName().toString();
                if (name.startsWith("get") && method.getParameters().isEmpty()){
                    name = BeanUtils.uncapitalize(name.substring(3));
                }else if (name.startsWith("is") && method.getParameters().isEmpty()){
                    name = BeanUtils.uncapitalize(name.substring(2));
                }else{
                    continue;
                }

                if (configuration.isBlockedGetter(method)){
                    blockedProperties.add(name);
                } else if (configuration.isValidGetter(method)){
                    handleMethodProperty(entityType, name, method, properties, blockedProperties, types);
                }
            }
        }

        for (Map.Entry<String,Property> entry : properties.entrySet()){
            if (!blockedProperties.contains(entry.getKey())){
                entityType.addProperty(entry.getValue());
            }
        }

        return entityType;
    }

    public EntityType handleProjectionType(TypeElement e) {
        Type c = typeFactory.getType(e.asType(), true);
        EntityType entityType = new EntityType(c.as(TypeCategory.ENTITY));
        typeMappings.register(entityType, queryTypeFactory.create(entityType));
        List<? extends Element> elements = e.getEnclosedElements();
        handleConstructors(entityType, elements);
        return entityType;
    }

    private Type getType(VariableElement element){
        Type rv = typeFactory.getType(element.asType(), true);
        if (element.getAnnotation(QueryType.class) != null){
            QueryType qt = element.getAnnotation(QueryType.class);
            if (qt.value() != PropertyType.NONE){
                TypeCategory typeCategory = qt.value().getCategory();
                rv = rv.as(typeCategory);
            }
        }
        return rv;
    }

    private void handleConstructors(EntityType entityType, List<? extends Element> elements) {
        for (ExecutableElement constructor : ElementFilter.constructorsIn(elements)){
            if (configuration.isValidConstructor(constructor)){
                List<Parameter> parameters = transformParams(constructor.getParameters());
                entityType.addConstructor(new Constructor(parameters));
            }
        }
    }

    private void handleFieldProperty(EntityType entityType, VariableElement field,
            Map<String, Property> properties,
            Set<String> blockedProperties,
            Map<String, TypeCategory> types) {
        String name = field.getSimpleName().toString();
        try{
            Type fieldType = typeFactory.getType(field.asType(), true);
            if (field.getAnnotation(QueryType.class) != null){
                TypeCategory typeCategory = field.getAnnotation(QueryType.class).value().getCategory();
                if (typeCategory == null){
                    blockedProperties.add(name);
                    return;
                }
                fieldType = fieldType.as(typeCategory);
                types.put(name, typeCategory);
            }
            String[] inits = new String[0];
            if (field.getAnnotation(QueryInit.class) != null){
                inits = field.getAnnotation(QueryInit.class).value();
            }
            properties.put(name, new Property(entityType, name, fieldType, inits));
        }catch(IllegalArgumentException ex){
            StringBuilder builder = new StringBuilder();
            builder.append("Caught exception for field ");
            builder.append(entityType.getFullName()).append("#").append(field.getSimpleName());
            throw new APTException(builder.toString(), ex);
        }
    }

    private void handleMethodProperty(EntityType entityType, String propertyName,
            ExecutableElement method,
            Map<String, Property> properties, Set<String> blockedProperties,
            Map<String, TypeCategory> types) {
        try{
            Type propertyType = typeFactory.getType(method.getReturnType(), true);
            if (method.getAnnotation(QueryType.class) != null){
                TypeCategory typeCategory = method.getAnnotation(QueryType.class).value().getCategory();
                if (typeCategory == null){
                    blockedProperties.add(propertyName);
                    return;
                }else if (blockedProperties.contains(propertyName)){
                    return;
                }
                propertyType = propertyType.as(typeCategory);
            }else if (types.containsKey(propertyName)){
                propertyType = propertyType.as(types.get(propertyName));
            }
            String[] inits = new String[0];
            if (method.getAnnotation(QueryInit.class) != null){
                inits = method.getAnnotation(QueryInit.class).value();
            }
            properties.put(propertyName, new Property(entityType, propertyName, propertyType, inits));

        }catch(IllegalArgumentException ex){
            StringBuilder builder = new StringBuilder();
            builder.append("Caught exception for method ");
            builder.append(entityType.getFullName()).append("#").append(method.getSimpleName());
            throw new APTException(builder.toString(), ex);
        }
    }


    public List<Parameter> transformParams(List<? extends VariableElement> params){
        List<Parameter> parameters = new ArrayList<Parameter>(params.size());
        for (VariableElement param : params){
            Type paramType = getType(param);
            parameters.add(new Parameter(param.getSimpleName().toString(), paramType));
        }
        return parameters;
    }

}
