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
public final class TypeModels {
    
    private TypeModels(){}
    
    public static final TypeModel OBJECTS = new ClassTypeModel(TypeCategory.SIMPLE, Object[].class);
    
    public static final TypeModel OBJECT = new ClassTypeModel(TypeCategory.SIMPLE, Object.class);

    public static final TypeModel BOOLEAN = new ClassTypeModel(TypeCategory.BOOLEAN, Boolean.class, boolean.class);
    
    public static final TypeModel BYTE = new ClassTypeModel(TypeCategory.NUMERIC, Byte.class, byte.class);
    
    public static final TypeModel CHAR = new ClassTypeModel(TypeCategory.COMPARABLE, Character.class, char.class);
    
    public static final TypeModel DOUBLE = new ClassTypeModel(TypeCategory.NUMERIC, Double.class, double.class);
    
    public static final TypeModel FLOAT = new ClassTypeModel(TypeCategory.NUMERIC, Float.class, float.class);
    
    public static final TypeModel INT = new ClassTypeModel(TypeCategory.NUMERIC, Integer.class, int.class);
    
    public static final TypeModel LONG = new ClassTypeModel(TypeCategory.NUMERIC, Long.class, long.class);
    
    public static final TypeModel SHORT = new ClassTypeModel(TypeCategory.NUMERIC, Short.class, short.class);

    public static final TypeModel STRING = new ClassTypeModel(TypeCategory.STRING, String.class);
    
}
