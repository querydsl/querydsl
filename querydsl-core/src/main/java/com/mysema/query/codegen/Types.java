/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

/**
 * @author tiwe
 *
 */
public final class Types {
    
    private Types(){}
    
    public static final Type OBJECTS = new ClassType(TypeCategory.ARRAY, Object[].class);
    
    public static final Type OBJECT = new ClassType(TypeCategory.SIMPLE, Object.class);

    public static final Type BOOLEAN = new ClassType(TypeCategory.BOOLEAN, Boolean.class, boolean.class);
    
    public static final Type BYTE = new ClassType(TypeCategory.NUMERIC, Byte.class, byte.class);
    
    public static final Type CHAR = new ClassType(TypeCategory.COMPARABLE, Character.class, char.class);
    
    public static final Type DOUBLE = new ClassType(TypeCategory.NUMERIC, Double.class, double.class);
    
    public static final Type FLOAT = new ClassType(TypeCategory.NUMERIC, Float.class, float.class);
    
    public static final Type INT = new ClassType(TypeCategory.NUMERIC, Integer.class, int.class);
    
    public static final Type LONG = new ClassType(TypeCategory.NUMERIC, Long.class, long.class);
    
    public static final Type SHORT = new ClassType(TypeCategory.NUMERIC, Short.class, short.class);

    public static final Type STRING = new ClassType(TypeCategory.STRING, String.class);
    
}
