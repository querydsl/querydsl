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
    public <T extends Appendable> T getLocalGenericName(TypeModel context, T builder, boolean asArgType) throws IOException {
        if (!asArgType){
            builder.append("? extends ");    
        }            
        return getTypeModel().getLocalGenericName(context, builder, true);
    }

}
