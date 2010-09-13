/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import org.apache.commons.collections15.Transformer;

import com.mysema.query.types.expr.StringExpression;
import com.mysema.query.types.expr.StringEscape;

/**
 * @author tiwe
 *
 */
public final class Converters {
    
    private Converters(){}
    
    public static final Transformer<StringExpression,StringExpression> toLowerCase = new Transformer<StringExpression,StringExpression>(){
        @Override
        public StringExpression transform(StringExpression arg) {
            return arg.toLowerCase();
        }
    };

    public static final Transformer<StringExpression,StringExpression> toUpperCase = new Transformer<StringExpression,StringExpression>(){
        @Override
        public StringExpression transform(StringExpression arg) {
            return arg.toUpperCase();
        }
    };

    public static final Transformer<StringExpression,StringExpression> toStartsWithViaLike = new Transformer<StringExpression,StringExpression>(){
        @Override
        public StringExpression transform(StringExpression arg) {
            return StringEscape.escapeForLike(arg).append("%");
        }
    };

    public static final Transformer<StringExpression,StringExpression> toStartsWithViaLikeLower = new Transformer<StringExpression,StringExpression>(){
        @Override
        public StringExpression transform(StringExpression arg) {
            return StringEscape.escapeForLike(arg).append("%").lower();
        }
    };

    public static final Transformer<StringExpression,StringExpression> toEndsWithViaLike = new Transformer<StringExpression,StringExpression>(){
        @Override
        public StringExpression transform(StringExpression arg) {
            return StringEscape.escapeForLike(arg).prepend("%");
        }
    };

    public static final Transformer<StringExpression,StringExpression> toEndsWithViaLikeLower = new Transformer<StringExpression,StringExpression>(){
        @Override
        public StringExpression transform(StringExpression arg) {
            return StringEscape.escapeForLike(arg).prepend("%").lower();
        }
    };

    public static final Transformer<StringExpression,StringExpression> toContainsViaLike = new Transformer<StringExpression,StringExpression>(){
        @Override
        public StringExpression transform(StringExpression arg) {
            return StringEscape.escapeForLike(arg).prepend("%").append("%");
        }
    };

    public static final Transformer<StringExpression,StringExpression> toContainsViaLikeLower = new Transformer<StringExpression,StringExpression>(){
        @Override
        public StringExpression transform(StringExpression arg) {
            return StringEscape.escapeForLike(arg).prepend("%").append("%").lower();
        }
    };

}
