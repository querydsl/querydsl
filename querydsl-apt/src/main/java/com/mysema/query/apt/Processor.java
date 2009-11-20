/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt;

import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import javax.annotation.Nullable;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.tools.JavaFileObject;
import javax.tools.Diagnostic.Kind;

import net.jcip.annotations.Immutable;

import com.mysema.commons.lang.Assert;
import com.mysema.query.annotations.QueryProjection;
import com.mysema.query.codegen.EntityModel;
import com.mysema.query.codegen.EntityModelFactory;
import com.mysema.query.codegen.Serializer;
import com.mysema.query.codegen.TypeModelFactory;

/**
 * Processor handles the actual work in the Querydsl APT module
 * 
 * @author tiwe
 * 
 */
@Immutable
public class Processor {
    
    private final ProcessingEnvironment env;
    
    private final APTTypeModelFactory typeFactory;
    
    private final Configuration configuration;
    
    private final EntityModelFactory entityModelFactory;
    
    public Processor(ProcessingEnvironment env, Configuration configuration) {
        this.configuration = configuration;
        List<Class<? extends Annotation>> anns = new ArrayList<Class<? extends Annotation>>();
        anns.add(configuration.getEntityAnn());
        if (configuration.getSuperTypeAnn() != null){
            anns.add(configuration.getSuperTypeAnn());
        }
        if (configuration.getEmbeddableAnn() != null){
            anns.add(configuration.getEmbeddableAnn());
        }
        this.env = Assert.notNull(env);        
        TypeModelFactory factory = new TypeModelFactory(anns);
        this.typeFactory = new APTTypeModelFactory(env, configuration, factory, anns);
        if (configuration.getSkipAnn() != null){
            this.entityModelFactory = new EntityModelFactory(factory, configuration.getSkipAnn());
        }else{
            this.entityModelFactory = new EntityModelFactory(factory);
        }
        
    }
    
    /**
     * Do the actual processing
     * 
     * @param roundEnv
     */
    public void process(RoundEnvironment roundEnv) {
        Map<String, EntityModel> actualSupertypes  = new HashMap<String, EntityModel>();
        Map<String, EntityModel> allSupertypes = new HashMap<String, EntityModel>();
        Map<String, EntityModel> entityTypes = new HashMap<String, EntityModel>();
        Map<String,EntityModel> embeddables = new HashMap<String,EntityModel>();
        Map<String, EntityModel> dtos = new HashMap<String, EntityModel>();
        
        EntityElementVisitor entityVisitor = new EntityElementVisitor(env, configuration, typeFactory);
        DTOElementVisitor dtoVisitor = new DTOElementVisitor(env, configuration, typeFactory);        
        
        // create models
        if (configuration.getSuperTypeAnn() != null) {
            handleSupertypes(roundEnv, actualSupertypes, entityVisitor);
            allSupertypes.putAll(actualSupertypes);
        }                        
        handleEntities(roundEnv, allSupertypes, entityTypes, entityVisitor);               
        if (configuration.getEmbeddableAnn() != null){
            handleEmbeddables(roundEnv, allSupertypes, embeddables, entityVisitor);            
        }           
        handleDTOs(roundEnv, dtos, dtoVisitor);
        
        // serialize models
        serialize(configuration.getSupertypeSerializer(), actualSupertypes);
        serialize(configuration.getEntitySerializer(), entityTypes);
        serialize(configuration.getEmbeddableSerializer(), embeddables);
        serialize(configuration.getDTOSerializer(), dtos);
        
    }

    private void handleDTOs(RoundEnvironment roundEnv,
            Map<String, EntityModel> dtos, DTOElementVisitor dtoVisitor) {
        Set<Element> visitedDTOTypes = new HashSet<Element>();
        for (Element element : roundEnv.getElementsAnnotatedWith(QueryProjection.class)) {
            Element parent = element.getEnclosingElement();
            if (parent.getAnnotation(configuration.getEntityAnn()) == null
                    && parent.getAnnotation(configuration.getEmbeddableAnn()) == null
                    && !visitedDTOTypes.contains(parent)){
                EntityModel model = parent.accept(dtoVisitor, null);
                dtos.put(model.getFullName(), model);    
                visitedDTOTypes.add(parent);
                
            }            
        }
    }

