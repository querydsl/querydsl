/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import javax.annotation.Nullable;

/**
 * Visitor defines a Visitor signature for {@link Expression} instances.
 *
 * @author tiwe
 */
public interface Visitor<R,C>{

    /**
     * @param expr
     */
    @Nullable
    R visit(Constant<?> expr, @Nullable C context);

    /**
     * @param expr
     */
    @Nullable
    R visit(FactoryExpression<?> expr, @Nullable C context);

    /**
     * @param expr
     */
    @Nullable
    R visit(Operation<?> expr, @Nullable C context);

    /**
     * @param expr
     */
    @Nullable
    R visit(ParamExpression<?> expr, @Nullable C context);

    /**
     * @param expr
     */
    @Nullable
    R visit(Path<?> expr, @Nullable C context);

    /**
     * @param expr
     */
    @Nullable
    R visit(SubQueryExpression<?> expr, @Nullable C context);

    /**
     * @param expr
     */
    @Nullable
    R visit(TemplateExpression<?> expr, @Nullable C context);

}
