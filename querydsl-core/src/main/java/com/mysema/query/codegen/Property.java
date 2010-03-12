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
    
    private Type transform(Type type, EntityType model){
        if (type instanceof TypeExtends){
            TypeExtends extendsType = (TypeExtends)type;
            if (extendsType.getVarName() != null){
                return extendsType.resolve(model, declaringType);
            }            
        }
//        else if (type instanceof TypeAdapter){
//            Type embedded = ((TypeAdapter)type).getType();
//            Type transformed = transform(embedded, model);
//            if (transformed != embedded){
//                return transformed;
//            }
//        }
        return type;
    }

    public Property createCopy(EntityType model) {
        // TODO : simplify
        Type newType = transform(type,model);
        
        if(newType.getParameterCount() > 0){
            Type[] params = new Type[newType.getParameterCount()];
            boolean transformed = false;
            for (int i = 0; i < newType.getParameterCount(); i++){
                Type param = newType.getParameter(i);
                params[i] = transform(param, model);
                if (params[i] != param){
                    transformed = true;
                }
            }
            if (transformed){
                newType = new SimpleType(newType.getCategory(), 
                    newType.getFullName(), newType.getPackageName(), newType.getSimpleName(),
                    newType.isFinal(), params);
            }
        }
        
        if (newType != type){
            return new Property(model, name, newType, inits, false);
        }else{
            return new Property(model, name, type, inits, model.getSuperType() != null);    
        }
        
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