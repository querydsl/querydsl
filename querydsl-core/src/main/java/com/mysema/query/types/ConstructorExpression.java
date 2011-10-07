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

import javax.annotation.Nullable;

import org.apache.commons.lang.ClassUtils;

import com.mysema.query.types.expr.SimpleExpression;
import com.mysema.query.types.expr.SimpleOperation;

/**
 * ConstructorExpression represents a constructor invocation
 *
 * @author tiwe
 *
 * @param <T> expression type
 */
public class ConstructorExpression<T> extends ExpressionBase<T> implements FactoryExpression<T> {

    private static final long serialVersionUID = -602747921848073175L;

    private static Class<?> normalize(Class<?> clazz){
        if (clazz.isPrimitive()){
            return ClassUtils.primitiveToWrapper(clazz);
        }else{
            return clazz;
        }
    }
    
    private static Class<?>[] getRealParameters(Class<?> type, Class<?>[] givenTypes) {
        for (Constructor<?> c : type.getConstructors()){
            Class<?>[] paramTypes = c.getParameterTypes();
            if (paramTypes.length == givenTypes.length){
                boolean found = true;
                for (int i = 0; i < paramTypes.length; i++){
                    if (!normalize(paramTypes[i]).isAssignableFrom(normalize(givenTypes[i]))){
                        found = false;
                        break;
                    }
                }
                if (found){
                    return paramTypes;
                }
            }
        }
        throw new ExpressionException("Got no matching constructor");
    }

    public static <D> ConstructorExpression<D> create(Class<D> type, Expression<?>... args){
        Class<?>[] paramTypes = new Class[args.length];
        for (int i = 0; i < paramTypes.length; i++) {
            paramTypes[i] = args[i].getType();
        }
        return new ConstructorExpression<D>(type, paramTypes, args);
    }

    private final List<Expression<?>> args;

    private final Class<?>[] parameterTypes;

    @Nullable
    private transient Constructor<?> constructor;

    public ConstructorExpression(Class<T> type, Class<?>[] paramTypes, Expression<?>... args) {
        this(type, paramTypes, Arrays.asList(args));
    }

    public ConstructorExpression(Class<T> type, Class<?>[] paramTypes, List<Expression<?>> args) {
        super(type);
        this.parameterTypes = getRealParameters(type, paramTypes).clone();
        this.args = args;
    }

    /**
     * Create an alias for the expression
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public Expression<T> as(Path<T> alias) {
        return OperationImpl.create((Class<T>)getType(),Ops.ALIAS, this, alias);
    }

    /**
     * Create an alias for the expression
     *
     * @return
     */
    public Expression<T> as(String alias) {
        return as(new PathImpl<T>(getType(), alias));
    }
    
    @Override
    public <R,C> R accept(Visitor<R,C> v, C context) {
        return v.visit(this, context);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this){
            return true;
        }else if (obj instanceof ConstructorExpression<?>){
            ConstructorExpression<?> c = (ConstructorExpression<?>)obj;
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

    public final List<Expression<?>> getArgs() {
        return args;
    }

    @SuppressWarnings("unchecked")
    public T newInstance(Object... args){
        try {
            if (constructor == null){
                constructor = getType().getConstructor(parameterTypes);
            }
            return (T) constructor.newInstance(args);
        } catch (SecurityException e) {
           throw new ExpressionException(e.getMessage(), e);
        } catch (NoSuchMethodException e) {
           throw new ExpressionException(e.getMessage(), e);
        } catch (InstantiationException e) {
            throw new ExpressionException(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            throw new ExpressionException(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            throw new ExpressionException(e.getMessage(), e);
        }
    }

}
