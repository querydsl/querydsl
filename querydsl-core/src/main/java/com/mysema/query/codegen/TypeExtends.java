/*
 * Copyright (c) 2009 Mysema Ltd.
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
    
    public String getVarName(){
        return varName;
    }
    
    public Type resolve(EntityType subtype, Type declaringType){
        // get parameter index of var in declaring type
        int index = -1;
        for (int i = 0; i < declaringType.getParameterCount(); i++){
            Type param = declaringType.getParameter(i);
            if (param instanceof TypeExtends && ((TypeExtends)param).getVarName().equals(varName)){
                index = i;
            }
        }

        // get binding of var via model supertype
        Supertype type = subtype.getSuperType();
        while (!type.getType().equals(declaringType)){                    
            type = type.getEntityType().getSuperType();
        }
        return type.getType().getParameter(index);
    }
    

}
