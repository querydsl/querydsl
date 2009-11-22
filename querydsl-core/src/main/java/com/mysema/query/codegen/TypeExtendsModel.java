/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

/**
 * @author tiwe
 *
 */
public class TypeExtendsModel extends TypeModelAdapter{
    
    public TypeExtendsModel(TypeModel typeModel) {
        super(typeModel);
    }

    @Override
    public StringBuilder getLocalGenericName(TypeModel context, StringBuilder builder, boolean asArgType) {
        if (!asArgType){
            builder.append("? extends ");    
        }            
        return typeModel.getLocalGenericName(context, builder, true);
    }

}
