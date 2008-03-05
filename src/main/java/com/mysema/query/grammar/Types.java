/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;

import java.util.Collection;

import com.mysema.query.grammar.Ops.Op;


/**
 * Types provides the types of the fluent grammar
 *
 * @author tiwe
 * @version $Id$
 */
public class Types {
    
    public interface Alias<D>{ 
        Expr<?> getFrom();
        String getTo();
    }
            
    public static class AliasCollection<D> extends ExprEntity<D> implements Alias<D>{
        private final Expr<?> from;
        private final String to;
        AliasCollection(PathEntityCollection<D> from, Path<D> to) {
            super(null);
            this.from = from;
            this.to = to.toString();
        }
        public Expr<?> getFrom() {return from;}
        public String getTo() {return to;}               
    }
        
    public static class AliasEntity<D> extends ExprEntity<D> implements Alias<D>{
        private final Expr<?> from;
        private final String to;
        AliasEntity(PathEntity<D> from, PathEntity<D> to) {
            super(from.type);
            this.from = from;
            this.to = to.toString();
        }
        AliasEntity(PathEntity<D> from, String to) {
            super(from.type);
            this.from = from;
            this.to = to;
        }
        public Expr<?> getFrom() {return from;}
        public String getTo() {return to;}  
    }
    
    public static class AliasNoEntity<D> extends ExprNoEntity<D> implements Alias<D>{     
        private final Expr<?> from;
        private final String to;
        AliasNoEntity(Expr<D> from, String to) {
            super(from.type);
            this.from = from;
            this.to = to;
        }
        public Expr<D> as(String to) {
            return Grammar.as(this, to);
        }   
        public Expr<?> getFrom() {return from;}
        public String getTo() {return to;}  
    }
    
    public static class ConstantExpr<D> extends Expr<D>{
        private final D constant;
        @SuppressWarnings("unchecked")
        ConstantExpr(D constant) {
            super((Class<D>) constant.getClass());
            this.constant = constant;
        }
        public D getConstant(){ return constant;}
    }
    
    public static abstract class Expr<D>{
        protected final Class<D> type;
        Expr(Class<D> type){this.type = type;}
        public <B extends D> ExprBoolean eq(B right){return Grammar.eq(this, right);}        
        public <B extends D> ExprBoolean eq(Expr<B> right){return Grammar.eq(this, right);}
        public <B extends D> ExprBoolean ne(B right){return Grammar.ne(this, right);}
        public <B extends D> ExprBoolean ne(Expr<B> right){return Grammar.ne(this, right);}        
    }
    
    public static class ExprBoolean extends ExprNoEntity<Boolean>{
        ExprBoolean() {super(Boolean.class);}
        public ExprBoolean and(ExprBoolean right) {return Grammar.and(this, right);}
        public ExprBoolean or(ExprBoolean right) {return Grammar.or(this, right);}
    }
    
    public static class ExprComparable<D extends Comparable<D>> extends ExprNoEntity<D>{
        ExprComparable(Class<D> type) {super(type);}
        public ExprBoolean after(Expr<D> right) {return Grammar.after(this,right);}
        public ExprBoolean after(D right) {return Grammar.after(this,right);}
        public OrderSpecifier<D> asc() {return Grammar.asc(this);}
        public ExprBoolean before(Expr<D> right) {return Grammar.before(this,right);}
        public ExprBoolean before(D right) {return Grammar.before(this,right);}       
        public ExprBoolean between(Expr<D> first, Expr<D> second) {return Grammar.between(this,first,second);}  
        public ExprBoolean between(D first, D second) {return Grammar.between(this,first,second);}
        public OrderSpecifier<D> desc() {return Grammar.desc(this);}  
        public ExprBoolean goe(D right) {return Grammar.goe(this,right);}  
        public ExprBoolean goe(Expr<D> right) {return Grammar.goe(this,right);}         
        public ExprBoolean gt(D right) {return Grammar.gt(this,right);}  
        public ExprBoolean gt(Expr<D> right) {return Grammar.gt(this,right);}  
        public ExprBoolean loe(D right) {return Grammar.loe(this,right);}  
        public ExprBoolean loe(Expr<D> right) {return Grammar.loe(this,right);}
        public ExprBoolean lt(D right) {return Grammar.lt(this,right);}
        public ExprBoolean lt(Expr<D> right) {return Grammar.lt(this,right);}
        public ExprBoolean in(D... args) {return Grammar.in(this,args);}
    }
    
    public static class ExprEntity<D> extends Expr<D>{
        ExprEntity(Class<D> type) {super(type);}        
    }        
            
    public static abstract class ExprNoEntity<D> extends Expr<D>{
        ExprNoEntity(Class<D> type) {super(type);}
        public Expr<D> as(String to){return Grammar.as(this, to);}
    }
        
    public static abstract class ExprString extends ExprComparable<String>{
        ExprString() {super(String.class);}
        public ExprBoolean like(String str) { return Grammar.like(this, str); }
        public ExprString lower() { return Grammar.lower(this); }
        public ExprString substring(int beginIndex) { return Grammar.substring(this, beginIndex);}
        public ExprString substring(int beginIndex, int endIndex) { return Grammar.substring(this, beginIndex, endIndex);}
        public ExprString trim() { return Grammar.trim(this); }
        public ExprString upper() { return Grammar.upper(this); }
    }  

    public interface Operation<OP,RT>{
        Expr<?>[] getArgs();
        Op<OP> getOperator();
    }
    
