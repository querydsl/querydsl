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
    
    public static ExprNoEntity<Long> count(Expr<?> expr){
        return new CountExpr(expr);
    }
    
    public static <D extends Comparable<D>> ExprNoEntity<D> distinct(ExprNoEntity<D> left){
        return _comparable(OpHql.DISTINCT, left);
    }
    
    public static <A> Expr<A> newInstance(Class<A> a, Expr<?>... args){
        return new Constructor<A>(a,args);
    }
    
    public static <D extends Comparable<D>> ExprComparable<D> sum(Expr<D> left){ 
        return _number(OpHql.SUM, left);
    }
    
    public static ExprComparable<Date> sysdate(){
        return _comparable(OpHql.SYSDATE);
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
        
    public static class Constructor<D> extends Expr<D>{
        public final Expr<?>[] args;
        public Constructor(Class<D> type, Expr<?>... args){
            super(type);
            this.args = args;
        }
        public Class<D> getType(){ return type;}
    }
    
    public static class CountExpr extends ExprComparable<Long>{
        public final Expr<?> target;
        CountExpr(Expr<?> expr) {
            super(Long.class);
            this.target = expr;
        }
    }
        
}
