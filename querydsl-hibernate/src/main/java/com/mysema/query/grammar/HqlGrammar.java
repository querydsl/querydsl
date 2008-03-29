/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;

import java.util.Collection;
import java.util.Date;

import com.mysema.query.Query;
import com.mysema.query.QueryBase;
import com.mysema.query.grammar.HqlOps.HqlPathType;
import com.mysema.query.grammar.HqlOps.OpHql;
import com.mysema.query.grammar.HqlOps.OpNumberAgg;
import com.mysema.query.grammar.HqlOps.OpQuant;
import com.mysema.query.grammar.Ops.Op;
import com.mysema.query.grammar.Types.*;

/**
 * HqlGrammar extends the Query DSL base grammar to provide HQL specific syntax elements
 *
 * @author tiwe
 * @version $Id$
 */
public class HqlGrammar extends Grammar{
            
    public static <D> Expr<D> all(CollectionType<D> col){
        return new ExprQuantSimple<D>(OpQuant.ALL, col);
    }    
    public static <D extends Comparable<D>> ExprComparable<D> all(CollectionType<D> col){
        return new ExprQuantComparable<D>(OpQuant.ALL, col);
    }
    
    public static <D> Expr<D> any(CollectionType<D> col){
        return new ExprQuantSimple<D>(OpQuant.ANY, col);
    }    
    public static <D extends Comparable<D>> ExprComparable<D> any(CollectionType<D> col){
        return new ExprQuantComparable<D>(OpQuant.ANY, col);
    }    
    
    public static <A extends Comparable<A>> ExprComparable<A> avg(Expr<A> left){
        return _number(OpNumberAgg.AVG, left);
    }    
    public static <A extends Comparable<A>> ExprComparable<A> avg(PathCollection<A> left){
        return new ExprQuantComparable<A>(OpQuant.AVG_IN_COL, left);
    }        
    
    public static ExprComparable<Long> count(){
        return new CountExpr(null);
    }
    
    public static ExprComparable<Long> count(Expr<?> expr){
        return new CountExpr(expr);
    }
    public static ExprComparable<Date> current_date(){
        return _comparable(OpHql.CURRENT_DATE);
    }    
    public static ExprComparable<Date> current_time(){
        return _comparable(OpHql.CURRENT_TIME);
    }    
    public static ExprComparable<Date> current_timestamp(){
        return _comparable(OpHql.CURRENT_TIMESTAMP);
    }
    
    public static ExprComparable<Date> day(Expr<Date> date){
        return _comparable(OpHql.DAY, date);
    }    
    public static <T> Expr<T> distinct(PathEntity<T> left){
        return new DistinctPath<T>(left);
    }
    
    public static <T> Expr<T> distinct(PathNoEntity<T> left){
        return new DistinctPath<T>(left);
    }    
    
    public static <D> ExprBoolean exists(CollectionType<D> col){
        return new ExprQuantBoolean<D>(OpQuant.EXISTS, col);
    }
    
    public static <A> SubQuery<A> from(ExprEntity<A> select){
        return new SubQuery<A>(select).from(select);
    }
    
    public static ExprComparable<Date> hour(Expr<Date> date){
        return _comparable(OpHql.HOUR, date);
    }        
    public static PathComponentCollection<Integer> indices(PathCollection<?> col){
        return new PathComponentCollection<Integer>(Integer.class, new PathMetadata<Collection<Integer>>(col, null, HqlPathType.LISTINDICES));
    }
    
    public static <K,V> PathComponentCollection<K> indices(PathMap<K,V> col){
        return new PathComponentCollection<K>(col.getKeyType(), new PathMetadata<Collection<Integer>>(col, null, HqlPathType.LISTINDICES));
    }
    
    public static ExprBoolean isempty(PathComponentCollection<?> collection) {
        return _boolean(OpHql.ISEMPTY, collection);        
    }
               
    public static ExprBoolean isempty(PathEntityCollection<?> collection) {
        return _boolean(OpHql.ISEMPTY, collection);        
    }
    
    public static ExprBoolean isnotempty(PathComponentCollection<?> collection) {
        return _boolean(OpHql.ISNOTEMPTY, collection);        
    }
    
    public static ExprBoolean isnotempty(PathEntityCollection<?> collection) {
        return _boolean(OpHql.ISNOTEMPTY, collection);        
    }
    
    public static <A extends Comparable<A>> ExprComparable<A> max(Expr<A> left){
        return _number(OpNumberAgg.MAX, left);
    }
    public static <A extends Comparable<A>> ExprComparable<A> max(PathCollection<A> left){
        return new ExprQuantComparable<A>(OpQuant.MAX_IN_COL, left);
    } 
    
    public static <A> PathEntity<A> maxelement(PathEntityCollection<A> col) {
        return new PathEntity<A>(col.getElementType(), new PathMetadata<A>(col, null, HqlPathType.MINELEMENT));
    }
    
    public static <A> PathComparable<Integer> maxindex(PathComponentCollection<A> col) {
        return new PathComparable<Integer>(Integer.class, new PathMetadata<Integer>(col, null, HqlPathType.MAXINDEX));
    }
    
    public static <A> PathComparable<Integer> maxindex(PathEntityCollection<A> col) {
        return new PathComparable<Integer>(Integer.class, new PathMetadata<Integer>(col, null, HqlPathType.MAXINDEX));
    }        
    public static <A extends Comparable<A>> ExprComparable<A> min(Expr<A> left){
        return _number(OpNumberAgg.MIN, left);
    }
    
    public static <A extends Comparable<A>> ExprComparable<A> min(PathCollection<A> left){
        return new ExprQuantComparable<A>(OpQuant.MIN_IN_COL, left);
    }       
    
