/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql.domain;

import com.mysema.query.types.Expression;
import com.mysema.query.types.expr.ConstructorExpression;

public class QIdName extends ConstructorExpression<IdName> {

    private static final long serialVersionUID = 5770565824515003611L;

    public QIdName(Expression<java.lang.Integer> id, Expression<java.lang.String> name) {
        super(IdName.class, new Class[]{int.class, String.class}, id, name);
    }

}
