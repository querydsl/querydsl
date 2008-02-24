/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import com.mysema.query.grammar.Types.ExprForBoolean;
import com.mysema.query.grammar.Types.ExprForEntity;

/**
 * ExtQuery externds the Query interface to provide innerJoin, leftJoin and with methods
 *
 * @author tiwe
 * @version $Id$
 */
public interface ExtQuery<A extends ExtQuery<A>> extends Query<A> {
    A innerJoin(ExprForEntity<?> object);
    A join(ExprForEntity<?> object);
    A leftJoin(ExprForEntity<?> object); 
    A with(ExprForBoolean... objects);
}
