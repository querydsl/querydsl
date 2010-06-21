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



// TODO: Auto-generated Javadoc
/**
 * VisitorTest provides.
 * 
 * @author tiwe
 * @version $Id$
 */
public class VisitorTest {
    
    /**
     * Test iteration.
     * 
     * @throws SecurityException the security exception
     * @throws NoSuchMethodException the no such method exception
     */
    @Test
    public void testIteration() throws SecurityException, NoSuchMethodException{
        List<Class<?>> types = new ArrayList<Class<?>>();
//        types.addAll(Arrays.<Class<?>>asList(Alias.class.getClasses()));
        types.addAll(Arrays.<Class<?>>asList(Operation.class.getClasses()));
        types.addAll(Arrays.<Class<?>>asList(Path.class.getClasses()));
        for (Class<?> innerType : types){
            if (!innerType.isInterface() && Expr.class.isAssignableFrom(innerType)){
                Visitor.class.getDeclaredMethod("visit",innerType);
            }
        }
        System.out.println("successful for " + types.size() + " types");
    }

}
