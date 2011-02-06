package com.mysema.query.types;

import java.util.Arrays;
import java.util.List;

/**
 * @author tiwe
 *
 */
public class Concatenation extends ExpressionBase<String> implements FactoryExpression<String>{

    private static final long serialVersionUID = -355693583588722395L;

    private final List<Expression<?>> args;
    
    public Concatenation(Expression<?>... args) {
        super(String.class);
        this.args = Arrays.<Expression<?>>asList(args);
    }
    
    @Override
    public List<Expression<?>> getArgs() {
        return args;
    }

    @Override
    public String newInstance(Object... args) {
        StringBuilder builder = new StringBuilder();
        for (Object a : args){
            builder.append(a);
        }
        return builder.toString();
    }

    @Override
    public <R, C> R accept(Visitor<R, C> v, C context) {
        return v.visit(this, context);
    }

}
