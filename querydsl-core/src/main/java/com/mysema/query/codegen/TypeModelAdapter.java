/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import com.mysema.commons.lang.Assert;

/**
 * @author tiwe
 *
 */
public class TypeModelAdapter implements TypeModel{
    
    protected final TypeModel typeModel;
    
    public TypeModelAdapter(TypeModel typeModel){
        this.typeModel = Assert.notNull(typeModel);
    }

    @Override
    public TypeModel as(TypeCategory category) {
        return typeModel.as(category);
    }

    @Override
    public String getFullName() {
        return typeModel.getFullName();
    }

    @Override
    public StringBuilder getLocalGenericName(EntityModel context, StringBuilder builder, boolean asArgType) {
        return typeModel.getLocalGenericName(context, builder, false);
    }

    @Override
    public StringBuilder getLocalRawName(EntityModel context, StringBuilder builder) {
        return typeModel.getLocalRawName(context, builder);
    }

    @Override
    public String getPackageName() {
        return typeModel.getPackageName();
    }

    @Override
    public TypeModel getParameter(int i) {
        return typeModel.getParameter(i);
    }

    @Override
    public int getParameterCount() {
        return typeModel.getParameterCount();
    }

    @Override
    public String getPrimitiveName() {
        return typeModel.getPrimitiveName();
    }

    @Override
    public TypeModel getSelfOrValueType() {
        return typeModel.getSelfOrValueType();
    }

    @Override
    public String getSimpleName() {
        return typeModel.getSimpleName();
    }

    @Override
    public TypeCategory getTypeCategory() {
        return typeModel.getTypeCategory();
    }

    @Override
    public boolean isFinal() {
        return typeModel.isFinal();
    }

    @Override
    public boolean isPrimitive() {
        return typeModel.isPrimitive();
    }

    @Override
    public String toString() {
        return typeModel.toString();
    }
    
    @Override
    public int hashCode(){
        return typeModel.hashCode();
    }
    
    @Override
    public boolean equals(Object o){
        return typeModel.equals(o);
    }

    @Override
    public boolean hasEntityFields() {
        return typeModel.hasEntityFields();
    }
}
