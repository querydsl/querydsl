package com.mysema.codegen.model;

import java.util.ArrayList;
import java.util.List;

public final class Factory {
    
    @SuppressWarnings("unchecked")
    public static <T> ClassType<T> type(Class<T> clazz, Class<?>... args){
        List<Type> parameters = new ArrayList<Type>(args.length);
        for (Class<?> arg : args){
            parameters.add(new ClassType(TypeCategory.SIMPLE, arg));
        }
        return new ClassType<T>(TypeCategory.SIMPLE, clazz, parameters);
    }
    
    public static Parameter param(String name, Type type){
        return new Parameter(name, type);
    }
    
    @SuppressWarnings("unchecked")
    public static Parameter param(String name, Class<?> type){
        return new Parameter(name, new ClassType(TypeCategory.SIMPLE, type));
    }
    
    private Factory(){}
    


}
