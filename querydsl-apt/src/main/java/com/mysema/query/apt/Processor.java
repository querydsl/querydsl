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
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.JavaFileObject;
import javax.tools.Diagnostic.Kind;

import com.mysema.commons.lang.Assert;
import com.mysema.query.annotations.QueryExtensions;
import com.mysema.query.annotations.QueryMethod;
import com.mysema.query.annotations.QueryProjection;
import com.mysema.query.codegen.EntityModel;
import com.mysema.query.codegen.EntityModelFactory;
import com.mysema.query.codegen.MethodModel;
import com.mysema.query.codegen.Serializer;
import com.mysema.query.codegen.SerializerConfig;
import com.mysema.query.codegen.TypeModelFactory;

/**
 * Processor handles the actual work in the Querydsl APT module
 * 
 * @author tiwe
 * 
 */
public class Processor {
   
    private final Map<String, EntityModel> actualSupertypes  = new HashMap<String, EntityModel>();
    
    private final Map<String, EntityModel> allSupertypes = new HashMap<String, EntityModel>();
    
    private final Configuration configuration;
    
    private final Map<String, EntityModel> dtos = new HashMap<String, EntityModel>();
    
    private final Map<String,EntityModel> embeddables = new HashMap<String,EntityModel>();
    
    private final EntityModelFactory entityModelFactory;
    
    private final Map<String, EntityModel> entityTypes = new HashMap<String, EntityModel>();
    
    private final ElementHandler entityVisitor;
    
    private final ProcessingEnvironment env;
    
    private final RoundEnvironment roundEnv;
    
    private final APTTypeModelFactory typeFactory;
    
