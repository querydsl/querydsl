/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import com.mysema.query.grammar.Types.ExprBoolean;
import com.mysema.query.grammar.Types.ExprEntity;

public class JoinExpression {
    private ExprBoolean[] conditions;
    private final ExprEntity<?> target;
    private final JoinType type;
    
    JoinExpression(JoinType type, ExprEntity<?> target) {
        this.type = type;
        this.target = target;
    }
    
    public ExprBoolean[] getConditions() {
        return conditions;
    }
    
    public void setConditions(ExprBoolean[] conditions) {
        this.conditions = conditions;
    }
    
    public ExprEntity<?> getTarget() {
        return target;
    }
    
    public JoinType getType() {
        return type;
    }
    
}