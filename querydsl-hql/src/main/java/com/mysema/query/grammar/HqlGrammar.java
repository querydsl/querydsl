/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;

import java.util.Collection;
import java.util.Date;

import com.mysema.query.grammar.HqlOps.HqlPathType;
import com.mysema.query.grammar.HqlOps.OpHql;
import com.mysema.query.grammar.HqlOps.OpQuant;
import com.mysema.query.grammar.types.*;
import com.mysema.query.grammar.types.Expr.EBoolean;
import com.mysema.query.grammar.types.Expr.EComparable;
import com.mysema.query.grammar.types.Expr.EEntity;
import com.mysema.query.grammar.types.Expr.ENumber;
import com.mysema.query.grammar.types.Expr.ESimple;
import com.mysema.query.grammar.types.HqlTypes.DistinctPath;
import com.mysema.query.grammar.types.Path.*;
import com.mysema.query.grammar.types.Quant.QBoolean;
import com.mysema.query.grammar.types.Quant.QComparable;
import com.mysema.query.grammar.types.Quant.QNumber;
import com.mysema.query.grammar.types.Quant.QSimple;

/**
 * HqlGrammar extends the Query DSL base grammar to provide HQL specific syntax elements.
 * 
 * @author tiwe
 * @version $Id$
 */
public class HqlGrammar extends GrammarWithAlias{
            
    public static <D> Expr<D> all(CollectionType<D> col){
        return new QSimple<D>(col.getElementType(), OpQuant.ALL, col);
    }    
    
    public static <D extends Number & Comparable<? super D>> ENumber<D> all(CollectionType<D> col){
        return new QNumber<D>(col.getElementType(), OpQuant.ALL, col);
    }
        
    public static <D> ESimple<D> any(CollectionType<D> col){
        return new QSimple<D>(col.getElementType(), OpQuant.ANY, col);
    }    
    
    public static <D extends Number & Comparable<? super D>> ENumber<D> any(CollectionType<D> col){
        return new QNumber<D>(col.getElementType(), OpQuant.ANY, col);
    }    
    
    public static <A extends Comparable<? super A>> EComparable<A> avg(PCollection<A> col){
        return new QComparable<A>(col.getElementType(), OpQuant.AVG_IN_COL, col);
    }        
    
    public static EComparable<Date> current_date(){
        return createComparable(Date.class,Ops.OpDateTime.CURRENT_DATE);
    }    
    
    public static EComparable<Date> current_time(){
        return createComparable(Date.class,Ops.OpDateTime.CURRENT_TIME);
    }    
    
    public static EComparable<Date> current_timestamp(){
        return createComparable(Date.class,Ops.OpDateTime.CURRENT_TIMESTAMP);
    }
    
    public static EComparable<Date> day(Expr<Date> date){
        return createComparable(Date.class,Ops.OpDateTime.DAY, date);
    }    
    
    public static <T> Expr<T> distinct(Path<T> left){
        return new DistinctPath<T>(left);
    }    
    
    public static <D> EBoolean exists(CollectionType<D> col){
        return new QBoolean<D>(OpQuant.EXISTS, col);
    }
    
    public static <A> SubQuery<HqlJoinMeta,A> from(EEntity<A> select){
        return new SubQuery<HqlJoinMeta,A>(select).from(select);
    }
    
    public static EComparable<Date> hour(Expr<Date> date){
        return createComparable(Date.class,Ops.OpDateTime.HOUR, date);
    }  

    public static PComponentCollection<Integer> indices(PCollection<?> col){
        return new PComponentCollection<Integer>(Integer.class, new PathMetadata<Collection<Integer>>(col, null, HqlPathType.LISTINDICES));
    }
    
    public static <K,V> PComponentCollection<K> indices(PMap<K,V> col){
        return new PComponentCollection<K>(col.getKeyType(), new PathMetadata<Collection<Integer>>(col, null, HqlPathType.LISTINDICES));
    }
    
    public static EBoolean isempty(PComponentCollection<?> collection) {
        return createBoolean(OpHql.ISEMPTY, collection);        
    }
               
    public static EBoolean isempty(PEntityCollection<?> collection) {
        return createBoolean(OpHql.ISEMPTY, collection);        
    }
    
    public static EBoolean isnotempty(PComponentCollection<?> collection) {
        return createBoolean(OpHql.ISNOTEMPTY, collection);        
    }
    
