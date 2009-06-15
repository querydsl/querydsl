/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql;

import com.mysema.query.types.Grammar;
import com.mysema.query.types.ListSubQuery;
import com.mysema.query.types.ObjectSubQuery;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.operation.Ops;

/**
 * SqlGrammar provides SQL specific extensions to the general Querydsl Grammar
 * 
 * @author tiwe
 * @version $Id$
 */
public class SQLGrammar extends Grammar {

    public static EBoolean exists(ObjectSubQuery<?> sq) {
        return operationFactory.createBoolean(Ops.EXISTS, sq);
    }

    public static EBoolean exists(ListSubQuery<?> sq) {
        return operationFactory.createBoolean(Ops.EXISTS, sq);
    }
    
}
