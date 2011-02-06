package com.mysema.query.types;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

/**
 * Utility class to expand FactoryExpression constructor arguments and compress FactoryExpression invocation arguments
 * 
 * @author tiwe
 *
 */
public final class FactoryExpressionUtils {
    
    public static List<Expression<?>> expand(List<Expression<?>> exprs){
        List<Expression<?>> rv = new ArrayList<Expression<?>>(exprs.size());
        for (Expression<?> expr : exprs){
            if (expr instanceof FactoryExpression<?>){
                rv.addAll(((FactoryExpression<?>)expr).getArgs());
            }else{
                rv.add(expr);
            }
        }
        return rv;
    }
    
    public static Object[] compress(List<Expression<?>> exprs, Object[] args){
        if (exprs.size() != args.length){
            Object[] rv = new Object[exprs.size()];
            int offset = 0; 
            for (int i = 0; i < exprs.size(); i++){
                if (exprs.get(i) instanceof FactoryExpression<?>){
                    FactoryExpression<?> fe = (FactoryExpression<?>)exprs.get(i);
                    rv[i] = fe.newInstance(ArrayUtils.subarray(args, offset, offset + fe.getArgs().size()));
                    offset += fe.getArgs().size();
                }else{
                    rv[i] = args[offset];
                    offset++;
                }
            }
            return rv;
        }else{
            return args;
        }
    }
    
    private FactoryExpressionUtils(){}

}
