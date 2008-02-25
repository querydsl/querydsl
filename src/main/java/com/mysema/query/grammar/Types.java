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
    
    public abstract static class Alias<D> extends ExprNonFinalImpl<D>{ 
        public final Expr<?> from;
        public final String to;
        Alias(Expr<?> from, String to) {
            this.from = from;
            this.to = to;
        }
    }
        
    public static class AliasCollection<D> extends Alias<D> implements ExprEntity<D>{
        AliasCollection(PathEntityCollection<D> from, Path<D> to) {
            super(from,to.toString());
        }        
    }
    
    public static class AliasEntity<D> extends Alias<D> implements ExprEntity<D>{
        AliasEntity(PathEntity<D> from, PathEntity<D> to) {
            super(from,to.toString());
        }
        AliasEntity(PathEntity<D> from, String to) {
            super(from,to);
        }
    }
    
    public static class AliasNoEntity<D> extends Alias<D> implements ExprNoEntity<D>{       
        AliasNoEntity(Expr<D> from, String to) {
            super(from,to);
        }
        public Expr<D> as(String to) {
            return Grammar.as(this, to);
        }        
    }
    
    public static class ConstantExpr<D> extends ExprNonFinalImpl<D>{
        public D constant;
    }
    
    public interface Expr<D> {
                
    }
    
    public interface ExprNonFinal<D> extends Expr<D> {
        public <B extends D> ExprBoolean eq(B right);        
        public <B extends D> ExprBoolean eq(Expr<B> right);
        public <B extends D> ExprBoolean ne(B right);
        public <B extends D> ExprBoolean ne(Expr<B> right);
    }
    
    public interface ExprBoolean extends ExprNoEntity<Boolean>{ }
    
    /**
     * Reference to an entity
     */
    public interface ExprEntity<D> extends Expr<D>{}  
    
    static abstract class ExprNonFinalImpl<D> implements ExprNonFinal<D>{
        public <B extends D> ExprBoolean eq(B right){return Grammar.eq(this, right);}        
        public <B extends D> ExprBoolean eq(Expr<B> right){return Grammar.eq(this, right);}
        public <B extends D> ExprBoolean ne(B right){return Grammar.ne(this, right);}
        public <B extends D> ExprBoolean ne(Expr<B> right){return Grammar.ne(this, right);}
    }
            
    public interface ExprNoEntity<D> extends ExprNonFinal<D>{
        public Expr<D> as(String to);
    }
    
    public static abstract class ExprNoEntityImpl<D> extends ExprNonFinalImpl<D> implements ExprNoEntity<D>{
        public Expr<D> as(String to){return Grammar.as(this, to);}
    }  
    
    public abstract static class Operation<RT> extends ExprNoEntityImpl<RT> {
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
        implements ExprBoolean {
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
    
    public abstract static class Path<D> extends ExprNonFinalImpl<D>{
        // path is hidden to not pollute the namespace of the domain types
        private final String path;
        public Path(String p) {
            path = p;
        }        
        @Override
        public final String toString(){ return path; }
    }
    
    public static class PathBoolean extends PathNoEntity<Boolean> implements ExprBoolean{
        PathBoolean(String path) {super(path);}
    }
    
    public static class PathEntity<D> extends Path<D> implements ExprEntity<D>{
        protected PathEntity(PathEntity<?> type, String path) {
            super(type+"."+path);
        } 
        protected PathEntity(String path) {super(path);}
        protected PathBoolean _boolean(String path){
            return new PathBoolean(this+"."+path);
        }
        protected <A>PathEntityCollection<A> _collection(String path,Class<A> type) {
            return new PathEntityCollection<A>(this+"."+path);
        }
        protected <A> PathNoEntity<A> _prop(String path,Class<A> type) {
            return new PathNoEntity<A>(this+"."+path);
        }
        
        public AliasEntity<D> as(PathEntity<D> to) {return Grammar.as(this, to);}
    }
    
    public static class PathEntityCollection<D> extends Path<Collection<D>> implements 
        ExprEntity<Collection<D>>{
        PathEntityCollection(String p) {
            super(p);
        }        
        public AliasCollection<D> as(PathEntity<D> to) {
            return Grammar.as(this, to);
        }
    }
    
    public static class PathNoEntity<D> extends Path<D> implements ExprNoEntity<D>{
        public PathNoEntity(String p) {
            super(p);
        }
        public Expr<D> as(String to) {
            return Grammar.as(this, to);
        }        
    }

}
