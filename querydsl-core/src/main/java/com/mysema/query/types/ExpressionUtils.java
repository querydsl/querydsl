package com.mysema.query.types;

import javax.annotation.Nullable;

import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.BooleanOperation;

/**
 * @author tiwe
 *
 */
public final class ExpressionUtils {
    
    @Nullable
    public static Predicate allOf(Predicate... exprs){
        Predicate rv = null;
        for (Predicate b : exprs){
            rv = rv == null ? b : and(rv,b);
        }
        return rv;
    }

    @Nullable
    public static Predicate anyOf(Predicate... exprs){
        Predicate rv = null;
        for (Predicate b : exprs){
            rv = rv == null ? b : or(rv,b);
        }
        return rv;
    }

    public static Predicate and(Predicate left, Predicate right){
        return BooleanOperation.create(Ops.OR, left, right);
    }
    
    public static Predicate or(Predicate left, Predicate right){
        return BooleanOperation.create(Ops.OR, left, right);
    }
    
    public static Predicate not(Predicate expr){
        if (expr instanceof BooleanExpression){
            return ((BooleanExpression)expr).not();
        }else{
            return BooleanOperation.create(Ops.NOT, expr);
        }
    }
    
    private ExpressionUtils(){}
    
}
