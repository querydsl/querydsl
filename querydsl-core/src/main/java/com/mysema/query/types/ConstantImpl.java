/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysema.commons.lang.Assert;
import com.mysema.query.types.expr.NumberConstant;
import com.mysema.query.types.expr.NumberExpression;

/**
 * @author tiwe
 */
@SuppressWarnings("unchecked")
public class ConstantImpl<T> extends ExpressionBase<T> implements Constant<T> {

    private static final long serialVersionUID = -3898138057967814118L;
    
    private static final int CACHE_SIZE = 256;
    
    private static final NumberExpression<Byte>[] BYTES = new NumberExpression[CACHE_SIZE];

    private static final NumberExpression<Integer>[] INTEGERS = new NumberExpression[CACHE_SIZE];
    
    private static final NumberExpression<Long>[] LONGS = new NumberExpression[CACHE_SIZE];

    private static final NumberExpression<Short>[] SHORTS = new NumberExpression[CACHE_SIZE];

    private static final Map<String,Constant<String>> STRINGS;

    static{
        List<String> strs = new ArrayList<String>(Arrays.asList("", ".", ".*", "%"));
        for (int i = 0; i < CACHE_SIZE; i++){
            strs.add(String.valueOf(i));
        }

        STRINGS = new HashMap<String,Constant<String>>(strs.size());
        for (String str : strs){
            STRINGS.put(str, new ConstantImpl<String>(str));
        }
        
        for (int i = 0; i < CACHE_SIZE; i++){
            INTEGERS[i] = new NumberConstant<Integer>(Integer.class, Integer.valueOf(i));
            SHORTS[i] = new NumberConstant<Short>(Short.class, Short.valueOf((short)i));
            BYTES[i] = new NumberConstant<Byte>(Byte.class, Byte.valueOf((byte)i));
            LONGS[i] = new NumberConstant<Long>(Long.class, Long.valueOf(i));
        }
    }

    public static NumberExpression<Byte> create(byte i){
        if (i >= 0 && i < CACHE_SIZE){
            return BYTES[i];
        }else{
            return new NumberConstant<Byte>(Byte.class, Byte.valueOf(i));
        }
    }

    public static NumberExpression<Integer> create(int i){
        if (i >= 0 && i < CACHE_SIZE){
            return INTEGERS[i];
        }else{
            return new NumberConstant<Integer>(Integer.class, Integer.valueOf(i));
        }
    }

    public static NumberExpression<Long> create(long i){
        if (i >= 0 && i < CACHE_SIZE){
            return LONGS[(int)i];
        }else{
            return new NumberConstant<Long>(Long.class, Long.valueOf(i));
        }
    }

    public static NumberExpression<Short> create(short i){
        if (i >= 0 && i < CACHE_SIZE){
            return SHORTS[i];
        }else{
            return new NumberConstant<Short>(Short.class, Short.valueOf(i));
        }
    }
    
    public static Constant<String> create(String str){
        return create(str, false);
    }

    public static Constant<String> create(String str, boolean populateCache) {
        if (STRINGS.containsKey(str)){
            return STRINGS.get(str);
        }else{
            Constant<String> rv = new ConstantImpl<String>(Assert.notNull(str,"str"));
            if (populateCache){
                STRINGS.put(str, rv);
            }
            return rv;
        }
    }

    private final T constant;
    
    public ConstantImpl(T constant){
        super((Class)constant.getClass());
        this.constant = constant;
    }
    
    @Override
    public <R, C> R accept(Visitor<R, C> v, C context) {
        return v.visit(this, context);
    }

    @Override
    public boolean equals(Object o){
        if (o == this){
            return true;
        }else if (o instanceof Constant<?>){
            return ((Constant<?>)o).getConstant().equals(constant);
        }else{
            return false;
        }
    }
    
    @Override
    public T getConstant() {
        return constant;
    }
    
    public int hashCode(){
        return constant.hashCode();
    }

}
