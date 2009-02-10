/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar.types;

import static com.mysema.query.grammar.types.Factory.createBoolean;
import static com.mysema.query.grammar.types.Factory.createConstant;

import com.mysema.query.grammar.Grammar;
import com.mysema.query.grammar.Ops;
import com.mysema.query.grammar.OrderSpecifier;

/**
 * Expr represents a general typed expression in a Query instance
 * 
 * @author tiwe
 * @version $Id$
 */
public abstract class Expr<D> {
        
    private final Class<D> type;
    public Expr(Class<D> type){this.type = type;}        
    public EBoolean eq(D right){return Grammar.eq(this, right);}        
    public EBoolean eq(Expr<? super D> right){return Grammar.eq(this, right);}
    public Class<D> getType(){ return type;}
    public EBoolean ne(D right){return Grammar.ne(this, right);}
    public EBoolean ne(Expr<? super D> right){return Grammar.ne(this, right);}
    
    public int hashCode(){
        return type != null ? type.hashCode() : super.hashCode();
    }
    
    /**
     * The Class Boolean.
     */
    public static abstract class EBoolean extends ELiteral<Boolean>{
        public EBoolean() {super(Boolean.class);}
        public EBoolean and(EBoolean right) {return Grammar.and(this, right);}
        public EBoolean or(EBoolean right) {return Grammar.or(this, right);}
    }
    
    /**
     * The Class Comparable.
     */
    public static abstract class EComparable<D extends Comparable<? super D>> extends ELiteral<D>{
        public EComparable(Class<D> type) {super(type);}
        public EBoolean after(D right) {return Grammar.after(this,right);}
        public EBoolean after(Expr<D> right) {return Grammar.after(this,right);}  
        public OrderSpecifier<D> asc() {return Grammar.asc(this);}
        public EBoolean before(D right) {return Grammar.before(this,right);}        
        public EBoolean before(Expr<D> right) {return Grammar.before(this,right);}
        public EBoolean between(D first, D second) {return Grammar.between(this,first,second);}       
        public EBoolean between(Expr<D> first, Expr<D> second) {return Grammar.between(this,first,second);}  
        public OrderSpecifier<D> desc() {return Grammar.desc(this);}        
        public EBoolean in(CollectionType<D> arg) {return Grammar.in(this, arg);}
        public EBoolean in(D... args) {return Grammar.in(this,args);}
        public EBoolean notBetween(D first, D second) {return Grammar.notBetween(this, first, second);}
        public EBoolean notBetween(Expr<D> first, Expr<D> second) {return Grammar.notBetween(this,first,second);}
        public EBoolean notIn(CollectionType<D> arg) {return Grammar.notIn(this, arg);}
        public EBoolean notIn(D...args) {return Grammar.notIn(this, args);}

        // cast methods
       public <A extends Number & Comparable<? super A>> ENumber<A> castToNum(Class<A> type){
           return Grammar.numericCast(this, type);
       }
       
       public EString stringValue(){
           return Grammar.stringCast(this);
       }
       
    }
    
    /**
     * The Class Constant.
     */
    public static class EConstant<D> extends Expr<D>{
        private final D constant;
        @SuppressWarnings("unchecked")
        public EConstant(D constant) {
            super((Class<D>) constant.getClass());
            this.constant = constant;
        }
        public D getConstant(){ return constant;}
        @Override public String toString(){ return constant.toString(); }
    }
        
//    *, 
//    /, 
//    DIV, 
//    %, 
//    MOD
//    -, 
//    +
    
    /**
     * The Class Embeddable.
     */
    public static abstract class EEmbeddable<D> extends ESimple<D>{
        public EEmbeddable(Class<D> type) {super(type);}
    }        
            
    /**
     * The Class Entity.
     */
    public static abstract class EEntity<D> extends Expr<D>{
        public EEntity(Class<D> type) {super(type);}        
    }
    
    /**
     * The Class Literal.
     */
    public static abstract class ELiteral<D> extends ESimple<D>{
        public ELiteral(Class<D> type) {super(type);}
    }
    
    /**
     * The Class Number.
     */
    public static abstract class ENumber<D extends Number & Comparable<? super D>> extends EComparable<D>{
        public ENumber(Class<D> type) {super(type);}
        
        // with Java level cast
        public <A extends Number & Comparable<? super A>> EBoolean goe(A right) { return createBoolean(Ops.GOE, this, createConstant(Grammar.castTo(right,getType())));}  
        public <A extends Number & Comparable<? super A>> EBoolean gt(A right) { return createBoolean(Ops.GT, this, createConstant(Grammar.castTo(right,getType())));}                
        public <A extends Number & Comparable<? super A>> EBoolean loe(A right) { return createBoolean(Ops.LOE, this, createConstant(Grammar.castTo(right,getType())));}
        public <A extends Number & Comparable<? super A>> EBoolean lt(A right) {  return createBoolean(Ops.LT, this, createConstant(Grammar.castTo(right,getType())));}
        
        // without cast
        public <A extends Number & Comparable<? super A>> EBoolean goe(Expr<A> right) {return createBoolean(Ops.GOE, this, right);}                
        public <A extends Number & Comparable<? super A>> EBoolean gt(Expr<A> right) {return createBoolean(Ops.GT, this, right);}        
        public <A extends Number & Comparable<? super A>> EBoolean loe(Expr<A> right) {return createBoolean(Ops.LOE, this, right);}          
        public <A extends Number & Comparable<? super A>> EBoolean lt(Expr<A> right) {return createBoolean(Ops.LT, this, right);}
        
        public ENumber<Byte> byteValue() { return castToNum(Byte.class); }
        public ENumber<Double> doubleValue() { return castToNum(Double.class); }
        public ENumber<Float> floatValue() { return castToNum(Float.class); }
        public ENumber<Integer> intValue() { return castToNum(Integer.class); }
        public ENumber<Long> longValue() { return castToNum(Long.class); }
        public ENumber<Short> shortValue() { return castToNum(Short.class); }
    }
    
    /**
     * The Class Simple.
     */
    public static abstract class ESimple<D> extends Expr<D>{
        public ESimple(Class<D> type) {super(type);}
        public Expr<D> as(String to){return Grammar.as(this, to);}
        public EBoolean in(CollectionType<D> arg) {return Grammar.in(this, arg);}
        public EBoolean in(D... args) {return Grammar.in(this,args);}
    }
        
    /**
     * The Class String.
     */
    public static abstract class EString extends EComparable<String>{
        private EString lower, trim, upper;
        public EString() {super(String.class);}
        public EString add(Expr<String> str) {return Grammar.concat(this, str);}
        public EString add(String str) {return Grammar.concat(this, str);}
        public EString concat(Expr<String> str) {return Grammar.concat(this, str);}        
        public EString concat(String str) {return Grammar.concat(this, str);}
        public EBoolean like(String str) { return Grammar.like(this, str); }
        public EString lower() { return lower == null ? lower = Grammar.lower(this) : lower; }        
        public EString substring(int beginIndex) { return Grammar.substring(this, beginIndex);}
        public EString substring(int beginIndex, int endIndex) { return Grammar.substring(this, beginIndex, endIndex);}
        public EString trim() { return trim == null ? trim = Grammar.trim(this) : trim; }
        public EString upper() { return upper == null ? upper = Grammar.upper(this) : upper; }        
    }  

}
