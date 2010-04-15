/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.codegen;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
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
    
    public static Class<?> normalize(Class<?> clazz){
        if (List.class.isAssignableFrom(clazz)){
            return List.class;
        }else if (Set.class.isAssignableFrom(clazz)){
            return Set.class;
        }else if (Collection.class.isAssignableFrom(clazz)){
            return Collection.class;
        }else{
            return clazz;    
        }
    }

    private ClassUtils(){}
}
