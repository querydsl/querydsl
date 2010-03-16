package com.mysema.query.codegen;

public final class TypeUtil {

    // FIXME
    public static Type transform(Type type, Type declaringType, EntityType context){
        if (type instanceof TypeExtends){
            TypeExtends extendsType = (TypeExtends)type;
            if (extendsType.getVarName() != null){
                type = extendsType.resolve(context, declaringType);
            }            
        }

        if(type != null && type.getParameterCount() > 0){
            Type[] params = new Type[type.getParameterCount()];
            boolean transformed = false;
            for (int i = 0; i < type.getParameterCount(); i++){
                Type param = type.getParameter(i);
                params[i] = transform(param, declaringType, context);
                if (params[i] != param){
                    transformed = true;
                }
            }
            if (transformed){
                type = new SimpleType(type.getCategory(), 
                    type.getFullName(), type.getPackageName(), type.getSimpleName(),
                    type.isFinal(), params);
            }
        }
        
        return type;
    }
    
    private TypeUtil(){}
}
