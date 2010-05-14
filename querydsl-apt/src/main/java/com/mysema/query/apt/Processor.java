/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt;

import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import com.mysema.codegen.JavaWriter;
import com.mysema.commons.lang.Assert;
import com.mysema.query.annotations.QueryExtensions;
import com.mysema.query.annotations.QueryMethod;
import com.mysema.query.annotations.QueryProjection;
import com.mysema.query.annotations.QuerydslVariables;
import com.mysema.query.codegen.EntityType;
import com.mysema.query.codegen.Method;
import com.mysema.query.codegen.Serializer;
import com.mysema.query.codegen.SerializerConfig;
import com.mysema.query.codegen.Supertype;
import com.mysema.query.codegen.Type;
import com.mysema.query.codegen.TypeFactory;
import com.mysema.query.codegen.TypeMappings;

/**
 * Processor handles the actual work in the Querydsl APT module
 * 
 * @author tiwe
 * 
 */
public class Processor {
    
    private static final Logger logger = LoggerFactory.getLogger(Processor.class);
   
    private final Map<String, EntityType> actualSupertypes  = new HashMap<String, EntityType>();
    
    private final Map<String, EntityType> allSupertypes = new HashMap<String, EntityType>();
    
    private final Configuration configuration;
    
    private final Map<String, EntityType> dtos = new HashMap<String, EntityType>();
    
    private final ElementHandler elementHandler;
    
    private final Map<String,EntityType> embeddables = new HashMap<String,EntityType>();
    
    private final Map<String, EntityType> entityTypes = new HashMap<String, EntityType>();
    
    private final ProcessingEnvironment env;
    
    private final Map<String,EntityType> extensionTypes = new HashMap<String,EntityType>();
    
    private final RoundEnvironment roundEnv;
    
    private final APTTypeFactory typeModelFactory;
    
    public Processor(ProcessingEnvironment env, RoundEnvironment roundEnv, Configuration configuration) {
        this.env = Assert.notNull(env,"env");
        this.roundEnv = Assert.notNull(roundEnv,"roundEnv");
        this.configuration = Assert.notNull(configuration,"configuration");
        
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
        Deque<Supertype> stypeStack = new ArrayDeque<Supertype>();
        stypeStack.addAll(model.getSuperTypes());        
        while (!stypeStack.isEmpty()) {
            Supertype sdecl = stypeStack.pop();
            EntityType entityType = superTypes.get(sdecl.getType().getFullName());
            if (entityType != null){
                sdecl.setEntityType(entityType);
                model.include(sdecl);
                for (Supertype type : sdecl.getEntityType().getSuperTypes()) {
                    stypeStack.push(type);
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


    public void process() {
        processCustomTypes();               
        processExtensions();        
        if (configuration.getSuperTypeAnn() != null) {
            processSupertypes();            
        }               
        processEntities();                       
        if (configuration.getEmbeddableAnn() != null){
            processEmbeddables();            
        }                   
        processDTOs();
        
        // remove entity types from embeddables
        for (String key : entityTypes.keySet()){
            extensionTypes.remove(key);
        }
        
        // serialize models
        Messager msg = env.getMessager();
        if (!actualSupertypes.isEmpty()){
            msg.printMessage(Kind.NOTE, "serializing super types");
            serialize(configuration.getSupertypeSerializer(), actualSupertypes.values());    
        }
        if (!entityTypes.isEmpty()){
            msg.printMessage(Kind.NOTE, "serializing entity types");
            serialize(configuration.getEntitySerializer(), entityTypes.values());    
        }
        if (!extensionTypes.isEmpty()){
            msg.printMessage(Kind.NOTE, "serializing extension types");
            serialize(configuration.getEmbeddableSerializer(), extensionTypes.values());    
        }        
        if (!embeddables.isEmpty()){
            msg.printMessage(Kind.NOTE, "serializing embeddable types");
            serialize(configuration.getEmbeddableSerializer(), embeddables.values());    
        }
        if (!dtos.isEmpty()){
            msg.printMessage(Kind.NOTE, "serializing dto types");
            serialize(configuration.getDTOSerializer(), dtos.values());    
        }        
        
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

    private void processCustomTypes() {
        for (Element queryMethod : roundEnv.getElementsAnnotatedWith(QueryMethod.class)){
            Element element = queryMethod.getEnclosingElement();
            if (element.getAnnotation(QueryExtensions.class) != null){
                continue;
            }
            if (element.getAnnotation(configuration.getEntityAnn()) != null){
                continue;
            }
            if (configuration.getSuperTypeAnn() != null 
                    && element.getAnnotation(configuration.getSuperTypeAnn()) != null){
                continue;
            }
            if (configuration.getEmbeddableAnn() != null 
                    && element.getAnnotation(configuration.getEmbeddableAnn()) != null){
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
        // FIXME
        for (Element element : roundEnv.getElementsAnnotatedWith(configuration.getEmbeddableAnn())) {
            typeModelFactory.create(element.asType());
        }        
        
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
        process(configuration.getEntityAnn(), entityTypes);
    }
    
    private void processSupertypes() {
        process(configuration.getSuperTypeAnn(), actualSupertypes);
    }
    
    private void process(Class<? extends Annotation> annotation, Map<String,EntityType> types){
        Deque<Type> superTypes = new ArrayDeque<Type>();        
        
        // FIXME        
        for (Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
            if (configuration.getEmbeddableAnn() == null 
                    || element.getAnnotation(configuration.getEmbeddableAnn()) == null){
                typeModelFactory.createEntityType(element.asType());
            }
        }    
        
        // get annotated types
        for (Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
            if (configuration.getEmbeddableAnn() == null 
                    || element.getAnnotation(configuration.getEmbeddableAnn()) == null){
                EntityType model = elementHandler.handleNormalType((TypeElement) element);
                types.put(model.getFullName(), model);    
                if (model.getSuperType() != null){                    
                    superTypes.push(model.getSuperType().getType());
                }
            }                
        }
        
        // get external supertypes
        while (!superTypes.isEmpty()){
            Type superType = superTypes.pop();
            if (!types.containsKey(superType.getFullName())  && !allSupertypes.containsKey(superType.getFullName())){
                TypeElement typeElement = env.getElementUtils().getTypeElement(superType.getFullName());
                EntityType entityType = elementHandler.handleNormalType(typeElement);
                if (entityType.getSuperType() != null){
                    superTypes.push(entityType.getSuperType().getType());
                }
                types.put(superType.getFullName(), entityType);
            }
        }    
        
        allSupertypes.putAll(types);
        
        // add supertype fields
        for (EntityType type : types.values()) {
            addSupertypeFields(type, allSupertypes);      
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
          
    private void serialize(Serializer serializer, Collection<EntityType> models) {
        Messager msg = env.getMessager();
        for (EntityType model : models) {
            msg.printMessage(Kind.NOTE, model.getFullName() + " is processed");
            try {
                String packageName = model.getPackageName();         
                String localName = configuration.getTypeMappings().getPathType(model, model, true);
                String className = packageName.length() > 0 ? (packageName + "." + localName) : localName;
                
                if (env.getElementUtils().getTypeElement(className) == null){
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
                }else{
                    msg.printMessage(Kind.NOTE, className + " already available");
                }
                                
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                msg.printMessage(Kind.ERROR, e.getMessage());
            }
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

}
