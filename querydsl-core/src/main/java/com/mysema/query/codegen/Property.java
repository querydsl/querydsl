/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.codegen;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.jcip.annotations.Immutable;

import com.mysema.codegen.model.Type;
import com.mysema.commons.lang.Assert;
import com.mysema.util.JavaSyntaxUtils;

/**
 * Property represents a property in a query domain type.
 *
 * @author tiwe
 */
@Immutable
public final class Property implements Comparable<Property> {

    private final EntityType declaringType;

    private final boolean inherited;

    private final String[] inits;

    private final String name, escapedName;
    
    private final Map<Class<?>,Annotation> annotations = new HashMap<Class<?>,Annotation>();

    private final Type type;

    public Property(EntityType declaringType, String name, Type type) {
        this(declaringType, name, type, new String[0], false);
    }
    
    public Property(EntityType declaringType, String name, Type type, String[] inits) {
        this(declaringType, name, type, inits, false);
    }

    public Property(EntityType declaringType, String name, Type type, String[] inits, boolean inherited) {
        this(declaringType, name, JavaSyntaxUtils.isReserved(name) ? (name + "$") : name, type, inits, inherited);
    }

    public Property(EntityType declaringType, String name, String escapedName, Type type, String[] inits, boolean inherited) {
        this.declaringType = declaringType;
        this.name = Assert.notNull(name,"name");
        this.escapedName = escapedName;
        this.type = Assert.notNull(type,"type");
        this.inits = inits.clone();
        this.inherited = inherited;
    }

    public void addAnnotation(Annotation annotation){
        annotations.put(annotation.annotationType(), annotation);
    }
    
    public int compareTo(Property o) {
        return name.compareToIgnoreCase(o.getName());
    }

    public Property createCopy(EntityType targetModel) {
        Type newType = TypeResolver.resolve(type, declaringType, targetModel);
        if (newType != type){
            return new Property(targetModel, name, newType, inits, false);
        }else{
            return new Property(targetModel, name, type, inits, targetModel.getSuperType() != null);
        }
    }
    
    @SuppressWarnings("unchecked")
    public <T extends Annotation> T getAnnotation(Class<T> type){
        return (T) annotations.get(type);
    }
    
    public Collection<Annotation> getAnnotations() {
        return annotations.values();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + name.hashCode();
        result = prime * result + type.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o){
        if (o == this){
            return true;
        }else if (o instanceof Property){
            Property p = (Property)o;
            return p.name.equals(name) && p.type.equals(type);
        }else{
            return false;
        }
    }

    public EntityType getDeclaringType() {
        return declaringType;
    }

    public String getEscapedName() {
        return escapedName;
    }

    public String[] getInits() {
        return inits;
    }

    public String getName() {
        return name;
    }

    public Type getParameter(int i) {
        return type.getParameters().get(i);
    }

    public Type getType() {
        return type;
    }

    public boolean isInherited() {
        return inherited;
    }

    @Override
    public String toString() {
        return declaringType.getFullName() + "." + name;
    }

}
