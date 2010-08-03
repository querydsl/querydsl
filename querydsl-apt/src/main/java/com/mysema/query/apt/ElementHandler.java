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

import org.apache.commons.lang.StringUtils;

import com.mysema.codegen.model.Constructor;
import com.mysema.codegen.model.Parameter;
import com.mysema.codegen.model.Type;
import com.mysema.codegen.model.TypeCategory;
import com.mysema.query.annotations.PropertyType;
import com.mysema.query.annotations.QueryInit;
import com.mysema.query.annotations.QueryMethod;
import com.mysema.query.annotations.QueryType;
import com.mysema.query.codegen.EntityType;
import com.mysema.query.codegen.Method;
import com.mysema.query.codegen.Property;

/**
 * EntityElementVisitor is a an APT visitor for entity types
 *
 * @author tiwe
 *
 */
@Immutable
public final class ElementHandler{

    private final Configuration configuration;

    private final APTTypeFactory typeFactory;

    public ElementHandler(Configuration configuration, APTTypeFactory typeFactory){
        this.configuration = configuration;
        this.typeFactory = typeFactory;
    }

    private Type getType(VariableElement element){
        Type rv = typeFactory.create(element.asType());
        if (element.getAnnotation(QueryType.class) != null){
            QueryType qt = element.getAnnotation(QueryType.class);
            if (qt.value() != PropertyType.NONE){
                TypeCategory typeCategory = qt.value().getCategory();
                rv = rv.as(typeCategory);
            }
        }
        return rv;
    }

    public void handleConstructors(EntityType entityModel, List<? extends Element> elements) {
        for (ExecutableElement constructor : ElementFilter.constructorsIn(elements)){
            if (configuration.isValidConstructor(constructor)){
                List<Parameter> parameters = transformParams(constructor.getParameters());
                entityModel.addConstructor(new Constructor(parameters));
            }
        }
    }

    public void handleFieldProperty(EntityType entityModel, VariableElement field,
            Map<String, Property> properties,
            Set<String> blockedProperties,
            Map<String, TypeCategory> types) {
        String name = field.getSimpleName().toString();
        try{
            Type fieldType = typeFactory.create(field.asType());
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
            properties.put(name, new Property(entityModel, name, fieldType, inits));
        }catch(IllegalArgumentException ex){
            StringBuilder builder = new StringBuilder();
            builder.append("Caught exception for field ");
            builder.append(entityModel.getFullName()).append("#").append(field.getSimpleName());
            throw new APTException(builder.toString(), ex);
        }
    }

    public void handleMethodProperty(EntityType entityModel, String propertyName,
            ExecutableElement method,
            Map<String, Property> properties, Set<String> blockedProperties,
            Map<String, TypeCategory> types) {
        try{
            Type propertyType = typeFactory.create(method.getReturnType());
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
            properties.put(propertyName, new Property(entityModel, propertyName, propertyType, inits));

        }catch(IllegalArgumentException ex){
            StringBuilder builder = new StringBuilder();
            builder.append("Caught exception for method ");
            builder.append(entityModel.getFullName()).append("#").append(method.getSimpleName());
            throw new APTException(builder.toString(), ex);
        }
    }

    public EntityType handleNormalType(TypeElement e) {
        EntityType entityType = typeFactory.createEntityType(e.asType());
        List<? extends Element> elements = e.getEnclosedElements();
        VisitorConfig config = configuration.getConfig(e, elements);

        Set<String> blockedProperties = new HashSet<String>();
        Map<String,Property> properties = new HashMap<String,Property>();
        Map<String,TypeCategory> types = new HashMap<String,TypeCategory>();
        Set<Method> queryMethods = new HashSet<Method>();

        // constructors
        if (config.visitConstructors()){
            handleConstructors(entityType, elements);
        }

        // fields
        if (config.visitFieldProperties()){
            for (VariableElement field : ElementFilter.fieldsIn(elements)){
                String name = field.getSimpleName().toString();
                if (configuration.isValidField(field)){
                    handleFieldProperty(entityType, field, properties, blockedProperties, types);
                }else if (configuration.isBlockedField(field)){
                    blockedProperties.add(name);
                }
            }
        }

        // methods
        for (ExecutableElement method : ElementFilter.methodsIn(elements)){
            if (method.getAnnotation(QueryMethod.class) != null){
                handleQueryMethod(entityType, method, queryMethods);

            }else if (config.visitMethodProperties()){
                String name = method.getSimpleName().toString();
                if (name.startsWith("get") && method.getParameters().isEmpty()){
                    name = StringUtils.uncapitalize(name.substring(3));
                }else if (name.startsWith("is") && method.getParameters().isEmpty()){
                    name = StringUtils.uncapitalize(name.substring(2));
                }else{
                    continue;
                }

                if (configuration.isValidGetter(method)){
                    handleMethodProperty(entityType, name, method, properties, blockedProperties, types);
                }else if (configuration.isBlockedGetter(method)){
                    blockedProperties.add(name);
                }
            }

        }

        for (Method entry : queryMethods){
            entityType.addMethod(entry);
        }

        for (Map.Entry<String,Property> entry : properties.entrySet()){
            if (!blockedProperties.contains(entry.getKey())){
                entityType.addProperty(entry.getValue());
            }
        }

        return entityType;
    }

    public EntityType handleProjectionType(TypeElement e) {
        Type c = typeFactory.create(e.asType());
        EntityType entityModel = new EntityType(configuration.getNamePrefix(), c.as(TypeCategory.ENTITY));
        List<? extends Element> elements = e.getEnclosedElements();
        handleConstructors(entityModel, elements);
        return entityModel;
    }

    public void handleQueryMethod(EntityType entityModel, ExecutableElement method, Set<Method> queryMethods) {
        String name = method.getSimpleName().toString();
        QueryMethod queryMethod = method.getAnnotation(QueryMethod.class);
        Type returnType = typeFactory.create(method.getReturnType());
        if (returnType.getCategory() == TypeCategory.ENTITY){
            returnType = returnType.as(TypeCategory.SIMPLE);
        }
        Method methodModel = new Method(entityModel, name, queryMethod.value(), transformParams(method.getParameters()), returnType);
        queryMethods.add(methodModel);
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
