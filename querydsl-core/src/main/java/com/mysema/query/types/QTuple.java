/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mysema.query.Tuple;

/**
 * QTuple represents a projection of type Tuple
 * 
 * @author tiwe
 * 
 */
public class QTuple extends ExpressionBase<Tuple> implements FactoryExpression<Tuple>{

    private static final long serialVersionUID = -2640616030595420465L;
    
    private final List<Expression<?>> args;
    
    private final List<Expression<?>> expandedArgs;
    
    public QTuple(Expression<?>... args) {
        super(Tuple.class);
        this.args = Arrays.asList(args);
        this.expandedArgs = FactoryExpressionUtils.expand(this.args);
    }
    
    public QTuple(Expression<?>[]... args) {
        super(Tuple.class);
        this.args = new ArrayList<Expression<?>>();
        for (Expression<?>[] exprs: args){
            this.args.addAll(Arrays.asList(exprs));
        }
        this.expandedArgs = FactoryExpressionUtils.expand(this.args);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Tuple newInstance(Object... a) {
        final Object[] compressedArgs = FactoryExpressionUtils.compress(this.args, a); 
        return new Tuple() {

            @Override
            public <T> T get(int index, Class<T> type) {
                return (T) compressedArgs[index];
            }

            @Override
            public <T> T get(Expression<T> expr) {
                int index = QTuple.this.args.indexOf(expr);
                return index != -1 ? (T) compressedArgs[index] : null;
            }

            @Override
            public Object[] toArray() {
                return compressedArgs;
            }

        };
    }

    @Override
    public <R,C> R accept(Visitor<R,C> v, C context) {
        return v.visit(this, context);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this){
            return true;
        }else if (obj instanceof QTuple){
            QTuple c = (QTuple)obj;
            return args.equals(c.args)
                && getType().equals(c.getType());
        }else{
            return false;
        }
    }
    
    @Override
    public int hashCode(){
        return getType().hashCode();
    }

    @Override
    public List<Expression<?>> getArgs() {
        return expandedArgs;
    }

}
