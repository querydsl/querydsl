/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;

import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.ExprFactory;
import com.mysema.query.grammar.types.SimpleExprFactory;
import com.mysema.query.grammar.types.Expr.EString;

/**
 * QString provides string functions
 *
 * @author tiwe
 * @version $Id$
 */
public final class QString {
    
    private QString(){}

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
