package com.mysema.query.types;

import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EComparable;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.EString;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.Op;

/**
 * @author tiwe
 * 
 */
public interface OperationFactory {

    EBoolean createBoolean(Op<Boolean> operator, Expr<?>... args);

    <OpType, RT extends Comparable<?>> EComparable<RT> createComparable(
            Class<RT> type, Op<OpType> operator, Expr<?>... args);

    <OpType extends Number, D extends Number & Comparable<?>> ENumber<D> createNumber(
            Class<? extends D> type, Op<OpType> operator, Expr<?>... args);

    EString createString(Op<String> operator, Expr<?>... args);

    Expr<String[]> createStringArray(Op<String> operator, Expr<?>... args);

}
