/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar.types;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.HashSet;
import java.util.Set;

/**
 * Projection provides
 *
 * @author tiwe
 * @version $Id$
 */
public class Projection extends Path.PEntity<Object>{
    
    private Set<String> fields = new HashSet<String>();
    
    private String entityName;
    
    private static Method _numberMethod, _comparableMethod;
    
    static{
        try {
            _numberMethod = PEntity.class.getDeclaredMethod("_number", String.class, Class.class);
            _comparableMethod = PEntity.class.getDeclaredMethod("_comparable", String.class, Class.class);
            _numberMethod.setAccessible(true);
            _comparableMethod.setAccessible(true);
        } catch (Exception e) {
            String error = "Caught " + e.getClass().getName();
            throw new RuntimeException(error, e);
        }
    }
    
    public Projection(String entityName) {
        super(Object.class, entityName, PathMetadata.forVariable(entityName));
        Class<?> cl = getClass();
        try{
            while (!cl.equals(Projection.class)){
                for (Field field : cl.getDeclaredFields()){
                    if (Expr.class.isAssignableFrom(field.getType()) && field.getGenericType() instanceof ParameterizedType){
                        field.setAccessible(true);
                        fields.add(field.getName());
                        handleField(field);                    
                    }                              
                }    
                cl = cl.getSuperclass();
            }   
        }catch(Exception e){
            String error = "Caught " + e.getClass().getName();
            throw new RuntimeException(error, e);
        }                
        this.entityName = entityName;        
    }

    private void handleField(Field field) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        ParameterizedType type = (ParameterizedType) field.getGenericType();
        Class<?> exprType = (Class<?>) type.getActualTypeArguments()[0];
        Expr<?> fieldVal;
        if (Boolean.class.isAssignableFrom(exprType)){            
            fieldVal = _boolean(field.getName());
        }else if (Number.class.isAssignableFrom(exprType)){
            fieldVal = (Expr<?>) _numberMethod.invoke(this, field.getName(), exprType);            
        }else if (String.class.isAssignableFrom(exprType)){
            fieldVal = _string(field.getName());
        }else if (Comparable.class.isAssignableFrom(exprType)){
            fieldVal = (Expr<?>) _comparableMethod.invoke(this, field.getName(), exprType);
        }else{
            fieldVal = _simple(field.getName(), exprType);
        }
        if (field.getType().isAssignableFrom(fieldVal.getClass())){
            field.set(this, fieldVal);    
        }else{
            // unsupported
        }
    }
        
    public String getName(){
        return entityName;
    }
    
    public void accept(SubQuery<?,?> query){
        // TODO : validate that the given SubQuery can be projected        
    }
    
}
