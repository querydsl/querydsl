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
public class TypeSuperModel extends TypeModelAdapter{
    
    private static final TypeModel objectModel = new SimpleClassTypeModel(TypeCategory.SIMPLE, Object.class);
    
    private TypeModel superModel;
    
    public TypeSuperModel(TypeModel typeModel) {
        super(objectModel);
        this.superModel = typeModel;
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
