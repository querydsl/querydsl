/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import com.mysema.query.types.expr.EBoolean;

/**
 * @author tiwe
 *
 * @param <Q>
 */
public interface SimpleQuery<Q extends SimpleQuery<Q>> {
    
    Q where(EBoolean... e);

    Q limit(long limit);

    Q offset(long offset);

    Q restrict(QueryModifiers modifiers);

}
