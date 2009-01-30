/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar.types;

import com.mysema.query.grammar.Grammar;


/**
 * ColTypes provides
 *
 * @author tiwe
 * @version $Id$
 *
 */
public class ExtTypes {
    
    private ExtTypes(){}
    
    public static class ExtString extends Path.PString{

        public ExtString(PathMetadata<?> arg0) {
            super(arg0);
        }
        public Expr<Character> charAt(Expr<Integer> i) {
            return Grammar.charAt(this, i);
        }
        public Expr<Character> charAt(int i) {
            return Grammar.charAt(this, i);
        }
        public EBoolean contains(Expr<String> str) {
            return Grammar.contains(this, str);
        }
        public EBoolean contains(String str) {
            return Grammar.contains(this, str);
        }
        public EBoolean endsWith(Expr<String> str) {
            return Grammar.endsWith(this, str);
        }
        public EBoolean endsWith(String str) {
            return Grammar.endsWith(this, str);
        }
        public EBoolean equalsIgnoreCase(Expr<String> str) {
            return Grammar.equalsIgnoreCase(this, str);
        }
        public EBoolean equalsIgnoreCase(String str) {
            return Grammar.equalsIgnoreCase(this, str);
        }
        public EComparable<Integer> indexOf(Expr<String> str) {
            return Grammar.indexOf(this, str);
        }
        public EComparable<Integer> indexOf(String str) {
            return Grammar.indexOf(this, str);
        }
        public EComparable<Integer> indexOf(String str, int i) {
            return Grammar.indexOf(this, str, i);
        }
        public EBoolean isEmpty() {
            return Grammar.isEmpty(this);
        }
        public EComparable<Integer> lastIndex(String str, int i) {
            return Grammar.lastIndex(this, str, i);
        }
        public EComparable<Integer> lastIndexOf(String str) {
            return Grammar.lastIndexOf(this, str);
        }
        public EComparable<Integer> length() {
            return Grammar.length(this);
        }
        public EBoolean matches(Expr<String> str) {
            return Grammar.matches(this, str);
        }
        public EBoolean matches(String str) {
            return Grammar.matches(this, str);
        }
        public Expr<String[]> split(String regex) { 
            return Grammar.split(this, regex);
        }
        public EBoolean startsWith(Expr<String> str) {
            return Grammar.startsWith(this, str);
        }
        public EBoolean startsWith(String str) {
            return Grammar.startsWith(this, str);
        }
        public EString toLowerCase(){ return lower(); }
        public EString toUpperCase(){ return upper(); }
    }
        
}
