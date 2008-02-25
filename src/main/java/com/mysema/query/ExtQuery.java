/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import com.mysema.query.grammar.Types.ExprBoolean;
import com.mysema.query.grammar.Types.ExprEntity;

/**
 * ExtQuery externds the Query interface to provide innerJoin, leftJoin and with methods
 *
 * @author tiwe
 * @version $Id$
 */
public interface ExtQuery<A extends ExtQuery<A>> extends Query<A> {
    A innerJoin(ExprEntity<?> o);
    A join(ExprEntity<?> o);
    A leftJoin(ExprEntity<?> o); 
    A with(ExprBoolean... o);
}
