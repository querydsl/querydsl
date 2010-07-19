/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.ClassUtils;

import com.mysema.query.types.expr.ESimple;

/**
 * EConstructor represents a constructor invocation
 *
 * @author tiwe
 *
 * @param <D> Java type
 */
// TODO : rename to something else, since the subclasses do not use constructor invocation
public class EConstructor<D> extends ESimple<D> {

    private static final long serialVersionUID = -602747921848073175L;

    private static Class<?> normalize(Class<?> clazz){
        if (clazz.isPrimitive()){
            return ClassUtils.primitiveToWrapper(clazz);
        }else{
            return clazz;
        }
    }

    public static <D> EConstructor<D> create(Class<D> type, Expr<?>... args){
        for (Constructor<?> c : type.getConstructors()){
            Class<?>[] paramTypes = c.getParameterTypes();
            if (paramTypes.length == args.length){
                boolean found = true;
                for (int i = 0; i < paramTypes.length; i++){
                    if (!normalize(paramTypes[i]).isAssignableFrom(args[i].getType())){
                        found = false;
                        break;
                    }
                }
                if (found){
                    return new EConstructor<D>(type, paramTypes, args);
                }
            }
        }
        throw new ExprException("Got no matching constructor");
    }

    private final List<Expr<?>> args;

    private final Class<?>[] parameterTypes;

    public EConstructor(Class<D> type, Class<?>[] paramTypes, Expr<?>... args) {
        this(type, paramTypes, Arrays.asList(args));
    }
    
    public EConstructor(Class<D> type, Class<?>[] paramTypes, List<Expr<?>> args) {
        super(type);
        this.parameterTypes = paramTypes.clone();
        this.args = args;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this){
            return true;
        }else if (obj instanceof EConstructor<?>){
            EConstructor<?> c = (EConstructor<?>)obj;
            return Arrays.equals(parameterTypes, c.parameterTypes)
                && args.equals(c.args)
                && getType().equals(c.getType());
        }else{
            return false;
        }
    }

    @Override
    public int hashCode(){
        return getType().hashCode();
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
     * Create a projection with the given arguments
     *
     * @param args
     * @return
     */
    public D newInstance(Object... args){
        try {
            return (D) getType().getConstructor(parameterTypes).newInstance(args);
        } catch (SecurityException e) {
           throw new ExprException(e.getMessage(), e);
        } catch (NoSuchMethodException e) {
           throw new ExprException(e.getMessage(), e);
        } catch (InstantiationException e) {
            throw new ExprException(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            throw new ExprException(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            throw new ExprException(e.getMessage(), e);
        }
    }

}
