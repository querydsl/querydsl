package com.mysema.query.codegen;

import javax.annotation.Nullable;

public final class ClassUtil {
    
    private ClassUtil(){}
    
    @Nullable
    public static Class<?> safeClassForName(String stype) {
        try {
            return stype != null ? Class.forName(stype) : null;
        } catch (ClassNotFoundException e) {
            return null;
        }
    } 

}
