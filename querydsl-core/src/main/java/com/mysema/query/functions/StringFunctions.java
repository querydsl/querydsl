/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.functions;

import com.mysema.query.types.expr.EConstant;
import com.mysema.query.types.expr.EString;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.OString;
import com.mysema.query.types.operation.Ops;

/**
 * StringFunctions provides additional string operations that are not available 
 * in standard String expressions
 * 
 * @author tiwe
 * @version $Id$
 */
public final class StringFunctions {

    private StringFunctions() {
    }

    public static EString ltrim(Expr<String> s) {
        return new OString(Ops.StringOps.LTRIM, s);
    }

    public static EString rtrim(Expr<String> s) {
        return new OString(Ops.StringOps.RTRIM, s);
    }

    public static EString space(int i) {
        return new OString(Ops.StringOps.SPACE, EConstant.create(i));
    }

}
