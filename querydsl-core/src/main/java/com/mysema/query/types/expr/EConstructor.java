/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.ClassUtils;

import com.mysema.query.types.Visitor;

/**
 * EConstructor represents a constructor invocation
 * 
 * @author tiwe
 * 
 * @param <D> Java type
 */
@SuppressWarnings("serial")
public class EConstructor<D> extends Expr<D> {
    
    private final List<Expr<?>> args;

    private final Class<?>[] parameterTypes;
    
    public static <D> EConstructor<D> create(Class<D> type, Expr<?>... args){
        for (Constructor<?> c : type.getConstructors()){
            Class<?>[] paramTypes = c.getParameterTypes();            
            if (paramTypes.length == args.length){
                boolean found = true;
                for (int i = 0; i < paramTypes.length; i++){
                    Class<?> paramType = paramTypes[i];
                    if (paramType.isPrimitive()) paramType = ClassUtils.primitiveToWrapper(paramType);                    
                    if (!paramType.isAssignableFrom(args[i].getType())){
                        found = false;
                        break;
                    }
                }
                if (found){
                    return new EConstructor<D>(type, paramTypes, args);    
                }                
            }            
        }
        throw new IllegalArgumentException("Got no matching constructor");        
    }

    public EConstructor(Class<D> type, Class<?>[] paramTypes, Expr<?>... args) {
        super(type);
        this.parameterTypes = paramTypes;
        this.args = Collections.unmodifiableList(Arrays.asList(args));
    }

    /**
     * Get the constructor invocation arguments
     * 
     * @return
     */
    public final List<Expr<?>> getArgs() {
        return args;
    }

    /**
     * Get the constructor invocation argument with the given index
     * 
     * @param index
     * @return
     */
    public final Expr<?> getArg(int index) {
        return args.get(index);
    }

    /**
     * Returns the "real" constructor that matches the Constructor expression
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public Constructor<D> getJavaConstructor() {
        try {
            return (Constructor<D>) getType().getConstructor(parameterTypes);
        } catch (SecurityException e) {
           throw new RuntimeException(e.getMessage(), e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    @Override
    public void accept(Visitor v) {
        v.visit(this);        
    }
}