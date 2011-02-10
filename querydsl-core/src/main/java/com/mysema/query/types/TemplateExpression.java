/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import java.util.List;

/**
 * TemplateExpression provides base types for custom expressions with integrated
 * serialization templates
 *
 * @author tiwe
 */
public interface TemplateExpression<T> extends Expression<T>{

    /**
     * Get the argument with the given index
     *
     * @param index
     * @return
     */
    Expression<?> getArg(int index);

    /**
     * Get the arguments of the custom expression
     *
     * @return
     */
    List<Expression<?>> getArgs();

    /**
     * Get the serialization template for this custom expression
     *
     * @return
     */
    Template getTemplate();

}
