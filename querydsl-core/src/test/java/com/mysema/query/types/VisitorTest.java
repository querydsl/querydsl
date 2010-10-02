/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class VisitorTest {

    @Test
    public void Iteration() throws SecurityException, NoSuchMethodException{
        List<Class<?>> types = new ArrayList<Class<?>>();
//        types.addAll(Arrays.<Class<?>>asList(Alias.class.getClasses()));
        types.addAll(Arrays.<Class<?>>asList(Operation.class.getClasses()));
        types.addAll(Arrays.<Class<?>>asList(Path.class.getClasses()));
        for (Class<?> innerType : types){
            if (!innerType.isInterface() && Expression.class.isAssignableFrom(innerType)){
                Visitor.class.getDeclaredMethod("visit",innerType);
            }
        }
        System.out.println("successful for " + types.size() + " types");
    }

}
