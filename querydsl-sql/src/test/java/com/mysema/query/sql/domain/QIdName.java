/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql.domain;

import com.mysema.query.types.Expr;
import com.mysema.query.types.expr.EConstructor;

@SuppressWarnings("all")
public class QIdName extends EConstructor<IdName> {
    
    public QIdName(Expr<java.lang.Integer> id, Expr<java.lang.String> name) {
        super(IdName.class, new Class[]{int.class, String.class}, id, name);
    }
    
}