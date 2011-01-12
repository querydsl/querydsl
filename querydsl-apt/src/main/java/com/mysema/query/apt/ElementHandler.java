/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.apt;

import java.util.ArrayList;
import java.util.Collections;
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
 * ElementHandler is a an APT visitor for entity types
 *
 * @author tiwe
 *
 */
@Immutable
public final class ElementHandler{

    private final Configuration configuration;

    private final ExtendedTypeFactory typeFactory;

    public ElementHandler(Configuration configuration, ExtendedTypeFactory typeFactory){
        this.configuration = configuration;
        this.typeFactory = typeFactory;
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

    public void handleConstructors(EntityType entityType, List<? extends Element> elements) {
        for (ExecutableElement constructor : ElementFilter.constructorsIn(elements)){
            if (configuration.isValidConstructor(constructor)){
                List<Parameter> parameters = transformParams(constructor.getParameters());
                entityType.addConstructor(new Constructor(parameters));
            }
        }
    }

    public void handleFieldProperty(EntityType entityType, VariableElement field,
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

    public void handleMethodProperty(EntityType entityType, String propertyName,
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

    public EntityType handleNormalType(TypeElement e) {
        //FIXME remove this
        if (e == null) {
            System.err.println("e null!");
        }
        EntityType entityType = typeFactory.getEntityType(e.asType(), true);
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
        Type c = typeFactory.getType(e.asType(), true);
        EntityType entityType = new EntityType(configuration.getNamePrefix(), c.as(TypeCategory.ENTITY));
        List<? extends Element> elements = e.getEnclosedElements();
        handleConstructors(entityType, elements);
        return entityType;
    }

    public void handleQueryMethod(EntityType entityType, ExecutableElement executable, Set<Method> queryMethods) {
        String name = executable.getSimpleName().toString();
        QueryMethod queryMethod = executable.getAnnotation(QueryMethod.class);
        Type returnType = typeFactory.getType(executable.getReturnType(), true);
        if (returnType.getCategory() == TypeCategory.ENTITY){
            returnType = returnType.as(TypeCategory.SIMPLE);
        }else if (returnType.getCategory() == TypeCategory.CUSTOM){
            returnType = returnType.as(TypeCategory.get(returnType.getRawName(Collections.<String>emptySet(), Collections.<String>emptySet())));
        }
        Method method = new Method(entityType, name, queryMethod.value(), transformParams(executable.getParameters()), returnType);
        queryMethods.add(method);
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
