/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.io.IOException;

import javax.annotation.Nullable;

/**
 * TypeSuper is a Type for type variables and wildcard types
 * 
 * @author tiwe
 *
 */
// TODO : take varName into account
public class TypeSuper extends TypeAdapter{
    
    private final Type superType;
    
    @Nullable
    private final String varName;

    public TypeSuper(String varName, Type type) {
        super(Types.OBJECT);        
        this.superType = type;
        this.varName = varName;
    }

    public TypeSuper(Type type) {
        super(Types.OBJECT);
        this.superType = type;
        this.varName = null;
    }
    
    @Override
    public void appendLocalGenericName(Type context, Appendable builder, boolean asArgType) throws IOException {
        if (!asArgType){
            builder.append("? super ");
            superType.appendLocalGenericName(context, builder, true);
        }else{
            super.appendLocalGenericName(context, builder, asArgType);
        }    
    }

    public String getVarName(){
        return varName;
    }
}
