/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysema.commons.lang.Assert;
import com.mysema.query.types.operation.OBoolean;
import com.mysema.query.types.operation.OComparable;
import com.mysema.query.types.operation.ONumber;
import com.mysema.query.types.operation.OSimple;
import com.mysema.query.types.operation.OString;
import com.mysema.query.types.operation.Ops;


/**
 * EString represents String expressions
 * 
 * @author tiwe
 * @see java.lang.String
 */
@SuppressWarnings("serial")
public abstract class EString extends EComparable<String> {
    
    private static final Map<String,EString> cache;
    
    static{
        List<String> strs = new ArrayList<String>(Arrays.asList("", ".", ".*", "%"));
        for (int i = 0; i < 256; i++){
            strs.add(String.valueOf(i));
        }
    
        cache = new HashMap<String,EString>(strs.size());
        for (String str : strs){
            cache.put(str, new EStringConst(str));
        }
    }
    
    
    /**
     * Factory method for constants
     * 
     * @param str
     * @return
     */
    public static final EString __create(String str){
        return __create(str, false);
    }
    
    public static EString __create(String str, boolean populateCache) {
        if (cache.containsKey(str)){
            return cache.get(str);            
        }else{
            EString rv = new EStringConst(Assert.notNull(str));
            if (populateCache) cache.put(str, rv);                
            return rv;
        }
    }
    
    private volatile ENumber<Long> length;
    
    private volatile EString lower, trim, upper;
    
    private volatile EBoolean isempty;

    public EString() {
        super(String.class);
    }

    /**
     * Get the concatenation of this and str
     * 
     * @param str
     * @return this + str
     */
    public EString append(Expr<String> str) {
        return OString.create(Ops.CONCAT, this, str);
    }

    /**
     * Get the concatenation of this and str
     * 
     * @param str
     * @return this + str
     */
    public EString append(String str) {
        return append(EString.__create(str));
    }
    
    /**
     * Get the character at the given index
     * 
     * @param i
     * @return this.charAt(i)
     * @see java.lang.String#charAt(int)
     */
    public Expr<Character> charAt(Expr<Integer> i) {
        return OComparable.create(Character.class, Ops.CHAR_AT, this, i);
    }

    /**
     * Get the character at the given index
     * 
     * @param i
     * @return this.charAt(i)
     * @see java.lang.String#charAt(int)
     */
    public Expr<Character> charAt(int i) {
        return charAt(ENumber.__create(i));
    }

    /**
     * Get the concatenation of this and str
     * 
     * @param str
     * @return this + str
     */
    public EString concat(Expr<String> str) {
        return append(str);
    }

    /**
     * Get the concatenation of this and str
     * 
     * @param str
     * @return this + str
     */
    public EString concat(String str) {
        return append(str);
    }

    /**
     * Returns true if the given String is contained
     * 
     * @param str
     * @return this.contains(str)
     * @see java.lang.String#contains(CharSequence)
     */
    public EBoolean contains(Expr<String> str) {
        return OBoolean.create(Ops.STRING_CONTAINS, this, str);
    }

    /**
     * Returns true if the given String is contained
     * 
     * @param str
     * @return this.contains(str)
     * @see java.lang.String#contains(CharSequence)
     */
    public EBoolean contains(String str) {
        return contains(EString.__create(str));
    }

    /**
     * Returns true if this ends with str
     * 
     * @param str
     * @return this.endsWith(str)
     * @see java.lang.String#endsWith(String)
     */
    public EBoolean endsWith(Expr<String> str) {
        return OBoolean.create(Ops.ENDS_WITH, this, str);
    }

    /**
     * Returns true if this ends with str  
     * 
     * @param str
     * @param caseSensitive case sensitivity of operation
     * @return
     * @see java.lang.String#endsWith(String)
     */
    public EBoolean endsWith(Expr<String> str, boolean caseSensitive) {
        if (caseSensitive){
            return endsWith(str);
        }else{
            return OBoolean.create(Ops.ENDS_WITH_IC, this, str);
        }        
    }

