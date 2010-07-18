/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql.domain;

import com.mysema.query.types.EConstructor;
import com.mysema.query.types.Expr;

public class QIdName extends EConstructor<IdName> {

    private static final long serialVersionUID = 5770565824515003611L;

    public QIdName(Expr<java.lang.Integer> id, Expr<java.lang.String> name) {
        super(IdName.class, new Class[]{int.class, String.class}, id, name);
    }

}
