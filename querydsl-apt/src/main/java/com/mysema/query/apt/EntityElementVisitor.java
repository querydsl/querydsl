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

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.lang.model.util.SimpleElementVisitor6;

import net.jcip.annotations.Immutable;

import org.apache.commons.lang.StringUtils;

import com.mysema.query.annotations.QueryInit;
import com.mysema.query.annotations.QueryType;
import com.mysema.query.codegen.ConstructorModel;
import com.mysema.query.codegen.EntityModel;
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
public final class EntityElementVisitor extends SimpleElementVisitor6<EntityModel, Void>{
    
    private final ProcessingEnvironment env;
    
    private final APTTypeModelFactory typeFactory;
    
    private final Configuration configuration;
    
    EntityElementVisitor(ProcessingEnvironment env, Configuration configuration, 
            APTTypeModelFactory typeFactory){
        this.env = env;
        this.configuration = configuration;
        this.typeFactory = typeFactory;
    }
    
    @Override
    public EntityModel visitType(TypeElement e, Void p) {
        Elements elementUtils = env.getElementUtils();
        EntityModel entityModel = typeFactory.createEntityModel(e.asType(), elementUtils);
        List<? extends Element> elements = e.getEnclosedElements();
    
        VisitorConfig config = configuration.getConfig(e, elements);
        
        Set<String> blockedProperties = new HashSet<String>();
        Map<String,PropertyModel> properties = new HashMap<String,PropertyModel>();
        Map<String,TypeCategory> types = new HashMap<String,TypeCategory>();
        
        if (config.isVisitConstructors()){
            visitConstructors(elementUtils, entityModel, elements);    
        }          
        
        if (config.isVisitFields()){
            visitFields(elementUtils, entityModel, elements, blockedProperties, properties, types);    
        }
        
        if (config.isVisitMethods()){
            visitMethods(elementUtils, entityModel, elements, blockedProperties, properties, types);   
        }
                       
        for (Map.Entry<String,PropertyModel> entry : properties.entrySet()){
            if (!blockedProperties.contains(entry.getKey())){
                entityModel.addProperty(entry.getValue());
            }
        }        
        
        return entityModel;
    }

    private void visitConstructors(Elements elementUtils, EntityModel entityModel, List<? extends Element> elements) {
        for (ExecutableElement constructor : ElementFilter.constructorsIn(elements)){
            if (configuration.isValidConstructor(constructor)){
                List<ParameterModel> parameters = new ArrayList<ParameterModel>(constructor.getParameters().size());
                for (VariableElement var : constructor.getParameters()){
                    TypeModel varType = typeFactory.create(var.asType(), elementUtils);                    
                    parameters.add(new ParameterModel(entityModel, var.getSimpleName().toString(), varType));
                }
                entityModel.addConstructor(new ConstructorModel(parameters));    
            }                
        }
    }

    private void visitMethods(Elements elementUtils, EntityModel entityModel,
            List<? extends Element> elements, Set<String> blockedProperties,
            Map<String, PropertyModel> properties,
            Map<String, TypeCategory> types) {
        for (ExecutableElement method : ElementFilter.methodsIn(elements)){
            String name = method.getSimpleName().toString();
            if (name.startsWith("get") && method.getParameters().isEmpty()){
                name = StringUtils.uncapitalize(name.substring(3));
            }else if (name.startsWith("is") && method.getParameters().isEmpty()){
                name = StringUtils.uncapitalize(name.substring(2));
            }else{
                continue;
            }
            
            if (!configuration.isValidGetter(method)){
                blockedProperties.add(name);
                continue;
            }
            
            try{
                TypeModel propertyType = typeFactory.create(method.getReturnType(), elementUtils);
                if (method.getAnnotation(QueryType.class) != null){
                    TypeCategory typeCategory = TypeCategory.get(method.getAnnotation(QueryType.class).value());
                    if (typeCategory == null){
                        blockedProperties.add(name);
                        continue;
                    }else if (blockedProperties.contains(name)){
                        continue;
                    }
                    propertyType = propertyType.as(typeCategory);
                }else if (types.containsKey(name)){
                    propertyType = propertyType.as(types.get(name));
                }
                String[] inits = new String[0];
                if (method.getAnnotation(QueryInit.class) != null){
                    inits = method.getAnnotation(QueryInit.class).value();
                }
                properties.put(name, new PropertyModel(entityModel, name, propertyType, inits));    
                
            }catch(IllegalArgumentException ex){
                StringBuilder builder = new StringBuilder();
                builder.append("Caught exception for method ");
                builder.append(entityModel.getFullName()).append("#").append(method.getSimpleName());
                throw new RuntimeException(builder.toString(), ex);
            }
        }
    }

    private void visitFields(Elements elementUtils, EntityModel entityModel,
            List<? extends Element> elements, Set<String> blockedProperties,
            Map<String, PropertyModel> properties,
            Map<String, TypeCategory> types) {
        for (VariableElement field : ElementFilter.fieldsIn(elements)){
            String name = field.getSimpleName().toString();
            // is particular field visited ?
            if (!configuration.isValidField(field)){
                blockedProperties.add(name);
                continue;
            }
            
            try{                        
                TypeModel fieldType = typeFactory.create(field.asType(), elementUtils);            
                if (field.getAnnotation(QueryType.class) != null){
                    TypeCategory typeCategory = TypeCategory.get(field.getAnnotation(QueryType.class).value());
                    if (typeCategory == null){
                        blockedProperties.add(name);
                        continue;
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
    }
    
}
