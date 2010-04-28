/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import java.util.Arrays;
import java.util.List;

import com.mysema.query.types.Expr;
import com.mysema.query.types.Operation;
import com.mysema.query.types.Operator;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Path;
import com.mysema.query.types.Visitor;

/**
 * OTime represents Time functions
 * 
 * @author tiwe
 *
 * @param <D>
 */
public class OTime<D extends Comparable<?>> extends ETime<D> implements Operation<D> {

    private static final long serialVersionUID = 9051606798649239240L;

    /**
     * Factory method
     * 
     * @param <D>
     * @param type
     * @param op
     * @param args
     * @return
     */
    public static <D extends Comparable<?>> ETime<D> create(Class<D> type, Operator<? super D> op, Expr<?>... args){
        return new OTime<D>(type, op, args);
    }
    
    private final Operation<D> opMixin;
    
    OTime(Class<D> type, Operator<? super D> op, Expr<?>... args) {
        this(type, op, Arrays.asList(args));
    }

    OTime(Class<D> type, Operator<? super D> op, List<Expr<?>> args) {
        super(type);
        this.opMixin = new OperationMixin<D>(this, op, args);
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);        
    }
    
    @Override
    public Expr<?> getArg(int index) {
        return opMixin.getArg(index);
    }

    @Override
    public List<Expr<?>> getArgs() {
        return opMixin.getArgs();
    }

    @Override
    public Operator<? super D> getOperator() {
        return opMixin.getOperator();
    }
    
    @Override
    public boolean equals(Object o){
        return opMixin.equals(o);
    }
    
    @Override
    public int hashCode(){
        return getType().hashCode();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public ETime<D> as(Path<D> alias) {
        return OTime.create(getType(),(Operator)Ops.ALIAS, this, alias.asExpr());
    }
}
