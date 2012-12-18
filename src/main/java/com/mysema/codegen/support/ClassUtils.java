/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.codegen.support;

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

    private static final Set<String> JAVA_LANG = Collections.singleton("java.lang");
    
    public static String getName(Class<?> cl) {
        return getName(cl, JAVA_LANG, Collections.<String> emptySet());
    }

    public static String getFullName(Class<?> cl) {
//        return cl.getCanonicalName();
        if (cl.isArray()) {
            return getFullName(cl.getComponentType()) + "[]";
        }
        final String name = cl.getName();
        if (name.indexOf('$') > 0) {
            return getFullName(cl.getDeclaringClass()) + "." + cl.getSimpleName();
        }
        return name;
    }

    public static String getPackageName(Class<?> cl) {
        while (cl.isArray()) {
            cl = cl.getComponentType();
        }
        final String name = cl.getName();
        final int i = name.lastIndexOf('.');
        if (i > 0) {
            return name.substring(0, i); 
        } else {
            return "";
        }
    }
    
    public static String getName(Class<?> cl, Set<String> packages, Set<String> classes) {
        if (cl.isArray()) {
            return getName(cl.getComponentType(), packages, classes) + "[]";
        }        
        if (cl.getName().indexOf('$') > 0) {
            return getName(cl.getDeclaringClass(), packages, classes) + "." + cl.getSimpleName();
        }
        final String canonicalName = cl.getName();
        final int i = canonicalName.lastIndexOf('.');
        if (i == -1) {    
            return canonicalName;        
        } else {
            final String packageName = canonicalName.substring(0, i);
            if (packages.contains(packageName)
                || classes.contains(canonicalName)
                || classes.contains(canonicalName.substring(0, canonicalName.lastIndexOf('.')))) {
                return canonicalName.substring(packageName.length() + 1);
            }  else {
                return canonicalName;
            }
        }
    }

    public static Class<?> normalize(Class<?> clazz) {
        if (List.class.isAssignableFrom(clazz)) {
            return List.class;
        } else if (Set.class.isAssignableFrom(clazz)) {
            return Set.class;
        } else if (Collection.class.isAssignableFrom(clazz)) {
            return Collection.class;
        } else if (Map.class.isAssignableFrom(clazz)) {
            return Map.class;
        // check for CGLIB generated classes
        } else if (clazz.getName().contains("$$")) {
            Class<?> zuper = clazz.getSuperclass();
            if (zuper != null && !Object.class.equals(zuper)) {
                return zuper;
            }
        }
        return clazz;
    }

    private ClassUtils() {
    }

}
