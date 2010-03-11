/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt;

import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.JavaFileObject;
import javax.tools.Diagnostic.Kind;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.commons.lang.Assert;
import com.mysema.query.annotations.QueryExtensions;
import com.mysema.query.annotations.QueryMethod;
import com.mysema.query.annotations.QueryProjection;
import com.mysema.query.annotations.QuerydslVariables;
import com.mysema.query.codegen.EntityType;
import com.mysema.query.codegen.Method;
import com.mysema.query.codegen.Serializer;
import com.mysema.query.codegen.SerializerConfig;
import com.mysema.query.codegen.TypeFactory;
import com.mysema.query.codegen.TypeMappings;
import com.mysema.util.JavaWriter;

/**
 * Processor handles the actual work in the Querydsl APT module
 * 
 * @author tiwe
 * 
 */
public class Processor {
    
    private static final Logger logger = LoggerFactory
        .getLogger(Processor.class);
   
    private final Map<String, EntityType> actualSupertypes  = new HashMap<String, EntityType>();
    
    private final Map<String, EntityType> allSupertypes = new HashMap<String, EntityType>();
    
    private final Configuration configuration;
    
    private final Map<String, EntityType> dtos = new HashMap<String, EntityType>();
    
    private final Map<String,EntityType> extensionTypes = new HashMap<String,EntityType>();
    
    private final Map<String,EntityType> embeddables = new HashMap<String,EntityType>();
    
    private final Map<String, EntityType> entityTypes = new HashMap<String, EntityType>();
    
    private final ElementHandler elementHandler;
    
    private final ProcessingEnvironment env;
    
    private final RoundEnvironment roundEnv;
    
    private final APTTypeFactory typeModelFactory;
    
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
                
