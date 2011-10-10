package com.mysema.query.group;

import com.mysema.query.types.Expression;
import com.mysema.query.types.Operator;
import com.mysema.query.types.OperatorImpl;
import com.mysema.query.types.expr.SimpleOperation;

/**
 * GroupExpression combines a GroupDefinition and related Expressions
 * 
 * @author tiwe
 *
 * @param <T>
 */
public class GroupExpression<T> extends SimpleOperation<T> {

    private static final long serialVersionUID = -339842770639127388L;

    private static final Operator<Object> GROUP_EXPRESSION = new OperatorImpl<Object>("GROUP_EXPRESSION", Object.class);
    
    private final GroupDefinition<?,T> definition;
    
    @SuppressWarnings("unchecked")
    public GroupExpression(Class<? extends T> type, GroupDefinition<?,T> definition, Expression<?>... args) {
        super((Class)type, GROUP_EXPRESSION, args);
        this.definition = definition; 
    }

    public GroupDefinition<?, T> getDefinition() {
        return definition;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof GroupExpression<?>) {
            return super.equals(o) && ((GroupExpression<?>)o).getDefinition().equals(definition);    
        } else {
            return false;
        }        
    }
    
}