    public Processor(ProcessingEnvironment env, RoundEnvironment roundEnv, Configuration configuration) {
        this.env = Assert.notNull(env);
        this.roundEnv = Assert.notNull(roundEnv);
        this.configuration = Assert.notNull(configuration);
        
        List<Class<? extends Annotation>> anns = new ArrayList<Class<? extends Annotation>>();
        anns.add(configuration.getEntityAnn());
        if (configuration.getSuperTypeAnn() != null){
            anns.add(configuration.getSuperTypeAnn());
        }
        if (configuration.getEmbeddableAnn() != null){
            anns.add(configuration.getEmbeddableAnn());
        }
                
        TypeModelFactory factory = new TypeModelFactory(anns);
        this.typeFactory = new APTTypeModelFactory(env, configuration, factory, anns);
        if (configuration.getSkipAnn() != null){
            this.entityModelFactory = new EntityModelFactory(factory, configuration.getSkipAnn());
        }else{
            this.entityModelFactory = new EntityModelFactory(factory);
        }
        
        this.entityVisitor = new ElementHandler(configuration, typeFactory);
        
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

    public void process() {
        // types without any type level annotations
        processCustomTypes();        
        
        // query extensions
        processExtensions();
        
        // super types
        if (configuration.getSuperTypeAnn() != null) {
            processSupertypes();
            allSupertypes.putAll(actualSupertypes);
        }       
        
        // entities
        processEntities();               
        
        // embeddables
        if (configuration.getEmbeddableAnn() != null){
            processEmbeddables();            
        }           
        
        // dtos
        processDTOs();
        
        // serialize models
        serialize(configuration.getSupertypeSerializer(), actualSupertypes);
        serialize(configuration.getEntitySerializer(), entityTypes);
        serialize(configuration.getEmbeddableSerializer(), embeddables);
        serialize(configuration.getDTOSerializer(), dtos);
        
    }


    private void processExtensions() {
        for (Element element : roundEnv.getElementsAnnotatedWith(QueryExtensions.class)){
            for (AnnotationMirror annotation : element.getAnnotationMirrors()){
                if (annotation.getAnnotationType().asElement().getSimpleName().toString().equals(QueryExtensions.class.getSimpleName())){
                    for (Map.Entry<? extends ExecutableElement,? extends AnnotationValue> entry : annotation.getElementValues().entrySet()){
                        if (entry.getKey().getSimpleName().toString().equals("value")){
                            TypeMirror type = (TypeMirror)entry.getValue().getValue();
                            handleExtensionType(type, element);
                        }
                    }                    
                }
            }            
        }    
    }
    

    private void handleExtensionType(TypeMirror type, Element element) {
        EntityModel entityModel = typeFactory.createEntityModel(type);
        // handle methods
        Map<String,MethodModel> queryMethods = new HashMap<String,MethodModel>();
        for (ExecutableElement method : ElementFilter.methodsIn(element.getEnclosedElements())){
            entityVisitor.handleQueryMethod(entityModel, method, queryMethods);
        }            
        for (MethodModel method : queryMethods.values()){
            entityModel.addMethod(method);
        }
        embeddables.put(entityModel.getFullName(), entityModel); 
        
    }

    private void processCustomTypes() {
        for (Element queryMethod : roundEnv.getElementsAnnotatedWith(QueryMethod.class)){
            Element element = queryMethod.getEnclosingElement();
            if (element.getAnnotation(QueryExtensions.class) != null) continue;
            if (element.getAnnotation(configuration.getEntityAnn()) != null) continue;
            if (configuration.getSuperTypeAnn() != null && element.getAnnotation(configuration.getSuperTypeAnn()) != null) continue;
            if (configuration.getEmbeddableAnn() != null && element.getAnnotation(configuration.getEmbeddableAnn()) != null) continue;
            handleExtensionType(element.asType(), element);
        }
    }

    private void processDTOs() {
        Set<Element> visitedDTOTypes = new HashSet<Element>();
        for (Element element : roundEnv.getElementsAnnotatedWith(QueryProjection.class)) {
            Element parent = element.getEnclosingElement();
            if (parent.getAnnotation(configuration.getEntityAnn()) == null
                    && parent.getAnnotation(configuration.getEmbeddableAnn()) == null
                    && !visitedDTOTypes.contains(parent)){
                EntityModel model = entityVisitor.handleProjectionType((TypeElement)parent);
                dtos.put(model.getFullName(), model);    
                visitedDTOTypes.add(parent);
                
            }            
        }
    }

    private void processEmbeddables() {        
        for (Element element : roundEnv.getElementsAnnotatedWith(configuration.getEmbeddableAnn())) {
            EntityModel model = entityVisitor.handleNormalType((TypeElement) element);
            embeddables.put(model.getFullName(), model);
        }  
        allSupertypes.putAll(embeddables);
        
        // add super type fields
        for (EntityModel embeddable : embeddables.values()) {
            addSupertypeFields(embeddable, allSupertypes);
        }
    }

    private void processEntities() {
        // populate entity type mappings

        for (Element element : roundEnv.getElementsAnnotatedWith(configuration.getEntityAnn())) {
            if (configuration.getEmbeddableAnn() == null || element.getAnnotation(configuration.getEmbeddableAnn()) == null){
                EntityModel model = entityVisitor.handleNormalType((TypeElement) element);
                entityTypes.put(model.getFullName(), model);    
            }            
        }
        allSupertypes.putAll(entityTypes);
        
        // add super type fields
        for (EntityModel entityType : entityTypes.values()) {
            addSupertypeFields(entityType, allSupertypes);
        }
    }
       
    private void processSupertypes() {
        for (Element element : roundEnv.getElementsAnnotatedWith(configuration.getSuperTypeAnn())) {
            if (configuration.getEmbeddableAnn() == null || element.getAnnotation(configuration.getEmbeddableAnn()) == null){
                EntityModel model = entityVisitor.handleNormalType((TypeElement) element);
                actualSupertypes.put(model.getFullName(), model);    
            }                
        }
        // add supertype fields
        for (EntityModel superType : actualSupertypes.values()) {
            addSupertypeFields(superType, actualSupertypes);      
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

    private void serialize(Serializer serializer, Map<String, EntityModel> models) {
        Messager msg = env.getMessager();
        for (EntityModel model : models.values()) {
            msg.printMessage(Kind.NOTE, model.getFullName() + " is processed");
            try {
                String packageName = model.getPackageName();         
                String localName = configuration.getTypeMappings().getPathType(model, model, true);
                String className = packageName + "." + localName;
                JavaFileObject fileObject = env.getFiler().createSourceFile(className);
                Writer writer = fileObject.openWriter();
                try {
                    SerializerConfig serializerConfig = configuration.getSerializerConfig(model);
                    serializer.serialize(model, serializerConfig, writer);    
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
