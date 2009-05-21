/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.mysema.query.types.Visitor;
import com.mysema.query.types.alias.Alias;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.Operation;
import com.mysema.query.types.path.Path;


/**
 * VisitorTest provides.
 * 
 * @author tiwe
 * @version $Id$
 */
public class VisitorTest {
    
	@Test
    public void testIteration() throws SecurityException, NoSuchMethodException{
        List<Class<?>> types = new ArrayList<Class<?>>();
        types.addAll(Arrays.<Class<?>>asList(Alias.class.getClasses()));
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
