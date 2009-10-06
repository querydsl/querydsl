/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import com.mysema.query.types.Visitor;




/**
 * EStringConst represents String constants
 * 
 * @author tiwe
 *
 */
@SuppressWarnings("serial")
public class EStringConst extends EString implements Constant<String>{
    
    private final String constant;
    
    private volatile ENumber<Long> length;

    private volatile EString lower, trim, upper;

    EStringConst(String constant){
        this.constant = constant;
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
        return EString.create(constant + s);
    }
    
    @Override
    public Expr<Character> charAt(int i) {
        return Expr.create(constant.charAt(i));
    }
    
    @Override
    public EString concat(String s) {
        return append(s);
    }

    @Override
    public EBoolean eq(String s){
        return EBoolean.create(constant.equals(s));
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object o) {
        return o instanceof Constant ? ((Constant<?>) o).getConstant().equals(constant) : false;
    }
    
    @Override
    public EBoolean equalsIgnoreCase(String str) {
        return EBoolean.create(constant.equalsIgnoreCase(str));
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
        return EBoolean.create(constant.isEmpty());
    }
    
    @Override
    public EBoolean isNotEmpty(){
        return EBoolean.create(!constant.isEmpty());
    }
    
    @Override
    public ENumber<Long> length() {
        if (length == null) {
            length = ENumber.create(Long.valueOf(constant.length()));
        }
        return length;
    }
    
    @Override
    public EString lower() {
        if (lower == null) {
            lower = EString.create(constant.toLowerCase());
        }
        return lower;
    }
    
    @Override
    public EBoolean matches(String pattern){
        return EBoolean.create(constant.matches(pattern));
    }

    @Override
    public EBoolean ne(String s){
        return EBoolean.create(!constant.equals(s));
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
        return EString.create(s + constant);
    }
    
    @Override
    public EString substring(int beginIndex) {
        return EString.create(constant.substring(beginIndex));
    }
    
    @Override
    public EString substring(int beginIndex, int endIndex) {
        return EString.create(constant.substring(beginIndex, endIndex));
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
            trim = EString.create(constant.trim());
        }
        return trim;
    }
    
    @Override
    public EString upper() {
        if (upper == null){
            upper = EString.create(constant.toUpperCase()); 
        }
        return upper; 
    }
    
    @Override
    public Expr<String[]> split(String regex) {
        return Expr.create(constant.split(regex));
    }
    
    @Override
    public void accept(Visitor v) {
        v.visit(this);        
    }
}
