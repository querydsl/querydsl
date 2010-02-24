/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Nullable;

import org.apache.commons.lang.StringUtils;

import com.mysema.commons.lang.Assert;

/**
 * EntityModel represents a model of a query domain type with properties
 * 
 * @author tiwe
 * @version $Id$
 */
// TODO : rename this
public final class EntityModel extends TypeModelAdapter implements Comparable<EntityModel> {
    
    private final Set<ConstructorModel> constructors = new HashSet<ConstructorModel>();
    
    // mutable
    private int escapeSuffix = 1;
    
    // mutable
    private boolean hasLists, hasMaps, hasEntityFields;
    
    private final Set<Annotation> annotations = new HashSet<Annotation>();

    private final Set<MethodModel> methods = new HashSet<MethodModel>();
    
    private final String prefix;
    
    private final Set<PropertyModel> properties = new TreeSet<PropertyModel>();
    
    // mutable
    @Nullable
    private EntityModel superModel;
    
    private final Collection<String> superTypes;

    // mutable
    private String uncapSimpleName;
    
    public EntityModel(String prefix, TypeModel typeModel) {
        this(prefix, typeModel, Collections.<String>emptyList());
    }
    
    public EntityModel(String prefix, TypeModel typeModel, Collection<String> superTypes) {
        super(typeModel);
        this.prefix = Assert.notNull(prefix);        
        this.uncapSimpleName = StringUtils.uncapitalize(typeModel.getSimpleName());
        this.superTypes = superTypes;
    }    

    public void addConstructor(ConstructorModel co) {
        constructors.add(co);
    }
    
    public void addAnnotation(Annotation annotation){
        annotations.add(annotation);
    }
    
    public void addMethod(MethodModel method){
        methods.add(method);
    }

    public void addProperty(PropertyModel field) {
        properties.add(validateField(field));
        switch(field.getType().getCategory()){
        case MAP: 
            hasMaps = true; 
            break;
        case LIST: 
            hasLists = true; 
            break;
        case ENTITY:    
            hasEntityFields = true;            
        }
    }

    @Override
    public int compareTo(EntityModel o) {
        return getTypeModel().getSimpleName().compareTo(o.getTypeModel().getSimpleName());
    }

    public TypeCategory getCategory() {
        return TypeCategory.ENTITY;
    }

    public Set<ConstructorModel> getConstructors() {
        return constructors;
    }
    
    public String getLocalGenericName(){
        try {
            return getTypeModel().getLocalGenericName(this, new StringBuilder(), false).toString();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }            
    }

    public String getLocalRawName() {
        try {
            return getTypeModel().getLocalRawName(this, new StringBuilder()).toString();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    public Set<MethodModel> getMethods(){
        return methods;
    }

    public TypeCategory getOriginalCategory(){
        return super.getCategory();
    }
        
    public String getPrefix(){
        return prefix;
    }
    
    public Set<PropertyModel> getProperties() {
        return properties;
    }

    @Nullable
    public EntityModel getSuperModel() {
        return superModel;
    }

    public Collection<String> getSuperTypes() {
        return superTypes;
    }

    public String getUncapSimpleName() {
        return uncapSimpleName;
    }

    public boolean hasEntityFields() {
        return hasEntityFields;
    }

    public boolean hasLists() {
        return hasLists;
    }
    
    public boolean hasMaps() {
        return hasMaps;
    }
    
    public Set<Annotation> getAnnotations() {
        return annotations;
    }

    public void include(EntityModel clazz) {
        for (MethodModel method : clazz.methods){
            addMethod(method);
        }
        
        for (PropertyModel property : clazz.properties){
            if (!property.isInherited()){                
                addProperty(property.createCopy(this));    
            }            
        }        
    }

    public void setSuperModel(EntityModel superModel) {
        this.superModel = superModel;
    }

    private PropertyModel validateField(PropertyModel field) {
        if (field.getName().equals(this.uncapSimpleName)) {
            uncapSimpleName = StringUtils.uncapitalize(getTypeModel().getSimpleName())+ (escapeSuffix++);
        }
        return field;
    }
        
}