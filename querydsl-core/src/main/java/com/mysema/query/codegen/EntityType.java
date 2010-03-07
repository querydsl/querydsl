/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

import com.mysema.commons.lang.Assert;

/**
 * EntityType represents a model of a query domain type with properties
 * 
 * @author tiwe
 * @version $Id$
 */
public final class EntityType extends TypeAdapter implements Comparable<EntityType> {
    
    private final Set<Annotation> annotations = new HashSet<Annotation>();
    
    private final Set<Constructor> constructors = new HashSet<Constructor>();
    
    // mutable
    private int escapeSuffix = 1;
    
    // mutable
    private boolean hasLists, hasMaps, hasEntityFields;

    private final Set<Method> methods = new HashSet<Method>();
    
    private final String prefix;
    
    private final Set<Property> properties = new TreeSet<Property>();
    
    private final Collection<EntityType> superTypes;

    // mutable
    private String uncapSimpleName;

    public EntityType(String prefix, Type type) {
        this(prefix, type, new HashSet<EntityType>());
    }

    public EntityType(String prefix, Type type, Set<EntityType> superTypes) {
        super(type);
        this.prefix = Assert.notNull(prefix);        
        this.uncapSimpleName = StringUtils.uncapitalize(type.getSimpleName());
        this.superTypes = superTypes;
    }    

    public void addAnnotation(Annotation annotation){
        annotations.add(annotation);
    }
    
    public void addConstructor(Constructor co) {
        constructors.add(co);
    }
    
    public void addMethod(Method method){
        methods.add(method);
    }

    public void addProperty(Property field) {
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

    public void addSuperType(EntityType entityType){
        superTypes.add(entityType);
    }

    @Override
    public int compareTo(EntityType o) {
        return getType().getSimpleName().compareTo(o.getType().getSimpleName());
    }

    public Set<Annotation> getAnnotations() {
        return annotations;
    }
    
    public TypeCategory getCategory() {
        return TypeCategory.ENTITY;
    }

    public Set<Constructor> getConstructors() {
        return constructors;
    }
    
    public String getLocalGenericName(){
        try {
            StringBuilder builder = new StringBuilder();
            getType().appendLocalGenericName(this, builder, false);
            return builder.toString();
        } catch (IOException e) {
            throw new CodeGenerationException(e.getMessage(), e);
        }            
    }

    public String getLocalRawName() {
        try {
            StringBuilder builder = new StringBuilder();
            getType().appendLocalRawName(this, builder);
            return builder.toString();
        } catch (IOException e) {
            throw new CodeGenerationException(e.getMessage(), e);
        }
    }
        
    public Set<Method> getMethods(){
        return methods;
    }
    
    public TypeCategory getOriginalCategory(){
        return super.getCategory();
    }

    public String getPrefix(){
        return prefix;
    }
    
    public Set<Property> getProperties() {
        return properties;
    }
    
    public EntityType getSuperType(){
        return superTypes.size() == 1 ? superTypes.iterator().next() : null;
    }

    public Collection<EntityType> getSuperTypes() {
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

    public void include(EntityType clazz) {
        for (Method method : clazz.methods){
            addMethod(method);
        }
        
        for (Property property : clazz.properties){
            if (!property.isInherited()){                
                addProperty(property.createCopy(this));    
            }            
        }        
    }

    private Property validateField(Property field) {
        if (field.getName().equals(this.uncapSimpleName)) {
            uncapSimpleName = StringUtils.uncapitalize(getType().getSimpleName())+ (escapeSuffix++);
        }
        return field;
    }
        
}