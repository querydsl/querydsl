/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import com.mysema.query.grammar.Types.ExprBoolean;
import com.mysema.query.grammar.Types.ExprEntity;

public class JoinExpression {
    public ExprBoolean[] conditions;
    public final ExprEntity<?> target;
    public final JoinType type;
    JoinExpression(JoinType type, ExprEntity<?> target) {
        this.type = type;
        this.target = target;
    }
}