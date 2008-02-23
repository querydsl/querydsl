/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;

import java.util.Collection;


/**
 * Types provides the types of the fluent grammar
 *
 * @author tiwe
 * @version $Id$
 */
public class Types {
    
    public static class Alias<D> extends Reference<D> implements EntityExpr<D>{
        public final Reference<D> from, to;
        Alias(Reference<D> from, Reference<D> to) {
            super(to.toString());
            this.from = from;
            this.to = to;
        }        
    }
    
    public static class BinaryBooleanOperation<L,R> extends BinaryOperation<Boolean,Boolean,L,R> 
        implements BooleanOperation {
        
    }
    
    public static class BinaryOperation<OP,RT extends OP,L,R> implements Operation<RT>{
        /**
         * arguments don't need to be of same type as return type
         */
        public Expr<L> left;
        public Op<OP> operator; 
        public Expr<R> right; 
    }
    
    /**
     * NOTE : BooleanExpr as a concrete interface instead of Expr<Boolean> avoids
     * compiler warnings when used in Query#where(BooleanExpr... objects);
     */
    public interface BooleanExpr extends Expr<Boolean>{ }
    
    public interface BooleanOperation extends Operation<Boolean>, BooleanExpr {}
    
    public static class BooleanProperty extends Reference<Boolean> implements BooleanExpr{
        public BooleanProperty(String path) {super(path);}
    }
    
    /**
     * Boolean operators (operators used with boolean operands)
     */
    public interface BoOp<RT> extends CompOp<RT>{ 
        BoOp<Boolean> AND = new BoOpImpl<Boolean>(); 
        BoOp<Boolean> NOT = new BoOpImpl<Boolean>();
        BoOp<Boolean> OR = new BoOpImpl<Boolean>();
        BoOp<Boolean> XNOR = new BoOpImpl<Boolean>();
        BoOp<Boolean> XOR = new BoOpImpl<Boolean>();
    }
        
    static class BoOpImpl<RT> implements BoOp<RT>{}
    
    public static class CollectionAlias<D> extends Reference<D> implements EntityExpr<D>{
        public final CollectionReference<D> from;
        public final Reference<D> to;
        CollectionAlias(CollectionReference<D> from, Reference<D> to) {
            super(to.toString());
            this.from = from;
            this.to = to;
        }        
    }
       
    public static class CollectionReference<A> extends Reference<Collection<A>> implements 
        EntityExpr<Collection<A>>{
        public CollectionReference(String p) {
            super(p);
        }        
    }
    
    /**
     * Operators for Comparable objects
     */
    public interface CompOp<RT> extends Op<RT>{
        CompOp<Boolean> BETWEEN = new CompOpImpl<Boolean>();
        CompOp<Boolean> GOE = new CompOpImpl<Boolean>();
        CompOp<Boolean> GT = new CompOpImpl<Boolean>();
        CompOp<Boolean> LOE = new CompOpImpl<Boolean>();
        CompOp<Boolean> LT = new CompOpImpl<Boolean>();
    }    
        
    static class CompOpImpl<RT> implements CompOp<RT> {}
    
    public static class ConstantExpr<A> implements Expr<A>{
        public A constant;
    }
    
    /**
     * Date Operators (operators used with Date operands)
     */
    public interface DateOp<RT> extends CompOp<RT>{       
        DateOp<Boolean> AFTER = new DateOpImpl<Boolean>();
        DateOp<Boolean> BEFORE = new DateOpImpl<Boolean>();        
    }
    
    static class DateOpImpl<RT> implements DateOp<RT>{}
    
    public static class DomainType<D> extends Reference<D> implements EntityExpr<D>{
        protected DomainType(DomainType<?> type, String path) {
            super(type+"."+path);
        } 
        protected DomainType(String path) {super(path);}
        protected BooleanProperty _boolean(String path){
            return new BooleanProperty(this+"."+path);
        }
        protected <A>CollectionReference<A> _collection(String path,Class<A> type) {
            return new CollectionReference<A>(this+"."+path);
        }
        protected <A> Reference<A> _prop(String path,Class<A> type) {
            return new Reference<A>(this+"."+path);
        }
    }
    
    /**
     * Reference to an entity
     */
    public static interface EntityExpr<T> extends Expr<T>{}
    
    public interface Expr<A> { }
           
    /**
     * Numeric Operators (operators used with numeric operands)
     */
    public interface NumOp<RT> extends CompOp<RT>{
        NumOp<Number> ADD = new NumOpImpl<Number>();   
        NumOp<Number> DIV = new NumOpImpl<Number>();        
        NumOp<Number> MOD = new NumOpImpl<Number>();
        NumOp<Number> MULT = new NumOpImpl<Number>();
        NumOp<Number> SUB = new NumOpImpl<Number>();
    }
    
    static class NumOpImpl<A> implements Types.NumOp<A> {}
    
    /**
     * Operators (the return type is encoded in the 1st generic parameter)
     */
    public interface Op<RT> {
        Op<Boolean> EQ = new OpImpl<Boolean>();
        Op<Boolean> IN = new OpImpl<Boolean>();
        Op<Boolean> ISNOTNULL = new OpImpl<Boolean>();
        Op<Boolean> ISNULL = new OpImpl<Boolean>();
        Op<Boolean> ISTYPEOF = new OpImpl<Boolean>();
        Op<Boolean> NE = new OpImpl<Boolean>();
    }
    
    public interface Operation<RT> extends Expr<RT> {}
    
    static class OpImpl<RT> implements Op<RT> {}
    
    public enum Order{ ASC,DESC }       
    
    public static class OrderSpecifier<A extends Comparable<A>>{
        public Order order; 
        public Expr<A> target;       
    }
    
    public static class Reference<T> implements Expr<T>{
        // _path is hidden to not pollute the namespace of the domain types
        private final String path;
        public Reference(String p) {
            path = p;
        }
        @Override
        public final String toString(){ return path; }
    }
        
    /**
     * String Operators (operators used with String operands)
     */
    public interface StrOp<RT> extends CompOp<RT>{       
        StrOp<String> CONCAT = new StrOpImpl<String>();
        StrOp<Boolean> LIKE = new StrOpImpl<Boolean>();
        StrOp<String> LOWER = new StrOpImpl<String>();
        StrOp<String> SUBSTR = new StrOpImpl<String>();
        StrOp<String> UPPER = new StrOpImpl<String>();
    }
    
    static class StrOpImpl<RT> implements StrOp<RT>{}
    
    public static class TertiaryBooleanOperation<F,S,T> extends TertiaryOperation<Boolean,Boolean,F,S,T>
        implements BooleanOperation{
        
    }
    
    public static class TertiaryOperation<OP,RT extends OP,F,S,T> implements Operation<RT>{
        /**
         * arguments don't need to be of same type as return type
         */
        public Expr<F> first;
        public Op<OP> operator;
        public Expr<S> second;
        public Expr<T> third; 
    }
    
    public static class UnaryBooleanOperation<A> extends UnaryOperation<Boolean,Boolean,A>
        implements BooleanOperation{
        
    }
    
    public static class UnaryOperation<OP,RT extends OP,A> implements Operation<RT>{
        /**
         * argument doesn't need to be of same type as return type
         */
        public Expr<A> left;
        public Op<OP> operator;                
    }

}
