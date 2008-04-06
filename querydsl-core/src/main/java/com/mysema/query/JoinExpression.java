/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import com.mysema.query.grammar.types.Expr;

/**
 * JoinExpression provides.
 * 
 * @author tiwe
 * @version $Id$
 */
public class JoinExpression {
    private Expr.Boolean condition;
    private final Expr.Entity<?> target;
    private final JoinType type;
    
    JoinExpression(JoinType type, Expr.Entity<?> target) {
        this.type = type;
        this.target = target;
    }
    
    public Expr.Boolean getCondition() {
        return condition;
    }
    
    public void setCondition(Expr.Boolean condition) {
        this.condition = condition;
    }
    
    public Expr.Entity<?> getTarget() {
        return target;
    }
    
    public JoinType getType() {
        return type;
    }
    
}