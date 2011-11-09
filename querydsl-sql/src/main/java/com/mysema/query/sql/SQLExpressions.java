package com.mysema.query.sql;

import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Expression;
import com.mysema.query.types.expr.SimpleExpression;
import com.mysema.query.types.expr.SimpleOperation;
import com.mysema.query.types.expr.Wildcard;

/**
 * Common SQL expressions
 * 
 * @author tiwe
 *
 */
public final class SQLExpressions {
    
    public static final Expression<Object[]> all = Wildcard.all;
    
    public static final Expression<Long> countAll = Wildcard.count;
    
    public static final SimpleExpression<Long> nextval(String sequence) {
        return nextval(Long.class, sequence);
    }    
    
    public static final <T extends Number> SimpleExpression<T> nextval(Class<T> type, String sequence) {
        return SimpleOperation.create(type, SQLTemplates.NEXTVAL, ConstantImpl.create("seq"));
    }
    
    private SQLExpressions() {}

}
