/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.expr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nullable;

import com.mysema.commons.lang.Assert;
import com.mysema.query.types.Constant;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Visitor;

/**
 * EStringConst represents String constants
 *
 * @author tiwe
 *
 */
public final class StringConstant extends StringExpression implements Constant<String>{

    private static final Map<String,StringExpression> CACHE;

    private static final int CACHE_SIZE = 256;

    private static final long serialVersionUID = 5182804405789674556L;

    static{
        List<String> strs = new ArrayList<String>(Arrays.asList("", ".", ".*", "%"));
        for (int i = 0; i < CACHE_SIZE; i++){
            strs.add(String.valueOf(i));
        }

        CACHE = new HashMap<String,StringExpression>(strs.size());
        for (String str : strs){
            CACHE.put(str, new StringConstant(str));
        }
    }

    /**
     * Factory method for constants
     *
     * @param str
     * @return
     */
    public static StringExpression create(String str){
        return create(str, false);
    }

    public static StringExpression create(String str, boolean populateCache) {
        if (CACHE.containsKey(str)){
            return CACHE.get(str);
        }else{
            StringExpression rv = new StringConstant(Assert.notNull(str,"str"));
            if (populateCache){
                CACHE.put(str, rv);
            }
            return rv;
        }
    }

    private final String constant;

    @Nullable
    private volatile NumberExpression<Integer> length;

    @Nullable
    private volatile StringExpression lower, trim, upper;

    StringConstant(String constant){
        this.constant = constant;
    }

    @Override
    public <R,C> R accept(Visitor<R,C> v, C context) {
        return v.visit(this, context);
    }

    @SuppressWarnings("unchecked")
    @Override
    public StringExpression append(Expression<String> s) {
        if (s instanceof Constant){
            return append(((Constant<String>)s).getConstant());
        }else{
            return super.append(s);
        }
    }

    @Override
    public StringExpression append(String s) {
        return StringConstant.create(constant + s);
    }

    @Override
    public SimpleExpression<Character> charAt(int i) {
        return SimpleConstant.create(constant.charAt(i));
    }

    @Override
    public StringExpression concat(String s) {
        return append(s);
    }

    @Override
    public BooleanExpression eq(String s){
        return BooleanConstant.create(constant.equals(s));
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
    public BooleanExpression equalsIgnoreCase(String str) {
        return BooleanConstant.create(constant.equalsIgnoreCase(str));
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
    public BooleanExpression isEmpty(){
        return BooleanConstant.create(constant.isEmpty());
    }

    @Override
    public BooleanExpression isNotEmpty(){
        return BooleanConstant.create(!constant.isEmpty());
    }

    @Override
    public NumberExpression<Integer> length() {
        if (length == null) {
            length = NumberConstant.create(Integer.valueOf(constant.length()));
        }
        return length;
    }

    @Override
    public StringExpression lower() {
        if (lower == null) {
            lower = StringConstant.create(constant.toLowerCase(Locale.ENGLISH));
        }
        return lower;
    }

    @Override
    public BooleanExpression matches(String pattern){
        return BooleanConstant.create(constant.matches(pattern));
    }

    @Override
    public BooleanExpression ne(String s){
        return BooleanConstant.create(!constant.equals(s));
    }

    @SuppressWarnings("unchecked")
    @Override
    public StringExpression prepend(Expression<String> s) {
        if (s instanceof Constant){
            return prepend(((Constant<String>)s).getConstant());
        }else{
            return super.prepend(s);
        }
    }

    @Override
    public StringExpression prepend(String s) {
        return StringConstant.create(s + constant);
    }

    @Override
    public Expression<String[]> split(String regex) {
        return SimpleConstant.create(constant.split(regex));
    }

    @Override
    public StringExpression substring(int beginIndex) {
        return StringConstant.create(constant.substring(beginIndex));
    }

    @Override
    public StringExpression substring(int beginIndex, int endIndex) {
        return StringConstant.create(constant.substring(beginIndex, endIndex));
    }

    @Override
    public StringExpression toLowerCase() {
        return lower();
    }

    @Override
    public StringExpression toUpperCase() {
        return upper();
    }

    @Override
    public StringExpression trim() {
        if (trim == null) {
            trim = StringConstant.create(constant.trim());
        }
        return trim;
    }

    @Override
    public StringExpression upper() {
        if (upper == null){
            upper = StringConstant.create(constant.toUpperCase(Locale.ENGLISH));
        }
        return upper;
    }
}
