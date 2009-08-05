package com.mysema.query.types.operation;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.mysema.query.types.expr.ETime;
import com.mysema.query.types.expr.Expr;

/**
 * @author tiwe
 *
 * @param <OpType>
 * @param <D>
 */
public class OTime<OpType, D extends Comparable<?>> extends
ETime<D> implements Operation<OpType, D> {

    private final List<Expr<?>> args;
    private final Operator<OpType> op;

    OTime(Class<D> type, Operator<OpType> op, Expr<?>... args) {
        this(type, op, Arrays.asList(args));
    }

    OTime(Class<D> type, Operator<OpType> op, List<Expr<?>> args) {
        super(type);
        this.op = op;
        this.args = Collections.unmodifiableList(args);
        validate();
    }

    @Override
    public List<Expr<?>> getArgs() {
        return args;
    }

    @Override
    public Expr<?> getArg(int i) {
        return args.get(i);
    }

    @Override
    public Operator<OpType> getOperator() {
        return op;
    }
    
    /**
     * Factory method
     * 
     * @param <O>
     * @param <D>
     * @param type
     * @param op
     * @param args
     * @return
     */
    public static <O,D extends Comparable<?>> ETime<D> create(Class<D> type, Operator<O> op, Expr<?>... args){
        return new OTime<O,D>(type, op, args);
    }
}
