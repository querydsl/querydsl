package com.mysema.query.jpa;

import com.mysema.query.support.NumberConversion;
import com.mysema.query.support.NumberConversions;
import com.mysema.query.types.Expression;
import com.mysema.query.types.FactoryExpression;
import com.mysema.query.types.Operation;
import com.mysema.query.types.Ops;

/**
 * @author tiwe
 *
 */
public final class Conversions {

    public static <RT> Expression<RT> convert(Expression<RT> expr){
        if (isAggSumWithConversion(expr) || isCountAggConversion(expr)) {
            return new NumberConversion(expr);
        } else if (expr instanceof FactoryExpression) {
            FactoryExpression<?> factorye = (FactoryExpression<?>)expr;           
            for (Expression e : factorye.getArgs()) {
                if (isAggSumWithConversion(e) || isCountAggConversion(expr)) {
                    return new NumberConversions(factorye);
                }
            }
            
        } 
        return expr;
    }
    
    private static boolean isAggSumWithConversion(Expression<?> expr) {
        if (expr instanceof Operation && ((Operation)expr).getOperator() == Ops.AggOps.SUM_AGG) {
            Class type = ((Operation)expr).getType();
            if (type.equals(Float.class) || type.equals(Integer.class) 
                    || type.equals(Short.class) || type.equals(Byte.class)) {
                return true;
            }
        } 
        return false;
    }

    private static boolean isCountAggConversion(Expression<?> expr) {
        return expr instanceof Operation && ((Operation)expr).getOperator() == Ops.AggOps.COUNT_AGG;
    }
    
}
