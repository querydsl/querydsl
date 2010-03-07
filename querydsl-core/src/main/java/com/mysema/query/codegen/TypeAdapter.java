/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.io.IOException;

import com.mysema.commons.lang.Assert;

/**
 * TypeAdapter is a basic adapter implementation for the Type interface
 * 
 * @author tiwe
 *
 */
public class TypeAdapter implements Type{
    
    private final Type type;
    
    public TypeAdapter(Type type){
        this.type = Assert.notNull(type);
    }
    
    @Override
    public void appendLocalGenericName(Type context, Appendable builder, boolean asArgType) throws IOException {
        type.appendLocalGenericName(context, builder, false);
    }

    @Override
    public void appendLocalRawName(Type context, Appendable builder) throws IOException {
        type.appendLocalRawName(context, builder);
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
    public String getLocalGenericName(Type context, boolean asArgType) {
        return type.getLocalGenericName(context, asArgType);
    }

    @Override
    public String getLocalRawName(Type context) {
        return type.getLocalRawName(context);
    }

    @Override
    public String getPackageName() {
        return type.getPackageName();
    }

    @Override
    public Type getParameter(int i) {
        return type.getParameter(i);
    }

    @Override
    public int getParameterCount() {
        return type.getParameterCount();
    }

    @Override
    public String getPrimitiveName() {
        return type.getPrimitiveName();
    }

    @Override
    public Type getSelfOrValueType() {
        return type.getSelfOrValueType();
    }
    
    @Override
    public String getSimpleName() {
        return type.getSimpleName();
    }
    
    protected Type getType(){
        return type;
    }

    @Override
    public boolean hasEntityFields() {
        return type.hasEntityFields();
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
