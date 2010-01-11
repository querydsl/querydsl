/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.io.IOException;

import com.mysema.commons.lang.Assert;

/**
 * TypeModelAdapter is a basic adapter implementation for the TypeModel interface
 * 
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
    public boolean equals(Object o){
        return typeModel.equals(o);
    }

    @Override
    public TypeCategory getCategory() {
        return typeModel.getCategory();
    }

    @Override
    public String getFullName() {
        return typeModel.getFullName();
    }

    @Override
    public String getLocalGenericName(TypeModel context, boolean asArgType) {
        return typeModel.getLocalGenericName(context, asArgType);
    }

    @Override
    public <T extends Appendable> T getLocalGenericName(TypeModel context, T builder, boolean asArgType) throws IOException {
        return typeModel.getLocalGenericName(context, builder, false);
    }

    @Override
    public String getLocalRawName(TypeModel context) {
        return typeModel.getLocalRawName(context);
    }

    @Override
    public <T extends Appendable> T getLocalRawName(TypeModel context, T builder) throws IOException {
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
    public boolean hasEntityFields() {
        return typeModel.hasEntityFields();
    }

    @Override
    public int hashCode(){
        return typeModel.hashCode();
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

}
