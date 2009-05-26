/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.util;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FactoryMap is a factory which lazily creates values via the given create
 * method
 * 
 * @author Timo Westk&auml;mper
 * @version $Id: FactoryMap.java 3549 2008-06-10 17:56:47Z tiwe $
 */
public abstract class FactoryMap<V> {

    private final Map<List<?>, V> cache = new HashMap<List<?>, V>();

    private Method createMethod;

    public FactoryMap() {
        for (Method m : getClass().getMethods()) {
            if (m.getName().equals("create"))
                createMethod = m;
        }
        if (createMethod == null) {
            throw new IllegalArgumentException("No create method given");
        } else {
            createMethod.setAccessible(true);
        }
    }

    @SuppressWarnings("unchecked")
    public final V get(Object... args) {
        List<?> key = Arrays.asList(args);
        if (cache.containsKey(key)) {
            return cache.get(key);
        } else {
            try {
                V value = (V) createMethod.invoke(this, args);
                cache.put(key, value);
                return value;
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }

    public void clear() {
        cache.clear();
    }

}
