package com.mysema.query.sql;

import java.util.Arrays;
import java.util.List;

import com.mysema.commons.lang.Assert;
import com.mysema.query.types.Expression;
import com.mysema.query.types.ExpressionBase;
import com.mysema.query.types.Template;
import com.mysema.query.types.TemplateExpression;
import com.mysema.query.types.TemplateFactory;
import com.mysema.query.types.Visitor;

/**
 * Represents a table valued function call
 * 
 * @author tiwe
 *
 * @param <T>
 */
public class RelationalFunctionCall<T> extends ExpressionBase<T> implements TemplateExpression<T> {

    private static final long serialVersionUID = 256739044928186923L;
    
    private final List<Expression<?>> args;

    private final Template template;

    /**
     * Create a new TableValuedFunctionCall with the given template in String form and template arguments
     * 
     * @param type
     * @param template
     * @param args
     * @return
     */
    public static <T> RelationalFunctionCall<T> create(Class<? extends T> type, String template, Expression<?>... args) {
        return new RelationalFunctionCall<T>(type, template, args);
    }    
    
    public RelationalFunctionCall(Class<? extends T> type, String template, Expression<?>... args) {
        super(type);
        this.args = Arrays.asList(Assert.notNull(args,"args"));
        this.template = TemplateFactory.DEFAULT.create(Assert.notNull(template,"template"));
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
               && c.getType().equals(type);
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
