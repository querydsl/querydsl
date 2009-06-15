/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import com.mysema.query.types.Grammar;

/**
 * EString represents String expressions
 * 
 * @author tiwe
 * @see java.lang.String
 */
public abstract class EString extends EComparable<String> {
    private EString lower, trim, upper;
    private ENumber<Long> length;

    public EString() {
        super(String.class);
    }

    /**
     * @param str
     * @return
     */
    public final EString add(Expr<String> str) {
        return Grammar.concat(this, str);
    }

    /**
     * @param str
     * @return
     */
    public final EString add(String str) {
        return Grammar.concat(this, str);
    }

    /**
     * @param i
     * @return
     */
    public final Expr<Character> charAt(Expr<Integer> i) {
        return Grammar.charAt(this, i);
    }

    /**
     * @param i
     * @return
     */
    public final Expr<Character> charAt(int i) {
        return Grammar.charAt(this, i);
    }

    /**
     * @param str
     * @return
     */
    public final EString concat(Expr<String> str) {
        return Grammar.concat(this, str);
    }

    /**
     * @param str
     * @return
     */
    public final EString concat(String str) {
        return Grammar.concat(this, str);
    }

    /**
     * @param str
     * @return
     */
    public final EBoolean contains(Expr<String> str) {
        return Grammar.contains(this, str);
    }

    /**
     * @param str
     * @return
     */
    public final EBoolean contains(String str) {
        return Grammar.contains(this, str);
    }

    /**
     * @param str
     * @return
     */
    public final EBoolean endsWith(Expr<String> str) {
        return Grammar.endsWith(this, str);
    }

    /**
     * @param str
     * @param caseSensitive
     * @return
     */
    public final EBoolean endsWith(Expr<String> str, boolean caseSensitive) {
        return Grammar.endsWith(this, str, caseSensitive);
    }

    /**
     * @param str
     * @return
     */
    public final EBoolean endsWith(String str) {
        return Grammar.endsWith(this, str);
    }

    /**
     * @param str
     * @param caseSensitive
     * @return
     */
    public final EBoolean endsWith(String str, boolean caseSensitive) {
        return Grammar.endsWith(this, str, caseSensitive);
    }

    /**
     * @param str
     * @return
     */
    public final EBoolean equalsIgnoreCase(Expr<String> str) {
        return Grammar.equalsIgnoreCase(this, str);
    }

    /**
     * @param str
     * @return
     */
    public final EBoolean equalsIgnoreCase(String str) {
        return Grammar.equalsIgnoreCase(this, str);
    }

    /**
     * @param str
     * @return
     */
    public final ENumber<Integer> indexOf(Expr<String> str) {
        return Grammar.indexOf(this, str);
    }

    /**
     * @param str
     * @return
     */
    public final ENumber<Integer> indexOf(String str) {
        return Grammar.indexOf(this, str);
    }

    /**
     * @param str
     * @param i
     * @return
     */
    public final ENumber<Integer> indexOf(String str, int i) {
        return Grammar.indexOf(this, str, i);
    }

    /**
     * @return
     * 
     */
    public final EBoolean isEmpty(){
        return Grammar.isEmpty(this);
    }
    
    /**
     * @return
     */
    public final EBoolean isNotEmpty(){
        return Grammar.isEmpty(this).not();
    }
    
    /**
     * @return
     */
    public final ENumber<Long> length() {
        if (length == null) {
            length = Grammar.length(this);
        }
        return length;
    }

    /**
     * Uses startsWith, endsWith and matches
     * 
     * @param str
     * @return
     */
    @Deprecated
    public final EBoolean like(String str) {
        return matches(str.replace("%", ".*").replace("_", "."));
    }

    public final EString lower() {
        if (lower == null) {
            lower = Grammar.lower(this);
        }
        return lower;
    }

    /**
     * @param regex
     * @return
     */
    public final EBoolean matches(String regex){
        return Grammar.matches(this, regex);
    }
    
    /**
     * @param regex
     * @return
     */
    public final EBoolean matches(Expr<String> regex){
        return Grammar.matches(this, regex);
    }
    
    /**
     * @param str
     * @return
     */
    public final EBoolean startsWith(Expr<String> str) {
        return Grammar.startsWith(this, str);
    }

    /**
     * @param str
     * @param caseSensitive
     * @return
     */
    public final EBoolean startsWith(Expr<String> str, boolean caseSensitive) {
        return Grammar.startsWith(this, str, caseSensitive);
    }

    /**
     * @param str
     * @return
     */
    public final EBoolean startsWith(String str) {
        return Grammar.startsWith(this, str);
    }

    /**
     * @param str
     * @param caseSensitive
     * @return
     */
    public final EBoolean startsWith(String str, boolean caseSensitive) {
        return Grammar.startsWith(this, str, caseSensitive);
    }

    /* (non-Javadoc)
     * @see com.mysema.query.types.expr.EComparable#stringValue()
     */
    public final EString stringValue() {
        return this;
    }

    /**
     * @param beginIndex
     * @return
     */
    public final EString substring(int beginIndex) {
        return Grammar.substring(this, beginIndex);
    }

    /**
     * @param beginIndex
     * @param endIndex
     * @return
     */
    public final EString substring(int beginIndex, int endIndex) {
        return Grammar.substring(this, beginIndex, endIndex);
    }

    /**
     * @return
     */
    public final EString trim() {
        if (trim == null) {
            trim = Grammar.trim(this);
        }
        return trim;
    }

    /**
     * @return
     */
    public final EString upper() {
        if (upper == null) {
            upper = Grammar.upper(this);
        }
        return upper;
    }
    
}