    public static EBoolean isnotempty(PEntityCollection<?> collection) {
        return createBoolean(OpHql.ISNOTEMPTY, collection);        
    }    
    
    public static <A extends Comparable<? super A>> EComparable<A> max(PCollection<A> left){
        return new QComparable<A>(left.getElementType(), OpQuant.MAX_IN_COL, left);
    } 
    
    public static <A> PEntity<A> maxelement(PEntityCollection<A> col) {
        return new PEntity<A>(col.getElementType(), col.getEntityName(), new PathMetadata<A>(col, null, HqlPathType.MINELEMENT));
    }
    
    public static <A> PComparable<Integer> maxindex(PComponentCollection<A> col) {
        return new PComparable<Integer>(Integer.class, new PathMetadata<Integer>(col, null, HqlPathType.MAXINDEX));
    }
    
    public static <A> PNumber<Integer> maxindex(PEntityCollection<A> col) {
        return new PNumber<Integer>(Integer.class, new PathMetadata<Integer>(col, null, HqlPathType.MAXINDEX));
    }  
        
    public static <A extends Comparable<? super A>> EComparable<A> min(PCollection<A> left){
        return new QComparable<A>(left.getElementType(), OpQuant.MIN_IN_COL, left);
    }       
    
    public static <A> PEntity<A> minelement(PEntityCollection<A> col) {
        return new PEntity<A>(col.getElementType(), col.getEntityName(), new PathMetadata<A>(col, null, HqlPathType.MINELEMENT));
    } 
    
    public static <A> PComparable<Integer> minindex(PComponentCollection<A> col) {
        return new PComparable<Integer>(Integer.class, new PathMetadata<Integer>(col, null, HqlPathType.MININDEX));
    }
    
    public static <A> PComparable<Integer> minindex(PEntityCollection<A> col) {
        return new PComparable<Integer>(Integer.class, new PathMetadata<Integer>(col, null, HqlPathType.MININDEX));
    }        
    
    public static EComparable<Date> minute(Expr<Date> date){
        return createComparable(Date.class,Ops.OpDateTime.MINUTE, date);
    }    
    
    public static EComparable<Date> month(Expr<Date> date){
        return createComparable(Date.class,Ops.OpDateTime.MONTH, date);
    }
    
    public static <A> Expr<A> newInstance(Class<A> a, Expr<?>... args){
        return new Constructor<A>(a,args);
    }
    
    public static <D> EBoolean notExists(CollectionType<D> col){
        return new QBoolean<D>(OpQuant.NOTEXISTS, col);
    }
        
    public static EComparable<Date> second(Expr<Date> date){
        return createComparable(Date.class,Ops.OpDateTime.SECOND, date);
    }
    
    public static <A> SubQuery<HqlJoinMeta,A> select(Expr<A> select){
        return new SubQuery<HqlJoinMeta,A>(select);
    }
    
    public static <D> Expr<D> some(CollectionType<D> col){
        return any(col);
    }
        
    /**
     * SUM returns Long when applied to state-fields of integral types (other than BigInteger); 
     *             Double when applied to state-fields of floating point types; 
     *             BigInteger when applied to state-fields of type BigInteger; 
     *             and BigDecimal when applied to state-fields of type BigDecimal. 
     */   
    public static <D extends Number & Comparable<? super D>> ENumber<?> sum(Expr<D> left){ 
        Class<?> type = left.getType();
        if (type.equals(Byte.class) || type.equals(Integer.class) || type.equals(Short.class)) type = Long.class;
        if (type.equals(Float.class)) type = Double.class;
        return createNumber((Class<D>)type, HqlOps.OpHql.SUM, left);
    }
    
    public static <D extends Number & Comparable<? super D>> ENumber<Long> sumAsLong(Expr<D> left){
        return sum(left).longValue();
    }
    
    public static <D extends Number & Comparable<? super D>> ENumber<Double> sumAsDouble(Expr<D> left){
        return sum(left).doubleValue();
    }
    
    public static EComparable<Date> sysdate(){
        return createComparable(Date.class,Ops.OpDateTime.SYSDATE);
    }
    
    public static EComparable<Date> year(Expr<Date> date){
        return createComparable(Date.class,Ops.OpDateTime.YEAR, date);
    }
              
}
