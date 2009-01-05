/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar.types;

import com.mysema.query.grammar.OrderSpecifier;

/**
 * Expr represents a general typed expression in a Query instance
 * 
 * @author tiwe
 * @version $Id$
 */
//TODO : add equals and hashCode
public abstract class Expr<D> {
        
    private final Class<D> type;
    public Expr(Class<D> type){this.type = type;}        
    public EBoolean eq(D right){return IntGrammar.eq(this, right);}        
    public EBoolean eq(Expr<? super D> right){return IntGrammar.eq(this, right);}
    public Class<D> getType(){ return type;}
    public EBoolean ne(D right){return IntGrammar.ne(this, right);}
    public EBoolean ne(Expr<? super D> right){return IntGrammar.ne(this, right);}
    
    /**
     * The Class Boolean.
     */
    public static abstract class EBoolean extends ELiteral<Boolean>{
        public EBoolean() {super(Boolean.class);}
        public EBoolean and(EBoolean right) {return IntGrammar.and(this, right);}
        public EBoolean or(EBoolean right) {return IntGrammar.or(this, right);}
    }
    
    /**
     * The Class Comparable.
     */
    public static abstract class EComparable<D extends Comparable<D>> extends ELiteral<D>{
        public EComparable(Class<D> type) {super(type);}
        public EBoolean after(D right) {return IntGrammar.after(this,right);}
        public EBoolean after(Expr<D> right) {return IntGrammar.after(this,right);}  
        public OrderSpecifier<D> asc() {return IntGrammar.asc(this);}
        public EBoolean before(D right) {return IntGrammar.before(this,right);}        
        public EBoolean before(Expr<D> right) {return IntGrammar.before(this,right);}
        public EBoolean between(D first, D second) {return IntGrammar.between(this,first,second);}       
        public EBoolean between(Expr<D> first, Expr<D> second) {return IntGrammar.between(this,first,second);}  
        public OrderSpecifier<D> desc() {return IntGrammar.desc(this);}        
        public EBoolean goe(D right) {return IntGrammar.goe(this,right);}  
        public EBoolean goe(Expr<D> right) {return IntGrammar.goe(this,right);}         
        public EBoolean gt(D right) {return IntGrammar.gt(this,right);}  
        public EBoolean gt(Expr<D> right) {return IntGrammar.gt(this,right);}
        public EBoolean in(CollectionType<D> arg) {return IntGrammar.in(this, arg);}
        public EBoolean in(D... args) {return IntGrammar.in(this,args);}
        public EBoolean loe(D right) {return IntGrammar.loe(this,right);}
        public EBoolean loe(Expr<D> right) {return IntGrammar.loe(this,right);}
        public EBoolean lt(D right) {return IntGrammar.lt(this,right);}  
        public EBoolean lt(Expr<D> right) {return IntGrammar.lt(this,right);}
        public EBoolean notBetween(D first, D second) {return IntGrammar.notBetween(this, first, second);}
        public EBoolean notBetween(Expr<D> first, Expr<D> second) {return IntGrammar.notBetween(this,first,second);}
        public EBoolean notIn(CollectionType<D> arg) {return IntGrammar.notIn(this, arg);}
        public EBoolean notIn(D...args) {return IntGrammar.notIn(this, args);}
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
     * The Class Simple.
     */
    public static abstract class ESimple<D> extends Expr<D>{
        public ESimple(Class<D> type) {super(type);}
        public Expr<D> as(String to){return IntGrammar.as(this, to);}
        public EBoolean in(CollectionType<D> arg) {return IntGrammar.in(this, arg);}
        public EBoolean in(D... args) {return IntGrammar.in(this,args);}
    }
        
    /**
     * The Class String.
     */
    public static abstract class EString extends EComparable<String>{
        private EString lower, trim, upper;
        public EString() {super(String.class);}
        public EString add(Expr<String> str) {return IntGrammar.concat(this, str);}
        public EString add(String str) {return IntGrammar.concat(this, str);}
        public EString concat(Expr<String> str) {return IntGrammar.concat(this, str);}        
        public EString concat(String str) {return IntGrammar.concat(this, str);}
        public EBoolean like(String str) { return IntGrammar.like(this, str); }
        public EString lower() { return lower == null ? lower = IntGrammar.lower(this) : lower; }        
        public EString substring(int beginIndex) { return IntGrammar.substring(this, beginIndex);}
        public EString substring(int beginIndex, int endIndex) { return IntGrammar.substring(this, beginIndex, endIndex);}
        public EString trim() { return trim == null ? trim = IntGrammar.trim(this) : trim; }
        public EString upper() { return upper == null ? upper = IntGrammar.upper(this) : upper; }        
    }  

}
