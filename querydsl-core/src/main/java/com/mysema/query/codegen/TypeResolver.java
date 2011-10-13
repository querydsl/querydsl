/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.codegen;

import com.mysema.codegen.model.SimpleType;
import com.mysema.codegen.model.Type;
import com.mysema.codegen.model.TypeExtends;

/**
 * TypeResolver provides type resolving funcationlity for resolving generic type variables to concrete types
 * 
 * @author tiwe
 *
 */
public final class TypeResolver {

    public static Type resolve(Type type, Type declaringType, EntityType context){        
        Type resolved = type;

        // unwrap entity type
        if (resolved instanceof EntityType) {
            resolved = ((EntityType)resolved).getInnerType();
        }
        
        // handle generic types
        if (resolved instanceof TypeExtends) {
            resolved = resolveTypeExtends((TypeExtends)resolved, declaringType, context);
        }

        // handle generic type parameters
        if(!resolved.getParameters().isEmpty()) {
            resolved = resolveWithParameters(resolved, declaringType, context);
        }
        
        // rewrap entity type
        if (type instanceof EntityType) {
            if (!((EntityType)type).getInnerType().equals(resolved)) {
                resolved = new EntityType(resolved, ((EntityType)type).getSuperTypes());    
            } else {
                // reset to original type
                resolved = type;
            }            
        }
        
        return resolved;
    }

    private static Type resolveTypeExtends(TypeExtends typeExtends, Type declaringType, EntityType subtype){
        // typeExtends without variable name can't be resolved
        if (typeExtends.getVarName() == null){ //NOSONAR
            return typeExtends;
        }

        // get parameter index of var in declaring type
        int index = -1;
        for (int i = 0; i < declaringType.getParameters().size(); i++){
            Type param = declaringType.getParameters().get(i);
            // unwrap entity type
            if (param instanceof EntityType) {
                param = ((EntityType)param).getInnerType();
            }
            if (param instanceof TypeExtends && ((TypeExtends)param).getVarName().equals(typeExtends.getVarName())){
                index = i;
            }
        }

        if (index > -1){
            // get binding of var via model supertype
            Supertype type = subtype.getSuperType();            
            while (!type.getEntityType().equals(declaringType)){
                type = type.getEntityType().getSuperType();                
            }
            return type.getType().getParameters().get(index);
        }else{
            // TODO : error
            return typeExtends;
        }
    }

    private static Type resolveWithParameters(Type type, Type declaringType, EntityType context) {
        Type[] params = new Type[type.getParameters().size()];
        boolean transformed = false;
        for (int i = 0; i < type.getParameters().size(); i++) {
            Type param = type.getParameters().get(i);
            if (param != null) {
                params[i] = resolve(param, declaringType, context);
                if (params[i] != param) {
                    transformed = true;
                }
            }
        }
        if (transformed) {
            return new SimpleType(
                    type.getCategory(),
                    type.getFullName(), 
                    type.getPackageName(), 
                    type.getSimpleName(),
                    type.isFinal(),
                    type.isPrimitive(),
                    params);
        } else {
            return type;
        }
    }

    private TypeResolver(){}
}