    /**
     * Returns true if this ends with str 
     * 
     * @param str
     * @return this.endsWith(str)
     * @see java.lang.String#endsWith(String)
     */
    public EBoolean endsWith(String str) {
        return endsWith(EString.__create(str));
    }

    /**
     * Returns true if this ends with str 
     * 
     * @param str
     * @param caseSensitive
     * @return
     * @see java.lang.String#endsWith(String)
     */
    public EBoolean endsWith(String str, boolean caseSensitive) {
        return endsWith(EString.__create(str), caseSensitive);
    }

    /**
     * Compares this {@code EString} to another {@code EString}, ignoring case
     * considerations.
     * 
     * @param str
     * @return this.equalsIgnoreCase(str)
     * @see java.lang.String#equalsIgnoreCase(String)
     */
    public EBoolean equalsIgnoreCase(Expr<String> str) {
        return OBoolean.create(Ops.EQ_IGNORE_CASE, this, str);
    }

    /**
     * Compares this {@code EString} to another {@code EString}, ignoring case
     * considerations.
     * 
     * @param str
     * @return this.equalsIgnoreCase(str)
     * @see java.lang.String#equalsIgnoreCase(String)
     */
    public EBoolean equalsIgnoreCase(String str) {
        return equalsIgnoreCase(EString.__create(str));
    }

    /**
     * Get the index of the given substring in this String
     * 
     * @param str
     * @return this.indexOf(str)
     * @see java.lang.String#indexOf(String)
     */
    public ENumber<Integer> indexOf(Expr<String> str) {
        return ONumber.create(Integer.class, Ops.INDEX_OF, this, str);
    }

    /**
     * Get the index of the given substring in this String
     * 
     * @param str
     * @return this.indexOf(str)
     * @see java.lang.String#indexOf(String)
     */
    public ENumber<Integer> indexOf(String str) {
        return indexOf(EString.__create(str));
    }

    /**
     * Get the index of the given substring in this String, starting from the given index
     * 
     * @param str
     * @param i
     * @return this.indexOf(str, i)
     * @see java.lang.String#indexOf(String, int)
     */
    public ENumber<Integer> indexOf(String str, int i) {
        return indexOf(EString.__create(str), i);
    }

    
    /**
     * Get the index of the given substring in this String, starting from the given index
     * 
     * @param str
     * @param i
     * @return
     */
    public ENumber<Integer> indexOf(Expr<String> str, int i) {
        return ONumber.create(Integer.class, Ops.INDEX_OF_2ARGS, this, str, ENumber.__create(i));
    }
    
    /**
     * Return true if this String is empty
     * 
     * @return this.isEmpty()
     * @see java.lang.String#isEmpty()
     */
    public EBoolean isEmpty(){
        if (isempty == null){
            isempty = OBoolean.create(Ops.STRING_IS_EMPTY, this); 
        }
        return isempty;
    }

    /**
     * Return true if this String is not empty
     * 
     * @return !this.isEmpty()
     * @see java.lang.String#isEmpty()
     */
    public EBoolean isNotEmpty(){
        return isEmpty().not();
    }

    /**
     * Return the length of this String
     * 
     * @return this.length()
     * @see java.lang.String#length()
     */
    public ENumber<Long> length() {
        if (length == null) {
            length = ONumber.create(Long.class, Ops.STRING_LENGTH, this);
        }
        return length;
    }
    
    /**
     * Expr: <code>this like str</code>
     * 
     * @param str
     * @return
     */
    public EBoolean like(String str){
        return OBoolean.create(Ops.LIKE, this, EString.__create(str));
    }
    
    /**
     * Expr: <code>this like str</code>
     * 
     * @param str
     * @return
     */
    public EBoolean like(EString str){
        return OBoolean.create(Ops.LIKE, this, str);
    }
    
    /**
     * Get the lower case form 
     * 
     * @return this.toLowerCase()
     * @see java.lang.String#toLowerCase()
     */
    public EString lower() {
        if (lower == null) {
            lower = OString.create(Ops.LOWER, this);
        }
        return lower;
    }

