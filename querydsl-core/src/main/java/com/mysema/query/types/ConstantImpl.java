/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mysema.query.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysema.commons.lang.Assert;

/**
 * ConstantImpl is the default implementation of the Constant interface
 * 
 * @author tiwe
 */
@SuppressWarnings("unchecked")
public class ConstantImpl<T> extends ExpressionBase<T> implements Constant<T> {

    private static final long serialVersionUID = -3898138057967814118L;
    
    private static final int CACHE_SIZE = 256;
    
    private static final Constant<Character>[] CHARACTERS = new Constant[CACHE_SIZE];
    
    private static final Constant<Byte>[] BYTES = new Constant[CACHE_SIZE];

    private static final Constant<Integer>[] INTEGERS = new Constant[CACHE_SIZE];
    
    private static final Constant<Long>[] LONGS = new Constant[CACHE_SIZE];

    private static final Constant<Short>[] SHORTS = new Constant[CACHE_SIZE];

    private static final Map<String,Constant<String>> STRINGS;
    
    private static final Constant<Boolean> FALSE = new ConstantImpl<Boolean>(Boolean.FALSE);

    private static final Constant<Boolean> TRUE = new ConstantImpl<Boolean>(Boolean.TRUE);

    static {
        List<String> strs = new ArrayList<String>(Arrays.asList("", ".", ".*", "%"));
        for (int i = 0; i < CACHE_SIZE; i++) {
            strs.add(String.valueOf(i));
        }

        STRINGS = new HashMap<String,Constant<String>>(strs.size());
        for (String str : strs) {
            STRINGS.put(str, new ConstantImpl<String>(str));
        }
        
        for (int i = 0; i < CACHE_SIZE; i++) {
            INTEGERS[i] = new ConstantImpl<Integer>(Integer.class, Integer.valueOf(i));
            SHORTS[i] = new ConstantImpl<Short>(Short.class, Short.valueOf((short)i));
            BYTES[i] = new ConstantImpl<Byte>(Byte.class, Byte.valueOf((byte)i));
            CHARACTERS[i] = new ConstantImpl<Character>(Character.class, Character.valueOf((char)i));
            LONGS[i] = new ConstantImpl<Long>(Long.class, Long.valueOf(i));
        }
    }

    /**
     * Get a constant for the given boolean value
     * 
     * @param b
     * @return
     */
    public static Constant<Boolean> create(boolean b) {
        return b ? TRUE : FALSE;
    }
    
    /**
     * Get a constant for the given byte value
     * 
     * @param i
     * @return
     */
    public static Constant<Byte> create(byte i) {
        if (i >= 0 && i < CACHE_SIZE) {
            return BYTES[i];
        } else {
            return new ConstantImpl<Byte>(Byte.class, Byte.valueOf(i));
        }
    }
    
    /**
     * Get a constant for the given char value
     * 
     * @param i
     * @return
     */
    public static Constant<Character> create(char i) {
        if (i >= 0 && i < CACHE_SIZE) {
            return CHARACTERS[i];
        } else {
            return new ConstantImpl<Character>(Character.class, Character.valueOf(i));
        }
    }

    /**
     * Get a constant for the given int value
     * 
     * @param i
     * @return
     */
    public static Constant<Integer> create(int i) {
        if (i >= 0 && i < CACHE_SIZE) {
            return INTEGERS[i];
        } else {
            return new ConstantImpl<Integer>(Integer.class, Integer.valueOf(i));
        }
    }

    /**
     * Get a constant for the given long value
     * 
     * @param i
     * @return
     */
    public static Constant<Long> create(long i) {
        if (i >= 0 && i < CACHE_SIZE) {
            return LONGS[(int)i];
        } else {
            return new ConstantImpl<Long>(Long.class, Long.valueOf(i));
        }
    }

    /**
     * Create a constant for the given short value
     * 
     * @param i
     * @return
     */
    public static Constant<Short> create(short i) {
        if (i >= 0 && i < CACHE_SIZE) {
            return SHORTS[i];
        } else {
            return new ConstantImpl<Short>(Short.class, Short.valueOf(i));
        }
    }
    
    /**
     * Create a constant for the given string 
     * 
     * @param str
     * @return
     */
    public static Constant<String> create(String str) {
        return create(str, false);
    }

    /**
     * Create a constant for the given string 
     * 
     * @param str
     * @param populateCache whether to add the created constant to the cache
     * @return
     */
    public static Constant<String> create(String str, boolean populateCache) {
        if (STRINGS.containsKey(str)) {
            return STRINGS.get(str);
        } else {
            Constant<String> rv = new ConstantImpl<String>(str);
            if (populateCache) {
                STRINGS.put(str, rv);
            }
            return rv;
        }
    }
    
    /**
     * Create a constant for the given class
     * 
     * @param constant
     * @return
     */
    public static <T> Constant<Class<T>> create(Class<T> constant) {
        return new ConstantImpl<Class<T>>(constant);
    }

    private final T constant;
    
    /**
     * Create a new Constant for the given object
     * 
     * @param constant
     */
    public ConstantImpl(T constant) {
        this((Class)constant.getClass(), constant);
    }
    
    /**
     * Create a new Constant of the given type for the given object
     * 
     * @param type
     * @param constant
     */
    public ConstantImpl(Class<T> type, T constant) {
        super(type);
        this.constant = constant;
    }
    
    @Override
    public <R, C> R accept(Visitor<R, C> v, C context) {
        return v.visit(this, context);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof Constant<?>) {
            return ((Constant<?>)o).getConstant().equals(constant);
        } else {
            return false;
        }
    }
    
    @Override
    public T getConstant() {
        return constant;
    }
    
}
