/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.io.IOException;

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
    
    public TypeExtendsModel(String varName, TypeModel typeModel) {
        this(typeModel);
        this.varName = varName;
    }

    public TypeExtendsModel(TypeModel typeModel) {
        super(typeModel);
    }

    @Override
    public void getLocalGenericName(TypeModel context, Appendable builder, boolean asArgType) throws IOException {
        if (!asArgType){
            builder.append("? extends ");    
        }            
        getTypeModel().getLocalGenericName(context, builder, true);
    }

}
