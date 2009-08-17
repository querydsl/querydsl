/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import com.mysema.query.annotations.Optional;
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
public abstract class EString extends EComparable<String> {
    
    private EString lower, trim, upper;
    
    private ENumber<Long> length;

    public EString() {
        super(String.class);
    }

    /**
     * @param str
     * @return this + str
     */
    public final EString add(Expr<String> str) {
        return concat(str);
    }

    /**
     * @param str
     * @return this + str
     */
    public final EString add(String str) {
        return concat(str);
    }

    /**
     * @param i
     * @return this.charAt(i)
     * @see java.lang.String#charAt(int)
     */
    public final Expr<Character> charAt(Expr<Integer> i) {
        return OComparable.create(Character.class, Ops.CHAR_AT, this, i);
    }

    /**
     * @param i
     * @return this.charAt(i)
     * @see java.lang.String#charAt(int)
     */
    public final Expr<Character> charAt(int i) {
        return charAt(EConstant.create(i));
    }

    /**
     * @param str
     * @return this + str
     */
    public final EString concat(Expr<String> str) {
        return new OString(Ops.CONCAT, this, str);
    }

    /**
     * @param str
     * @return this + str
     */
    public final EString concat(String str) {
        return concat(EConstant.create(str));
    }

    /**
     * @param str
     * @return this.contains(str)
     * @see java.lang.String#contains(CharSequence)
     */
    public final EBoolean contains(Expr<String> str) {
        return new OBoolean(Ops.STRING_CONTAINS, this, str);
    }

    /**
     * @param str
     * @return this.contains(str)
     * @see java.lang.String#contains(CharSequence)
     */
    public final EBoolean contains(String str) {
        return contains(EConstant.create(str));
    }

    /**
     * @param str
     * @return this.endsWith(str)
     * @see java.lang.String#endsWith(String)
     */
    public final EBoolean endsWith(Expr<String> str) {
        return new OBoolean(Ops.ENDSWITH, this, str);
    }

    /**
     * @param str
     * @param caseSensitive
     * @return
     * @see java.lang.String#endsWith(String)
     */
    public final EBoolean endsWith(Expr<String> str, boolean caseSensitive) {
        if (caseSensitive){
            return endsWith(str);
        }else{
            return new OBoolean(Ops.ENDSWITH_IC, this, str);
        }        
    }

    /**
     * @param str
     * @return this.endsWith(str)
     * @see java.lang.String#endsWith(String)
     */
    public final EBoolean endsWith(String str) {
        return endsWith(EConstant.create(str));
    }

    /**
     * @param str
     * @param caseSensitive
     * @return
     * @see java.lang.String#endsWith(String)
     */
    public final EBoolean endsWith(String str, boolean caseSensitive) {
        return endsWith(EConstant.create(str), caseSensitive);
    }

    /**
     * @param str
     * @return this.equalsIgnoreCase(str)
     * @see java.lang.String#equalsIgnoreCase(String)
     */
    public final EBoolean equalsIgnoreCase(Expr<String> str) {
        return new OBoolean(Ops.EQ_IGNORECASE, this, str);
    }

    /**
     * @param str
     * @return this.equalsIgnoreCase(str)
     * @see java.lang.String#equalsIgnoreCase(String)
     */
    public final EBoolean equalsIgnoreCase(String str) {
        return equalsIgnoreCase(EConstant.create(str));
    }

    /**
     * @param str
     * @return this.indexOf(str)
     * @see java.lang.String#indexOf(String)
     */
    public final ENumber<Integer> indexOf(Expr<String> str) {
        return ONumber.create(Integer.class, Ops.INDEXOF, this, str);
    }

    /**
     * @param str
     * @return this.indexOf(str)
     * @see java.lang.String#indexOf(String)
     */
    public final ENumber<Integer> indexOf(String str) {
        return indexOf(EConstant.create(str));
    }

    /**
     * @param str
     * @param i
     * @return this.indexOf(str, i)
     * @see java.lang.String#indexOf(String, int)
     */
    public final ENumber<Integer> indexOf(String str, int i) {
        return ONumber.create(Integer.class, Ops.INDEXOF_2ARGS, this, EConstant.create(str), EConstant.create(i));
    }

    /**
     * @return this.isEmpty()
     * @see java.lang.String#isEmpty()
     */
    public final EBoolean isEmpty(){
        return new OBoolean(Ops.STRING_ISEMPTY, this);
    }
    
    /**
     * @return !this.isEmpty()
     * @see java.lang.String#isEmpty()
     */
    public final EBoolean isNotEmpty(){
        return isEmpty().not();
    }
    
    /**
     * Expr : <code>this.lastIndexOf(right, third);</code>
     * 
     * @param right
     * @param third
     * @return this.lastIndexOf(right, third)
     * @see java.lang.String#lastIndexOf(String, int)
     */
    @Optional
    public ENumber<Integer> lastIndex(String right, int third) {
        return ONumber.create(Integer.class, Ops.StringOps.LAST_INDEX_2ARGS, this, EConstant.create(right), EConstant.create(third));
    }

