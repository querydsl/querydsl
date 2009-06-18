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

    EBoolean createBoolean(Operator<Boolean> operator, Expr<?>... args);

    <OpType, RT extends Comparable<?>> EComparable<RT> createComparable(
            Class<RT> type, Operator<OpType> operator, Expr<?>... args);

    <OpType extends Number, D extends Number & Comparable<?>> ENumber<D> createNumber(
            Class<? extends D> type, Operator<OpType> operator, Expr<?>... args);

    EString createString(Operator<String> operator, Expr<?>... args);

    Expr<String[]> createStringArray(Operator<String> operator, Expr<?>... args);

}
