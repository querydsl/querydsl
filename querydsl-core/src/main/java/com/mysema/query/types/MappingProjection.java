package com.mysema.query.types;

import java.util.List;

import javax.annotation.Nullable;

import com.mysema.query.Tuple;

/**
 * Projection template that allows implementing arbitrary mapping of rows to result objects.
 *
 * @param <T> expression type
 */
public abstract class MappingProjection<T> extends ExpressionBase<T> implements FactoryExpression<T> {

    private static final long serialVersionUID = -948494350919774466L;

    private final QTuple qTuple;

    @SuppressWarnings("unchecked")
    public MappingProjection(Class<? super T> type, Expression<?>... args) {
        super((Class)type);
        qTuple = new QTuple(args);
    }

    @SuppressWarnings("unchecked")
    public MappingProjection(Class<? super T> type, Expression<?>[]... args) {
        super((Class)type);
        qTuple = new QTuple(args);
    }

    public T newInstance(Object... values) {
        return map(qTuple.newInstance(values));
    }

    /**
     * Creates a result object from the given row.
     *
     * @param row The row to map
     * @return The result object
     */
    protected abstract T map(Tuple row);

    public List<Expression<?>> getArgs() {
        return qTuple.getArgs();
    }

    public <R, C> R accept(Visitor<R, C> v, @Nullable C context) {
        return v.visit(this, context);
    }
    
}