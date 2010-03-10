/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types;

import com.mysema.query.types.expr.EString;
import com.mysema.query.types.expr.Expr;

/**
 * @author tiwe
 *
 * @param <D>
 */
interface Converter<Source extends Expr<?>, Target extends Expr<?>>{
    
    Converter<EString,EString> toLowerCase = new Converter<EString,EString>(){
        @Override
        public EString convert(EString arg) {
            return arg.toLowerCase();
        } 
    };
    
    Converter<EString,EString> toUpperCase = new Converter<EString,EString>(){
        @Override
        public EString convert(EString arg) {
            return arg.toUpperCase();
        } 
    };
    
    Converter<EString,EString> toStartsWithViaLike = new Converter<EString,EString>(){
        @Override
        public EString convert(EString arg) {
            return EStringEscape.escapeForLike(arg).append("%");
        } 
    };
    
    Converter<EString,EString> toEndsWithViaLike = new Converter<EString,EString>(){
        @Override
        public EString convert(EString arg) {
            return EStringEscape.escapeForLike(arg).prepend("%");
        } 
    };
    
    Converter<EString,EString> toContainsViaLike = new Converter<EString,EString>(){
        @Override
        public EString convert(EString arg) {
            return EStringEscape.escapeForLike(arg).prepend("%").append("%");
        }
    };
    

    
    /**
     * @param arg
     * @return
     */
    Target convert(Source arg); 
}