    /**
     * Return true if this String matches the given regular expression
     * 
     * @param regex
     * @return this.matches(right)
     * @see java.lang.String#matches(String)
     */
    public EBoolean matches(Expr<String> regex){
        return OBoolean.create(Ops.MATCHES, this, regex);
    }

    /**
     * Return true if this String matches the given regular expression
     * 
     * @param regex
     * @return this.matches(regex)
     * @see java.lang.String#matches(String)
     */
    public EBoolean matches(String regex){
        return matches(EString.__create(regex));
    }

    /**
     * Prepend the given String and return the result
     * 
     * @param str
     * @return str + this
     */
    public EString prepend(Expr<String> str) {
        return OString.create(Ops.CONCAT, str, this);
    }
    
    /**
     * Prepend the given String and return the result
     * 
     * @param str
     * @return str + this
     */
    public EString prepend(String str) {
        return prepend(EString.__create(str));
    }
    
    /**
     * Split the given String with regex as the matcher for the separator
     * 
     * @param regex
     * @return this.split(regex)
     * @see java.lang.String#split(String)
     */
    public Expr<String[]> split(String regex) {
        return OSimple.create(String[].class, Ops.StringOps.SPLIT, this, EString.__create(regex));
    }
    
    /**
     * Return true if this starts with str
     * 
     * @param str
     * @return this.startsWith(str)
     * @see java.lang.String#startsWith(String)
     */
    public EBoolean startsWith(Expr<String> str) {
        return OBoolean.create(Ops.STARTS_WITH, this, str);
    }

    /**
     * Return true if this starts with str
     * 
     * @param str
     * @param caseSensitive
     * @return
     * @see java.lang.String#startsWith(String)
     */
    public EBoolean startsWith(Expr<String> str, boolean caseSensitive) {
        if (caseSensitive){
            return startsWith(str);
        }else{
            return OBoolean.create(Ops.STARTS_WITH_IC, this, str);
        }  
    }

    /**
     * Return true if this starts with str
     * 
     * @param str
     * @return this.startsWith(str)
     * @see java.lang.String#startsWith(String)
     */
    public EBoolean startsWith(String str) {
        return startsWith(EString.__create(str));
    }

    /**
     * Return true if this starts with str
     * 
     * @param str
     * @param caseSensitive
     * @return
     * @see java.lang.String#startsWith(String)
     */
    public EBoolean startsWith(String str, boolean caseSensitive) {
        return startsWith(EString.__create(str), caseSensitive);
    }

    /* (non-Javadoc)
     * @see com.mysema.query.types.expr.EComparable#stringValue()
     */
    public EString stringValue() {
        return this;
    }

    /**
     * Get the given substring
     * 
     * @param beginIndex
     * @return this.substring(beginIndex)
     * @see java.lang.String#substring(int)
     */
    public EString substring(int beginIndex) {
        return OString.create(Ops.SUBSTR_1ARG, this, ENumber.__create(beginIndex));
    }

    /**
     * Get the given substring
     * 
     * @param beginIndex
     * @param endIndex
     * @return this.substring(beginIndex, endIndex)
     * @see java.lang.String#substring(int, int)
     */
    public EString substring(int beginIndex, int endIndex) {
        return OString.create(Ops.SUBSTR_2ARGS, this, ENumber.__create(beginIndex), ENumber.__create(endIndex));
    }

    /**
     * Get the lower case form
     * 
     * @return this.toLowerCase()
     * @see java.lang.String#toLowerCase()
     */
    public EString toLowerCase() {
        return lower();
    }

    /**
     * Get the upper case form
     * 
     * @return
     * @see java.lang.String#toUpperCase()
     */
    public EString toUpperCase() {
        return upper();
    }
    
    /**
     * Get a copy of the string, with leading and trailing whitespace
     * omitted.
     * 
     * @return
     * @see java.lang.String#trim()
     */
    public EString trim() {
        if (trim == null) {
            trim = OString.create(Ops.TRIM, this);
        }
        return trim;
    }
    
    /**
     * Get the upper case form
     * 
     * @return
     * @see java.lang.String#toUpperCase()
     */
    public EString upper() {
        if (upper == null) {
            upper = OString.create(Ops.UPPER, this);
        }
        return upper;
    }


    
}