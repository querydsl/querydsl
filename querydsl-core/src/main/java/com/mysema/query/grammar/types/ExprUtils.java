package com.mysema.query.grammar.types;

import java.util.Arrays;

/**
 * ExprUtils provides
 *
 * @author tiwe
 * @version $Id$
 */
public class ExprUtils {
    
    public static String toString(Operation<?,?> o){
        return o.getOperator() + " " + Arrays.asList(o.getArgs());
    }
}
