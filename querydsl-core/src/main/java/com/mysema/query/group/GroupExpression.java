package com.mysema.query.group;

import java.util.List;

import com.mysema.query.types.Expression;
import com.mysema.query.types.Operation;
import com.mysema.query.types.OperationImpl;
import com.mysema.query.types.Operator;
import com.mysema.query.types.OperatorImpl;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.SimpleExpression;

/**
 * @author tiwe
 *
 * @param <T>
 */
public class GroupExpression<T> extends SimpleExpression<T> implements Operation<T> {

    private static final long serialVersionUID = -339842770639127388L;

    private static final Operator<Object> GROUP_EXPRESSION = new OperatorImpl<Object>("GROUP_EXPRESSION", Object.class);
    
    private final GroupDefinition<?,T> definition;
    
    private final Operation< T> opMixin;
    
    @SuppressWarnings("unchecked")
    public GroupExpression(Class<? extends T> type, GroupDefinition<?,T> definition, Expression<?>... args) {
        super(type);
        this.definition = definition; 
        this.opMixin = new OperationImpl(type, GROUP_EXPRESSION, args);
    }

    @Override
    public boolean equals(Object o) {
        return opMixin.equals(o);
    }
    
    @Override
    public int hashCode() {
        return opMixin.hashCode();
    }

    @Override
    public <R, C> R accept(Visitor<R, C> v, C context) {
        return v.visit(opMixin, context);
    }

    public GroupDefinition<?, T> getDefinition() {
        return definition;
    }

    @Override
    public Expression<?> getArg(int index) {
        return opMixin.getArg(index);
    }

    @Override
    public List<Expression<?>> getArgs() {
        return opMixin.getArgs();
    }

    @Override
    public Operator<? super T> getOperator() {
        return opMixin.getOperator();
    }
    
}
