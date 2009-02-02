/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar.types;

import org.apache.commons.lang.ClassUtils;

/**
 * Constructor represents a constructor invocation in a projection
 *
 * @author tiwe
 * @version $Id$
 *
 * @param <D>
 */
public class Constructor<D> extends Expr<D> {
    private final Expr<?>[] args;
    private java.lang.reflect.Constructor<D> javaConstructor;
    public Constructor(Class<D> type, Expr<?>... args) {
        super(type);
        this.args = args;
    }
    public Expr<?>[] getArgs() {
        return args;
    }
    
    public static class CArray<D> extends Constructor<D[]> {
        private Class<D> elementType;
        public CArray(Class<D> type, Expr<D>... args) {
            super(null, args);
            this.elementType = type;
        }
        public Class<D> getElementType(){
            return elementType;
        }
    }
    
    /**
     * Returns the "real" constructor that matches the Constructor expression
     * @return
     */
    @SuppressWarnings("unchecked")
    public java.lang.reflect.Constructor<D> getJavaConstructor(){   
        if (javaConstructor == null){
            Class<D> type = getType();
            Expr<?>[] args = getArgs();
            for (java.lang.reflect.Constructor<?> c : type.getConstructors()){
                if (c.getParameterTypes().length == args.length){
                    boolean match = true;
                    for (int i = 0; i < args.length && match; i++){
                        Class<?> ptype = c.getParameterTypes()[i];
                        if (ptype.isPrimitive()){
                            ptype = ClassUtils.primitiveToWrapper(ptype);
                        }
                        match &= ptype.isAssignableFrom(args[i].getType());                                        
                    }
                    if (match){
                        javaConstructor = (java.lang.reflect.Constructor<D>)c;
                        return javaConstructor;
                    }
                }
            }
            throw new IllegalArgumentException("no suitable constructor found");       
        }else{
            return javaConstructor;
        }
         
    }

}