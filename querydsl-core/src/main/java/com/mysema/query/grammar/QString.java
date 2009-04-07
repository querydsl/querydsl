/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;

import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Factory;
import com.mysema.query.grammar.types.Expr.EString;

/**
 * QString provides string functions
 *
 * @author tiwe
 * @version $Id$
 */
public class QString extends Factory{

    public static EString ltrim(Expr<String> s) {
        return createString(Ops.OpString.LTRIM, s);
    }

    public static EString rtrim(Expr<String> s) {
        return createString(Ops.OpString.RTRIM, s);
    }

    public static EString space(int i) {
        return createString(Ops.OpString.SPACE, createConstant(i));        
    }


}
