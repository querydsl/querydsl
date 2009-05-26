/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql.dto;

import com.mysema.query.types.expr.EConstructor;
import com.mysema.query.types.expr.Expr;

public class QIdName extends EConstructor<IdName> {
    public QIdName(Expr<java.lang.Integer> id, Expr<java.lang.String> name) {
        super(IdName.class, id, name);
    }
}