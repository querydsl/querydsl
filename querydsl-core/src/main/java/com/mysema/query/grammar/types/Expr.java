/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar.types;

import static com.mysema.query.grammar.types.Factory.createBoolean;
import static com.mysema.query.grammar.types.Factory.createConstant;

import java.lang.reflect.Constructor;

import org.apache.commons.lang.ClassUtils;

import com.mysema.query.grammar.Grammar;
import com.mysema.query.grammar.Ops;
import com.mysema.query.grammar.OrderSpecifier;
import com.mysema.query.serialization.ToStringVisitor;
import com.mysema.query.util.NumberUtil;

/**
 * Expr represents a general typed expression in a Query instance
 * 
 * @author tiwe
 * @version $Id$
 */
public abstract class Expr<D>{
        
    private final Class<? extends D> type;
    private String toString;
    
    public Expr(Class<? extends D> type){this.type = type;}        
    public EBoolean eq(D right){return Grammar.eq(this, right);}        
    public EBoolean eq(Expr<? super D> right){return Grammar.eq(this, right);}
    public Class<? extends D> getType(){ return type;}
    public EBoolean ne(D right){return Grammar.ne(this, right);}
    public EBoolean ne(Expr<? super D> right){return Grammar.ne(this, right);}
    
    public String toString(){
        if (toString == null){
            toString = new ToStringVisitor().handle(this).toString();
        }
        return toString;
    }
    
    public int hashCode(){
        return type != null ? type.hashCode() : super.hashCode();
    }
    
    public static class EArrayConstructor<D> extends EConstructor<D[]> {
        private Class<D> elementType;
        public EArrayConstructor(Class<D> type, Expr<D>... args) {
            super(null, args);
            this.elementType = type;
        }
        public Class<D> getElementType(){
            return elementType;
        }
    }
    public static abstract class EBoolean extends EComparable<Boolean>{
        private EBoolean not;
        public EBoolean() {super(Boolean.class);}
        public EBoolean and(EBoolean right) {return Grammar.and(this, right);}
        public EBoolean not(){
            return not == null ? not = Grammar.not(this) : not;
        }        
        public EBoolean or(EBoolean right) {return Grammar.or(this, right);}
    }        
    public static abstract class EComparable<D extends Comparable<? super D>> extends ESimple<D>{
        public EComparable(Class<? extends D> type) {super(type);}
        public EBoolean after(D right) {return Grammar.after(this,right);}
        public EBoolean after(Expr<D> right) {return Grammar.after(this,right);}  
        public EBoolean aoe(D right) {return Grammar.aoe(this,right);}
        public EBoolean aoe(Expr<D> right) {return Grammar.aoe(this,right);}  
                
        public OrderSpecifier<D> asc() {return Grammar.asc(this);}        
        public EBoolean before(D right) {return Grammar.before(this,right);}
        public EBoolean before(Expr<D> right) {return Grammar.before(this,right);}        
        public EBoolean between(D first, D second) {return Grammar.between(this,first,second);}
        
        public EBoolean between(Expr<D> first, Expr<D> second) {return Grammar.between(this,first,second);}       
        public EBoolean boe(D right) {return Grammar.boe(this,right);}  
        
        public EBoolean boe(Expr<D> right) {return Grammar.boe(this,right);}
        // cast methods
           public <A extends Number & Comparable<? super A>> ENumber<A> castToNum(Class<A> type){
               return Grammar.numericCast(this, type);
           }        
        public OrderSpecifier<D> desc() {return Grammar.desc(this);}
        public EBoolean in(CollectionType<D> arg) {return Grammar.in(this, arg);}
        public EBoolean in(D... args) {return Grammar.in(this,args);}
        public EBoolean notBetween(D first, D second) {return Grammar.notBetween(this, first, second);}
        public EBoolean notBetween(Expr<D> first, Expr<D> second) {return Grammar.notBetween(this,first,second);}
        public EBoolean notIn(CollectionType<D> arg) {return Grammar.notIn(this, arg);}

        public EBoolean notIn(D...args) {return Grammar.notIn(this, args);}
       
       public EString stringValue(){
           return Grammar.stringCast(this);
       }
       
    }        

    public static class EConstructor<D> extends Expr<D> {
        private final Expr<?>[] args;
        private java.lang.reflect.Constructor<D> javaConstructor;
        public EConstructor(Class<D> type, Expr<?>... args) {
            super(type);
            this.args = args;
        }
        public Expr<?>[] getArgs() {
            return args;
        }
                
        /**
         * Returns the "real" constructor that matches the Constructor expression
         * @return
         */
        @SuppressWarnings("unchecked")
        public Constructor<D> getJavaConstructor(){   
            if (javaConstructor == null){
                Class<? extends D> type = getType();
                Expr<?>[] args = getArgs();
                for (Constructor<?> c : type.getConstructors()){
                    if (c.getParameterTypes().length == args.length){
                        boolean match = true;
                        for (int i = 0; i < args.length && match; i++){
                            Class<?> ptype = c.getParameterTypes()[i];
                            if (ptype.isPrimitive()){
                                ptype = ClassUtils.primitiveToWrapper(ptype);
                            }
                            match &= ptype.isAssignableFrom(args[i].getType());                                        
                        }
                        if (match){
                            javaConstructor = (Constructor<D>)c;
                            return javaConstructor;
                        }
                    }
                }
                throw new IllegalArgumentException("no suitable constructor found");       
            }else{
                return javaConstructor;
            }             
        }
    }

