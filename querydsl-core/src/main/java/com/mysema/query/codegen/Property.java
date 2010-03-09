/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.util.Arrays;

import net.jcip.annotations.Immutable;

import com.mysema.commons.lang.Assert;
import com.mysema.util.JavaSyntaxUtils;

/**
 * Property represents a property in a query domain type.
 * 
 * @author tiwe
 * @version $Id$
 */
@Immutable
public final class Property implements Comparable<Property> {

    private final EntityType declaringType;
    
    private final boolean inherited;

    private final String[] inits;

    private final String name, escapedName;

    private final Type type;
    
    private TypeVariable[] typeVariables;

    /**
     * @param context
     * @param name
     * @param type
     * @param inits
     */
    public Property(EntityType declaringType, String name, Type type, String[] inits) {
        this(declaringType, name, type, inits, false);
    }

    public Property(EntityType declaringType, String name, Type type, String[] inits, boolean inherited) {
        this(declaringType, name, JavaSyntaxUtils.isReserved(name) ? (name + "_") : name, type, inits, inherited);
    }

    public Property(EntityType declaringType, String name, String escapedName, Type type, String[] inits, boolean inherited) {
        this.declaringType = declaringType;
        this.name = Assert.notNull(name);
        this.escapedName = escapedName;
        this.type = Assert.notNull(type);
        this.inits = inits.clone();
        this.inherited = inherited;
    }

    public int compareTo(Property o) {
        return name.compareToIgnoreCase(o.getName());
    }

    public Property createCopy(EntityType model) {
        return new Property(model, name, type, inits, model.getSuperType() != null);
    }

    public boolean equals(Object o) {
        if (o == this){
            return true;
        }else if (o instanceof Property){
            return name.equals(((Property) o).name);            
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
        return type.getParameter(i);
    }

    public Type getType() {
        return type;
    }

    public int hashCode() {
        return Arrays.asList(name, type).hashCode();
    }

    public boolean isInherited() {
        return inherited;
    }

    public String toString() {
        return declaringType.getFullName() + "." + name;
    }

}