/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.functions;

import com.mysema.query.types.ExprFactory;
import com.mysema.query.types.OperationFactory;
import com.mysema.query.types.SimpleExprFactory;
import com.mysema.query.types.SimpleOperationFactory;
import com.mysema.query.types.expr.EString;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.Ops;

/**
 * QString provides string functions
 * 
 * @author tiwe
 * @version $Id$
 */
public final class StringFunctions {

    private StringFunctions() {
    }

    private static final OperationFactory factory = SimpleOperationFactory
            .getInstance();

    private static final ExprFactory exprFactory = SimpleExprFactory
            .getInstance();

    public static EString ltrim(Expr<String> s) {
        return factory.createString(Ops.OpString.LTRIM, s);
    }

    public static EString rtrim(Expr<String> s) {
        return factory.createString(Ops.OpString.RTRIM, s);
    }

    public static EString space(int i) {
        return factory.createString(Ops.OpString.SPACE, exprFactory
                .createConstant(i));
    }

}
