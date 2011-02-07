/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import com.mysema.commons.lang.Assert;

/**
 * ArrayConstructorExpression extends {@link ExpressionBase} to represent array initializers
 *
 * @author tiwe
 *
 * @param <D> component type
 */
public class ArrayConstructorExpression<D> extends ExpressionBase<D[]> implements FactoryExpression<D[]> {

    private static final long serialVersionUID = 8667880104290226505L;

    private final Class<D> elementType;
    
    private final List<Expression<?>> args;
    
    private final List<Expression<?>> expandedArgs;

    @SuppressWarnings("unchecked")
    public ArrayConstructorExpression(Expression<?>... args) {
        this((Class)Object[].class, (Expression[])args);
    }

    @SuppressWarnings("unchecked")
    public ArrayConstructorExpression(Class<D[]> type, Expression<D>... args) {
        super(type);
        this.elementType = (Class<D>) Assert.notNull(type.getComponentType(),"componentType");
        this.args = Arrays.<Expression<?>>asList(args);
        this.expandedArgs = FactoryExpressionUtils.expand(this.args);
    }

    public final Class<D> getElementType() {
        return elementType;
    }

    @Override
    public <R,C> R accept(Visitor<R,C> v, C context) {
        return v.visit(this, context);
    }

    @SuppressWarnings("unchecked")
    @Override
    public D[] newInstance(Object... a){
        Object[] compressedArgs = FactoryExpressionUtils.compress(this.args, a);
        if (compressedArgs.getClass().getComponentType().equals(elementType)){
            return (D[])compressedArgs;
        }else{
            D[] rv = (D[]) Array.newInstance(elementType, compressedArgs.length);
            System.arraycopy(compressedArgs, 0, rv, 0, compressedArgs.length);
            return rv;
        }
    }


    @Override
    public List<Expression<?>> getArgs() {
        return expandedArgs;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this){
            return true;
        }else if (obj instanceof ArrayConstructorExpression<?>){
            ArrayConstructorExpression<?> c = (ArrayConstructorExpression<?>)obj;
            return args.equals(c.args) && getType().equals(c.getType());
        }else{
            return false;
        }
    }
    
    @Override
    public int hashCode(){
        return getType().hashCode();
    }
    
}
