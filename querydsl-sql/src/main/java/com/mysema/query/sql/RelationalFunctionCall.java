package com.mysema.query.sql;

import java.util.ArrayList;
import java.util.List;

import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Template;
import com.mysema.query.types.TemplateExpression;
import com.mysema.query.types.TemplateFactory;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.SimpleExpression;

/**
 * Represents a table valued function call
 * 
 * @author tiwe
 *
 * @param <T>
 */
public class RelationalFunctionCall<T> extends SimpleExpression<T> implements TemplateExpression<T> {

    private static final long serialVersionUID = 256739044928186923L;
    
    private final List<Expression<?>> args;

    private final Template template;

    private static final Template createTemplate(String function, int argCount) {
        StringBuilder builder = new StringBuilder();
        builder.append(function);
        builder.append("(");
        for (int i = 0; i < argCount; i++) {
            if (i > 0) builder.append(", ");
            builder.append("{"+ i + "}");
        }
        builder.append(")");
        return TemplateFactory.DEFAULT.create(builder.toString());               
    }
    
    private final List<Expression<?>> normalizeArgs(Object... args) {
        List<Expression<?>> expressions = new ArrayList<Expression<?>>();
        for (Object arg : args) {
            if (arg instanceof Expression) {
                expressions.add((Expression<?>)arg);
            } else {
                expressions.add(new ConstantImpl<Object>(arg));
            }
        }
        return expressions;
    }
    
    
    /**
     * Create a new TableValuedFunctionCall for the given function and arguments
     * 
     * @param type
     * @param function
     * @param args
     * @return
     */
    public static <T> RelationalFunctionCall<T> create(Class<? extends T> type, String function, Object... args) {
        return new RelationalFunctionCall<T>(type, function, args);
    }    
    
    public RelationalFunctionCall(Class<? extends T> type, String function, Object... args) {
        super(type);
        this.args = normalizeArgs(args);
        this.template = createTemplate(function, args.length);
    }    

    @Override
    public Expression<?> getArg(int index) {
        return getArgs().get(index);
    }

    @Override
    public List<Expression<?>> getArgs() {
        return args;
    }

    @Override
    public Template getTemplate() {
        return template;
    }

    @Override
    public boolean equals(Object o) {
       if (o == this) {
           return true;
       } else if (o instanceof TemplateExpression) {
           TemplateExpression<?> c = (TemplateExpression<?>)o;
           return c.getTemplate().equals(template)
               && c.getType().equals(getType());
       } else {
           return false;
       }
    }

    @Override
    public int hashCode(){
        return getType().hashCode();
    }
    
    @Override
    public <R, C> R accept(Visitor<R, C> v, C context) {
        return v.visit(this, context);
    }
    
}
