/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import com.mysema.query.types.Expr;
import com.mysema.query.types.Operation;
import com.mysema.query.types.Operator;
import com.mysema.query.types.Path;

/**
 * @author tiwe
 *
 * @param <RT>
 */
public final class OperationMixin<RT> implements Operation<RT>, Serializable {

    private static final long serialVersionUID = 4796432056083507588L;

    private final List<Expr<?>> args;
    
    private final Operator<? super RT> operator;
    
    private final Expr<RT> self;
    
    public OperationMixin(Operation<RT> self, Operator<? super RT> operator, List<Expr<?>> args){
        this.self = self.asExpr();
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
    public Operator<? super RT> getOperator() {
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
    
    @Override
    public Expr<RT> as(Path<RT> alias) {
        throw new UnsupportedOperationException();
    }

}
