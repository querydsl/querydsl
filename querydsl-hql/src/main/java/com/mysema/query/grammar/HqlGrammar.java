/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;

import static com.mysema.query.grammar.types.Factory.createBoolean;
import static com.mysema.query.grammar.types.Factory.createComparable;
import static com.mysema.query.grammar.types.Factory.createNumber;

import java.util.Collection;
import java.util.Date;

import com.mysema.query.grammar.HqlOps.HqlPathType;
import com.mysema.query.grammar.HqlOps.OpHql;
import com.mysema.query.grammar.HqlOps.OpNumberAgg;
import com.mysema.query.grammar.HqlOps.OpQuant;
import com.mysema.query.grammar.types.*;
import com.mysema.query.grammar.types.HqlTypes.CountExpression;
import com.mysema.query.grammar.types.HqlTypes.DistinctPath;
import com.mysema.query.grammar.types.HqlTypes.SubQuery;

/**
 * HqlGrammar extends the Query DSL base grammar to provide HQL specific syntax elements.
 * 
 * @author tiwe
 * @version $Id$
 */
public class HqlGrammar extends Grammar{
            
    public static <D> Expr<D> all(CollectionType<D> col){
        return new Quant.Simple<D>(OpQuant.ALL, col);
    }    
    public static <D extends Comparable<D>> Expr.EComparable<D> all(CollectionType<D> col){
        return new Quant.Comparable<D>(OpQuant.ALL, col);
    }
    
    public static <D> Expr.ESimple<D> any(CollectionType<D> col){
        return new Quant.Simple<D>(OpQuant.ANY, col);
    }    
    public static <D extends Comparable<D>> Expr.EComparable<D> any(CollectionType<D> col){
        return new Quant.Comparable<D>(OpQuant.ANY, col);
    }    
    
    public static <A extends Comparable<A>> Expr.EComparable<A> avg(Expr<A> left){
        return createNumber(OpNumberAgg.AVG, left);
    }    
    public static <A extends Comparable<A>> Expr.EComparable<A> avg(Path.PCollection<A> left){
        return new Quant.Comparable<A>(OpQuant.AVG_IN_COL, left);
    }        
    
    public static Expr.EComparable<Long> count(){
        return new CountExpression(null);
    }
    
    public static Expr.EComparable<Long> count(Expr<?> expr){
        return new CountExpression(expr);
    }
    public static Expr.EComparable<Date> current_date(){
        return createComparable(OpHql.CURRENT_DATE);
    }    
    public static Expr.EComparable<Date> current_time(){
        return createComparable(OpHql.CURRENT_TIME);
    }    
    public static Expr.EComparable<Date> current_timestamp(){
        return createComparable(OpHql.CURRENT_TIMESTAMP);
    }
    
    public static Expr.EComparable<Date> day(Expr<Date> date){
        return createComparable(OpHql.DAY, date);
    }    
    
    public static <T> Expr<T> distinct(Path<T> left){
        return new DistinctPath<T>(left);
    }    
    
    public static <D> Expr.EBoolean exists(CollectionType<D> col){
        return new Quant.Boolean<D>(OpQuant.EXISTS, col);
    }
    
    public static <A> SubQuery<A> from(Expr.EEntity<A> select){
        return new SubQuery<A>(select).from(select);
    }
    
    public static Expr.EComparable<Date> hour(Expr<Date> date){
        return createComparable(OpHql.HOUR, date);
    }  

    public static Path.PComponentCollection<Integer> indices(Path.PCollection<?> col){
        return new Path.PComponentCollection<Integer>(Integer.class, new PathMetadata<Collection<Integer>>(col, null, HqlPathType.LISTINDICES));
    }
    
    public static <K,V> Path.PComponentCollection<K> indices(Path.PMap<K,V> col){
        return new Path.PComponentCollection<K>(col.getKeyType(), new PathMetadata<Collection<Integer>>(col, null, HqlPathType.LISTINDICES));
    }
    
    public static Expr.EBoolean isempty(Path.PComponentCollection<?> collection) {
        return createBoolean(OpHql.ISEMPTY, collection);        
    }
               
