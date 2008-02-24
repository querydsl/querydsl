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
    
    public abstract static class Alias<D> extends ExprImpl<D>{ 
        public final Expr<?> from;
        public final String to;
        Alias(Expr<?> from, String to) {
            this.from = from;
            this.to = to;
        }
    }
        
    public static class AliasForCollection<D> extends Alias<D> implements ExprForEntity<D>{
        AliasForCollection(PathForEntityCollection<D> from, Path<D> to) {
            super(from,to.toString());
        }        
    }
    
    public static class AliasForEntity<D> extends Alias<D> implements ExprForEntity<D>{
        AliasForEntity(PathForEntity<D> from, PathForEntity<D> to) {
            super(from,to.toString());
        }
        AliasForEntity(PathForEntity<D> from, String to) {
            super(from,to);
        }
    }
    
    public static class AliasForNoEntity<D> extends Alias<D> implements ExprForNoEntity<D>{       
        AliasForNoEntity(Expr<D> from, String to) {
            super(from,to);
        }
        public Expr<D> as(String to) {
            return Grammar.as(this, to);
        }        
    }
    
    public static class ConstantExpr<D> extends ExprImpl<D>{
        public D constant;
    }
    
    public static class CountExpr<D> extends ExprForNoEntityImpl<D>{
        // TODO : move to querydsl-hibernate
    }
    
    public interface Expr<D> { 
        public <B extends D> ExprForBoolean eq(B right);        
        public <B extends D> ExprForBoolean eq(Expr<B> right);
        public <B extends D> ExprForBoolean ne(B right);
        public <B extends D> ExprForBoolean ne(Expr<B> right);        
    }
    
    static abstract class ExprImpl<D> implements Expr<D>{
        public <B extends D> ExprForBoolean eq(B right){return Grammar.eq(this, right);}        
        public <B extends D> ExprForBoolean eq(Expr<B> right){return Grammar.eq(this, right);}
        public <B extends D> ExprForBoolean ne(B right){return Grammar.ne(this, right);}
        public <B extends D> ExprForBoolean ne(Expr<B> right){return Grammar.ne(this, right);}
    }
    
    public interface ExprForBoolean extends ExprForNoEntity<Boolean>{ }  
    
    /**
     * Reference to an entity
     */
    public interface ExprForEntity<D> extends Expr<D>{}
            
    public interface ExprForNoEntity<D> extends Expr<D>{
        public Expr<D> as(String to);
    }
    
    static abstract class ExprForNoEntityImpl<D> extends ExprImpl<D> implements ExprForNoEntity<D>{
        public Expr<D> as(String to){return Grammar.as(this, to);}
    }  
    
    public abstract static class Operation<RT> extends ExprForNoEntityImpl<RT> {
        public Expr<RT> as(String to) {
            return Grammar.as(this, to);
        }  
    }
    
    public static class OperationBinary<OP,RT extends OP,L,R> extends Operation<RT>{
        /**
         * arguments don't need to be of same type as return type
         */
        public Expr<L> left;
        public Op<OP> operator; 
        public Expr<R> right; 
    }
    
    public static class OperationBinaryBoolean<L,R> extends OperationBinary<Boolean,Boolean,L,R> 
        implements ExprForBoolean {
        public Expr<Boolean> as(String to) {
            return Grammar.as(this, to);
        }        
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
        implements ExprForBoolean{  
    }
    
    public static class OperationUnary<OP,RT extends OP,A> extends Operation<RT>{
        /**
         * argument doesn't need to be of same type as return type
         */
        public Expr<A> left;
        public Op<OP> operator;                
    }
    
    public static class OperationUnaryBoolean<A> extends OperationUnary<Boolean,Boolean,A>
        implements ExprForBoolean{
    }
    
    public enum Order{ ASC,DESC }
        
    public static class OrderSpecifier<A extends Comparable<A>>{
        public Order order; 
        public Expr<A> target;       
    }
    
    public abstract static class Path<D> extends ExprImpl<D>{
        // path is hidden to not pollute the namespace of the domain types
        private final String path;
        public Path(String p) {
            path = p;
        }        
        @Override
        public final String toString(){ return path; }
    }
    
    public static class PathForBoolean extends PathForNoEntity<Boolean> implements ExprForBoolean{
        PathForBoolean(String path) {super(path);}
    }
    
    public static class PathForEntity<D> extends Path<D> implements ExprForEntity<D>{
        protected PathForEntity(PathForEntity<?> type, String path) {
            super(type+"."+path);
        } 
        protected PathForEntity(String path) {super(path);}
        protected PathForBoolean _boolean(String path){
            return new PathForBoolean(this+"."+path);
        }
        protected <A>PathForEntityCollection<A> _collection(String path,Class<A> type) {
            return new PathForEntityCollection<A>(this+"."+path);
        }
        protected <A> PathForNoEntity<A> _prop(String path,Class<A> type) {
            return new PathForNoEntity<A>(this+"."+path);
        }
        
        public AliasForEntity<D> as(PathForEntity<D> to) {return Grammar.as(this, to);}
    }
    
    public static class PathForEntityCollection<D> extends Path<Collection<D>> implements 
        ExprForEntity<Collection<D>>{
        PathForEntityCollection(String p) {
            super(p);
        }        
        public AliasForCollection<D> as(PathForEntity<D> to) {
            return Grammar.as(this, to);
        }
    }
    
    public static class PathForNoEntity<D> extends Path<D> implements ExprForNoEntity<D>{
        public PathForNoEntity(String p) {
            super(p);
        }
        public Expr<D> as(String to) {
            return Grammar.as(this, to);
        }        
    }

}
