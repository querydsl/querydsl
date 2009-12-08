/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import javax.annotation.Nullable;

/**
 * TypeSuperModel is a TypeModel for type variables and wildcard types
 * 
 * @author tiwe
 *
 */
public class TypeSuperModel extends TypeModelAdapter{
    
    private static final TypeModel objectModel = new ClassTypeModel(TypeCategory.SIMPLE, Object.class);
    
    @Nullable
    private String varName;
    
    private TypeModel superModel;
    
    public TypeSuperModel(TypeModel typeModel) {
        super(objectModel);
        this.superModel = typeModel;
    }

    public TypeSuperModel(String varName, TypeModel typeModel) {
        this(typeModel);
        this.varName = varName;
    }

    @Override
    public StringBuilder getLocalGenericName(TypeModel context, StringBuilder builder, boolean asArgType) {
        if (!asArgType){
            builder.append("? super ");
            return superModel.getLocalGenericName(context, builder, true);
        }else{
            return super.getLocalGenericName(context, builder, asArgType);
        }    
    }

}
