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
// TODO : take varName into account
public class TypeExtendsModel extends TypeModelAdapter{

    @Nullable
    private final String varName;
    
    public TypeExtendsModel(String varName, TypeModel typeModel) {
        super(typeModel);
        this.varName = varName;
    }

    public TypeExtendsModel(TypeModel typeModel) {
        super(typeModel);
        varName = null;
    }

    @Override
    public void appendLocalGenericName(TypeModel context, Appendable builder, boolean asArgType) throws IOException {
        if (!asArgType){
            builder.append("? extends ");    
        }            
        getTypeModel().appendLocalGenericName(context, builder, true);
    }
    
    public String getVarName(){
        return varName;
    }
    

}
