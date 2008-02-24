/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;

import java.util.Collection;
import com.mysema.query.grammar.Ops.*;


/**
 * Types provides the types of the fluent grammar
 *
 * @author tiwe
 * @version $Id$
 */
public class Types {
    
    public static class Alias<D> extends ExprImpl<D>{ 
        public final Expr<?> from;
        public final String to;
        Alias(Expr<?> from, String to) {
            this.from = from;
            this.to = to;
        }
    }
        
    public static class AliasForAnything<D> extends Alias<D>{       
        AliasForAnything(Expr<D> from, String to) {
            super(from,to);
        }        
    }
    
    public static class AliasForCollection<D> extends Alias<D> implements ExprEntity<D>{
        AliasForCollection(PathCollection<D> from, Path<D> to) {
            super(from,to.toString());
        }        
    }
    
    public static class AliasForEntity<D> extends Alias<D> implements ExprEntity<D>{
        AliasForEntity(PathDomainType<D> from, PathDomainType<D> to) {
            super(from,to.toString());
        }
        AliasForEntity(PathDomainType<D> from, String to) {
            super(from,to);
        }
    }
    
    public static class ConstantExpr<A> extends ExprImpl<A>{
        public A constant;
    }
    
    public static class CountExpr<D> extends ExprImpl<D>{
        // TODO : add count selection etc
    }
    
    public interface Expr<A> { 
        public <B extends A> ExprBoolean eq(B right);        
        public <B extends A> ExprBoolean eq(Expr<B> right);
        public <B extends A> ExprBoolean ne(B right);
        public <B extends A> ExprBoolean ne(Expr<B> right);
    }
    
    public interface ExprBoolean extends Expr<Boolean>{ }
        
    /**
     * Reference to an entity
     */
    public interface ExprEntity<T> extends Expr<T>{}
    
    static class ExprImpl<T> implements Expr<T>{
        public <B extends T> ExprBoolean eq(B right){return Grammar.eq(this, right);}        
        public <B extends T> ExprBoolean eq(Expr<B> right){return Grammar.eq(this, right);}
        public <B extends T> ExprBoolean ne(B right){return Grammar.ne(this, right);}
        public <B extends T> ExprBoolean ne(Expr<B> right){return Grammar.ne(this, right);}
    }  
    
    public static class Operation<RT> extends ExprImpl<RT> {}
    
    public static class OperationBinary<OP,RT extends OP,L,R> extends Operation<RT>{
        /**
         * arguments don't need to be of same type as return type
         */
        public Expr<L> left;
        public Op<OP> operator; 
        public Expr<R> right; 
    }
    
    public static class OperationBinaryBoolean<L,R> extends OperationBinary<Boolean,Boolean,L,R> 
        implements ExprBoolean {
        
    }
    
    public static class OperationTertiary<OP,RT extends OP,F,S,T> extends Operation<RT>{
        /**
         * arguments don't need to be of same type as return type
         */
        public Expr<F> first;
        public Op<OP> operator;
        public Expr<S> second;
        public Expr<T> third; 
    }
           
    public static class OperationTertiaryBoolean<F,S,T> extends OperationTertiary<Boolean,Boolean,F,S,T>
        implements ExprBoolean{
        
    }
    
    public static class OperationUnary<OP,RT extends OP,A> extends Operation<RT>{
        /**
         * argument doesn't need to be of same type as return type
         */
        public Expr<A> left;
        public Op<OP> operator;                
    }
    
    public static class OperationUnaryBoolean<A> extends OperationUnary<Boolean,Boolean,A>
        implements ExprBoolean{
        
    }
    
    public enum Order{ ASC,DESC }
        
    public static class OrderSpecifier<A extends Comparable<A>>{
        public Order order; 
        public Expr<A> target;       
    }
    
    public static class Path<T> extends ExprImpl<T>{
        // _path is hidden to not pollute the namespace of the domain types
        private final String path;
        public Path(String p) {
            path = p;
        }
        
        public ExprBoolean isnotnull(){return Grammar.isnotnull(this);}
//        Op<Boolean> IN = new OpImpl<Boolean>();
//        Op<Boolean> ISTYPEOF = new OpImpl<Boolean>();
        // these should only be applied to paths
        public ExprBoolean isnull(){return Grammar.isnull(this);}
        
        @Override
        public final String toString(){ return path; }
    }
    
    public static class PathBoolean extends Path<Boolean> implements ExprBoolean{
        PathBoolean(String path) {super(path);}
    }
    
    public static class PathCollection<A> extends Path<Collection<A>> implements 
        ExprEntity<Collection<A>>{
        PathCollection(String p) {
            super(p);
        }        
        public AliasForCollection<A> as(PathDomainType<A> to) {
            return Grammar.as(this, to);
        }
    }
    
    public static class PathComparable<A extends Comparable<A>> extends Path<A>{
        PathComparable(String p) {
            super(p);
        }
        
        // convenience methods
        public ExprBoolean between(A start, A end){ 
            return Grammar.between(this,start, end);}
        public ExprBoolean between(Expr<A> start, Expr<A> end){ 
            return Grammar.between(this,start, end);}
        public ExprBoolean goe(A right){ return Grammar.goe(this, right);}
        public ExprBoolean goe(Expr<A> right){ return Grammar.goe(this, right);}
        public ExprBoolean gt(A right){ return Grammar.gt(this, right);}
        public ExprBoolean gt(Expr<A> right){ return Grammar.gt(this, right);}
        public ExprBoolean loe(A right){ return Grammar.loe(this, right);}
        public ExprBoolean loe(Expr<A> right){ return Grammar.loe(this, right);}
        public ExprBoolean lt(A right){ return Grammar.lt(this, right);}
        public ExprBoolean lt(Expr<A> right){ return Grammar.lt(this, right);}
    }
    
    public static class PathDomainType<D> extends Path<D> implements ExprEntity<D>{
        protected PathDomainType(PathDomainType<?> type, String path) {
            super(type+"."+path);
        } 
        protected PathDomainType(String path) {super(path);}
        protected PathBoolean _boolean(String path){
            return new PathBoolean(this+"."+path);
        }
        protected <A>PathCollection<A> _collection(String path,Class<A> type) {
            return new PathCollection<A>(this+"."+path);
        }
        protected <A> Path<A> _prop(String path,Class<A> type) {
            return new Path<A>(this+"."+path);
        }
        
        // convenience
        public AliasForEntity<D> as(PathDomainType<D> to) {return Grammar.as(this, to);}
        public AliasForEntity<D> as(String to) {return Grammar.as(this, to);}
    }

}
