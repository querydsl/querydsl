package com.mysema.query.grammar;

import org.junit.Test;

import com.mysema.query.grammar.Types.Expr;


/**
 * VisitorTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public class VisitorTest {
    
    @Test
    public void testIteration() throws SecurityException, NoSuchMethodException{
        for (Class<?> innerType : Types.class.getClasses()){
            if (!innerType.isInterface()
                    && Expr.class.isAssignableFrom(innerType)
                    && !innerType.getSimpleName().startsWith("Expr")){                
                Visitor.class.getDeclaredMethod("visit", innerType);
            }
        }
    }

}
