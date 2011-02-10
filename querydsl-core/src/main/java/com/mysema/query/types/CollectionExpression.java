/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import java.util.Collection;

/**
 * CollectionExpression represents java.util.Collection typed expressions
 *
 * @author tiwe
 *
 * @param <E> element type
 * @see java.util.Collection
 */
public interface CollectionExpression<C extends Collection<E>, E> extends ParametrizedExpression<C>{

}
