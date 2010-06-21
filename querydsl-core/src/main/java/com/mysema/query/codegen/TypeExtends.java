/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.codegen;

import java.io.IOException;

import javax.annotation.Nullable;

/**
 * TypeExtends is a Type implementation for type variables and wildcard types
 *
 * @author tiwe
 *
 */
public class TypeExtends extends TypeAdapter{

    @Nullable
    private final String varName;

    public TypeExtends(String varName, Type type) {
        super(type);
        this.varName = varName;
    }

    public TypeExtends(Type type) {
        super(type);
        varName = null;
    }

    @Override
    public void appendLocalGenericName(Type context, Appendable builder, boolean asArgType) throws IOException {
        if (!asArgType){
            builder.append("? extends ");
        }
        getType().appendLocalGenericName(context, builder, true);
    }

    @Nullable
    public String getVarName(){
        return varName;
    }

}