    public static class EConstant<D> extends Expr<D>{
        private final D constant;
        @SuppressWarnings("unchecked")
        public EConstant(D constant) {
            super((Class<D>) constant.getClass());
            this.constant = constant;
        }
        public D getConstant(){ return constant;}
        public int hashCode(){
            return constant.hashCode();
        }
        public boolean equals(Object o){
            return o instanceof EConstant ? ((EConstant<?>)o).getConstant().equals(constant) : false;
        }
    }
        
    public static abstract class EEmbeddable<D> extends ESimple<D>{
        public EEmbeddable(Class<? extends D> type) {super(type);}
    }
    
    public static abstract class EEntity<D> extends Expr<D>{
        public EEntity(Class<? extends D> type) {super(type);}        
    }
    
    public static abstract class ENumber<D extends Number & Comparable<? super D>> extends EComparable<D>{
        public ENumber(Class<? extends D> type) {super(type);}
        
        public ENumber<Byte> byteValue() { return castToNum(Byte.class); }  
        public ENumber<Double> doubleValue() { return castToNum(Double.class); }                
        public ENumber<Float> floatValue() { return castToNum(Float.class); }
        // with Java level cast
        public <A extends Number & Comparable<? super A>> EBoolean goe(A right) { return createBoolean(Ops.GOE, this, createConstant(NumberUtil.castTo(right,getType())));}
        
        // without cast
        public <A extends Number & Comparable<? super A>> EBoolean goe(Expr<A> right) {return createBoolean(Ops.GOE, this, right);}                
        public <A extends Number & Comparable<? super A>> EBoolean gt(A right) { return createBoolean(Ops.GT, this, createConstant(NumberUtil.castTo(right,getType())));}        
        public <A extends Number & Comparable<? super A>> EBoolean gt(Expr<A> right) {return createBoolean(Ops.GT, this, right);}          
        public ENumber<Integer> intValue() { return castToNum(Integer.class); }
        
        public <A extends Number & Comparable<? super A>> EBoolean loe(A right) { return createBoolean(Ops.LOE, this, createConstant(NumberUtil.castTo(right,getType())));}
        public <A extends Number & Comparable<? super A>> EBoolean loe(Expr<A> right) {return createBoolean(Ops.LOE, this, right);}
        public ENumber<Long> longValue() { return castToNum(Long.class); }
        public <A extends Number & Comparable<? super A>> EBoolean lt(A right) {  return createBoolean(Ops.LT, this, createConstant(NumberUtil.castTo(right,getType())));}
        public <A extends Number & Comparable<? super A>> EBoolean lt(Expr<A> right) {return createBoolean(Ops.LT, this, right);}
        public ENumber<Short> shortValue() { return castToNum(Short.class); }
    }
    
    public static abstract class ESimple<D> extends Expr<D>{
        public ESimple(Class<? extends D> type) {super(type);}
        public Expr<D> as(String to){return Grammar.as(this, to);}
        public EBoolean in(CollectionType<D> arg) {return Grammar.in(this, arg);}
        public EBoolean in(D... args) {return Grammar.in(this,args);}
    }
    
    public static abstract class EString extends EComparable<String>{
        private EString lower, trim, upper;
        public EString() {super(String.class);}
        
        public EString add(Expr<String> str) {return Grammar.concat(this, str);}
        public EString add(String str) {return Grammar.concat(this, str);}
        
        public Expr<Character> charAt(Expr<Integer> i) {return Grammar.charAt(this, i);}        
        public Expr<Character> charAt(int i) {return Grammar.charAt(this, i);}
        
        public EString concat(Expr<String> str) {return Grammar.concat(this, str);}
        public EString concat(String str) {return Grammar.concat(this, str);}        
        
        public EBoolean contains(Expr<String> str) {return Grammar.contains(this, str);}
        public EBoolean contains(String str) {return Grammar.contains(this, str);}
        
        public EBoolean endsWith(Expr<String> str) {return Grammar.endsWith(this, str);}
        public EBoolean endsWith(String str) {return Grammar.endsWith(this, str);}        
        
        public EBoolean equalsIgnoreCase(Expr<String> str) {return Grammar.equalsIgnoreCase(this, str);}
        public EBoolean equalsIgnoreCase(String str) {return Grammar.equalsIgnoreCase(this, str);}
        
        public EComparable<Integer> indexOf(Expr<String> str) {return Grammar.indexOf(this, str);}
        public EComparable<Integer> indexOf(String str) {return Grammar.indexOf(this, str);}
        public EComparable<Integer> indexOf(String str, int i) {return Grammar.indexOf(this, str, i);}
        public EComparable<Integer> lastIndex(String str, int i) {return Grammar.lastIndex(this, str, i);}
        public EComparable<Integer> lastIndexOf(String str) {return Grammar.lastIndexOf(this, str);}
        
        public EComparable<Integer> length() {return Grammar.length(this);}
        
        public EBoolean like(String str) { return Grammar.like(this, str); }
        
        public EString lower() { return lower == null ? lower = Grammar.lower(this) : lower; }
        
        public EBoolean startsWith(Expr<String> str) {return Grammar.startsWith(this, str);}
        public EBoolean startsWith(String str) {return Grammar.startsWith(this, str);}
        
        public EString substring(int beginIndex) { return Grammar.substring(this, beginIndex);}
        public EString substring(int beginIndex, int endIndex) { return Grammar.substring(this, beginIndex, endIndex);}
        
        public EString trim() { return trim == null ? trim = Grammar.trim(this) : trim; }
        public EString upper() { return upper == null ? upper = Grammar.upper(this) : upper; }
    }
}