    private void handleSupertypes(RoundEnvironment roundEnv,
            Map<String, EntityModel> superTypes,
            EntityElementVisitor entityVisitor) {
        for (Element element : roundEnv.getElementsAnnotatedWith(configuration.getSuperTypeAnn())) {
            if (configuration.getEmbeddableAnn() == null || element.getAnnotation(configuration.getEmbeddableAnn()) == null){
                EntityModel model = element.accept(entityVisitor, null);
                superTypes.put(model.getFullName(), model);    
            }                
        }
        // add supertype fields
        for (EntityModel superType : superTypes.values()) {
            addSupertypeFields(superType, superTypes);      
        }
    }

    private void handleEmbeddables(RoundEnvironment roundEnv,
            Map<String, EntityModel> superTypes,
            Map<String, EntityModel> embeddables,
            EntityElementVisitor entityVisitor) {        
        for (Element element : roundEnv.getElementsAnnotatedWith(configuration.getEmbeddableAnn())) {
            EntityModel model = element.accept(entityVisitor, null);
            embeddables.put(model.getFullName(), model);
        }  
        superTypes.putAll(embeddables);
        
        // add super type fields
        for (EntityModel embeddable : embeddables.values()) {
            addSupertypeFields(embeddable, superTypes);
        }
    }

    private void handleEntities(RoundEnvironment roundEnv,
            Map<String, EntityModel> superTypes,
            Map<String, EntityModel> entityTypes,
            EntityElementVisitor entityVisitor) {
        // populate entity type mappings

        for (Element element : roundEnv.getElementsAnnotatedWith(configuration.getEntityAnn())) {
            if (configuration.getEmbeddableAnn() == null || element.getAnnotation(configuration.getEmbeddableAnn()) == null){
                EntityModel model = element.accept(entityVisitor, null);
                entityTypes.put(model.getFullName(), model);    
            }            
        }
        superTypes.putAll(entityTypes);
        
        // add super type fields
        for (EntityModel entityType : entityTypes.values()) {
            addSupertypeFields(entityType, superTypes);
        }
    }
        
    private void addSupertypeFields(EntityModel model, Map<String, EntityModel> superTypes) {
        boolean singleSuperType = model.getSuperTypes().size() == 1;
        for (String stype : model.getSuperTypes()) {
            // iterate over supertypes
            if (superTypes.containsKey(stype)) {
                Stack<String> stypeStack = new Stack<String>();
                stypeStack.push(stype);
                while (!stypeStack.isEmpty()) {
                    String top = stypeStack.pop();
                    if (superTypes.containsKey(top)) {
                        EntityModel sdecl = superTypes.get(top);
                        if (singleSuperType && model.getSuperTypes().contains(top)) {
                            model.setSuperModel(sdecl);
                        }
                        model.include(sdecl);
                        for (String type : sdecl.getSuperTypes()) {
                            stypeStack.push(type);
                        }
                    }
                }
            }
        }

        // create super class model via reflection
        if (model.getSuperModel() == null && singleSuperType) {
            String stype = model.getSuperTypes().iterator().next();
            Class<?> superClass = safeClassForName(stype);
            if (superClass != null && !superClass.equals(Object.class)) {
                // handle the supertype only, if it has the proper annotations
                if ((configuration.getSuperTypeAnn() == null 
                        || superClass.getAnnotation(configuration.getSuperTypeAnn()) != null)
                        || superClass.getAnnotation(configuration.getEntityAnn()) != null) {
                    EntityModel type = entityModelFactory.create(superClass, configuration.getNamePrefix());
                    // include fields of supertype
                    model.include(type);
                }
            }
        }
    }
            
    @Nullable
    private Class<?> safeClassForName(String stype) {
        try {
            return Class.forName(stype);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private void serialize(Serializer serializer, Map<String, EntityModel> types) {
        Messager msg = env.getMessager();
        for (EntityModel type : types.values()) {
            msg.printMessage(Kind.NOTE, type.getFullName() + " is processed");
            try {
                String packageName = type.getPackageName();                    
                String className = packageName + "." + configuration.getNamePrefix() + type.getSimpleName();
                JavaFileObject fileObject = env.getFiler().createSourceFile(className);
                Writer writer = fileObject.openWriter();
                try {
                    serializer.serialize(type, writer);    
                }finally{
                    if (writer != null) {
                        writer.close();
                    }
                }                
            } catch (Exception e) {
                e.printStackTrace();
                msg.printMessage(Kind.ERROR, e.getMessage());
            }
        }
    }

}
