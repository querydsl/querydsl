/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

/**
 * Visitor defines a Visitor signature for {@link Expr} instances.
 *
 * @author tiwe
 * @version $Id$
 */
public interface Visitor<R,C>{

    /**
     * @param expr
     */
    R visit(Constant<?> expr, C context);

    /**
     * @param expr
     */
    R visit(Custom<?> expr, C context);

    /**
     * @param expr
     */
    R visit(FactoryExpression<?> expr, C context);

    /**
     * @param expr
     */
    R visit(Operation<?> expr, C context);

    /**
     * @param expr
     */
    R visit(Path<?> expr, C context);

    /**
     * @param expr
     */
    R visit(SubQueryExpression<?> expr, C context);

    /**
     * @param expr
     */
    R visit(Param<?> expr, C context);

}
