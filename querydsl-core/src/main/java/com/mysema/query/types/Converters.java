package com.mysema.query.types;

import org.apache.commons.collections15.Transformer;

import com.mysema.query.types.expr.EString;
import com.mysema.query.types.expr.EStringEscape;

public final class Converters {
    
    private Converters(){}
    
    public static final Transformer<EString,EString> toLowerCase = new Transformer<EString,EString>(){
        @Override
        public EString transform(EString arg) {
            return arg.toLowerCase();
        }
    };

    public static final Transformer<EString,EString> toUpperCase = new Transformer<EString,EString>(){
        @Override
        public EString transform(EString arg) {
            return arg.toUpperCase();
        }
    };

    public static final Transformer<EString,EString> toStartsWithViaLike = new Transformer<EString,EString>(){
        @Override
        public EString transform(EString arg) {
            return EStringEscape.escapeForLike(arg).append("%");
        }
    };

    public static final Transformer<EString,EString> toStartsWithViaLikeLower = new Transformer<EString,EString>(){
        @Override
        public EString transform(EString arg) {
            return EStringEscape.escapeForLike(arg).append("%").lower();
        }
    };

    public static final Transformer<EString,EString> toEndsWithViaLike = new Transformer<EString,EString>(){
        @Override
        public EString transform(EString arg) {
            return EStringEscape.escapeForLike(arg).prepend("%");
        }
    };

    public static final Transformer<EString,EString> toEndsWithViaLikeLower = new Transformer<EString,EString>(){
        @Override
        public EString transform(EString arg) {
            return EStringEscape.escapeForLike(arg).prepend("%").lower();
        }
    };

    public static final Transformer<EString,EString> toContainsViaLike = new Transformer<EString,EString>(){
        @Override
        public EString transform(EString arg) {
            return EStringEscape.escapeForLike(arg).prepend("%").append("%");
        }
    };

    public static final Transformer<EString,EString> toContainsViaLikeLower = new Transformer<EString,EString>(){
        @Override
        public EString transform(EString arg) {
            return EStringEscape.escapeForLike(arg).prepend("%").append("%").lower();
        }
    };

}
