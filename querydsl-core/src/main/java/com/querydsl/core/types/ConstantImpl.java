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
package com.querydsl.core.types;

import javax.annotation.concurrent.Immutable;

/**
 * ConstantImpl is the default implementation of the Constant interface
 *
 * @author tiwe
 */
@Immutable
@SuppressWarnings("unchecked")
public final class ConstantImpl<T> extends ExpressionBase<T> implements Constant<T> {

    private static final long serialVersionUID = -3898138057967814118L;

    private static final int CACHE_SIZE = 256;

    private static final Constant<Character>[] CHARACTERS = new Constant[CACHE_SIZE];

    private static final Constant<Byte>[] BYTES = new Constant[CACHE_SIZE];

    private static final Constant<Integer>[] INTEGERS = new Constant[CACHE_SIZE];

    private static final Constant<Long>[] LONGS = new Constant[CACHE_SIZE];

    private static final Constant<Short>[] SHORTS = new Constant[CACHE_SIZE];

    private static final Constant<Boolean> FALSE = new ConstantImpl<Boolean>(Boolean.FALSE);

    private static final Constant<Boolean> TRUE = new ConstantImpl<Boolean>(Boolean.TRUE);

    static {
        for (int i = 0; i < CACHE_SIZE; i++) {
            INTEGERS[i] = new ConstantImpl<Integer>(Integer.class, Integer.valueOf(i));
            SHORTS[i] = new ConstantImpl<Short>(Short.class, Short.valueOf((short)i));
            BYTES[i] = new ConstantImpl<Byte>(Byte.class, Byte.valueOf((byte)i));
            CHARACTERS[i] = new ConstantImpl<Character>(Character.class, Character.valueOf((char)i));
            LONGS[i] = new ConstantImpl<Long>(Long.class, Long.valueOf(i));
        }
    }

    public static Constant<Boolean> create(boolean b) {
        return b ? TRUE : FALSE;
    }

    public static Constant<Byte> create(byte i) {
        if (i >= 0 && i < CACHE_SIZE) {
            return BYTES[i];
        } else {
            return new ConstantImpl<Byte>(Byte.class, Byte.valueOf(i));
        }
    }

    public static Constant<Character> create(char i) {
        if (i >= 0 && i < CACHE_SIZE) {
            return CHARACTERS[i];
        } else {
            return new ConstantImpl<Character>(Character.class, Character.valueOf(i));
        }
    }

    public static Constant<Integer> create(int i) {
        if (i >= 0 && i < CACHE_SIZE) {
            return INTEGERS[i];
        } else {
            return new ConstantImpl<Integer>(Integer.class, Integer.valueOf(i));
        }
    }

    public static Constant<Long> create(long i) {
        if (i >= 0 && i < CACHE_SIZE) {
            return LONGS[(int)i];
        } else {
            return new ConstantImpl<Long>(Long.class, Long.valueOf(i));
        }
    }

    public static Constant<Short> create(short i) {
        if (i >= 0 && i < CACHE_SIZE) {
            return SHORTS[i];
        } else {
            return new ConstantImpl<Short>(Short.class, Short.valueOf(i));
        }
    }

    public static <T> Constant<T> create(T obj) {
        return new ConstantImpl<T>(obj);
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
