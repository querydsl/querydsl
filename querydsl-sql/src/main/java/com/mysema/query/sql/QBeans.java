package com.mysema.query.sql;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;

import com.mysema.query.types.Expression;
import com.mysema.query.types.ExpressionBase;
import com.mysema.query.types.FactoryExpression;
import com.mysema.query.types.QBean;
import com.mysema.query.types.Visitor;

/**
 * Expression used to project a list of beans
 * 
 * @author luis
 */
public class QBeans extends ExpressionBase<Beans> implements FactoryExpression<Beans> {

    private static final long serialVersionUID = -4411839816134215923L;
    
    private final Map<RelationalPath<?>, QBean<?>> qBeans = new LinkedHashMap<RelationalPath<?>, QBean<?>>();
    
    private final List<Expression<?>> expressions = new ArrayList<Expression<?>>();

    @SuppressWarnings("unchecked")
    public QBeans(RelationalPath<?>... beanPaths) {
        super(Beans.class);
        try {
            for (RelationalPath path : beanPaths) {
                Map<String, Expression<?>> bindings = new LinkedHashMap<String, Expression<?>>();
                for (Field field : path.getClass().getFields()) {
                    if (Expression.class.isAssignableFrom(field.getType()) && !Modifier.isStatic(field.getModifiers())) {
                        field.setAccessible(true);
                        Expression<?> column = (Expression<?>) field.get(path);
                        bindings.put(field.getName(), column);
                        expressions.add(column);
                    }
                }
                qBeans.put(path, new QBean<Object>(path.getType(), bindings));
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public <R, C> R accept(Visitor<R, C> v, C context) {
        return v.visit(this, context);
    }

    @Override
    public List<Expression<?>> getArgs() {
        return expressions;
    }

    @Override
    public Beans newInstance(Object... args) {
        int offset = 0;
        Map<RelationalPath<?>, Object> beans = new HashMap<RelationalPath<?>, Object>();
        for (Map.Entry<RelationalPath<?>, QBean<?>> entry : qBeans.entrySet()) {
            RelationalPath<?> path = entry.getKey();
            QBean<?> qBean = entry.getValue();
            int argsSize = qBean.getArgs().size();
            Object[] subArgs = ArrayUtils.subarray(args, offset, offset + argsSize);
            beans.put(path, qBean.newInstance(subArgs));
            offset += argsSize;
        }
        return new Beans(beans);
    }

}