    public static <A> PathEntity<A> minelement(PathEntityCollection<A> col) {
        return new PathEntity<A>(col.getElementType(), new PathMetadata<A>(col, null, HqlPathType.MINELEMENT));
    } 
    
    public static <A> PathComparable<Integer> minindex(PathComponentCollection<A> col) {
        return new PathComparable<Integer>(Integer.class, new PathMetadata<Integer>(col, null, HqlPathType.MININDEX));
    }
    
    public static <A> PathComparable<Integer> minindex(PathEntityCollection<A> col) {
        return new PathComparable<Integer>(Integer.class, new PathMetadata<Integer>(col, null, HqlPathType.MININDEX));
    }        
    
    public static ExprComparable<Date> minute(Expr<Date> date){
        return _comparable(OpHql.MINUTE, date);
    }    
    
    public static ExprComparable<Date> month(Expr<Date> date){
        return _comparable(OpHql.MONTH, date);
    }
    
    public static <A> Expr<A> newInstance(Class<A> a, Expr<?>... args){
        return new Constructor<A>(a,args);
    }
    
    public static <D> ExprBoolean notExists(CollectionType<D> col){
        return new ExprQuantBoolean<D>(OpQuant.NOTEXISTS, col);
    }
        
    public static ExprComparable<Date> second(Expr<Date> date){
        return _comparable(OpHql.SECOND, date);
    }
    
    public static <A> SubQuery<A> select(Expr<A> select){
        return new SubQuery<A>(select);
    }
    
    public static <D> Expr<D> some(CollectionType<D> col){
        return any(col);
    }
        
    public static <D extends Comparable<D>> ExprComparable<D> sum(Expr<D> left){ 
        return _number(OpHql.SUM, left);
    }
    
    public static ExprComparable<Date> sysdate(){
        return _comparable(OpHql.SYSDATE);
    }
    
    public static ExprComparable<Date> year(Expr<Date> date){
        return _comparable(OpHql.YEAR, date);
    }
    
    public static class Constructor<D> extends Expr<D>{
        private final Expr<?>[] args;
        public Constructor(Class<D> type, Expr<?>... args){
            super(type);
            this.args = args;
        }
        public Expr<?>[] getArgs(){ return args; }
    }
    
    public static class CountExpr extends ExprComparable<Long>{
        private final Expr<?> target;
        CountExpr(Expr<?> expr) {
            super(Long.class);
            this.target = expr;
        }
        public Expr<?> getTarget(){ return target; }
    }
    
    public static class DistinctPath<T> extends Expr<T>{
        private final Path<T> path;
        @SuppressWarnings("unchecked")
        public DistinctPath(Path<T> path) {
            super(((Expr<T>)path).getType());
            this.path = path;
        }
        public Path<T> getPath(){ return path; }
    }
    
    public interface ExprQuant{
        Op<?> getOperator();
        Expr<?> getTarget();
    }
    
    public static class ExprQuantBoolean<Q> extends ExprBoolean implements ExprQuant{
        private final Expr<?> col;
        private final Op<?> op;
        ExprQuantBoolean(Op<?> op, CollectionType<Q> col) {
            this.op = op;
            this.col = (Expr<?>) col;
        }
        public Op<?> getOperator() {return op;}
        public Expr<?> getTarget() {return col;}                           
    }
    
    public static class ExprQuantComparable<Q extends Comparable<Q>> extends ExprComparable<Q> implements ExprQuant{
        private final Expr<?> col;
        private final Op<?> op;
        ExprQuantComparable(Op<?> op, CollectionType<Q> col) {
            super(null);
            this.op = op;
            this.col = (Expr<?>)col;
        }
        public Op<?> getOperator() {return op;}
        public Expr<?> getTarget() {return col;}                          
    }
        
    public static class ExprQuantSimple<Q> extends Expr<Q> implements ExprQuant{
        private final Expr<?> col;
        private final Op<?> op;
        ExprQuantSimple(Op<?> op, CollectionType<Q> col) {
            super(null);
            this.op = op;
            this.col = (Expr<?>)col;
        }
        public Op<?> getOperator() {return op;}
        public Expr<?> getTarget() {return col;}                       
    }
    
    public static class SubQuery<A> extends Expr<A> implements Query<SubQuery<A>>,CollectionType<A>{
        @SuppressWarnings("unchecked")
        private QueryBase<?> query = new QueryBase();
        SubQuery(Expr<A> select) {
            super(null);
            query.select(select);
        }
        public SubQuery<A> from(ExprEntity<?>... o) {query.from(o); return this;}
        public SubQuery<A> fullJoin(ExprEntity<?> o) {query.fullJoin(o); return this;}
        public QueryBase<?> getQuery(){ return query;}
        public SubQuery<A> groupBy(Expr<?>... o) {query.groupBy(o); return this;}
        public SubQuery<A> having(ExprBoolean... o) {query.having(o); return this;}
        public SubQuery<A> innerJoin(ExprEntity<?> o) {query.innerJoin(o); return this;}
        public SubQuery<A> join(ExprEntity<?> o) {query.join(o); return this;}
        public SubQuery<A> leftJoin(ExprEntity<?> o) {query.leftJoin(o); return this;}
        public SubQuery<A> orderBy(OrderSpecifier<?>... o) {query.orderBy(o); return this;}
        public SubQuery<A> select(Expr<?>... o) {query.select(o); return this;}
        public SubQuery<A> where(ExprBoolean... o) {query.where(o); return this;}
        public SubQuery<A> with(ExprBoolean... o) {query.with(o); return this;}       
    }
        
}
