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
import java.util.Map;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.tools.JavaFileObject;
import javax.tools.Diagnostic.Kind;

import net.jcip.annotations.Immutable;

import com.mysema.commons.lang.Assert;
import com.mysema.query.codegen.ClassModel;
import com.mysema.query.codegen.ClassModelFactory;
import com.mysema.query.codegen.Serializer;
import com.mysema.query.codegen.Serializers;
import com.mysema.query.codegen.TypeModelFactory;

/**
 * 
 * @author tiwe
 * 
 */
@Immutable
public class Processor {
    
    private final ProcessingEnvironment env;
    
    private final APTModelFactory typeFactory;
    
    private final ClassModelFactory classModelFactory;
    
    private final String namePrefix = "Q";
    
    private final Configuration conf;
    
    @SuppressWarnings("unchecked")
    public Processor(ProcessingEnvironment env, Configuration configuration) {
        this.conf = configuration;
        Class<? extends Annotation>[] anns ;
        if (conf.getEmbeddableAnn() != null){
            anns = new Class[]{conf.getEntityAnn(), conf.getEmbeddableAnn()};
        }else{
            anns = new Class[]{conf.getEntityAnn()};            
        }
        this.env = Assert.notNull(env);        
        TypeModelFactory factory = new TypeModelFactory(anns);
        this.typeFactory = new APTModelFactory(factory, Arrays.asList(anns));
        this.classModelFactory = new ClassModelFactory(factory);
    }
    
    public void process(RoundEnvironment roundEnv) {
        Map<String, ClassModel> superTypes = new HashMap<String, ClassModel>();

        EntityElementVisitor entityVisitor = new EntityElementVisitor(env, conf, namePrefix, classModelFactory, typeFactory);
        
        // populate super type mappings
        if (conf.getSuperTypeAnn() != null) {
            for (Element element : roundEnv.getElementsAnnotatedWith(conf.getSuperTypeAnn())) {
                ClassModel model = element.accept(entityVisitor, null);
                superTypes.put(model.getName(), model);
            }
            
            // serialize supertypes
            if (!superTypes.isEmpty()){
                serialize(Serializers.SUPERTYPE, superTypes);
            }
        }

        // ENTITIES
        
        // populate entity type mappings
        Map<String, ClassModel> entityTypes = new HashMap<String, ClassModel>();
        for (Element element : roundEnv.getElementsAnnotatedWith(conf.getEntityAnn())) {
            ClassModel model = element.accept(entityVisitor, null);
            entityTypes.put(model.getName(), model);
        }
        // add super type fields
        for (ClassModel entityType : entityTypes.values()) {
            entityType.addSupertypeFields(entityTypes, superTypes);
        }
        // serialize entity types
        if (!entityTypes.isEmpty()) {
            serialize(Serializers.ENTITY, entityTypes);
        }
        
        // EMBEDDABLES (optional)
        
        if (conf.getEmbeddableAnn() != null){
            // populate entity type mappings
            Map<String, ClassModel> embeddables = new HashMap<String, ClassModel>();
            for (Element element : roundEnv.getElementsAnnotatedWith(conf.getEmbeddableAnn())) {
                ClassModel model = element.accept(entityVisitor, null);
                embeddables.put(model.getName(), model);
            }
            // add super type fields
            for (ClassModel embeddable : embeddables.values()) {
                embeddable.addSupertypeFields(embeddables, superTypes);
            }
            // serialize entity types
            if (!embeddables.isEmpty()) {
                serialize(Serializers.EMBEDDABLE, embeddables);
            }            
        }

        // DTOS (optional)
        
        if (conf.getDtoAnn() != null){
            DTOElementVisitor dtoVisitor = new DTOElementVisitor(env, conf, namePrefix, classModelFactory, typeFactory);
            Map<String, ClassModel> dtos = new HashMap<String, ClassModel>();
            for (Element element : roundEnv.getElementsAnnotatedWith(conf.getDtoAnn())) {
                ClassModel model = element.accept(dtoVisitor, null);
                dtos.put(model.getName(), model);
            }
            // serialize entity types
            if (!dtos.isEmpty()) {
                serialize(Serializers.DTO, dtos);
            }    
        }         
        
    }
    
    private void serialize(Serializer serializer, Map<String, ClassModel> types) {
        Messager msg = env.getMessager();
        for (ClassModel type : types.values()) {
            msg.printMessage(Kind.NOTE, type.getName() + " is processed");
            try {
                String packageName = type.getPackageName();                    
                String className = packageName + "." + namePrefix + type.getSimpleName();
                JavaFileObject fileObject = env.getFiler().createSourceFile(className);
                Writer writer = fileObject.openWriter();
                try {
                    serializer.serialize(type, writer);    
                }finally{
                    if (writer != null) writer.close();
                }                
            } catch (Exception e) {
                msg.printMessage(Kind.ERROR, e.getMessage());
            }
        }
    }

}
