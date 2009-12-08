/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import javax.annotation.Nullable;

/**
 * TypeExtendsModel is a TypeModel for type variables and wildcard types
 * 
 * @author tiwe
 *
 */
public class TypeExtendsModel extends TypeModelAdapter{

    @Nullable
    private String varName;
    
    public TypeExtendsModel(TypeModel typeModel) {
        super(typeModel);
    }

    public TypeExtendsModel(String varName, TypeModel typeModel) {
        this(typeModel);
        this.varName = varName;
    }

    @Override
    public StringBuilder getLocalGenericName(TypeModel context, StringBuilder builder, boolean asArgType) {
        if (!asArgType){
            builder.append("? extends ");    
        }            
        return typeModel.getLocalGenericName(context, builder, true);
    }

}
