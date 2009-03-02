/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import java.util.Iterator;
import java.util.List;

import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Expr.EBoolean;

/**
 * IteratorSource provides
 *
 * @author tiwe
 * @version $Id$
 */
public interface IteratorSource {
    
    void init(List<Expr<?>> sources, EBoolean where);
    
    Iterator<?> getIterator(Expr<?> expr, Object[] bindings);

}
