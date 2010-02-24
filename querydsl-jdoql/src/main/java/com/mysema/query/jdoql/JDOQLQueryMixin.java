package com.mysema.query.jdoql;

import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryMixin;
import com.mysema.query.types.expr.EConstructor;
import com.mysema.query.types.expr.Expr;

/**
 * @author tiwe
 *
 * @param <T>
 */
public class JDOQLQueryMixin<T> extends QueryMixin<T>{

    public JDOQLQueryMixin(QueryMetadata metadata) {
        super(metadata);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public T addToProjection(Expr<?>... o) {
        for (Expr<?> expr : o) {
            if (expr instanceof EConstructor) {
                EConstructor<?> constructor = (EConstructor<?>) expr;
                for (Expr<?> arg : constructor.getArgs()) {
                    super.addToProjection(arg);
                }
            } else {
                super.addToProjection(expr);
            }
        }
        return getSelf();
    }

}
