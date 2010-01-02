/**
 * 
 */
package com.mysema.query.alias;

import java.lang.reflect.Method;

import com.mysema.query.types.path.PEntity;

/**
 * @author tiwe
 *
 */
enum MethodType{
    /**
     * 
     */
    GET_MAPPED_PATH,
    /**
     * 
     */
    GETTER,
    /**
     * 
     */
    HASH_CODE,
    /**
     * 
     */
    LIST_ACCESS,
    /**
     * 
     */
    MAP_ACCESS,    
    /**
     * 
     */
    SIZE,
    /**
     * 
     */
    TO_STRING;
    
    public static MethodType get(Method method) {
        String name = method.getName();
        int paramCount = method.getParameterTypes().length;
        Class<?> returnType = method.getReturnType();
        
        if ((name.startsWith("get") || name.startsWith("is")) && paramCount == 0){
            return GETTER;
            
        }else if (name.equals("get") && paramCount == 1){
            if (method.getParameterTypes()[0].equals(int.class)){
                return LIST_ACCESS;    
            }else{
                return MAP_ACCESS;    
            }            
            
        }else if (name.equals("hashCode") && paramCount == 0 && returnType.equals(int.class)){
            return HASH_CODE;
            
        }else if (name.equals("size") && paramCount == 0 && returnType.equals(int.class)){
            return SIZE;
            
        }else if (name.equals("toString") && paramCount == 0 && returnType.equals(String.class)){
            return TO_STRING;
            
        }else if (name.equals("__mappedPath") && paramCount == 0 && returnType.equals(PEntity.class)){
            return GET_MAPPED_PATH;
                        
        }else{
            return null;
        }
    }
}