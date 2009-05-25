/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import java.util.Collection;
import java.util.Date;

import com.mysema.query.alias.GrammarWithAlias;
import com.mysema.query.hql.HQLOps.HqlPathType;
import com.mysema.query.hql.HQLOps.OpHql;
import com.mysema.query.hql.HQLOps.OpQuant;
import com.mysema.query.types.CollectionType;
import com.mysema.query.types.SubQuery;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EComparable;
import com.mysema.query.types.expr.EConstructor;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.ESimple;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.Ops;
import com.mysema.query.types.path.PCollection;
import com.mysema.query.types.path.PComparable;
import com.mysema.query.types.path.PComponentCollection;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PEntityCollection;
import com.mysema.query.types.path.PMap;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PathMetadata;
import com.mysema.query.types.quant.QBoolean;
import com.mysema.query.types.quant.QComparable;
import com.mysema.query.types.quant.QNumber;
import com.mysema.query.types.quant.QSimple;

/**
 * HqlGrammar extends the Query DSL base grammar to provide HQL specific syntax elements.
 * 
 * @author tiwe
 * @version $Id$
 */
public class HQLGrammar extends GrammarWithAlias{
            
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
        return operationFactory.createComparable(Date.class,Ops.OpDateTime.CURRENT_DATE);
    }    
    
    public static EComparable<Date> current_time(){
        return operationFactory.createComparable(Date.class,Ops.OpDateTime.CURRENT_TIME);
    }    
    
    public static EComparable<Date> current_timestamp(){
        return operationFactory.createComparable(Date.class,Ops.OpDateTime.CURRENT_TIMESTAMP);
    }
    
    public static EComparable<Date> day(Expr<Date> date){
        return operationFactory.createComparable(Date.class,Ops.OpDateTime.DAY, date);
    }    
    
//    public static <T> Expr<T> distinct(Path<T> left){
//        return new DistinctPath<T>(left);
//    }    
    
    public static EBoolean exists(CollectionType<?> col){
        return new QBoolean(OpQuant.EXISTS, col);
    }
    
    public static <A> SubQuery<HQLJoinMeta,A> from(PEntity<A> select){
        return new SubQuery<HQLJoinMeta,A>(select).from(select);
    }
    
    public static EComparable<Date> hour(Expr<Date> date){
        return operationFactory.createComparable(Date.class,Ops.OpDateTime.HOUR, date);
    }  

    public static PComponentCollection<Integer> indices(PCollection<?> col){
        return new PComponentCollection<Integer>(Integer.class, new PathMetadata<Collection<Integer>>(col, null, HqlPathType.LISTINDICES));
    }
    
    public static <K,V> PComponentCollection<K> indices(PMap<K,V> col){
        return new PComponentCollection<K>(col.getKeyType(), new PathMetadata<Collection<Integer>>(col, null, HqlPathType.LISTINDICES));
    }
    
    public static EBoolean isempty(PComponentCollection<?> collection) {
        return operationFactory.createBoolean(OpHql.ISEMPTY, collection);        
    }
               
    public static EBoolean isempty(PEntityCollection<?> collection) {
        return operationFactory.createBoolean(OpHql.ISEMPTY, collection);        
    }
    
    public static EBoolean isnotempty(PComponentCollection<?> collection) {
        return operationFactory.createBoolean(OpHql.ISNOTEMPTY, collection);        
    }
    
    public static EBoolean isnotempty(PEntityCollection<?> collection) {
        return operationFactory.createBoolean(OpHql.ISNOTEMPTY, collection);        
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
        return operationFactory.createComparable(Date.class,Ops.OpDateTime.MINUTE, date);
    }    
    
    public static EComparable<Date> month(Expr<Date> date){
        return operationFactory.createComparable(Date.class,Ops.OpDateTime.MONTH, date);
    }
    
    public static <A> Expr<A> newInstance(Class<A> a, Expr<?>... args){
        return new EConstructor<A>(a,args);
    }
    
    public static EBoolean notExists(CollectionType<?> col){
        return new QBoolean(OpQuant.NOTEXISTS, col);
    }
        
    public static EComparable<Date> second(Expr<Date> date){
        return operationFactory.createComparable(Date.class,Ops.OpDateTime.SECOND, date);
    }
    
    public static <A> SubQuery<HQLJoinMeta,A> select(Expr<A> select){
        return new SubQuery<HQLJoinMeta,A>(select);
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
    @SuppressWarnings("unchecked")
    public static <D extends Number & Comparable<? super D>> ENumber<?> sum(Expr<D> left){ 
        Class<?> type = left.getType();
        if (type.equals(Byte.class) || type.equals(Integer.class) || type.equals(Short.class)) type = Long.class;
        if (type.equals(Float.class)) type = Double.class;
        return operationFactory.createNumber((Class<D>)type, HQLOps.OpHql.SUM, left);
    }
    
    public static <D extends Number & Comparable<? super D>> ENumber<Long> sumAsLong(Expr<D> left){
        return sum(left).longValue();
    }
    
    public static <D extends Number & Comparable<? super D>> ENumber<Double> sumAsDouble(Expr<D> left){
        return sum(left).doubleValue();
    }
    
    public static EComparable<Date> sysdate(){
        return operationFactory.createComparable(Date.class,Ops.OpDateTime.SYSDATE);
    }
    
    public static EComparable<Date> year(Expr<Date> date){
        return operationFactory.createComparable(Date.class,Ops.OpDateTime.YEAR, date);
    }
              
}
