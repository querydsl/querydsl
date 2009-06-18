/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.functions;

import com.mysema.query.types.expr.EConstant;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.EString;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.ONumber;
import com.mysema.query.types.operation.OSimple;
import com.mysema.query.types.operation.OString;
import com.mysema.query.types.operation.Ops;

/**
 * StringFunctions provides string functions
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

    /**
     * Expr : <code>left.lastIndexOf(right, third);</code>
     * 
     * @param left
     * @param right
     * @param third
     * @return
     */
    public static ENumber<Integer> lastIndex(Expr<String> left, String right,
            int third) {
        return ONumber.create(Integer.class,
                Ops.StringOps.LAST_INDEX_2ARGS, left, EConstant.create(right),
                EConstant.create(third));
    }

    /**
     * Expr : <code>left.lastIndexOf(right)</code>
     * 
     * @param left
     * @param right
     * @return
     */
    public static ENumber<Integer> lastIndexOf(Expr<String> left,
            Expr<String> right) {
        return ONumber.create(Integer.class, Ops.StringOps.LAST_INDEX, left, right);
    }

    /**
     * Expr : <code>left.lastIndexOf(right)</code>
     * 
     * @param left
     * @param right
     * @return
     */
    public static ENumber<Integer> lastIndexOf(Expr<String> left, String right) {
        return ONumber.create(Integer.class, Ops.StringOps.LAST_INDEX,
                left, EConstant.create(right));
    }
    

    /**
     * Split the given String left with refex as the matcher for the separator
     * 
     * @param left
     * @param regex
     * @return
     */
    public static Expr<String[]> split(Expr<String> left, String regex) {
        return new OSimple<String,String[]>(String[].class, Ops.StringOps.SPLIT, left, EConstant.create(regex));
    }

}
