/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.codegen;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author tiwe
 *
 */
public final class ClassUtils {
    
    public static String getName(Class<?> cl){
        return getName(cl, Collections.singleton(Object.class.getPackage()), Collections.<Class<?>>emptySet());
    }
    
    public static String getName(Class<?> cl, Set<Package> packages, Set<Class<?>> classes) {
        if (cl.isArray()) {
            return getName(cl.getComponentType(), packages, classes) + "[]";
        } else if (cl.getPackage() == null || packages.contains(cl.getPackage()) || classes.contains(cl)) {
            return cl.getSimpleName().replace('$', '.');
        } else {
            return cl.getName().replace('$', '.');
        }
    }
    
    public static String getName(Type<?> cl){
        return getName(cl, Collections.singleton(Object.class.getPackage()), Collections.<Class<?>>emptySet());
    }
    
    public static String getName(Type<?> type, Set<Package> packages, Set<Class<?>> classes){
        if (type.getParameters().isEmpty()){
            return getName(type.getJavaClass(), packages, classes);
        }else{
            StringBuilder builder = new StringBuilder();
            builder.append(getName(type.getJavaClass(), packages, classes));
            builder.append("<");
            boolean first = true;
            for (Type<?> parameter : type.getParameters()){
                builder.append(getName(parameter, packages, classes));
                if (!first){
                    builder.append(",");
                }
                first = false;
            }
            builder.append(">");
            return builder.toString();
        }
    }
    
    public static Class<?> normalize(Class<?> clazz){
        if (List.class.isAssignableFrom(clazz)){
            return List.class;
        }else if (Set.class.isAssignableFrom(clazz)){
            return Set.class;
        }else if (Collection.class.isAssignableFrom(clazz)){
            return Collection.class;
        }else if (Map.class.isAssignableFrom(clazz)){    
            return Map.class;
        }else{
            return clazz;    
        }
    }

    private ClassUtils(){}
}
