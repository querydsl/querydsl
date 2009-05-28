/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt.impl;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;

import com.mysema.query.codegen.ClassModel;
import com.mysema.query.codegen.ClassModelFactory;
import com.mysema.query.codegen.Serializer;
import com.mysema.query.codegen.Serializers;
import com.mysema.query.util.FileUtils;

public class DefaultHandler {

    private String namePrefix = "Q";
    
    private String targetFolder = "target/generated-sources/java";
    
    public void processClasses(Set<? extends Element> elements, RoundEnvironment env) {
        Map<String, ClassModel> mappedSupertypes = new HashMap<String, ClassModel>();
        // TODO : handle super types 
        
        Map<String,ClassModel> entityTypes = new HashMap<String,ClassModel>();
        for (Element element : elements){
            // TODO
        }
        
        for (ClassModel entityType : entityTypes.values()) {
            addSupertypeFields(entityType, entityTypes, mappedSupertypes);
        }

        if (!entityTypes.isEmpty()) {
            serializeAsOuterClasses(entityTypes.values(), Serializers.DOMAIN);
        }
    }
    

    private void addSupertypeFields(ClassModel typeDecl, Map<String, ClassModel> entityTypes, Map<String, ClassModel> mappedSupertypes) {
        String stype = typeDecl.getSupertypeName();
        Class<?> superClass = safeClassForName(stype);
        if (entityTypes.containsKey(stype)
                || mappedSupertypes.containsKey(stype)) {
            while (true) {
                ClassModel sdecl;
                if (entityTypes.containsKey(stype)) {
                    sdecl = entityTypes.get(stype);
                } else if (mappedSupertypes.containsKey(stype)) {
                    sdecl = mappedSupertypes.get(stype);
                } else {
                    return;
                }
                typeDecl.include(sdecl);
                stype = sdecl.getSupertypeName();
            }

        } else if (superClass != null && !superClass.equals(Object.class)) {
            // TODO : recursively up ?
            ClassModel type = ClassModelFactory.createType(superClass);
            // include fields of supertype
            typeDecl.include(type);
        }
    }
    
    private Class<?> safeClassForName(String stype) {
        try {
            return stype != null ? Class.forName(stype) : null;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
    
    protected void serializeAsOuterClasses(Collection<ClassModel> entityTypes, Serializer serializer) {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("pre", namePrefix);
        for (ClassModel type : entityTypes) {
            String packageName = type.getPackageName();
            model.put("package", packageName);
            model.put("type", type);
            model.put("classSimpleName", type.getSimpleName());
            try {
                String path = packageName.replace('.', '/') + "/" + namePrefix + type.getSimpleName() + ".java";
                serializer.serialize(model, FileUtils.writerFor(new File(targetFolder, path)));
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }
}
