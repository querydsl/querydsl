/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.codegen.model;

import java.util.List;
import java.util.Set;

import net.jcip.annotations.Immutable;

/**
 * TypeAdapter is a basic adapter implementation for the Type interface
 *
 * @author tiwe
 *
 */
@Immutable
public class TypeAdapter implements Type{

    private final Type type;

    public TypeAdapter(Type type){
        this.type = type;
    }

    @Override
    public Type as(TypeCategory category) {
        return type.as(category);
    }

    @Override
    public Type asArrayType() {
        return type.asArrayType();
    }

    @Override
    public boolean equals(Object o){
        return type.equals(o);
    }

    @Override
    public TypeCategory getCategory() {
        return type.getCategory();
    }

    @Override
    public String getFullName() {
        return type.getFullName();
    }

    @Override
    public String getGenericName(boolean asArgType) {
        return type.getGenericName(asArgType);
    }

    @Override
    public String getGenericName(boolean asArgType, Set<String> packages, Set<String> classes) {
        return type.getGenericName(asArgType, packages, classes);
    }

    @Override
    public String getPackageName() {
        return type.getPackageName();
    }

    @Override
    public List<Type> getParameters() {
        return type.getParameters();
    }

    @Override
    public String getPrimitiveName() {
        return type.getPrimitiveName();
    }

    @Override
    public String getRawName(Set<String> packages, Set<String> classes) {
        return type.getRawName(packages, classes);
    }

    @Override
    public String getSimpleName() {
        return type.getSimpleName();
    }

    protected Type getType(){
        return type;
    }

    @Override
    public int hashCode(){
        return type.hashCode();
    }

    @Override
    public boolean isFinal() {
        return type.isFinal();
    }

    @Override
    public boolean isPrimitive() {
        return type.isPrimitive();
    }

    @Override
    public String toString() {
        return type.toString();
    }

}
