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

    public static String getName(Class<?> cl) {
        return getName(cl, Collections.singleton("java.lang"), Collections.<String> emptySet());
    }

    public static String getFullName(Class<?> cl) {
        if (cl.isArray()) {
            return getFullName(cl.getComponentType()) + "[]";
        } else {
            return cl.getCanonicalName();
        }
    }

    public static String getPackageName(Class<?> cl) {
        if (cl.isArray()) {
            return getPackageName(cl.getComponentType());
        } else if (cl.getPackage() != null) {
            return cl.getPackage().getName();
        } else {
            return "";
        }
    }

    public static String getName(Class<?> cl, Set<String> packages, Set<String> classes) {
        if (cl.isArray()) {
            return getName(cl.getComponentType(), packages, classes) + "[]";
        } else if (cl.getPackage() == null
                || packages.contains(cl.getPackage().getName())
                || classes.contains(cl.getCanonicalName())
                || classes.contains(cl.getCanonicalName().substring(0,
                        cl.getName().lastIndexOf('.')))) {
            if (cl.getPackage() != null) {
                return cl.getCanonicalName().substring(cl.getPackage().getName().length() + 1);
            } else {
                return cl.getCanonicalName();
            }
        } else {
            return cl.getCanonicalName();
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
