/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import java.lang.reflect.AnnotatedElement;

/**
 * Path represents a path expression
 *
 * @author tiwe
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
     * Return the annotated element related to the given path
     * For property paths the annotated element contains the annotations of the
     * related field and/or getter method and for all others paths the annotated element
     * is the expression type.
     *
     * @return
     */
    AnnotatedElement getAnnotatedElement();

}
