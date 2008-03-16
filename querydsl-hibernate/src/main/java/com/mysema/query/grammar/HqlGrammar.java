/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;

import java.util.Date;

import com.mysema.query.grammar.HqlOps.OpHql;
import com.mysema.query.grammar.Types.*;

/**
 * HqlGrammar extends the Query DSL base grammar to provide HQL specific syntax elements
 *
 * @author tiwe
 * @version $Id$
 */
public class HqlGrammar extends Grammar{
        
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
    
    public static ExprBoolean exists(PathEntityCollection<?> col) {
       return _boolean(OpHql.EXISTS, col);
    }
    
    public static ExprComparable<Date> hour(Expr<Date> date){
        return _comparable(OpHql.HOUR, date);
    }
    
    public static ExprBoolean isempty(PathEntityCollection<?> collection) {
        return _boolean(OpHql.ISEMPTY, collection);        
    }
    
    public static ExprBoolean isnotempty(PathEntityCollection<?> collection) {
        return _boolean(OpHql.ISNOTEMPTY, collection);        
    }
    
    public static ExprComparable<Integer> maxindex(PathEntityCollection<?> collection) {
        return _comparable(OpHql.MAXINDEX, collection);        
    }
    
    public static ExprComparable<Integer> minindex(PathEntityCollection<?> collection) {
        return _comparable(OpHql.MININDEX, collection);        
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
    
    public static ExprComparable<Date> second(Expr<Date> date){
        return _comparable(OpHql.SECOND, date);
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
        public DistinctPath(Path<T> path) {
            super(((Expr<T>)path).getType());
            this.path = path;
        }
        public Path<T> getPath(){ return path; }
    }
        
}
