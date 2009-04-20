/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar.types;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
   
    private static final SimpleExprFactory factory = new SimpleExprFactory();
    
    private final Class<? extends D> type;
    private String toString;
    
    public Expr(Class<? extends D> type){this.type = type;}
    public Class<? extends D> getType(){ return type;}
    
    // eq
    public final EBoolean eq(D right){return Grammar.eq(this, right);}        
    public final EBoolean eq(Expr<? super D> right){return Grammar.eq(this, right);}
    
    // ne
    public final EBoolean ne(D right){return Grammar.ne(this, right);}
    public final EBoolean ne(Expr<? super D> right){return Grammar.ne(this, right);}
    
    // containment
    public final EBoolean in(CollectionType<? extends D> arg) {return Grammar.in(this, arg);}
    public final EBoolean in(Collection<? extends D> arg) {return Grammar.in(this, arg); }
    public final EBoolean in(D... args) {return Grammar.in(this,args);}
    
    public final EBoolean notIn(CollectionType<? extends D> arg) {return Grammar.notIn(this, arg);}
    public final EBoolean notIn(Collection<? extends D> arg) {return Grammar.in(this, arg); }
    public final EBoolean notIn(D...args) {return Grammar.notIn(this, args);}
    
    void validate(){
        new ValidationVisitor().handle(this);
    }
    
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
        public final Class<D> getElementType(){
            return elementType;
        }
    }
    public static abstract class EBoolean extends EComparable<Boolean>{
        private EBoolean not;        
        public EBoolean() {super(Boolean.class);}
        
        public final EBoolean and(EBoolean right) {return Grammar.and(this, right);}
        public final EBoolean not(){
            if (not == null) not = Grammar.not(this);
            return not;
        }        
        public final EBoolean or(EBoolean right) {return Grammar.or(this, right);}
    }        
    public static abstract class EComparable<D extends Comparable<?>> extends ESimple<D>{
        private OrderSpecifier<D> asc;
        private OrderSpecifier<D> desc;
        private EString stringCast;               
        public EComparable(Class<? extends D> type) {super(type);}
        
        public final EBoolean after(D right) {return Grammar.after(this,right);}
        public final EBoolean after(Expr<D> right) {return Grammar.after(this,right);}  
        public final EBoolean aoe(D right) {return Grammar.aoe(this,right);}
        public final EBoolean aoe(Expr<D> right) {return Grammar.aoe(this,right);}  
                
        public final OrderSpecifier<D> asc() { 
            if (asc == null) asc = Grammar.asc(this);
            return asc;
        }        
        public final EBoolean before(D right) {return Grammar.before(this,right);}
        public final EBoolean before(Expr<D> right) {return Grammar.before(this,right);}        
        public final EBoolean boe(D right) {return Grammar.boe(this,right);}          
        public final EBoolean boe(Expr<D> right) {return Grammar.boe(this,right);}
        
        public final EBoolean between(D first, D second) {return Grammar.between(this,first,second);}       
        public final EBoolean between(Expr<D> first, Expr<D> second) {return Grammar.between(this,first,second);}
        
        // cast methods
        public <A extends Number & Comparable<? super A>> ENumber<A> castToNum(Class<A> type){
            return Grammar.numericCast(this, type);
        }        
        public final OrderSpecifier<D> desc() {
            if (desc == null) desc = Grammar.desc(this);
            return desc ;
        }
        public final EBoolean notBetween(D first, D second) {return Grammar.notBetween(this, first, second);}
        public final EBoolean notBetween(Expr<D> first, Expr<D> second) {return Grammar.notBetween(this,first,second);}
       
       public EString stringValue(){
           if (stringCast == null) stringCast = Grammar.stringCast(this);
           return stringCast;
       }
       
    }        

    public static class EConstructor<D> extends Expr<D> {
        private final List<Expr<?>> args;
        private java.lang.reflect.Constructor<D> javaConstructor;
        public EConstructor(Class<D> type, Expr<?>... args) {
            super(type);
            this.args = Collections.unmodifiableList(Arrays.asList(args));
        }
        public final List<Expr<?>> getArgs() {
            return args;
        }
        public final Expr<?> getArg(int index){
            return args.get(index);
        }
                
        /**
         * Returns the "real" constructor that matches the Constructor expression
         * @return
         */
        @SuppressWarnings("unchecked")
        public Constructor<D> getJavaConstructor(){   
            if (javaConstructor == null){
                Class<? extends D> type = getType();
                List<Expr<?>> args = getArgs();
                for (Constructor<?> c : type.getConstructors()){
                    if (c.getParameterTypes().length == args.size()){
                        boolean match = true;
                        for (int i = 0; i < args.size() && match; i++){
                            Class<?> ptype = c.getParameterTypes()[i];
                            if (ptype.isPrimitive()){
                                ptype = ClassUtils.primitiveToWrapper(ptype);
                            }
                            match &= ptype.isAssignableFrom(args.get(i).getType());                                        
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
    
    public static abstract class ENumber<D extends Number & Comparable<?>> extends EComparable<D>{
        public ENumber(Class<? extends D> type) {super(type);}
        
        public final ENumber<Byte> byteValue() { return castToNum(Byte.class); }  
        public final ENumber<Double> doubleValue() { return castToNum(Double.class); }                
        public final ENumber<Float> floatValue() { return castToNum(Float.class); }
        public final ENumber<Integer> intValue() { return castToNum(Integer.class); }
        public final ENumber<Long> longValue() { return castToNum(Long.class); }
        public final ENumber<Short> shortValue() { return castToNum(Short.class); }
        
        public final <A extends Number & Comparable<?>> EBoolean goe(A right) { return factory.createBoolean(Ops.GOE, this, factory.createConstant(NumberUtil.castTo(right,getType())));}       
        public final <A extends Number & Comparable<?>> EBoolean goe(Expr<A> right) {return factory.createBoolean(Ops.GOE, this, right);}                
        public final <A extends Number & Comparable<?>> EBoolean gt(A right) { return factory.createBoolean(Ops.GT, this, factory.createConstant(NumberUtil.castTo(right,getType())));}        
        public final <A extends Number & Comparable<?>> EBoolean gt(Expr<A> right) {return factory.createBoolean(Ops.GT, this, right);}          
                
        public final <A extends Number & Comparable<?>> EBoolean loe(A right) { return factory.createBoolean(Ops.LOE, this, factory.createConstant(NumberUtil.castTo(right,getType())));}
        public final <A extends Number & Comparable<?>> EBoolean loe(Expr<A> right) {return factory.createBoolean(Ops.LOE, this, right);}        
        public final <A extends Number & Comparable<?>> EBoolean lt(A right) {  return factory.createBoolean(Ops.LT, this, factory.createConstant(NumberUtil.castTo(right,getType())));}
        public final <A extends Number & Comparable<?>> EBoolean lt(Expr<A> right) {return factory.createBoolean(Ops.LT, this, right);}
    }
    
    public static abstract class ESimple<D> extends Expr<D>{
        public ESimple(Class<? extends D> type) {super(type);}
        public final Expr<D> as(String to){return Grammar.as(this, to);}
    }
    
    public static abstract class EString extends EComparable<String>{
        private EString lower, trim, upper;
        private EComparable<Integer> length;
        
        public EString() {super(String.class);}
        
        public final EString add(Expr<String> str) {return Grammar.concat(this, str);}
        public final EString add(String str) {return Grammar.concat(this, str);}
        
        public final Expr<Character> charAt(Expr<Integer> i) {return Grammar.charAt(this, i);}        
        public final Expr<Character> charAt(int i) {return Grammar.charAt(this, i);}
        
        public final EString concat(Expr<String> str) {return Grammar.concat(this, str);}
        public final EString concat(String str) {return Grammar.concat(this, str);}        
        
        public final EBoolean contains(Expr<String> str) {return Grammar.contains(this, str);}
        public final EBoolean contains(String str) {return Grammar.contains(this, str);}
        
        public final EBoolean endsWith(Expr<String> str) {return Grammar.endsWith(this, str);}
        public final EBoolean endsWith(String str) {return Grammar.endsWith(this, str);}        
        public final EBoolean endsWith(Expr<String> str, boolean caseSensitive) {return Grammar.endsWith(this, str, caseSensitive);}
        public final EBoolean endsWith(String str, boolean caseSensitive) {return Grammar.endsWith(this, str, caseSensitive);}
        
        public final EBoolean equalsIgnoreCase(Expr<String> str) {return Grammar.equalsIgnoreCase(this, str);}
        public final EBoolean equalsIgnoreCase(String str) {return Grammar.equalsIgnoreCase(this, str);}
        
        public final EComparable<Integer> indexOf(Expr<String> str) {return Grammar.indexOf(this, str);}
        public final EComparable<Integer> indexOf(String str) {return Grammar.indexOf(this, str);}
        public final EComparable<Integer> indexOf(String str, int i) {return Grammar.indexOf(this, str, i);}
        public final EComparable<Integer> lastIndex(String str, int i) {return Grammar.lastIndex(this, str, i);}
        public final EComparable<Integer> lastIndexOf(String str) {return Grammar.lastIndexOf(this, str);}
        
        public final EComparable<Integer> length() {
            if (length == null) length = Grammar.length(this);
            return length;
        }
        public final EBoolean like(String str) { return Grammar.like(this, str); }        
        public final EString lower() { 
            if (lower == null) lower = Grammar.lower(this);
            return lower;
        }
        public final EBoolean startsWith(Expr<String> str) {return Grammar.startsWith(this, str);}
        public final EBoolean startsWith(String str) {return Grammar.startsWith(this, str);}
        public final EBoolean startsWith(Expr<String> str, boolean caseSensitive) {return Grammar.startsWith(this, str, caseSensitive);}
        public final EBoolean startsWith(String str, boolean caseSensitive) {return Grammar.startsWith(this, str, caseSensitive);}
        
        public final EString substring(int beginIndex) { return Grammar.substring(this, beginIndex);}
        public final EString substring(int beginIndex, int endIndex) { return Grammar.substring(this, beginIndex, endIndex);}
        
        public final EString trim() { 
            if (trim == null) trim = Grammar.trim(this);
            return trim;
        }
        public final EString upper() {
            if (upper == null) upper = Grammar.upper(this);
            return upper; 
         }        
        public final EString stringValue(){
            return this;
        }
    }
}
