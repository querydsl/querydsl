/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import java.util.Collections;
import java.util.List;


/**
 * @author tiwe
 *
 * @param <RT>
 */
public final class OperationMixin<RT> extends MixinBase<RT> implements Operation<RT>{

    private static final long serialVersionUID = 4796432056083507588L;

    private final List<Expression<?>> args;

    private final Operator<? super RT> operator;

    private final Expression<RT> self;

    public OperationMixin(Operation<RT> self, Operator<? super RT> operator, List<Expression<?>> args){
        this.self = self;
        this.operator = operator;
        this.args = Collections.unmodifiableList(args);
    }

    @Override
    public Expression<?> getArg(int i) {
        return args.get(i);
    }

    @Override
    public List<Expression<?>> getArgs() {
        return args;
    }

    @Override
    public Operator<? super RT> getOperator() {
        return operator;
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
