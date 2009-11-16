/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt;

import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.tools.JavaFileObject;
import javax.tools.Diagnostic.Kind;

import net.jcip.annotations.Immutable;

import com.mysema.commons.lang.Assert;
import com.mysema.query.annotations.QueryProjection;
import com.mysema.query.codegen.BeanModel;
import com.mysema.query.codegen.BeanModelFactory;
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
    
    private final APTModelFactory typeFactory;
    
    private final SimpleConfiguration conf;
    
    private final BeanModelFactory classModelFactory;
    
    @SuppressWarnings("unchecked")
    public Processor(ProcessingEnvironment env, SimpleConfiguration configuration) {
        this.conf = configuration;
        List<Class<? extends Annotation>> anns ;
        if (conf.getEmbeddableAnn() != null){
            anns = Arrays.<Class<? extends Annotation>>asList(conf.getEntityAnn(), conf.getEmbeddableAnn());
        }else{
            anns = Arrays.<Class<? extends Annotation>>asList(conf.getEntityAnn());            
        }
        this.env = Assert.notNull(env);        
        TypeModelFactory factory = new TypeModelFactory(anns);
        this.typeFactory = new APTModelFactory(env, factory, anns);
        if (conf.getSkipAnn() != null){
            this.classModelFactory = new BeanModelFactory(factory, conf.getSkipAnn());
        }else{
            this.classModelFactory = new BeanModelFactory(factory);
        }
        
    }
    
    public void process(RoundEnvironment roundEnv) {
        Map<String, BeanModel> superTypes = new HashMap<String, BeanModel>();
        EntityElementVisitor entityVisitor = new EntityElementVisitor(env, conf, typeFactory);
        
        // supertypes
        if (conf.getSuperTypeAnn() != null) {
            handleSupertypes(roundEnv, superTypes, entityVisitor);
        }
        
        // entities
        handleEntities(roundEnv, superTypes, entityVisitor);
        
        // embeddables
        if (conf.getEmbeddableAnn() != null){
            handleEmbeddables(roundEnv, superTypes, entityVisitor);            
        }

        // projections
        DTOElementVisitor dtoVisitor = new DTOElementVisitor(env, conf, typeFactory);
        Map<String, BeanModel> dtos = new HashMap<String, BeanModel>();
        Set<Element> visitedDTOTypes = new HashSet<Element>();
        for (Element element : roundEnv.getElementsAnnotatedWith(QueryProjection.class)) {
            Element parent = element.getEnclosingElement();
            if (parent.getAnnotation(conf.getEntityAnn()) == null
                    && parent.getAnnotation(conf.getEmbeddableAnn()) == null
                    && !visitedDTOTypes.contains(parent)){
                BeanModel model = parent.accept(dtoVisitor, null);
                dtos.put(model.getFullName(), model);    
                visitedDTOTypes.add(parent);
                
            }            
        }
        // serialize entity types
        if (!dtos.isEmpty()) {
            serialize(conf.getDTOSerializer(), dtos);
        }     
        
    }

    private void handleSupertypes(RoundEnvironment roundEnv,
            Map<String, BeanModel> superTypes,
            EntityElementVisitor entityVisitor) {
        for (Element element : roundEnv.getElementsAnnotatedWith(conf.getSuperTypeAnn())) {
            if (conf.getEmbeddableAnn() == null || element.getAnnotation(conf.getEmbeddableAnn()) == null){
                BeanModel model = element.accept(entityVisitor, null);
                superTypes.put(model.getFullName(), model);    
            }                
        }
        // add supertype fields
        for (BeanModel superType : superTypes.values()) {
            addSupertypeFields(superType, superTypes);      
        }
        // serialize supertypes
        if (!superTypes.isEmpty()){
            serialize(conf.getSupertypeSerializer(), superTypes);
        }
    }

    private void handleEmbeddables(RoundEnvironment roundEnv,
            Map<String, BeanModel> superTypes,
            EntityElementVisitor entityVisitor) {
        // populate entity type mappings
        Map<String, BeanModel> embeddables = new HashMap<String, BeanModel>();
        
        for (Element element : roundEnv.getElementsAnnotatedWith(conf.getEmbeddableAnn())) {
            BeanModel model = element.accept(entityVisitor, null);
            embeddables.put(model.getFullName(), model);
        }  
        superTypes.putAll(embeddables);
        
        // add super type fields
        for (BeanModel embeddable : embeddables.values()) {
            addSupertypeFields(embeddable, superTypes);
        }
        // serialize entity types
        if (!embeddables.isEmpty()) {
            serialize(conf.getEmbeddableSerializer(), embeddables);
        }
    }

    private void handleEntities(RoundEnvironment roundEnv,
            Map<String, BeanModel> superTypes,
            EntityElementVisitor entityVisitor) {
        // populate entity type mappings
        Map<String, BeanModel> entityTypes = new HashMap<String, BeanModel>();
        for (Element element : roundEnv.getElementsAnnotatedWith(conf.getEntityAnn())) {
            if (conf.getEmbeddableAnn() == null || element.getAnnotation(conf.getEmbeddableAnn()) == null){
                BeanModel model = element.accept(entityVisitor, null);
                entityTypes.put(model.getFullName(), model);    
            }            
        }
        superTypes.putAll(entityTypes);
        
        // add super type fields
        for (BeanModel entityType : entityTypes.values()) {
            addSupertypeFields(entityType, superTypes);
        }
        // serialize entity types
        if (!entityTypes.isEmpty()) {
            serialize(conf.getEntitySerializer(), entityTypes);
        }
    }
        
    private void addSupertypeFields(BeanModel model, Map<String, BeanModel> superTypes) {
        boolean singleSuperType = model.getSuperTypes().size() == 1;
        for (String stype : model.getSuperTypes()) {
            // iterate over supertypes
            if (superTypes.containsKey(stype)) {
                Stack<String> stypeStack = new Stack<String>();
                stypeStack.push(stype);
                while (!stypeStack.isEmpty()) {
                    String top = stypeStack.pop();
                    if (superTypes.containsKey(top)) {
                        BeanModel sdecl = superTypes.get(top);
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
                if ((conf.getSuperTypeAnn() == null 
                        || superClass.getAnnotation(conf.getSuperTypeAnn()) != null)
                        || superClass.getAnnotation(conf.getEntityAnn()) != null) {
                    BeanModel type = classModelFactory.create(superClass, conf.getNamePrefix());
                    // include fields of supertype
                    model.include(type);
                }
            }
        }
    }
            
    private Class<?> safeClassForName(String stype) {
        try {
            return Class.forName(stype);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private void serialize(Serializer serializer, Map<String, BeanModel> types) {
        Messager msg = env.getMessager();
        for (BeanModel type : types.values()) {
            msg.printMessage(Kind.NOTE, type.getFullName() + " is processed");
            try {
                String packageName = type.getPackageName();                    
                String className = packageName + "." + conf.getNamePrefix() + type.getSimpleName();
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
