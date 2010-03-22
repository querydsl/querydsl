/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nullable;

import com.mysema.commons.lang.Assert;

/**
 * EStringConst represents String constants
 * 
 * @author tiwe
 *
 */
public final class EStringConst extends EString implements Constant<String>{
    
    private static final Map<String,EString> CACHE;
    
    private static final int CACHE_SIZE = 256;

    private static final long serialVersionUID = 5182804405789674556L;
    
    static{
        List<String> strs = new ArrayList<String>(Arrays.asList("", ".", ".*", "%"));
        for (int i = 0; i < CACHE_SIZE; i++){
            strs.add(String.valueOf(i));
        }
    
        CACHE = new HashMap<String,EString>(strs.size());
        for (String str : strs){
            CACHE.put(str, new EStringConst(str));
        }
    }
    
    
    /**
     * Factory method for constants
     * 
     * @param str
     * @return
     */
    public static EString create(String str){
        return create(str, false);
    }
    
    public static EString create(String str, boolean populateCache) {
        if (CACHE.containsKey(str)){
            return CACHE.get(str);            
        }else{
            EString rv = new EStringConst(Assert.notNull(str,"str"));
            if (populateCache){
                CACHE.put(str, rv);                
            }
            return rv;
        }
    }
    
    private final String constant;
    
    @Nullable
    private volatile ENumber<Long> length;

    @Nullable
    private volatile EString lower, trim, upper;

    EStringConst(String constant){
        this.constant = constant;
    }
    
    @Override
    public void accept(Visitor v) {
        v.visit(this);        
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public EString append(Expr<String> s) {
        if (s instanceof Constant){
            return append(((Constant<String>)s).getConstant());
        }else{
            return super.append(s);
        }
    }
    
    @Override
    public EString append(String s) {
        return EStringConst.create(constant + s);
    }
    
    @Override
    public Expr<Character> charAt(int i) {
        return ExprConst.create(constant.charAt(i));
    }

    @Override
    public EString concat(String s) {
        return append(s);
    }
    
    @Override
    public EBoolean eq(String s){
        return EBooleanConst.create(constant.equals(s));
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object o) {
        if (o == this){
            return true;
        }else if (o instanceof Constant){
            return ((Constant)o).getConstant().equals(constant);
        }else{
            return false;
        }
    }

    @Override
    public EBoolean equalsIgnoreCase(String str) {
        return EBooleanConst.create(constant.equalsIgnoreCase(str));
    }

    @Override
    public String getConstant() {
        return constant;
    }
    
    @Override
    public int hashCode() {
        return constant.hashCode();
    }
    
    @Override
    public EBoolean isEmpty(){
        return EBooleanConst.create(constant.isEmpty());
    }
    
    @Override
    public EBoolean isNotEmpty(){
        return EBooleanConst.create(!constant.isEmpty());
    }
    
    @Override
    public ENumber<Long> length() {
        if (length == null) {
            length = ENumberConst.create(Long.valueOf(constant.length()));
        }
        return length;
    }

    @Override
    public EString lower() {
        if (lower == null) {
            lower = EStringConst.create(constant.toLowerCase(Locale.ENGLISH));
        }
        return lower;
    }
    
    @Override
    public EBoolean matches(String pattern){
        return EBooleanConst.create(constant.matches(pattern));
    }
    
    @Override
    public EBoolean ne(String s){
        return EBooleanConst.create(!constant.equals(s));
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public EString prepend(Expr<String> s) {
        if (s instanceof Constant){
            return prepend(((Constant<String>)s).getConstant());
        }else{
            return super.prepend(s);
        }
    }
    
    @Override
    public EString prepend(String s) {
        return EStringConst.create(s + constant);
    }
    
    @Override
    public Expr<String[]> split(String regex) {
        return ExprConst.create(constant.split(regex));
    }

    @Override
    public EString substring(int beginIndex) {
        return EStringConst.create(constant.substring(beginIndex));
    }
    
    @Override
    public EString substring(int beginIndex, int endIndex) {
        return EStringConst.create(constant.substring(beginIndex, endIndex));
    }
    
    @Override
    public EString toLowerCase() {
        return lower();
    }
    
    @Override
    public EString toUpperCase() {
        return upper();
    }
    
    @Override
    public EString trim() {
        if (trim == null) {
            trim = EStringConst.create(constant.trim());
        }
        return trim;
    }
    
    
    @Override
    public EString upper() {
        if (upper == null){
            upper = EStringConst.create(constant.toUpperCase(Locale.ENGLISH)); 
        }
        return upper; 
    }
}
