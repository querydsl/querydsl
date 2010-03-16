/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

/**
 * @author tiwe
 *
 */
public final class TypeResolver {
    
    public static Type resolve(Type type, Type declaringType, EntityType context){
        // handle generic types
        if (type instanceof TypeExtends){
            type = resolveTypeExtends((TypeExtends)type, declaringType, context);
        }

        // handle generic type parameters
        if(type.getParameterCount() > 0){
            type = resolveWithParameters(type, declaringType, context);
        }
        
        return type;
    }

    private static Type resolveTypeExtends(TypeExtends typeExtends, Type declaringType, EntityType subtype){
        // typeExtends without variable name can't be resolved
        if (typeExtends.getVarName() == null){
            return typeExtends;
        }
        
        // get parameter index of var in declaring type
        int index = -1;
        for (int i = 0; i < declaringType.getParameterCount(); i++){
            Type param = declaringType.getParameter(i);
            if (param instanceof TypeExtends && ((TypeExtends)param).getVarName().equals(typeExtends.getVarName())){
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

    private static Type resolveWithParameters(Type type, Type declaringType, EntityType context) {
        Type[] params = new Type[type.getParameterCount()];
        boolean transformed = false;
        for (int i = 0; i < type.getParameterCount(); i++){
            Type param = type.getParameter(i);
            if (param != null){
                params[i] = resolve(param, declaringType, context);
                if (params[i] != param){
                    transformed = true;
                }    
            }                
        }
        if (transformed){
            type = new SimpleType(type.getCategory(), 
                type.getFullName(), type.getPackageName(), type.getSimpleName(),
                type.isFinal(), params);
        }
        return type;
    }
    
    private TypeResolver(){}
}
