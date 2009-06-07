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
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.EString;
import com.mysema.query.types.expr.Expr;
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

    private static final OperationFactory factory = SimpleOperationFactory
            .getInstance();

    private static final ExprFactory exprFactory = SimpleExprFactory
            .getInstance();

    public static EString ltrim(Expr<String> s) {
        return factory.createString(Ops.StringOps.LTRIM, s);
    }

    public static EString rtrim(Expr<String> s) {
        return factory.createString(Ops.StringOps.RTRIM, s);
    }

    public static EString space(int i) {
        return factory.createString(Ops.StringOps.SPACE, exprFactory.createConstant(i));
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
        return factory.createNumber(Integer.class,
                Ops.StringOps.LAST_INDEX_2ARGS, left, exprFactory.createConstant(right),
                exprFactory.createConstant(third));
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
        return factory.createNumber(Integer.class, Ops.StringOps.LAST_INDEX, left, right);
    }

    /**
     * Expr : <code>left.lastIndexOf(right)</code>
     * 
     * @param left
     * @param right
     * @return
     */
    public static ENumber<Integer> lastIndexOf(Expr<String> left, String right) {
        return factory.createNumber(Integer.class, Ops.StringOps.LAST_INDEX,
                left, exprFactory.createConstant(right));
    }
    

    /**
     * Split the given String left with refex as the matcher for the separator
     * 
     * @param left
     * @param regex
     * @return
     */
    public static Expr<String[]> split(Expr<String> left, String regex) {
        return factory.createStringArray(Ops.StringOps.SPLIT, left, exprFactory.createConstant(regex));
    }

}
