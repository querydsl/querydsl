package com.mysema.query.types;

import com.mysema.commons.lang.Assert;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EComparable;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.EString;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.OBoolean;
import com.mysema.query.types.operation.OComparable;
import com.mysema.query.types.operation.ONumber;
import com.mysema.query.types.operation.OString;
import com.mysema.query.types.operation.OStringArray;
import com.mysema.query.types.operation.Operator;

/**
 * 
 * @author tiwe
 *
 */
public class SimpleOperationFactory implements OperationFactory {

    private static final OperationFactory instance = new SimpleOperationFactory();

    public static OperationFactory getInstance() {
        return instance;
    }

    public EBoolean createBoolean(Operator<Boolean> operator, Expr<?>... args) {
        return new OBoolean(Assert.notNull(operator), Assert.notNull(args));
    }

    public <OpType, RT extends Comparable<?>> EComparable<RT> createComparable(
            Class<RT> type, Operator<OpType> operator, Expr<?>... args) {
        return new OComparable<OpType, RT>(type, Assert.notNull(operator),
                Assert.notNull(args));
    }

    public <OpType extends Number, D extends Number & Comparable<?>> ENumber<D> createNumber(
            Class<? extends D> type, Operator<OpType> operator, Expr<?>... args) {
        return new ONumber<OpType, D>(type, Assert.notNull(operator), Assert
                .notNull(args));
    }

    public EString createString(Operator<String> operator, Expr<?>... args) {
        return new OString(Assert.notNull(operator), Assert.notNull(args));
    }

    public Expr<String[]> createStringArray(Operator<String> operator,Expr<?>... args) {
        return new OStringArray(Assert.notNull(operator), Assert.notNull(args));
    }
}
