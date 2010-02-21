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
public interface TypeModels {   
    
    TypeModel OBJECTS = new ClassTypeModel(TypeCategory.SIMPLE, Object[].class);
    
    TypeModel OBJECT = new ClassTypeModel(TypeCategory.SIMPLE, Object.class);

    TypeModel BOOLEAN = new ClassTypeModel(TypeCategory.BOOLEAN, Boolean.class, boolean.class);
    
    TypeModel BYTE = new ClassTypeModel(TypeCategory.NUMERIC, Byte.class, byte.class);
    
    TypeModel CHAR = new ClassTypeModel(TypeCategory.COMPARABLE, Character.class, char.class);
    
    TypeModel DOUBLE = new ClassTypeModel(TypeCategory.NUMERIC, Double.class, double.class);
    
    TypeModel FLOAT = new ClassTypeModel(TypeCategory.NUMERIC, Float.class, float.class);
    
    TypeModel INT = new ClassTypeModel(TypeCategory.NUMERIC, Integer.class, int.class);
    
    TypeModel LONG = new ClassTypeModel(TypeCategory.NUMERIC, Long.class, long.class);
    
    TypeModel SHORT = new ClassTypeModel(TypeCategory.NUMERIC, Short.class, short.class);

    TypeModel STRING = new ClassTypeModel(TypeCategory.STRING, String.class);
    
}
