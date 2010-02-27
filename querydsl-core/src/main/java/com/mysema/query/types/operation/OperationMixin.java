/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.operation;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import com.mysema.query.types.expr.Expr;

/**
 * @author tiwe
 *
 * @param <OP>
 * @param <RT>
 */
@SuppressWarnings("serial")
public final class OperationMixin<OP, RT> implements Operation<OP, RT>, Serializable {

    private final List<Expr<?>> args;
    
    private final Operator<OP> operator;
    
    private final Expr<RT> self;
    
    public OperationMixin(Expr<RT> self, Operator<OP> operator, List<Expr<?>> args){
        this.self = self;
        this.operator = operator;
        this.args = Collections.unmodifiableList(args);
    }
    
    @Override
    public Expr<RT> asExpr() {
        return self;
    }

    @Override
    public Expr<?> getArg(int i) {
        return args.get(i);
    }

    @Override
    public List<Expr<?>> getArgs() {
        return args;
    }
    
    @Override
    public Operator<OP> getOperator() {
        return operator;
    }

    @Override
    public Class<? extends RT> getType() {
        return self.getType();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object o){
        if (o == this || o == self){
            return true;
        }else if (o instanceof Operation){
            Operation op = (Operation)o;
            return op.getOperator().equals(operator) 
                && op.getArgs().equals(args)
                && op.getType().equals(self.getType());
        }else{
            return false;
        }
    }
    
    @Override
    public int hashCode(){
        return self.getType().hashCode();
    }

}
