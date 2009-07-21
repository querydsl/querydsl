/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql;

import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.operation.OBoolean;
import com.mysema.query.types.operation.Ops;
import com.mysema.query.types.query.ListSubQuery;
import com.mysema.query.types.query.ObjectSubQuery;

/**
 * SqlGrammar provides SQL specific extensions to the general Querydsl Grammar
 * 
 * @author tiwe
 * @version $Id$
 */
public class SQLGrammar {

    public static EBoolean exists(ObjectSubQuery<?> sq) {
        return new OBoolean(Ops.EXISTS, sq);
    }

    public static EBoolean exists(ListSubQuery<?> sq) {
        return new OBoolean(Ops.EXISTS, sq);
    }
    
}
