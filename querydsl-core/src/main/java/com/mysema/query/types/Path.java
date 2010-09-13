/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import java.lang.reflect.AnnotatedElement;

import com.mysema.query.types.expr.BooleanExpression;

/**
 * Path represents a path expression
 *
 * @author tiwe
 * @version $Id$
 */
public interface Path<C> extends Expression<C> {

    /**
     * Get the metadata for this path
     *
     * @return
     */
    PathMetadata<?> getMetadata();

    /**
     * Get the root for this path
     *
     * @return
     */
    Path<?> getRoot();

    /**
     * Create a <code>this is not null</code> expression
     *
     * @return
     */
    // TODO : move isNotNull to Expr ?!?
    BooleanExpression isNotNull();

    /**
     * Create a <code>this is null</code> expression
     *
     *
     * @return
     */
    // TODO : move isNull to Expr ?!?
    BooleanExpression isNull();

    /**
     * Return the annotated element related to the given path
     * For property paths the annotated element contains the annotations of the
     * related field and/or getter method and for all others paths the annotated element
     * is the expression type.
     *
     * @return
     */
    AnnotatedElement getAnnotatedElement();

}
