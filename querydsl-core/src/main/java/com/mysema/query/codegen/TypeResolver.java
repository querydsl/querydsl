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
        Type resolved = type;
        
        // handle generic types
        if (resolved instanceof TypeExtends){
            resolved = resolveTypeExtends((TypeExtends)resolved, declaringType, context);
        }

        // handle generic type parameters
        if(resolved.getParameterCount() > 0){
            resolved = resolveWithParameters(resolved, declaringType, context);
        }
        
        return resolved;
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

        if (index > -1){
            // get binding of var via model supertype
            Supertype type = subtype.getSuperType();
            while (!type.getType().equals(declaringType)){                    
                type = type.getEntityType().getSuperType();
            }
            return type.getType().getParameter(index);            
        }else{
            // TODO : error
            return typeExtends;
        }        
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
            return new SimpleType(type.getCategory(), 
                type.getFullName(), type.getPackageName(), type.getSimpleName(),
                type.isFinal(), params);
        }else{
            return type;    
        }        
    }
    
    private TypeResolver(){}
}