    /**
     * Expr : <code>this.lastIndexOf(right)</code>
     * 
     * @param right
     * @return this.lastIndexOf(right)
     * @see java.lang.String#lastIndexOf(String)
     */
    @Optional
    public ENumber<Integer> lastIndexOf(Expr<String> right) {
        return ONumber.create(Integer.class, Ops.StringOps.LAST_INDEX, this, right);
    }
    

    /**
     * Expr : <code>this.lastIndexOf(right)</code>
     * 
     * @param right
     * @return this.lastIndexOf(right)
     * @see java.lang.String#lastIndexOf(String)
     */
    @Optional
    public ENumber<Integer> lastIndexOf(String right) {
        return ONumber.create(Integer.class, Ops.StringOps.LAST_INDEX, this, EConstant.create(right));
    }

    /**
     * @return this.length()
     * @see java.lang.String#length()
     */
    public final ENumber<Long> length() {
        if (length == null) {
            length = ONumber.create(Long.class, Ops.STRING_LENGTH, this);
        }
        return length;
    }

    /**
     * 
     * @return this.toLowerCase()
     * @see java.lang.String#toLowerCase()
     */
    public final EString lower() {
        if (lower == null) {
            lower = new OString(Ops.LOWER, this);
        }
        return lower;
    }

    /**
     * @param regex
     * @return this.matches(right)
     * @see java.lang.String#matches(String)
     */
    public final EBoolean matches(Expr<String> regex){
        return new OBoolean(Ops.MATCHES, this, regex);
    }
    
    /**
     * @param regex
     * @return this.matches(regex)
     * @see java.lang.String#matches(String)
     */
    public final EBoolean matches(String regex){
        return matches(EConstant.create(regex));
    }
    
    /**
     * Split the given String with regex as the matcher for the separator
     * 
     * @param regex
     * @return this.split(regex)
     * @see java.lang.String#split(String)
     */
    @Optional
    public Expr<String[]> split(String regex) {
        return OSimple.create(String[].class, Ops.StringOps.SPLIT, this, EConstant.create(regex));
    }
    
    /**
     * @param str
     * @return this.startsWith(str)
     * @see java.lang.String#startsWith(String)
     */
    public final EBoolean startsWith(Expr<String> str) {
        return new OBoolean(Ops.STARTSWITH, this, str);
    }

    /**
     * @param str
     * @param caseSensitive
     * @return
     * @see java.lang.String#startsWith(String)
     */
    public final EBoolean startsWith(Expr<String> str, boolean caseSensitive) {
        if (caseSensitive){
            return startsWith(str);
        }else{
            return new OBoolean(Ops.STARTSWITH_IC, this, str);
        }  
    }

    /**
     * @param str
     * @return this.startsWith(str)
     * @see java.lang.String#startsWith(String)
     */
    public final EBoolean startsWith(String str) {
        return startsWith(EConstant.create(str));
    }

    /**
     * @param str
     * @param caseSensitive
     * @return
     * @see java.lang.String#startsWith(String)
     */
    public final EBoolean startsWith(String str, boolean caseSensitive) {
        return startsWith(EConstant.create(str), caseSensitive);
    }

    /* (non-Javadoc)
     * @see com.mysema.query.types.expr.EComparable#stringValue()
     */
    public final EString stringValue() {
        return this;
    }

    /**
     * @param beginIndex
     * @return this.substring(beginIndex)
     * @see java.lang.String#substring(int)
     */
    public final EString substring(int beginIndex) {
        return new OString(Ops.SUBSTR1ARG, this, EConstant.create(beginIndex));
    }

    /**
     * @param beginIndex
     * @param endIndex
     * @return this.substring(beginIndex, endIndex)
     * @see java.lang.String#substring(int, int)
     */
    public final EString substring(int beginIndex, int endIndex) {
        return new OString(Ops.SUBSTR2ARGS, this, EConstant.create(beginIndex), EConstant.create(endIndex));
    }

    /**
     * 
     * @return this.toLowerCase()
     * @see java.lang.String#toLowerCase()
     */
    public final EString toLowerCase() {
        return lower();
    }

    /**
     * 
     * @return
     * @see java.lang.String#toUpperCase()
     */
    public final EString toUpperCase() {
        return upper();
    }
    
    /**
     * @return
     * @see java.lang.String#trim()
     */
    public final EString trim() {
        if (trim == null) {
            trim = new OString(Ops.TRIM, this);
        }
        return trim;
    }
    
    /**
     * @return
     * @see java.lang.String#toUpperCase()
     */
    public final EString upper() {
        if (upper == null) {
            upper = new OString(Ops.UPPER, this);
        }
        return upper;
    }
    
}