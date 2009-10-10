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
import java.util.List;
import java.util.Map;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.tools.JavaFileObject;
import javax.tools.Diagnostic.Kind;

import net.jcip.annotations.Immutable;

import com.mysema.commons.lang.Assert;
import com.mysema.query.codegen.BeanModel;
import com.mysema.query.codegen.BeanModelFactory;
import com.mysema.query.codegen.Serializer;
import com.mysema.query.codegen.Serializers;
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
    
    private final String namePrefix = "Q";
    
    private final Configuration conf;
    
    private final BeanModelFactory classModelFactory;
    
    @SuppressWarnings("unchecked")
    public Processor(ProcessingEnvironment env, Configuration configuration) {
        this.conf = configuration;
        List<Class<? extends Annotation>> anns ;
        if (conf.getEmbeddableAnn() != null){
            anns = Arrays.<Class<? extends Annotation>>asList(conf.getEntityAnn(), conf.getEmbeddableAnn());
        }else{
            anns = Arrays.<Class<? extends Annotation>>asList(conf.getEntityAnn());            
        }
        this.env = Assert.notNull(env);        
        TypeModelFactory factory = new TypeModelFactory(anns);
        this.typeFactory = new APTModelFactory(factory, anns);
        if (conf.getSkipAnn() != null){
            this.classModelFactory = new BeanModelFactory(factory, conf.getSkipAnn());
        }else{
            this.classModelFactory = new BeanModelFactory(factory);
        }
        
    }
    
    @SuppressWarnings("unchecked")
    public void process(RoundEnvironment roundEnv) {
        Map<String, BeanModel> superTypes = new HashMap<String, BeanModel>();

        EntityElementVisitor entityVisitor = new EntityElementVisitor(env, conf, namePrefix, typeFactory);
        
        // populate super type mappings
        if (conf.getSuperTypeAnn() != null) {
            for (Element element : roundEnv.getElementsAnnotatedWith(conf.getSuperTypeAnn())) {
                BeanModel model = element.accept(entityVisitor, null);
                model.setEntityModel(false);
                superTypes.put(model.getName(), model);
            }
            // add supertype fields
            for (BeanModel superType : superTypes.values()) {
                addSupertypeFields(superType, superTypes);      
            }
            // serialize supertypes
            if (!superTypes.isEmpty()){
                serialize(Serializers.SUPERTYPE, superTypes);
            }
        }

        // ENTITIES
        
        // populate entity type mappings
        Map<String, BeanModel> entityTypes = new HashMap<String, BeanModel>();
        for (Element element : roundEnv.getElementsAnnotatedWith(conf.getEntityAnn())) {
            BeanModel model = element.accept(entityVisitor, null);
            entityTypes.put(model.getName(), model);
        }
        // add super type fields
        for (BeanModel entityType : entityTypes.values()) {
            addSupertypeFields(entityType, superTypes, entityTypes);
        }
        // serialize entity types
        if (!entityTypes.isEmpty()) {
            serialize(Serializers.ENTITY, entityTypes);
        }
        
        // EMBEDDABLES (optional)
        
        if (conf.getEmbeddableAnn() != null){
            // populate entity type mappings
            Map<String, BeanModel> embeddables = new HashMap<String, BeanModel>();
            
            for (Element element : roundEnv.getElementsAnnotatedWith(conf.getEmbeddableAnn())) {
                BeanModel model = element.accept(entityVisitor, null);
                embeddables.put(model.getName(), model);
            }  
            
            // add super type fields
            for (BeanModel embeddable : embeddables.values()) {
                addSupertypeFields(embeddable, superTypes, embeddables);
            }
            // serialize entity types
            if (!embeddables.isEmpty()) {
                serialize(Serializers.EMBEDDABLE, embeddables);
            }            
        }

        // DTOS (optional)
        
        if (conf.getDtoAnn() != null){
            DTOElementVisitor dtoVisitor = new DTOElementVisitor(env, conf, namePrefix, typeFactory);
            Map<String, BeanModel> dtos = new HashMap<String, BeanModel>();
            for (Element element : roundEnv.getElementsAnnotatedWith(conf.getDtoAnn())) {
                BeanModel model = element.accept(dtoVisitor, null);
                dtos.put(model.getName(), model);
            }
            // serialize entity types
            if (!dtos.isEmpty()) {
                serialize(Serializers.DTO, dtos);
            }    
        }         
        
    }
        
    private void addSupertypeFields(BeanModel model, Map<String,BeanModel>... superTypes){
        String stype = model.getSupertypeName();
        // iterate over supertypes
        for (Map<String,BeanModel> stypes : superTypes){
            if (stypes.containsKey(stype)) {
                while (true) {
                    BeanModel sdecl;
                    if (stypes.containsKey(stype)) {
                        sdecl = stypes.get(stype);
                    } else {
                        break;
                    }
                    if (stype.equals(model.getSupertypeName())){
                        model.setSuperModel(sdecl);    
                    }               
                    if (sdecl.isEntityModel()){
                        model.include(sdecl);    
                    }                    
                    stype = sdecl.getSupertypeName();
                }
            } 
        }
        
        // create super class model via reflection
        if (model.getSuperModel() == null){
            stype = model.getSupertypeName();
            Class<?> superClass = safeClassForName(stype);
            if (superClass != null && !superClass.equals(Object.class)) {
                // handle the supertype only, if it has the proper annotations
                if((conf.getSuperTypeAnn() == null 
                    || superClass.getAnnotation(conf.getSuperTypeAnn()) != null)
                    || superClass.getAnnotation(conf.getEntityAnn()) != null){
                    BeanModel type = classModelFactory.create(superClass, namePrefix);
                    // include fields of supertype
                    model.include(type);    
                }            
            }
        }
    }
    
    private static Class<?> safeClassForName(String stype) {
        try {
            return stype != null ? Class.forName(stype) : null;
        } catch (ClassNotFoundException e) {
            return null;
        }
    } 
        
    private void serialize(Serializer serializer, Map<String, BeanModel> types) {
        Messager msg = env.getMessager();
        for (BeanModel type : types.values()) {
            msg.printMessage(Kind.NOTE, type.getName() + " is processed");
            try {
                String packageName = type.getPackageName();                    
                String className = packageName + "." + namePrefix + type.getSimpleName();
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
                msg.printMessage(Kind.ERROR, e.getMessage());
            }
        }
    }

}
