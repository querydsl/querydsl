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
import java.util.List;
import java.util.Map;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.lang.model.util.SimpleElementVisitor6;
import javax.tools.JavaFileObject;
import javax.tools.Diagnostic.Kind;

import org.apache.commons.lang.StringUtils;

import com.mysema.commons.lang.Assert;
import com.mysema.query.codegen.ClassModel;
import com.mysema.query.codegen.ConstructorModel;
import com.mysema.query.codegen.FieldModel;
import com.mysema.query.codegen.ParameterModel;
import com.mysema.query.codegen.Serializer;
import com.mysema.query.codegen.Serializers;
import com.mysema.query.codegen.TypeModel;

/**
 * 
 * @author tiwe
 * 
 */
public class Processor {
    
    private final ElementVisitor<ClassModel, Void> dtoElementVisitor = new SimpleElementVisitor6<ClassModel, Void>() {
        
        @Override
        public ClassModel visitType(TypeElement e, Void p) {
            Elements elementUtils = env.getElementUtils();
            TypeModel c = APTTypeModel.get(e.asType(), elementUtils);
            ClassModel classModel = new ClassModel(null, c.getPackageName(), c.getName(), c.getSimpleName());
            List<? extends Element> elements = e.getEnclosedElements();
            
            // CONSTRUCTOR
            for (ExecutableElement constructor : ElementFilter.constructorsIn(elements)){
                if (isValidConstructor(constructor)){
                    List<ParameterModel> parameters = new ArrayList<ParameterModel>(constructor.getParameters().size());
                    for (VariableElement var : constructor.getParameters()){
                        TypeModel varType = APTTypeModel.get(var.asType(), elementUtils);
                        parameters.add(new ParameterModel(var.getSimpleName().toString(), varType.getName()));
                    }
                    classModel.addConstructor(new ConstructorModel(parameters));    
                }                
            }                                    
            return classModel;
        }

        
    };
    
    private final ElementVisitor<ClassModel, Void> elementVisitor = new SimpleElementVisitor6<ClassModel, Void>() {

        @Override
        public ClassModel visitType(TypeElement e, Void p) {
            Elements elementUtils = env.getElementUtils();
            TypeModel sc = APTTypeModel.get(e.getSuperclass(), elementUtils);
            TypeModel c = APTTypeModel.get(e.asType(), elementUtils);
            ClassModel classModel = new ClassModel(sc.getName(), c.getPackageName(), c.getName(), c.getSimpleName());
            List<? extends Element> elements = e.getEnclosedElements();
            
            // GETTERS
            for (ExecutableElement method : ElementFilter.methodsIn(elements)){
                String name = method.getSimpleName().toString();
                if (name.startsWith("get") && method.getParameters().isEmpty()){
                    name = StringUtils.uncapitalize(name.substring(3));
                }else if (name.startsWith("is") && method.getParameters().isEmpty()){
                    name = StringUtils.uncapitalize(name.substring(2));
                }else{
                    continue;
                }
                if (isValidGetter(method)){
                    try{
                        TypeModel fieldType = APTTypeModel.get(method.getReturnType(), elementUtils);
                        String docs = elementUtils.getDocComment(method);
                        classModel.addField(new FieldModel(classModel, name, fieldType, docs != null ? docs : name));    
                        
                    }catch(IllegalArgumentException ex){
                        throw new RuntimeException("Caught exception for method " + c.getName()+"#"+method.getSimpleName(), ex);
                    }
                }                
            }
            
            // FIELDS
            for (VariableElement field : ElementFilter.fieldsIn(elements)){
                if (isValidField(field)){
                    try{
                        TypeModel fieldType = APTTypeModel.get(field.asType(), elementUtils);     
                        String name = field.getSimpleName().toString();
                        String docs = elementUtils.getDocComment(field);
                        classModel.addField(new FieldModel(classModel, name, fieldType, docs != null ? docs : name));    
                    }catch(IllegalArgumentException ex){
                        throw new RuntimeException("Caught exception for field " + c.getName()+"#"+field.getSimpleName(), ex);
                    }
                        
                }                
            }                        
            return classModel;
        }

    };
    
    private final Class<? extends Annotation> entityAnn, superTypeAnn, embeddableAnn, dtoAnn, skipAnn;
    
