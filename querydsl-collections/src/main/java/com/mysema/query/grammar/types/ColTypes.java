/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar.types;


/**
 * ColTypes provides
 *
 * @author tiwe
 * @version $Id$
 */
/**
 * ColTypes provides
 *
 * @author tiwe
 * @version $Id$
 *
 */
public class ColTypes {
    
    private ColTypes(){}
    
    public static class ExtString extends Path.PString{

        public ExtString(PathMetadata<?> arg0) {
            super(arg0);
        }
        public Expr<Character> charAt(Expr<Integer> i) {
            return IntGrammar.charAt(this, i);
        }
        public Expr<Character> charAt(int i) {
            return IntGrammar.charAt(this, i);
        }
        public EBoolean contains(Expr<String> str) {
            return IntGrammar.contains(this, str);
        }
        public EBoolean contains(String str) {
            return IntGrammar.contains(this, str);
        }
        public EBoolean endsWith(Expr<String> str) {
            return IntGrammar.endsWith(this, str);
        }
        public EBoolean endsWith(String str) {
            return IntGrammar.endsWith(this, str);
        }
        public EBoolean equalsIgnoreCase(Expr<String> str) {
            return IntGrammar.equalsIgnoreCase(this, str);
        }
        public EBoolean equalsIgnoreCase(String str) {
            return IntGrammar.equalsIgnoreCase(this, str);
        }
        public EComparable<Integer> indexOf(Expr<String> str) {
            return IntGrammar.indexOf(this, str);
        }
        public EComparable<Integer> indexOf(String str) {
            return IntGrammar.indexOf(this, str);
        }
        public EComparable<Integer> indexOf(String str, int i) {
            return IntGrammar.indexOf(this, str, i);
        }
        public EBoolean isEmpty() {
            return IntGrammar.isEmpty(this);
        }
        public EComparable<Integer> lastIndex(String str, int i) {
            return IntGrammar.lastIndex(this, str, i);
        }
        public EComparable<Integer> lastIndexOf(String str) {
            return IntGrammar.lastIndexOf(this, str);
        }
        public EComparable<Integer> length() {
            return IntGrammar.length(this);
        }
        public EBoolean matches(Expr<String> str) {
            return IntGrammar.matches(this, str);
        }
        public EBoolean matches(String str) {
            return IntGrammar.matches(this, str);
        }
        public Expr<String[]> split(String regex) { 
            return IntGrammar.split(this, regex);
        }
        public EBoolean startsWith(Expr<String> str) {
            return IntGrammar.startsWith(this, str);
        }
        public EBoolean startsWith(String str) {
            return IntGrammar.startsWith(this, str);
        }
        public EString toLowerCase(){ return lower(); }
        public EString toUpperCase(){ return upper(); }
    }
    
}
