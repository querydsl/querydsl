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
    
    public static String getName(Class<?> cl){
        return getName(cl, Collections.singleton("java.lang"), Collections.<String>emptySet());
    }
    
    public static String getName(Class<?> cl, Set<String> packages, Set<String> classes) {
        if (cl.isArray()) {
            return getName(cl.getComponentType(), packages, classes) + "[]";
        } else if (cl.getPackage() == null 
                || cl.getPackage().getName().equals("java.lang") 
                || packages.contains(cl.getPackage().getName()) 
                || classes.contains(cl.getName())) {
            if (cl.getPackage() != null){
                String localName = cl.getName().substring(cl.getPackage().getName().length()+1);
                return localName.replace('$', '.');
            }else{
                return cl.getSimpleName().replace('$', '.');    
            }
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
        }else if (Map.class.isAssignableFrom(clazz)){    
            return Map.class;
        }else{
            return clazz;    
        }
    }

    private ClassUtils(){}
}