        TypeFactory factory = new TypeFactory(anns);
        this.typeModelFactory = new APTTypeFactory(env, configuration, factory, anns);        
        this.elementHandler = new ElementHandler(configuration, typeModelFactory);
        
    }
    
    private void addSupertypeFields(EntityType model, Map<String, EntityType> superTypes) {
        Stack<EntityType> stypeStack = new Stack<EntityType>();
        for (EntityType stype : model.getSuperTypes()) {
            stypeStack.push(stype);
            while (!stypeStack.isEmpty()) {
                EntityType sdecl = stypeStack.pop();
                model.include(sdecl);
                for (EntityType type : sdecl.getSuperTypes()) {
                    stypeStack.push(type);
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
        
        // remove entity types from embeddables
        for (String key : entityTypes.keySet()){
            extensionTypes.remove(key);
        }
        
        // serialize models
        serialize(configuration.getSupertypeSerializer(), actualSupertypes);
        serialize(configuration.getEntitySerializer(), entityTypes);
        serialize(configuration.getEmbeddableSerializer(), extensionTypes);
        serialize(configuration.getEmbeddableSerializer(), embeddables);
        serialize(configuration.getDTOSerializer(), dtos);
        
        // serialize variable classes
        for (Element element : roundEnv.getElementsAnnotatedWith(QuerydslVariables.class)){
            if (element instanceof PackageElement){
                QuerydslVariables vars = element.getAnnotation(QuerydslVariables.class);
                PackageElement packageElement = (PackageElement)element;
                List<EntityType> models = new ArrayList<EntityType>();
                for (EntityType model : entityTypes.values()){
                    if (model.getPackageName().equals(packageElement.getQualifiedName().toString())){
                        models.add(model);
                    }
                }
                serializeVariableList(packageElement.getQualifiedName().toString(), vars, models);
            }
        }        
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
        EntityType entityModel = typeModelFactory.createEntityType(type);
        // handle methods
        Set<Method> queryMethods = new HashSet<Method>();
        for (ExecutableElement method : ElementFilter.methodsIn(element.getEnclosedElements())){
            if (method.getAnnotation(QueryMethod.class) != null){
                elementHandler.handleQueryMethod(entityModel, method, queryMethods);    
            }            
        }            
        for (Method method : queryMethods){
            entityModel.addMethod(method);
        }
        extensionTypes.put(entityModel.getFullName(), entityModel); 
        
    }

    private void processCustomTypes() {
        for (Element queryMethod : roundEnv.getElementsAnnotatedWith(QueryMethod.class)){
            Element element = queryMethod.getEnclosingElement();
            if (element.getAnnotation(QueryExtensions.class) != null){
                continue;
            }
            if (element.getAnnotation(configuration.getEntityAnn()) != null){
                continue;
            }
            if (configuration.getSuperTypeAnn() != null && element.getAnnotation(configuration.getSuperTypeAnn()) != null){
                continue;
            }
            if (configuration.getEmbeddableAnn() != null && element.getAnnotation(configuration.getEmbeddableAnn()) != null){
                continue;
            }
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
                EntityType model = elementHandler.handleProjectionType((TypeElement)parent);
                dtos.put(model.getFullName(), model);    
                visitedDTOTypes.add(parent);
                
            }            
        }
    }

    private void processEmbeddables() {        
        for (Element element : roundEnv.getElementsAnnotatedWith(configuration.getEmbeddableAnn())) {
            EntityType model = elementHandler.handleNormalType((TypeElement) element);
            embeddables.put(model.getFullName(), model);
        }  
        allSupertypes.putAll(embeddables);
        
        // add super type fields
        for (EntityType embeddable : embeddables.values()) {
            addSupertypeFields(embeddable, allSupertypes);
        }
    }

    private void processEntities() {
        // populate entity type mappings
        Set<EntityType> superTypes = new HashSet<EntityType>();
        
        for (Element element : roundEnv.getElementsAnnotatedWith(configuration.getEntityAnn())) {
            if (configuration.getEmbeddableAnn() == null || element.getAnnotation(configuration.getEmbeddableAnn()) == null){
                EntityType model = elementHandler.handleNormalType((TypeElement) element);
                entityTypes.put(model.getFullName(), model);
                if (model.getSuperType() != null){
                    superTypes.add(model.getSuperType());
                }
            }            
        }
        
        for (EntityType superType : superTypes){
            if (!allSupertypes.containsKey(superType.getFullName()) 
             && !entityTypes.containsKey(superType.getFullName())){
                TypeElement typeElement = env.getElementUtils().getTypeElement(superType.getFullName());
                elementHandler.handleNormalType(superType, typeElement);
                entityTypes.put(superType.getFullName(), superType);
            }
        }        
        
        allSupertypes.putAll(entityTypes);
        
        // add super type fields
        for (EntityType entityType : entityTypes.values()) {
            addSupertypeFields(entityType, allSupertypes);
        }
    }
       
    private void processSupertypes() {
        for (Element element : roundEnv.getElementsAnnotatedWith(configuration.getSuperTypeAnn())) {
            if (configuration.getEmbeddableAnn() == null || element.getAnnotation(configuration.getEmbeddableAnn()) == null){
                EntityType model = elementHandler.handleNormalType((TypeElement) element);
                actualSupertypes.put(model.getFullName(), model);    
            }                
        }
        // add supertype fields
        for (EntityType superType : actualSupertypes.values()) {
            addSupertypeFields(superType, actualSupertypes);      
        }
    }
      
    private void serializeVariableList(String packageName, QuerydslVariables vars, List<EntityType> models){
        String className = packageName + "." + vars.value();
        TypeMappings typeMappings = configuration.getTypeMappings();             
        try{
            JavaFileObject fileObject = env.getFiler().createSourceFile(className);
            Writer w = fileObject.openWriter();   
            try{
                JavaWriter writer = new JavaWriter(w);
                writer.packageDecl(packageName);
                writer.nl();
                if (vars.asInterface()){
                    writer.beginInterface(vars.value());
                }else{
                    writer.beginClass(vars.value(), null);    
                }                
                for (EntityType model : models){
                    String queryType = typeMappings.getPathType(model, model, true);          
                    String simpleName = model.getUncapSimpleName();
                    writer.publicStaticFinal(queryType, simpleName, "new " + queryType + "(\"" + simpleName + "\")");
                }
                writer.end();
            }finally{
                w.close();
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            env.getMessager().printMessage(Kind.ERROR, e.getMessage());
        }

    }

    private void serialize(Serializer serializer, Map<String, EntityType> models) {
        Messager msg = env.getMessager();
        for (EntityType model : models.values()) {
            msg.printMessage(Kind.NOTE, model.getFullName() + " is processed");
            try {
                String packageName = model.getPackageName();         
                String localName = configuration.getTypeMappings().getPathType(model, model, true);
                String className = packageName + "." + localName;
                JavaFileObject fileObject = env.getFiler().createSourceFile(className);
                Writer writer = fileObject.openWriter();
                try {
                    SerializerConfig serializerConfig = configuration.getSerializerConfig(model);
                    serializer.serialize(model, serializerConfig, new JavaWriter(writer));    
                }finally{
                    if (writer != null) {
                        writer.close();
                    }
                }                
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                msg.printMessage(Kind.ERROR, e.getMessage());
            }
        }
    }

}
