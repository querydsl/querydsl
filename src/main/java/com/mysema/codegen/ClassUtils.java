/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.codegen;

import java.util.Collections;
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

    private ClassUtils(){}
}
