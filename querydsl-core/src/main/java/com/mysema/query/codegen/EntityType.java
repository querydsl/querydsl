/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.codegen;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Nullable;

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

    private int escapeSuffix = 1;

    private boolean hasLists, hasMaps, hasEntityFields;

    private final Set<Method> methods = new HashSet<Method>();

    private final Set<Delegate> delegates = new HashSet<Delegate>();

    private final String prefix;

    private final Set<Property> properties = new TreeSet<Property>();

    private final Collection<Supertype> superTypes;

    private final Map<Object,Object> data = new HashMap<Object,Object>();

    private String uncapSimpleName;

    public EntityType(String prefix, Type type) {
        this(prefix, type, new HashSet<Supertype>());
    }

    public EntityType(String prefix, Type type, Set<Supertype> superTypes) {
        super(type);
        this.prefix = Assert.notNull(prefix,"prefix");
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

    public void addDelegate(Delegate delegate){
        delegates.add(delegate);
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

    public void addSupertype(Supertype entityType){
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

    public Set<Delegate> getDelegates(){
        return delegates;
    }

    public TypeCategory getOriginalCategory(){
        return super.getCategory();
    }

    @Override
    public String getPackageName(){
        String pkg = super.getPackageName();
        return pkg.equals("java.lang") ? "com.mysema.query.types" : pkg;
    }
    
    @Override
    public String getFullName(){
        String name = super.getFullName();
        if (name.startsWith("java.lang.")){
            return "com.mysema.query.types." + name.substring(10);
        }else{
            return name;
        }
    }
    
    public String getPrefix(){
        return prefix;
    }

    public Set<Property> getProperties() {
        return properties;
    }

    @Nullable
    public Supertype getSuperType(){
        return superTypes.size() == 1 ? superTypes.iterator().next() : null;
    }

    public Collection<Supertype> getSuperTypes() {
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

    public Map<Object, Object> getData() {
        return data;
    }

    public void include(Supertype supertype) {
        EntityType entityType = supertype.getEntityType();
        for (Method method : entityType.getMethods()){
            addMethod(method.createCopy(this));
        }
        for (Delegate delegate : entityType.getDelegates()){
            addDelegate(delegate);
        }

        for (Property property : entityType.getProperties()){
            if (!property.isInherited()){
                addProperty(property.createCopy(this));
            }
        }
    }

    private Property validateField(Property field) {
        if (field.getName().equals(uncapSimpleName)) {
            uncapSimpleName = StringUtils.uncapitalize(getType().getSimpleName())+ (escapeSuffix++);
        }
        return field;
    }
    
    @Override
    public boolean equals(Object o){
        if (o == this){
            return true;
        }else if (o instanceof Type){
            return getFullName().equals(((Type)o).getFullName());    
        }else{
            return false;
        }        
    }
    
    @Override
    public int hashCode(){
        return getFullName().hashCode();
    }
    
}
