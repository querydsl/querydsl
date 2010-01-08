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

import com.mysema.query.annotations.PropertyType;
import com.mysema.query.annotations.QueryInit;
import com.mysema.query.annotations.QueryMethod;
import com.mysema.query.annotations.QueryType;
import com.mysema.query.codegen.ConstructorModel;
import com.mysema.query.codegen.EntityModel;
import com.mysema.query.codegen.MethodModel;
import com.mysema.query.codegen.ParameterModel;
import com.mysema.query.codegen.PropertyModel;
import com.mysema.query.codegen.TypeCategory;
import com.mysema.query.codegen.TypeModel;

/**
 * EntityElementVisitor is a an APT visitor for entity types
 * 
 * @author tiwe
 *
 */
@Immutable
public final class ElementHandler{
    
    private final Configuration configuration;
    
    private final APTTypeModelFactory typeFactory;
    
    public ElementHandler(Configuration configuration, APTTypeModelFactory typeFactory){
        this.configuration = configuration;
        this.typeFactory = typeFactory;
    }
    
    private TypeModel getType(VariableElement element){
        TypeModel rv = typeFactory.create(element.asType());              
        if (element.getAnnotation(QueryType.class) != null){
            QueryType qt = element.getAnnotation(QueryType.class);
            if (qt.value() != PropertyType.NONE){
                TypeCategory typeCategory = TypeCategory.get(qt.value());
                rv = rv.as(typeCategory);    
            }                        
        }
        return rv;
    }
    
    private void handleConstructors(EntityModel entityModel, List<? extends Element> elements) {
        for (ExecutableElement constructor : ElementFilter.constructorsIn(elements)){
            if (configuration.isValidConstructor(constructor)){
                List<ParameterModel> parameters = transformParams(constructor.getParameters());
                entityModel.addConstructor(new ConstructorModel(parameters));    
            }                
        }
    }

    private void handleFieldProperty(EntityModel entityModel, VariableElement field,            
            Map<String, PropertyModel> properties,
            Set<String> blockedProperties,
            Map<String, TypeCategory> types) {
        String name = field.getSimpleName().toString();
        try{                        
            TypeModel fieldType = typeFactory.create(field.asType());            
            if (field.getAnnotation(QueryType.class) != null){
                TypeCategory typeCategory = TypeCategory.get(field.getAnnotation(QueryType.class).value());
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
            properties.put(name, new PropertyModel(entityModel, name, fieldType, inits));    
        }catch(IllegalArgumentException ex){
            StringBuilder builder = new StringBuilder();
            builder.append("Caught exception for field ");
            builder.append(entityModel.getFullName()).append("#").append(field.getSimpleName());
            throw new RuntimeException(builder.toString(), ex);
        }
    }

    private void handleMethodProperty(EntityModel entityModel, String propertyName, 
            ExecutableElement method,            
            Map<String, PropertyModel> properties, Set<String> blockedProperties,
            Map<String, TypeCategory> types) {
        try{
            TypeModel propertyType = typeFactory.create(method.getReturnType());
            if (method.getAnnotation(QueryType.class) != null){
                TypeCategory typeCategory = TypeCategory.get(method.getAnnotation(QueryType.class).value());
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
            properties.put(propertyName, new PropertyModel(entityModel, propertyName, propertyType, inits));    
            
        }catch(IllegalArgumentException ex){
            StringBuilder builder = new StringBuilder();
            builder.append("Caught exception for method ");
            builder.append(entityModel.getFullName()).append("#").append(method.getSimpleName());
            throw new RuntimeException(builder.toString(), ex);
        }
    }

    
    public EntityModel handleNormalType(TypeElement e) {
        EntityModel entityModel = typeFactory.createEntityModel(e.asType());
        List<? extends Element> elements = e.getEnclosedElements();
    
        VisitorConfig config = configuration.getConfig(e, elements);
        
        Set<String> blockedProperties = new HashSet<String>();
        Map<String,PropertyModel> properties = new HashMap<String,PropertyModel>();
        Map<String,MethodModel> queryMethods = new HashMap<String,MethodModel>();
        Map<String,TypeCategory> types = new HashMap<String,TypeCategory>();
        
        // constructors
        if (config.isVisitConstructors()){
            handleConstructors(entityModel, elements);    
        }          
        
        // fields
        if (config.isVisitFields()){
            for (VariableElement field : ElementFilter.fieldsIn(elements)){
                String name = field.getSimpleName().toString();
                if (configuration.isValidField(field)){
                    handleFieldProperty(entityModel, field, properties, blockedProperties, types);
                }else if (configuration.isBlockedField(field)){
                    blockedProperties.add(name);
                }   
                
            }        
        }
        
        // methods
        if (config.isVisitMethods()){
            for (ExecutableElement method : ElementFilter.methodsIn(elements)){
                if (method.getAnnotation(QueryMethod.class) != null){
                    handleQueryMethod(entityModel, method, queryMethods);
                    
                }else{
                    String name = method.getSimpleName().toString();
                    if (name.startsWith("get") && method.getParameters().isEmpty()){
                        name = StringUtils.uncapitalize(name.substring(3));
                    }else if (name.startsWith("is") && method.getParameters().isEmpty()){
                        name = StringUtils.uncapitalize(name.substring(2));
                    }else{
                        continue;
                    }
                    
                    if (configuration.isValidGetter(method)){
                        handleMethodProperty(entityModel, name, method, properties, blockedProperties, types);                    
                    }else if (configuration.isBlockedGetter(method)){
                        blockedProperties.add(name);
                    }    
                }
                
            }   
        }
                       
        for (Map.Entry<String, MethodModel> entry : queryMethods.entrySet()){
            entityModel.addMethod(entry.getValue());
        }
        
        for (Map.Entry<String,PropertyModel> entry : properties.entrySet()){
            if (!blockedProperties.contains(entry.getKey())){
                entityModel.addProperty(entry.getValue());
            }
        }                
        
        return entityModel;
    }
    
    public EntityModel handleProjectionType(TypeElement e) {
        TypeModel c = typeFactory.create(e.asType());        
        EntityModel entityModel = new EntityModel(configuration.getNamePrefix(), c.as(TypeCategory.ENTITY));
        List<? extends Element> elements = e.getEnclosedElements();
        handleConstructors(entityModel, elements);
        return entityModel;
    }
    
    private void handleQueryMethod(EntityModel entityModel, ExecutableElement method, Map<String,MethodModel> queryMethods) {
        String name = method.getSimpleName().toString();
        QueryMethod queryMethod = method.getAnnotation(QueryMethod.class);
        TypeModel returnType = typeFactory.create(method.getReturnType());
        MethodModel methodModel = new MethodModel(entityModel, name, queryMethod.value(), transformParams(method.getParameters()), returnType);        
        queryMethods.put(name, methodModel);
    }

    private List<ParameterModel> transformParams(List<? extends VariableElement> params){
        List<ParameterModel> parameters = new ArrayList<ParameterModel>(params.size());
        for (VariableElement param : params){
            TypeModel paramType = getType(param);
            parameters.add(new ParameterModel(param.getSimpleName().toString(), paramType));
        }
        return parameters;
    }
    
}
