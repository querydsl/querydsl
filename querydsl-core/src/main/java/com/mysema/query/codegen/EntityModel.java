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
public final class EntityModel extends TypeModelAdapter implements Comparable<EntityModel> {
    
    private final Collection<ConstructorModel> constructors = new HashSet<ConstructorModel>();
    
    // mutable
    private int escapeSuffix = 1;

    // mutable
    private boolean hasLists, hasMaps, hasEntityFields;
    
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

    @Override
    public int compareTo(EntityModel o) {
        return typeModel.getSimpleName().compareTo(o.typeModel.getSimpleName());
    }

    public Collection<ConstructorModel> getConstructors() {
        return constructors;
    }

    public String getLocalGenericName(){
        return typeModel.getLocalGenericName(this, new StringBuilder(), false).toString();            
    }

    public String getLocalRawName() {
        return typeModel.getLocalRawName(this, new StringBuilder()).toString();
    }
    
    public Set<PropertyModel> getProperties() {
        return properties;
    }

    public String getPrefix(){
        return prefix;
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

    @Override
    public boolean hasEntityFields() {
        return hasEntityFields;
    }

    public boolean hasLists() {
        return hasLists;
    }

    public boolean hasMaps() {
        return hasMaps;
    }

    public void include(EntityModel clazz) {
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
            uncapSimpleName = StringUtils.uncapitalize(typeModel.getSimpleName())+ (escapeSuffix++);
        }
        return field;
    }
    
    
}