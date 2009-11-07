/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.lang.model.util.SimpleElementVisitor6;

import net.jcip.annotations.Immutable;

import org.apache.commons.lang.StringUtils;

import com.mysema.query.annotations.QueryInit;
import com.mysema.query.annotations.QueryType;
import com.mysema.query.codegen.BeanModel;
import com.mysema.query.codegen.ConstructorModel;
import com.mysema.query.codegen.ParameterModel;
import com.mysema.query.codegen.PropertyModel;
import com.mysema.query.codegen.TypeCategory;
import com.mysema.query.codegen.TypeModel;

/**
 * @author tiwe
 *
 */
@Immutable
public final class EntityElementVisitor extends SimpleElementVisitor6<BeanModel, Void>{
    
    private final ProcessingEnvironment env;
    
    private final APTModelFactory typeFactory;
    
    private final SimpleConfiguration configuration;
    
    EntityElementVisitor(ProcessingEnvironment env, SimpleConfiguration conf, APTModelFactory typeFactory){
        this.env = env;
        this.configuration = conf;
        this.typeFactory = typeFactory;
    }
    
    @Override
    public BeanModel visitType(TypeElement e, Void p) {
        Elements elementUtils = env.getElementUtils();
        Collection<String> superTypes;
        if (e.getKind() == ElementKind.CLASS){
            superTypes = Collections.singleton(typeFactory.create(e.getSuperclass(), elementUtils).getFullName());
        }else{
            superTypes = new ArrayList<String>(e.getInterfaces().size());
            for (TypeMirror mirror : e.getInterfaces()){
                TypeModel iface = typeFactory.create(mirror, elementUtils);
                if (!iface.getFullName().startsWith("java")){
                    superTypes.add(iface.getFullName());
                }
            }
        }
        TypeModel c = typeFactory.create(e.asType(), elementUtils);
        BeanModel classModel = new BeanModel(configuration.getNamePrefix(), c, superTypes);
        List<? extends Element> elements = e.getEnclosedElements();
    
        // CONSTRUCTORS
        
        for (ExecutableElement constructor : ElementFilter.constructorsIn(elements)){
            if (configuration.isValidConstructor(constructor)){
                List<ParameterModel> parameters = new ArrayList<ParameterModel>(constructor.getParameters().size());
                for (VariableElement var : constructor.getParameters()){
                    TypeModel varType = typeFactory.create(var.asType(), elementUtils);                    
                    parameters.add(new ParameterModel(classModel, var.getSimpleName().toString(), varType));
                }
                classModel.addConstructor(new ConstructorModel(parameters));    
            }                
        }  

        VisitorConfig config = configuration.getConfig(e, elements);
                
        Set<String> blockedProperties = new HashSet<String>();
        Map<String,PropertyModel> properties = new HashMap<String,PropertyModel>();
        Map<String,TypeCategory> types = new HashMap<String,TypeCategory>();
        
        // FIELDS
        
        // are fields visited ?
        if (config.isVisitFields()){
            for (VariableElement field : ElementFilter.fieldsIn(elements)){
                String name = field.getSimpleName().toString();
                // is particular field visited ?
                if (configuration.isValidField(field)){
                    try{                        
                        TypeModel typeModel = typeFactory.create(field.asType(), elementUtils);            
                        if (field.getAnnotation(QueryType.class) != null){
                            TypeCategory typeCategory = TypeCategory.get(field.getAnnotation(QueryType.class).value());
                            if (typeCategory == null){
                                blockedProperties.add(name);
                                continue;
                            }
                            typeModel = typeModel.as(typeCategory);
                            types.put(name, typeCategory);
                        }
                        String[] inits = new String[0];
                        if (field.getAnnotation(QueryInit.class) != null){
                            inits = field.getAnnotation(QueryInit.class).value();
                        }
                        properties.put(name, new PropertyModel(classModel, name, typeModel, inits));    
                    }catch(IllegalArgumentException ex){
                        StringBuilder builder = new StringBuilder();
                        builder.append("Caught exception for field ");
                        builder.append(c.getFullName()).append("#").append(field.getSimpleName());
                        throw new RuntimeException(builder.toString(), ex);
                    }
                        
                }else{
                    blockedProperties.add(name);
                }
            }    
        }
        
        // METHODS
        
        // are methods visited ?
        if (config.isVisitMethods()){
            for (ExecutableElement method : ElementFilter.methodsIn(elements)){
                String name = method.getSimpleName().toString();
                if (name.startsWith("get") && method.getParameters().isEmpty()){
                    name = StringUtils.uncapitalize(name.substring(3));
                }else if (name.startsWith("is") && method.getParameters().isEmpty()){
                    name = StringUtils.uncapitalize(name.substring(2));
                }else{
                    continue;
                }
                
                // is particular method visited ?
                if (configuration.isValidGetter(method)){
                    try{
                        TypeModel typeModel = typeFactory.create(method.getReturnType(), elementUtils);
                        if (method.getAnnotation(QueryType.class) != null){
                            TypeCategory typeCategory = TypeCategory.get(method.getAnnotation(QueryType.class).value());
                            if (typeCategory == null){
                                blockedProperties.add(name);
                                continue;
                            }else if (blockedProperties.contains(name)){
                                continue;
                            }
                            typeModel = typeModel.as(typeCategory);
                        }else if (types.containsKey(name)){
                            typeModel = typeModel.as(types.get(name));
                        }
                        String[] inits = new String[0];
                        if (method.getAnnotation(QueryInit.class) != null){
                            inits = method.getAnnotation(QueryInit.class).value();
                        }
                        properties.put(name, new PropertyModel(classModel, name, typeModel, inits));    
                        
                    }catch(IllegalArgumentException ex){
                        StringBuilder builder = new StringBuilder();
                        builder.append("Caught exception for method ");
                        builder.append(c.getFullName()).append("#").append(method.getSimpleName());
                        throw new RuntimeException(builder.toString(), ex);
                    }
                }else{
                    blockedProperties.add(name);
                }
            }   
        }
               
        
        for (Map.Entry<String,PropertyModel> entry : properties.entrySet()){
            if (!blockedProperties.contains(entry.getKey())){
                classModel.addProperty(entry.getValue());
            }
        }        
        
        return classModel;
    }
    
}
