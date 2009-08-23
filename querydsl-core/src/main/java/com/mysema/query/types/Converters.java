/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types;

import com.mysema.query.types.expr.EString;

/**
 * @author tiwe
 *
 */
final class Converters {
    
    private Converters(){}

    static final Converter<EString,EString> toLowerCase = new Converter<EString,EString>(){
        @Override
        public EString convert(EString arg) {
            return arg.toLowerCase();
        } 
    };

    static final Converter<EString,EString> toUpperCase = new Converter<EString,EString>(){
        @Override
        public EString convert(EString arg) {
            return arg.toUpperCase();
        } 
    };
}
