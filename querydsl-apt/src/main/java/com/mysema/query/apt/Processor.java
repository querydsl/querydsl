/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.SimpleElementVisitor6;

import org.apache.commons.lang.StringUtils;

import com.mysema.commons.lang.Assert;
import com.mysema.query.codegen.ClassModel;
import com.mysema.query.codegen.FieldModel;
import com.mysema.query.codegen.Serializers;
import com.mysema.query.codegen.TypeModel;

/**
 * 
 * @author tiwe
 * 
 */
public class Processor {

    private ElementVisitor<ClassModel, Void> elementVisitor = new SimpleElementVisitor6<ClassModel, Void>() {

        @Override
        public ClassModel visitType(TypeElement e, Void p) {
            TypeModel sc = APTTypeModel.get(e.getSuperclass());
            TypeModel c = APTTypeModel.get(e.asType());
            ClassModel classModel = new ClassModel(sc.getFullName(), c.getPackageName(), c.getFullName(), c.getSimpleName());
            List<? extends Element> elements = e.getEnclosedElements();
            for (ExecutableElement method : ElementFilter.methodsIn(elements)){
                String name = method.getSimpleName().toString();
                if (name.startsWith("get") && method.getParameters().isEmpty()){
                    name = StringUtils.uncapitalize(name.substring(3));
                }else if (name.startsWith("is") && method.getParameters().isEmpty()){
                    name = StringUtils.uncapitalize(name.substring(2));
                }else{
                    continue;
                }
                TypeModel fieldType = APTTypeModel.get(method.getReturnType());
                classModel.addField(new FieldModel(name, fieldType));
            }
            for (VariableElement field : ElementFilter.fieldsIn(elements)){
                TypeModel fieldType = APTTypeModel.get(field.asType());
                classModel.addField(new FieldModel(field.getSimpleName().toString(), fieldType));
            }                        
            return classModel;
        }

    };

    private String namePrefix, targetFolder;

    private Class<? extends Annotation> entityAnn, superTypeAnn;

    public Processor(Class<? extends Annotation> entityAnn,
            Class<? extends Annotation> superTypeAnn, String namePrefix,
            String targetFolder) {
        this.entityAnn = Assert.notNull(entityAnn);
        this.superTypeAnn = superTypeAnn;
        this.namePrefix = Assert.notNull(namePrefix);
        this.targetFolder = Assert.notNull(targetFolder);
    }

    public void process(RoundEnvironment env) {
        Map<String, ClassModel> superTypes = new HashMap<String, ClassModel>();

        // populate super type mappings
        if (superTypeAnn != null) {
            for (Element element : env.getElementsAnnotatedWith(superTypeAnn)) {
                ClassModel model = getClassModel(element);
                superTypes.put(model.getName(), model);
            }
        }

        // populate entity type mappings
        Map<String, ClassModel> entityTypes = new HashMap<String, ClassModel>();
        for (Element element : env.getElementsAnnotatedWith(entityAnn)) {
            ClassModel model = getClassModel(element);
            entityTypes.put(model.getName(), model);
        }

        // add super type fields
        for (ClassModel entityType : entityTypes.values()) {
            entityType.addSupertypeFields(entityTypes, superTypes);
        }

        // serialize entity types
        if (!entityTypes.isEmpty()) {
            Serializers.DOMAIN.serialize(targetFolder, namePrefix, entityTypes
                    .values());
        }
    }

    private ClassModel getClassModel(Element element) {
        return element.accept(elementVisitor, null);
    }

}