    public static Expr.EBoolean isempty(Path.PEntityCollection<?> collection) {
        return createBoolean(OpHql.ISEMPTY, collection);        
    }
    
    public static Expr.EBoolean isnotempty(Path.PComponentCollection<?> collection) {
        return createBoolean(OpHql.ISNOTEMPTY, collection);        
    }
    
    public static Expr.EBoolean isnotempty(Path.PEntityCollection<?> collection) {
        return createBoolean(OpHql.ISNOTEMPTY, collection);        
    }
    
    public static <A extends Comparable<A>> Expr.EComparable<A> max(Expr<A> left){
        return createNumber(OpNumberAgg.MAX, left);
    }
    
    public static <A extends Comparable<A>> Expr.EComparable<A> max(Path.PCollection<A> left){
        return new Quant.Comparable<A>(OpQuant.MAX_IN_COL, left);
    } 
    
    public static <A> Path.PEntity<A> maxelement(Path.PEntityCollection<A> col) {
        return new Path.PEntity<A>(col.getElementType(), col.getEntityName(), new PathMetadata<A>(col, null, HqlPathType.MINELEMENT));
    }
    
    public static <A> Path.PComparable<Integer> maxindex(Path.PComponentCollection<A> col) {
        return new Path.PComparable<Integer>(Integer.class, new PathMetadata<Integer>(col, null, HqlPathType.MAXINDEX));
    }
    
    public static <A> Path.PComparable<Integer> maxindex(Path.PEntityCollection<A> col) {
        return new Path.PComparable<Integer>(Integer.class, new PathMetadata<Integer>(col, null, HqlPathType.MAXINDEX));
    }  
    
    public static <A extends Comparable<A>> Expr.EComparable<A> min(Expr<A> left){
        return createNumber(OpNumberAgg.MIN, left);
    }
    
    public static <A extends Comparable<A>> Expr.EComparable<A> min(Path.PCollection<A> left){
        return new Quant.Comparable<A>(OpQuant.MIN_IN_COL, left);
    }       
    
    public static <A> Path.PEntity<A> minelement(Path.PEntityCollection<A> col) {
        return new Path.PEntity<A>(col.getElementType(), col.getEntityName(), new PathMetadata<A>(col, null, HqlPathType.MINELEMENT));
    } 
    
    public static <A> Path.PComparable<Integer> minindex(Path.PComponentCollection<A> col) {
        return new Path.PComparable<Integer>(Integer.class, new PathMetadata<Integer>(col, null, HqlPathType.MININDEX));
    }
    
    public static <A> Path.PComparable<Integer> minindex(Path.PEntityCollection<A> col) {
        return new Path.PComparable<Integer>(Integer.class, new PathMetadata<Integer>(col, null, HqlPathType.MININDEX));
    }        
    
    public static Expr.EComparable<Date> minute(Expr<Date> date){
        return createComparable(OpHql.MINUTE, date);
    }    
    
    public static Expr.EComparable<Date> month(Expr<Date> date){
        return createComparable(OpHql.MONTH, date);
    }
    
    public static <A> Expr<A> newInstance(Class<A> a, Expr<?>... args){
        return new Constructor<A>(a,args);
    }
    
    public static <D> Expr.EBoolean notExists(CollectionType<D> col){
        return new Quant.Boolean<D>(OpQuant.NOTEXISTS, col);
    }
        
    public static Expr.EComparable<Date> second(Expr<Date> date){
        return createComparable(OpHql.SECOND, date);
    }
    
    public static <A> SubQuery<A> select(Expr<A> select){
        return new SubQuery<A>(select);
    }
    
    public static <D> Expr<D> some(CollectionType<D> col){
        return any(col);
    }
        
    public static <D extends Comparable<D>> Expr.EComparable<D> sum(Expr<D> left){ 
        return createNumber(OpHql.SUM, left);
    }
    
    public static Expr.EComparable<Date> sysdate(){
        return createComparable(OpHql.SYSDATE);
    }
    
    public static Expr.EComparable<Date> year(Expr<Date> date){
        return createComparable(OpHql.YEAR, date);
    }
              
}
