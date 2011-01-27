package com.mysema.query.types;

/**
 * Factory class for FactoryExpression instances
 * 
 * @author tiwe
 *
 */
public final class Projections {

    /**
     * Create a typed array projection for the given type and expressions
     * 
     * @param <T>
     * @param type
     * @param exprs
     * @return
     */
    public static <T> ArrayConstructorExpression<T> array(Class<T[]> type, Expression<T>... exprs){
        return new ArrayConstructorExpression<T>(type, exprs);
    }
    
    /**
     * Create a Bean populating projection for the given type and expressions
     * 
     * @param <T>
     * @param type
     * @param exprs
     * @return
     */
    public static <T> QBean<T> bean(Class<T> type, Expression<?>... exprs){
        return new QBean<T>(type, exprs);
    }
    
    /**
     * Create a Bean populating projection for the given type and expressions
     * 
     * @param <T>
     * @param type
     * @param exprs
     * @return
     */
    public static <T> QBean<T> bean(Path<T> type, Expression<?>... exprs){
        return new QBean<T>(type, exprs);
    }
    
    /**
     * Create a constructor invocation projection for the given type and expressions
     * 
     * @param <T>
     * @param type
     * @param exprs
     * @return
     */
    public static <T> ConstructorExpression<T> constructor(Class<T> type, Expression<?>... exprs){
        return ConstructorExpression.create(type, exprs);
    }
    
    /**
     * Create a field access based Bean populating projection for the given type and expressions
     * 
     * @param <T>
     * @param type
     * @param exprs
     * @return
     */
    public static <T> QBean<T> fields(Class<T> type, Expression<?>... exprs){
        return new QBean<T>(type, true, exprs);
    }
    
    /**
     * Create a field access based Bean populating projection for the given type and expressions
     * 
     * @param <T>
     * @param type
     * @param exprs
     * @return
     */
    public static <T> QBean<T> fields(Path<T> type, Expression<?>... exprs){
        return new QBean<T>(type, true, exprs);
    }
    
    private Projections(){}
}
