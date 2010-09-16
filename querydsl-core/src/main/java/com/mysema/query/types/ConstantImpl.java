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

/**
 * @author tiwe
 */
public class ConstantImpl<T> extends ExpressionBase<T> implements Constant<T> {

    private static final long serialVersionUID = -3898138057967814118L;
    
    private static final Map<String,Constant<String>> CACHE;

    private static final int CACHE_SIZE = 256;

    static{
        List<String> strs = new ArrayList<String>(Arrays.asList("", ".", ".*", "%"));
        for (int i = 0; i < CACHE_SIZE; i++){
            strs.add(String.valueOf(i));
        }

        CACHE = new HashMap<String,Constant<String>>(strs.size());
        for (String str : strs){
            CACHE.put(str, new ConstantImpl<String>(str));
        }
    }
    

    /**
     * Factory method for constants
     *
     * @param str
     * @return
     */
    public static Constant<String> create(String str){
        return create(str, false);
    }

    public static Constant<String> create(String str, boolean populateCache) {
        if (CACHE.containsKey(str)){
            return CACHE.get(str);
        }else{
            Constant<String> rv = new ConstantImpl<String>(Assert.notNull(str,"str"));
            if (populateCache){
                CACHE.put(str, rv);
            }
            return rv;
        }
    }

    private final T constant;
    
    @SuppressWarnings("unchecked")
    public ConstantImpl(T constant){
        super((Class)constant.getClass());
        this.constant = constant;
    }
    
    @Override
    public T getConstant() {
        return constant;
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
    
    public int hashCode(){
        return constant.hashCode();
    }

}
