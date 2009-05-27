package com.mysema.query.types;

import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EComparable;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.EString;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.Operator;

/**
 * @author tiwe
 * 
 */
public interface OperationFactory {

    /**
     * 
     * @param operator
     * @param args
     * @return
     */
    EBoolean createBoolean(Operator<Boolean> operator, Expr<?>... args);

    /**
     * 
     * @param <OpType>
     * @param <RT>
     * @param type
     * @param operator
     * @param args
     * @return
     */
    <OpType, RT extends Comparable<?>> EComparable<RT> createComparable(
            Class<RT> type, Operator<OpType> operator, Expr<?>... args);

    /**
     * 
     * @param <OpType>
     * @param <D>
     * @param type
     * @param operator
     * @param args
     * @return
     */
    <OpType extends Number, D extends Number & Comparable<?>> ENumber<D> createNumber(
            Class<? extends D> type, Operator<OpType> operator, Expr<?>... args);

    /**
     * 
     * @param operator
     * @param args
     * @return
     */
    EString createString(Operator<String> operator, Expr<?>... args);

    /**
     * 
     * @param operator
     * @param args
     * @return
     */
    Expr<String[]> createStringArray(Operator<String> operator, Expr<?>... args);

}