    public static class OperationBoolean extends ExprBoolean implements Operation<Boolean,Boolean>{
        private final Expr<?>[] args;
        private final Op<Boolean> op;
        OperationBoolean(Op<Boolean> op, Expr<?>... args){
            this.op = op;
            this.args = args;
        }
        public Expr<?>[] getArgs() {return args;}
        public Op<Boolean> getOperator() {return op;}    
    }    
    
    public static class OperationComparable<OpType,D extends Comparable<D>> extends ExprComparable<D> implements Operation<OpType,D> {
        private final Expr<?>[] args;
        private final Op<OpType> op;
        OperationComparable(Op<OpType> op, Expr<?>... args){
            super(null);
            this.op = op;
            this.args = args;
        }
        public Expr<?>[] getArgs() {return args;}
        public Op<OpType> getOperator() {return op;}    
    }
    
    public static class OperationNumber<D extends Comparable<D>> extends OperationComparable<Number,D>{
        OperationNumber(Op<Number> op, Expr<?>[] args) {
            super(op, args);
        }        
    }
    
    public static class OperationString extends ExprString implements Operation<String,String>{
        private final Expr<?>[] args;
        private final Op<String> op;
        OperationString(Op<String> op, Expr<?>... args){
            this.op = op;
            this.args = args;
        }
        public Expr<?>[] getArgs() {return args;}
        public Op<String> getOperator() {return op;}    
    }
    
    public enum Order{ ASC,DESC }
        
    public static class OrderSpecifier<A extends Comparable<A>>{
        public Order order; 
        public Expr<A> target;       
    }
        
    public interface Path<D>{
        ExprBoolean isnotnull();
        ExprBoolean isnull();
    }
    
    public static class PathBoolean extends ExprBoolean implements PathNoEntity<Boolean>{
        private final  String path;
        PathBoolean(String path) {this.path = path;}        
        public ExprBoolean isnotnull() {return Grammar.isnotnull(this);}
        public ExprBoolean isnull() {return Grammar.isnull(this);}
        public String toString(){ return path;}
    }
    
    public static class PathComparable<D extends Comparable<D>> extends ExprComparable<D> implements PathNoEntity<D>{
        private final String path;
        public PathComparable(Class<D> type, String path) {
            super(type);
            this.path = path;
        }
        public ExprBoolean isnotnull() {return Grammar.isnotnull(this);}
        public ExprBoolean isnull() {return Grammar.isnull(this);}
        public String toString() {return path;}
    }
    
    public static class PathEntity<D> extends ExprEntity<D> implements Path<D>{
        private final String path;
        protected PathEntity(Class<D> type, String path) {
            super(type);
            this.path = path;
        }
        protected PathBoolean _boolean(String path){
            return new PathBoolean(this+"."+path);
        }
        protected <A>PathEntityCollection<A> _collection(String path,Class<A> type) {
            return new PathEntityCollection<A>(type, this+"."+path);
        }
        protected <A extends Comparable<A>> PathComparable<A> _comparable(String path,Class<A> type) {
            return new PathComparable<A>(type, this+"."+path);
        }
        protected <A> PathEntityRenamable<A> _entity(String path, Class<A> type){
            return new PathEntityRenamable<A>(type, this+"."+path); 
        }
        protected <A> PathNoEntitySimple<A> _simple(String path, Class<A> type){
            return new PathNoEntitySimple<A>(type, this+"."+path);
        }
        protected PathString _string(String path){
            return new PathString(this+"."+path);
        }        
        public ExprBoolean in(ExprEntity<Collection<D>> right){return Grammar.in(this, right);}
        public ExprBoolean isnotnull() {return Grammar.isnotnull(this);}
        public ExprBoolean isnull() {return Grammar.isnull(this);}
        public String toString() {return path;}
        public <B extends D> ExprBoolean typeOf(Class<B> type) {return Grammar.typeOf(this, type);}
    }
    
    public static class PathEntityCollection<D> extends ExprEntity<Collection<D>> implements Path<Collection<D>>{
        private final String path;
        PathEntityCollection(Class<D> type, String path) {
            super(null);
            this.path = path;
        }        
        public AliasCollection<D> as(PathEntity<D> to) {return Grammar.as(this, to);}
        public ExprBoolean isnotnull() {return Grammar.isnotnull(this);}
        public ExprBoolean isnull() {return Grammar.isnull(this);}
        public String toString() {return path;}
    }
    
    public static class PathEntityRenamable<D> extends PathEntity<D>{
        protected PathEntityRenamable(Class<D> type, String path) {super(type,path);}
        public AliasEntity<D> as(PathEntity<D> to) {return Grammar.as(this, to);}
    }
    
    public interface PathNoEntity<D> extends Path<D>{
        Expr<D> as(String to);              
    }
    
    public static class PathNoEntitySimple<D> extends ExprNoEntity<D> implements PathNoEntity<D>{
        private final String path;
        public PathNoEntitySimple(Class<D> type, String path) {
            super(type);
            this.path = path;
        }
        public ExprBoolean isnotnull() {return Grammar.isnotnull(this);}
        public ExprBoolean isnull() {return Grammar.isnull(this);}
        public String toString() {return path;}
    }
    
    public static class PathString extends ExprString implements PathNoEntity<String>{
        private final String path;
        public PathString(String path) {            
            this.path = path;
        }
        public ExprBoolean isnotnull() {return Grammar.isnotnull(this);}
        public ExprBoolean isnull() {return Grammar.isnull(this);}
        public String toString() {return path;}
    }
    
}
