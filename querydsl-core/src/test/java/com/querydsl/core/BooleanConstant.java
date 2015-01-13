/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.core;

import com.querydsl.core.types.Constant;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Visitor;
import com.querydsl.core.types.expr.BooleanExpression;

/**
 * BooleanConstant provides constants for Boolean.TRUE and Boolean.FALSE
 *
 * @author tiwe
 *
 */
public final class BooleanConstant extends BooleanExpression implements Constant<Boolean> {

    public static final BooleanExpression FALSE = new BooleanConstant(Boolean.FALSE);

    private static final long serialVersionUID = -4106376704553234781L;

    public static final BooleanExpression TRUE = new BooleanConstant(Boolean.TRUE);

    public static BooleanExpression create(Boolean b) {
        return b.booleanValue() ? TRUE : FALSE;
    }

    private final Boolean constant;

    private BooleanConstant(Boolean b) {
        super(ConstantImpl.create(b.booleanValue()));
        this.constant = b;
    }

    @Override
    public final <R,C> R accept(Visitor<R,C> v, C context) {
        return v.visit(this, context);
    }
    
    @Override
    public BooleanExpression eq(Boolean b) {
        return constant.equals(b) ? TRUE : FALSE;
    }

    @Override
    public Boolean getConstant() {
        return constant;
    }

    @Override
    public BooleanExpression ne(Boolean b) {
        return constant.equals(b) ? FALSE : TRUE;
    }

    @Override
    public BooleanExpression not() {
        return constant.booleanValue() ? FALSE : TRUE;
    }

}
