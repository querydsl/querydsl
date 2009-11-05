/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Nullable;

import org.apache.commons.lang.StringUtils;

import com.mysema.commons.lang.Assert;

/**
 * BeanModel represents the model of a query domain type with properties
 * 
 * @author tiwe
 * @version $Id$
 */
public final class BeanModel implements Comparable<BeanModel> {
        
    public static final String DEFAULT_PREFIX = "Q";
    
    private final Collection<ConstructorModel> constructors = new HashSet<ConstructorModel>();
    
    private boolean entityModel = true;
    
    // mutable
    private int escapeSuffix = 1;
    
    private boolean hasLists, hasMaps, hasEntityFields;
    
    private final String prefix;
    
    private final Set<PropertyModel> properties = new TreeSet<PropertyModel>();
    
    @Nullable
    private BeanModel superModel;
    
    private final Collection<String> superTypes;
    
    private final TypeModel typeModel;
    
    private String uncapSimpleName, genericName;
    
    public BeanModel(String prefix, TypeModel typeModel) {
        this(prefix, typeModel, Collections.<String>emptyList());
    }
    
    public BeanModel(String prefix, TypeModel typeModel, Collection<String> superTypes) {
        this.prefix = Assert.notNull(prefix);        
        this.typeModel = typeModel;
        this.uncapSimpleName = StringUtils.uncapitalize(typeModel.getSimpleName());
        this.superTypes = superTypes;
    }
    

    public void addConstructor(ConstructorModel co) {
        constructors.add(co);
    }

    public void addProperty(PropertyModel field) {
        properties.add(validateField(field));
        switch(field.getTypeCategory()){
        case ENTITYMAP:
        case SIMPLEMAP: 
            hasMaps = true; 
            break;
        case ENTITYLIST:
        case SIMPLELIST: 
            hasLists = true; 
            break;
        case ENTITY:    
            hasEntityFields = true;            
        }
    }

    public int compareTo(BeanModel o) {
        return typeModel.getSimpleName().compareTo(o.typeModel.getSimpleName());
    }

    public boolean equals(Object o) {
        return o instanceof BeanModel && typeModel.getName().equals(((BeanModel) o).typeModel.getName());
    }

    public Collection<ConstructorModel> getConstructors() {
        return constructors;
    }

    public String getGenericName(){
        if (genericName == null){
            if (typeModel.getParameterCount() == 0){
                genericName = typeModel.getLocalName();
            }else{
                StringBuilder builder = new StringBuilder(typeModel.getLocalName()).append("<");
                for (int i = 0; i < typeModel.getParameterCount(); i++){
                    if (i > 0) builder.append(",");
                    builder.append("?");
                }
                genericName = builder.append(">").toString();    
            }    
        }
        return genericName;            
    }

    public String getLocalName() {
        return typeModel.getLocalName();
    }
    
    public String getName() {
        return typeModel.getName();
    }

    public Set<PropertyModel> getProperties() {
        return properties;
    }

    public String getPackageName() {
        return typeModel.getPackageName();
    }

    public String getPrefix(){
        return prefix;
    }

    public String getSimpleName() {
        return typeModel.getSimpleName();
    }
        
    @Nullable
    public BeanModel getSuperModel() {
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

    public int hashCode() {
        return typeModel.getName().hashCode();
    }


    public boolean hasLists() {
        return hasLists;
    }

    public boolean hasMaps() {
        return hasMaps;
    }

    public void include(BeanModel clazz) {
        for (PropertyModel property : clazz.properties){
            if (!property.isInherited()){
                addProperty(property.createCopy(this));    
            }            
        }        
    }

    public boolean isEntityModel() {
        return entityModel;
    }

    public void setEntityModel(boolean entityModel) {
        this.entityModel = entityModel;
    }

    public void setSuperModel(BeanModel superModel) {
        this.superModel = superModel;
    }

    private PropertyModel validateField(PropertyModel field) {
        if (field.getName().equals(this.uncapSimpleName)) {
            uncapSimpleName = StringUtils.uncapitalize(typeModel.getSimpleName())+ (escapeSuffix++);
        }
        return field;
    }
    
    
}