    private final ProcessingEnvironment env;
    
    private final String namePrefix;
    
    private boolean useFields = true;
    
    private boolean useGetters = true;
    
    public Processor(ProcessingEnvironment env,
            Class<? extends Annotation> entityAnn, 
            Class<? extends Annotation> superTypeAnn,
            Class<? extends Annotation> embeddableAnn,
            Class<? extends Annotation> dtoAnn,
            Class<? extends Annotation> skipAnn,
            String namePrefix) {
        this.env = Assert.notNull(env);
        this.entityAnn = Assert.notNull(entityAnn);
        this.superTypeAnn = superTypeAnn;
        this.embeddableAnn = embeddableAnn;
        this.dtoAnn = dtoAnn;
        this.skipAnn = skipAnn;
        this.namePrefix = Assert.notNull(namePrefix);
    }
    
    private ClassModel getClassModel(Element element) {
        return element.accept(elementVisitor, null);
    }
    
    private ClassModel getClassModelForDTO(Element element){
        return element.accept(dtoElementVisitor, null);
    }

    protected boolean isValidConstructor(ExecutableElement constructor) {
        return constructor.getModifiers().contains(Modifier.PUBLIC);
    }

    protected boolean isValidField(VariableElement field) {
        return useFields
            && field.getAnnotation(skipAnn) == null
            && !field.getModifiers().contains(Modifier.TRANSIENT) 
            && !field.getModifiers().contains(Modifier.STATIC);
    }

    protected boolean isValidGetter(ExecutableElement getter){
        return useGetters
            && getter.getAnnotation(skipAnn) == null
            && !getter.getModifiers().contains(Modifier.STATIC);
    }

    public void process(RoundEnvironment roundEnv) {
        Map<String, ClassModel> superTypes = new HashMap<String, ClassModel>();

        // populate super type mappings
        if (superTypeAnn != null) {
            for (Element element : roundEnv.getElementsAnnotatedWith(superTypeAnn)) {
                ClassModel model = getClassModel(element);
                superTypes.put(model.getName(), model);
            }
        }

        // ENTITIES
        
        // populate entity type mappings
        Map<String, ClassModel> entityTypes = new HashMap<String, ClassModel>();
        for (Element element : roundEnv.getElementsAnnotatedWith(entityAnn)) {
            ClassModel model = getClassModel(element);
            entityTypes.put(model.getName(), model);
        }
        // add super type fields
        for (ClassModel entityType : entityTypes.values()) {
            entityType.addSupertypeFields(entityTypes, superTypes);
        }
        // serialize entity types
        if (!entityTypes.isEmpty()) {
            serialize(Serializers.DOMAIN, entityTypes);
        }
        
        // EMBEDDABLES (optional)
        
        if (embeddableAnn != null){
            // populate entity type mappings
            Map<String, ClassModel> embeddables = new HashMap<String, ClassModel>();
            for (Element element : roundEnv.getElementsAnnotatedWith(embeddableAnn)) {
                ClassModel model = getClassModel(element);
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
        
        if (dtoAnn != null){
            Map<String, ClassModel> dtos = new HashMap<String, ClassModel>();
            for (Element element : roundEnv.getElementsAnnotatedWith(dtoAnn)) {
                ClassModel model = getClassModelForDTO(element);
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
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("pre", namePrefix);
        for (ClassModel type : types.values()) {
            msg.printMessage(Kind.NOTE, type.getName() + " is processed");
            String packageName = type.getPackageName();
            model.put("package", packageName);
            model.put("type", type);
            model.put("classSimpleName", type.getSimpleName());
            try {                    
                String className = packageName + "." + namePrefix + type.getSimpleName();
                JavaFileObject fileObject = env.getFiler().createSourceFile(className);
                Writer writer = fileObject.openWriter();
                try{
                    serializer.serialize(model, writer);
                }finally{
                    writer.close();
                }
                
            } catch (Exception e) {
                msg.printMessage(Kind.ERROR, e.getMessage());
            }
        }
    }

    /***
     * Skip processing of fields
     * 
     * @return
     */
    public Processor skipFields(){
        useFields = false;
        return this;
    }
    
    /**
     * Skip processing of getters
     * 
     * @return
     */
    public Processor skipGetters() {
        useGetters = false;        
        return this;
    }

}
