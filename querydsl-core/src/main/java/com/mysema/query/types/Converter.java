/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import com.mysema.query.types.expr.EString;
import com.mysema.query.types.expr.EStringEscape;

/**
 * @author tiwe
 *
 * @param <D>
 */
interface Converter<S extends Expr<?>, T extends Expr<?>>{

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

    Converter<EString,EString> toStartsWithViaLikeLower = new Converter<EString,EString>(){
        @Override
        public EString convert(EString arg) {
            return EStringEscape.escapeForLike(arg).append("%").lower();
        }
    };

    Converter<EString,EString> toEndsWithViaLike = new Converter<EString,EString>(){
        @Override
        public EString convert(EString arg) {
            return EStringEscape.escapeForLike(arg).prepend("%");
        }
    };

    Converter<EString,EString> toEndsWithViaLikeLower = new Converter<EString,EString>(){
        @Override
        public EString convert(EString arg) {
            return EStringEscape.escapeForLike(arg).prepend("%").lower();
        }
    };

    Converter<EString,EString> toContainsViaLike = new Converter<EString,EString>(){
        @Override
        public EString convert(EString arg) {
            return EStringEscape.escapeForLike(arg).prepend("%").append("%");
        }
    };

    Converter<EString,EString> toContainsViaLikeLower = new Converter<EString,EString>(){
        @Override
        public EString convert(EString arg) {
            return EStringEscape.escapeForLike(arg).prepend("%").append("%").lower();
        }
    };

    /**
     * @param arg
     * @return
     */
    T convert(S arg);
}
