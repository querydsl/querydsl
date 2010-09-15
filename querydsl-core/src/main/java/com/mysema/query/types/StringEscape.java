/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;


// TODO : move somewhere else
public final class StringEscape {

    private StringEscape(){}

    public static String escapeForLike(Constant<String> expr){
        String str = expr.getConstant();
        if (str.contains("%") || str.contains("_")){
            str = str.replace("%", "\\%").replace("_", "\\_");
        }
        return str;
    }

}
