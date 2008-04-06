/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar.types;

import com.mysema.query.grammar.OrderSpecifier;

/**
 * Expr provides.
 * 
 * @author tiwe
 * @version $Id$
 */
public abstract class Expr<D> {
    
    private final Class<D> type;
    public Expr(Class<D> type){this.type = type;}        
    public Boolean eq(D right){return IntGrammar.eq(this, right);}        
    public Boolean eq(Expr<? super D> right){return IntGrammar.eq(this, right);}
    public Class<D> getType(){ return type;}
    public Boolean ne(D right){return IntGrammar.ne(this, right);}
    public Boolean ne(Expr<? super D> right){return IntGrammar.ne(this, right);}
    
    /**
     * The Class Boolean.
     */
    public static abstract class Boolean extends Literal<java.lang.Boolean>{
        public Boolean() {super(java.lang.Boolean.class);}
        public Boolean and(Boolean right) {return IntGrammar.and(this, right);}
        public Boolean or(Boolean right) {return IntGrammar.or(this, right);}
    }
    
    /**
     * The Class Comparable.
     */
    public static abstract class Comparable<D extends java.lang.Comparable<D>> extends Literal<D>{
        public Comparable(Class<D> type) {super(type);}
        public Boolean after(D right) {return IntGrammar.after(this,right);}
        public Boolean after(Expr<D> right) {return IntGrammar.after(this,right);}  
        public OrderSpecifier<D> asc() {return IntGrammar.asc(this);}
        public Boolean before(D right) {return IntGrammar.before(this,right);}        
        public Boolean before(Expr<D> right) {return IntGrammar.before(this,right);}
        public Boolean between(D first, D second) {return IntGrammar.between(this,first,second);}       
        public Boolean between(Expr<D> first, Expr<D> second) {return IntGrammar.between(this,first,second);}  
        public OrderSpecifier<D> desc() {return IntGrammar.desc(this);}        
        public Boolean goe(D right) {return IntGrammar.goe(this,right);}  
        public Boolean goe(Expr<D> right) {return IntGrammar.goe(this,right);}         
        public Boolean gt(D right) {return IntGrammar.gt(this,right);}  
        public Boolean gt(Expr<D> right) {return IntGrammar.gt(this,right);}
        public Boolean in(D... args) {return IntGrammar.in(this,args);}
        public Boolean in(CollectionType<D> arg) {return IntGrammar.in(this, arg);}
        public Boolean loe(D right) {return IntGrammar.loe(this,right);}
        public Boolean loe(Expr<D> right) {return IntGrammar.loe(this,right);}
        public Boolean lt(D right) {return IntGrammar.lt(this,right);}  
        public Boolean lt(Expr<D> right) {return IntGrammar.lt(this,right);}
        public Boolean notBetween(D first, D second) {return IntGrammar.notBetween(this, first, second);}
        public Boolean notBetween(Expr<D> first, Expr<D> second) {return IntGrammar.notBetween(this,first,second);}
        public Boolean notIn(D...args) {return IntGrammar.notIn(this, args);}
        public Boolean notIn(CollectionType<D> arg) {return IntGrammar.notIn(this, arg);}
    }
    
    /**
     * The Class Constant.
     */
    public static class Constant<D> extends Expr<D>{
        private final D constant;
        @SuppressWarnings("unchecked")
        public Constant(D constant) {
            super((Class<D>) constant.getClass());
            this.constant = constant;
        }
        public D getConstant(){ return constant;}
        @Override public java.lang.String toString(){ return constant.toString(); }
    }
        
//    *, 
//    /, 
//    DIV, 
//    %, 
//    MOD
//    -, 
//    +
    
    /**
     * The Class Entity.
     */
    public static abstract class Entity<D> extends Expr<D>{
        public Entity(Class<D> type) {super(type);}        
    }        
            
    /**
     * The Class Simple.
     */
    public static abstract class Simple<D> extends Expr<D>{
        public Simple(Class<D> type) {super(type);}
        public Expr<D> as(java.lang.String to){return IntGrammar.as(this, to);}
    }
    
    /**
     * The Class Embeddable.
     */
    public static abstract class Embeddable<D> extends Simple<D>{
        public Embeddable(Class<D> type) {super(type);}
    }
    
    /**
     * The Class Literal.
     */
    public static abstract class Literal<D> extends Simple<D>{
        public Literal(Class<D> type) {super(type);}
    }
        
    /**
     * The Class String.
     */
    public static abstract class String extends Comparable<java.lang.String>{
        public String() {super(java.lang.String.class);}
        public String concat(Expr<java.lang.String> str) {return IntGrammar.concat(this, str);}
        public String concat(java.lang.String str) {return IntGrammar.concat(this, str);}
        public Boolean like(java.lang.String str) { return IntGrammar.like(this, str); }
        public String lower() { return IntGrammar.lower(this); }
        public String substring(int beginIndex) { return IntGrammar.substring(this, beginIndex);}
        public String substring(int beginIndex, int endIndex) { return IntGrammar.substring(this, beginIndex, endIndex);}
        public String trim() { return IntGrammar.trim(this); }
        public String upper() { return IntGrammar.upper(this); }
    }  

}
