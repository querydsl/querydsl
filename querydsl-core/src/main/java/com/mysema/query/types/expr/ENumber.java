/**
 * 
 */
package com.mysema.query.types.expr;

import com.mysema.query.types.operation.Ops;
import com.mysema.query.util.NumberUtil;

/**
 * 
 * @author tiwe
 *
 * @param <D>
 */
public abstract class ENumber<D extends Number & Comparable<?>> extends EComparable<D>{
    public ENumber(Class<? extends D> type) {super(type);}
    
    public final ENumber<Byte> byteValue() { return castToNum(Byte.class); }  
    public final ENumber<Double> doubleValue() { return castToNum(Double.class); }                
    public final ENumber<Float> floatValue() { return castToNum(Float.class); }
    public final ENumber<Integer> intValue() { return castToNum(Integer.class); }
    public final ENumber<Long> longValue() { return castToNum(Long.class); }
    public final ENumber<Short> shortValue() { return castToNum(Short.class); }
    
    public final <A extends Number & Comparable<?>> EBoolean goe(A right) { return factory.createBoolean(Ops.GOE, this, factory.createConstant(NumberUtil.castTo(right,getType())));}       
    public final <A extends Number & Comparable<?>> EBoolean goe(Expr<A> right) {return factory.createBoolean(Ops.GOE, this, right);}                
    public final <A extends Number & Comparable<?>> EBoolean gt(A right) { return factory.createBoolean(Ops.GT, this, factory.createConstant(NumberUtil.castTo(right,getType())));}        
    public final <A extends Number & Comparable<?>> EBoolean gt(Expr<A> right) {return factory.createBoolean(Ops.GT, this, right);}          
            
    public final <A extends Number & Comparable<?>> EBoolean loe(A right) { return factory.createBoolean(Ops.LOE, this, factory.createConstant(NumberUtil.castTo(right,getType())));}
    public final <A extends Number & Comparable<?>> EBoolean loe(Expr<A> right) {return factory.createBoolean(Ops.LOE, this, right);}        
    public final <A extends Number & Comparable<?>> EBoolean lt(A right) {  return factory.createBoolean(Ops.LT, this, factory.createConstant(NumberUtil.castTo(right,getType())));}
    public final <A extends Number & Comparable<?>> EBoolean lt(Expr<A> right) {return factory.createBoolean(Ops.LT, this, right);}
}