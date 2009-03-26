/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar.types;

import com.mysema.query.grammar.Grammar;


/**
 * ExtTypes provides extended versions of the basic types
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
        public EBoolean isEmpty() {
            return Grammar.isEmpty(this);
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
        public EString toLowerCase(){ return lower(); }
        public EString toUpperCase(){ return upper(); }
    }
        
}
