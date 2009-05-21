/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.functions;

import com.mysema.query.types.expr.EString;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.expr.ExprFactory;
import com.mysema.query.types.expr.SimpleExprFactory;
import com.mysema.query.types.operation.Ops;

/**
 * QString provides string functions
 *
 * @author tiwe
 * @version $Id$
 */
public final class StringFunctions {
    
    private StringFunctions(){}

    protected static final ExprFactory factory = SimpleExprFactory.getInstance();
    
    public static EString ltrim(Expr<String> s) {
        return factory.createString(Ops.OpString.LTRIM, s);
    }

    public static EString rtrim(Expr<String> s) {
        return factory.createString(Ops.OpString.RTRIM, s);
    }

    public static EString space(int i) {
        return factory.createString(Ops.OpString.SPACE, factory.createConstant(i));        
    }


}
