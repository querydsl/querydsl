/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types;

import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EComparable;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.EString;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.Operator;

/**
 * OperationFactory is a factory interface for operation expression creation
 * 
 * @author tiwe
 * 
 */
public interface OperationFactory {

    /**
     * Create a boolean expression
     * 
     * @param operator operator of the operation
     * @param args operands of the operation
     * @return
     */
    EBoolean createBoolean(Operator<Boolean> operator, Expr<?>... args);

    /**
     * Create a comparable expression
     * 
     * @param <OpType>
     * @param <RT>
     * @param type return type of the operation
     * @param operator operator of the operation
     * @param args operands of the operation
     * @return
     */
    <OpType, RT extends Comparable<?>> EComparable<RT> createComparable(
            Class<RT> type, Operator<OpType> operator, Expr<?>... args);

    /**
     * Create a number expression
     * 
     * @param <OpType>
     * @param <D>
     * @param type return type of the operation
     * @param operator operator of the operation
     * @param args operands of the operation
     * @return
     */
    <OpType extends Number, D extends Number & Comparable<?>> ENumber<D> createNumber(
            Class<? extends D> type, Operator<OpType> operator, Expr<?>... args);

    /**
     * Create a String expression
     * 
     * @param operator operator of the operation
     * @param args operands of the operation
     * @return
     */
    EString createString(Operator<String> operator, Expr<?>... args);

    /**
     * Create a String array expression
     * 
     * @param operator operator of the operation
     * @param args operands of the operation
     * @return
     */
    Expr<String[]> createStringArray(Operator<String> operator, Expr<?>... args);

}
