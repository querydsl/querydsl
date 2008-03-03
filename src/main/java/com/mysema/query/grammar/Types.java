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
    
    public static class AliasComparable<D extends Comparable<D>> extends AliasNoEntity<D> 
        implements ExprComparable<D>{
        AliasComparable(Expr<D> from, String to) {
            super(from,to);
        }
        public ExprBoolean goe(D right) {return Grammar.goe(this,right);}       
        public ExprBoolean goe(Expr<D> right) {return Grammar.goe(this,right);}  
        public ExprBoolean gt(D right) {return Grammar.gt(this,right);}  
        public ExprBoolean gt(Expr<D> right) {return Grammar.gt(this,right);}  
        public ExprBoolean loe(D right) {return Grammar.loe(this,right);}         
        public ExprBoolean loe(Expr<D> right) {return Grammar.loe(this,right);}  
        public ExprBoolean lt(D right) {return Grammar.lt(this,right);}  
        public ExprBoolean lt(Expr<D> right) {return Grammar.lt(this,right);}  
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
    
    public interface ExprBoolean extends ExprNoEntity<Boolean>{ 
        ExprBoolean and(ExprBoolean right);
        ExprBoolean or(ExprBoolean right);
    }
    
    public interface ExprComparable<D extends Comparable<D>> extends ExprNoEntity<D>{
        ExprBoolean goe(D right);        
        ExprBoolean goe(Expr<D> right);
        ExprBoolean gt(D right);
        ExprBoolean gt(Expr<D> right);
        ExprBoolean loe(D right);        
        ExprBoolean loe(Expr<D> right);
        ExprBoolean lt(D right);
        ExprBoolean lt(Expr<D> right);
    }
    
    
    /**
     * Reference to an entity
     */
    public interface ExprEntity<D> extends Expr<D>{}
            
    public interface ExprNoEntity<D> extends ExprNonFinal<D>{
        public Expr<D> as(String to);
    }
    
    public static abstract class ExprNoEntityImpl<D> extends ExprNonFinalImpl<D> implements ExprNoEntity<D>{
        public Expr<D> as(String to){return Grammar.as(this, to);}
    }  
    
    public interface ExprNonFinal<D> extends Expr<D> {
        <B extends D> ExprBoolean eq(B right);        
        <B extends D> ExprBoolean eq(Expr<B> right);
        <B extends D> ExprBoolean ne(B right);
        <B extends D> ExprBoolean ne(Expr<B> right);
    }
    
    static abstract class ExprNonFinalImpl<D> implements ExprNonFinal<D>{
        public <B extends D> ExprBoolean eq(B right){return Grammar.eq(this, right);}        
        public <B extends D> ExprBoolean eq(Expr<B> right){return Grammar.eq(this, right);}
        public <B extends D> ExprBoolean ne(B right){return Grammar.ne(this, right);}
        public <B extends D> ExprBoolean ne(Expr<B> right){return Grammar.ne(this, right);}
    }  
    
    public static class Operation<OP,RT extends OP> extends ExprNoEntityImpl<RT>{
        public Expr<?>[] args;
        /**
         * arguments don't need to be of same type as return type
         */
        public Op<OP> operator;
        public Expr<RT> as(String to) {
            return Grammar.as(this, to);
        }  
    }
    
    public static class OperationBoolean extends Operation<Boolean,Boolean> 
        implements ExprBoolean {    
        public ExprBoolean and(ExprBoolean right) {return Grammar.and(this, right);}
        public ExprBoolean or(ExprBoolean right) {return Grammar.or(this, right);}
    }
    
    
    public enum Order{ ASC,DESC }
        
    public static class OrderSpecifier<A extends Comparable<A>>{
        public Order order; 
        public Expr<A> target;       
    }
    
    public abstract static class Path<D> extends ExprNonFinalImpl<D>{
        private final String path;
        @SuppressWarnings("unused")
        // this field can be accessed via reflection
        private final Class<D> type;
        Path(Class<D> type, String path) {
            this.type = type;
            this.path = path;
        }        
        @Override
        public final String toString(){ return path; }
    }
    
    public static class PathBoolean extends PathNoEntity<Boolean> implements ExprBoolean{
        PathBoolean(String path) {super(Boolean.class,path);}
        public ExprBoolean and(ExprBoolean right) {return Grammar.and(this, right);}
        public ExprBoolean or(ExprBoolean right) {return Grammar.or(this, right);}        
    }
    
    public static class PathComparable<D extends Comparable<D>> extends PathNoEntity<D>
        implements ExprComparable<D>{
        public PathComparable(Class<D> type, String p) {
            super(type, p);
        }
        public ExprBoolean goe(D right) {return Grammar.goe(this,right);}       
        public ExprBoolean goe(Expr<D> right) {return Grammar.goe(this,right);}  
        public ExprBoolean gt(D right) {return Grammar.gt(this,right);}  
        public ExprBoolean gt(Expr<D> right) {return Grammar.gt(this,right);}  
        public ExprBoolean loe(D right) {return Grammar.loe(this,right);}         
        public ExprBoolean loe(Expr<D> right) {return Grammar.loe(this,right);}  
        public ExprBoolean lt(D right) {return Grammar.lt(this,right);}  
        public ExprBoolean lt(Expr<D> right) {return Grammar.lt(this,right);}  
    }
    
    public static class PathEntity<D> extends Path<D> implements ExprEntity<D>{
        protected PathEntity(Class<D> type, String path) {super(type,path);}
        protected PathBoolean _boolean(String path){
            return new PathBoolean(this+"."+path);
        }
        protected <A>PathEntityCollection<A> _collection(String path,Class<A> type) {
            return new PathEntityCollection<A>(type, this+"."+path);
        }
        protected <A extends Comparable<A>> PathComparable<A> _comparable(String path,Class<A> type) {
            return new PathComparable<A>(type, this+"."+path);
        }
        protected <A> PathEntity<A> _entity(String path, Class<A> type){
            return new PathEntity<A>(type, this+"."+path); 
        }
        protected PathString _string(String path){
            return new PathString(this+"."+path);
        }

        
        public AliasEntity<D> as(PathEntity<D> to) {return Grammar.as(this, to);}
    }
    
    public static class PathEntityCollection<D> extends Path<Collection<D>> implements 
        ExprEntity<Collection<D>>{
        PathEntityCollection(Class<D> type, String p) {
            // FIXME
            super(null, p);
        }        
        public AliasCollection<D> as(PathEntity<D> to) {
            return Grammar.as(this, to);
        }
    }
    
    public static class PathNoEntity<D> extends Path<D> implements ExprNoEntity<D>{
        public PathNoEntity(Class<D> type, String p) {
            super(type, p);
        }
        public Expr<D> as(String to) {
            return Grammar.as(this, to);
        }        
    }
    
    public static class PathString extends PathComparable<String>{
        public PathString(String p) {
            super(String.class, p);
        }
    }

}
