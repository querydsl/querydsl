/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.mysema.query.types.alias.Alias;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.Path;


/**
 * TypesTest provides.
 * 
 * @author tiwe
 * @version $Id$
 */
public class TypesTest {
    
    @Test
    public void testAlias(){
        for (Class<?> cl : Alias.class.getClasses()){
            assertTrue(cl.getName(),Alias.class.isAssignableFrom(cl));
            if (!cl.isInterface()){                
                assertTrue(cl.getName(),Expr.class.isAssignableFrom(cl));    
            }            
        }
    }
    
    @Test
    public void testExpr(){
        for (Class<?> cl : Expr.class.getClasses()){
            assertTrue(cl.getName(),Expr.class.isAssignableFrom(cl));
        }
    }
    
    @Test
    public void testPath(){
        for (Class<?> cl : Path.class.getClasses()){
            assertTrue(cl.getName(),Path.class.isAssignableFrom(cl));
            if (!cl.isInterface()){         
                assertTrue(cl.getName(),Expr.class.isAssignableFrom(cl));    
            }            
        }
    }

}
