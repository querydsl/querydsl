/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import com.mysema.query.types.operation.OBoolean;
import com.mysema.query.types.operation.OComparable;
import com.mysema.query.types.operation.ONumber;
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
     * @return
     */
    public final EString add(Expr<String> str) {
        return concat(str);
    }

    /**
     * @param str
     * @return
     */
    public final EString add(String str) {
        return concat(str);
    }

    /**
     * @param i
     * @return
     */
    public final Expr<Character> charAt(Expr<Integer> i) {
        return OComparable.create(Character.class, Ops.CHAR_AT, this, i);
    }

    /**
     * @param i
     * @return
     */
    public final Expr<Character> charAt(int i) {
        return charAt(EConstant.create(i));
    }

    /**
     * @param str
     * @return
     */
    public final EString concat(Expr<String> str) {
        return new OString(Ops.CONCAT, this, str);
    }

    /**
     * @param str
     * @return
     */
    public final EString concat(String str) {
        return concat(EConstant.create(str));
    }

    /**
     * @param str
     * @return
     */
    public final EBoolean contains(Expr<String> str) {
        return new OBoolean(Ops.STRING_CONTAINS, this, str);
    }

    /**
     * @param str
     * @return
     */
    public final EBoolean contains(String str) {
        return contains(EConstant.create(str));
    }

    /**
     * @param str
     * @return
     */
    public final EBoolean endsWith(Expr<String> str) {
        return new OBoolean(Ops.ENDSWITH, this, str);
    }

    /**
     * @param str
     * @param caseSensitive
     * @return
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
     * @return
     */
    public final EBoolean endsWith(String str) {
        return endsWith(EConstant.create(str));
    }

    /**
     * @param str
     * @param caseSensitive
     * @return
     */
    public final EBoolean endsWith(String str, boolean caseSensitive) {
        return endsWith(EConstant.create(str), caseSensitive);
    }

    /**
     * @param str
     * @return
     */
    public final EBoolean equalsIgnoreCase(Expr<String> str) {
        return new OBoolean(Ops.EQ_IGNORECASE, this, str);
    }

    /**
     * @param str
     * @return
     */
    public final EBoolean equalsIgnoreCase(String str) {
        return equalsIgnoreCase(EConstant.create(str));
    }

    /**
     * @param str
     * @return
     */
    public final ENumber<Integer> indexOf(Expr<String> str) {
        return ONumber.create(Integer.class, Ops.INDEXOF, this, str);
    }

    /**
     * @param str
     * @return
     */
    public final ENumber<Integer> indexOf(String str) {
        return indexOf(EConstant.create(str));
    }

    /**
     * @param str
     * @param i
     * @return
     */
    public final ENumber<Integer> indexOf(String str, int i) {
        return ONumber.create(Integer.class, Ops.INDEXOF_2ARGS, this, EConstant.create(str), EConstant.create(i));
    }

    /**
     * @return
     * 
     */
    public final EBoolean isEmpty(){
        return new OBoolean(Ops.STRING_ISEMPTY, this);
    }
    
    /**
     * @return
     */
    public final EBoolean isNotEmpty(){
        return isEmpty().not();
    }
    
    /**
     * @return
     */
    public final ENumber<Long> length() {
        if (length == null) {
            length = ONumber.create(Long.class, Ops.STRING_LENGTH, this);
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
            lower = new OString(Ops.LOWER, this);
        }
        return lower;
    }

    /**
     * @param regex
     * @return
     */
    public final EBoolean matches(String regex){
        return matches(EConstant.create(regex));
    }
    
    /**
     * @param regex
     * @return
     */
    public final EBoolean matches(Expr<String> regex){
        return new OBoolean(Ops.MATCHES, this, regex);
    }
    
    /**
     * @param str
     * @return
     */
    public final EBoolean startsWith(Expr<String> str) {
        return new OBoolean(Ops.STARTSWITH, this, str);
    }

    /**
     * @param str
     * @param caseSensitive
     * @return
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
     * @return
     */
    public final EBoolean startsWith(String str) {
        return startsWith(EConstant.create(str));
    }

    /**
     * @param str
     * @param caseSensitive
     * @return
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
     * @return
     */
    public final EString substring(int beginIndex) {
        return new OString(Ops.SUBSTR1ARG, this, EConstant.create(beginIndex));
    }

    /**
     * @param beginIndex
     * @param endIndex
     * @return
     */
    public final EString substring(int beginIndex, int endIndex) {
        return new OString(Ops.SUBSTR2ARGS, this, EConstant.create(beginIndex), EConstant.create(endIndex));
    }

    /**
     * @return
     */
    public final EString trim() {
        if (trim == null) {
            trim = new OString(Ops.TRIM, this);
        }
        return trim;
    }

    /**
     * @return
     */
    public final EString upper() {
        if (upper == null) {
            upper = new OString(Ops.UPPER, this);
        }
        return upper;
    }
    
}