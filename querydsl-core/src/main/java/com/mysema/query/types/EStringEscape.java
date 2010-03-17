/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types;

import com.mysema.query.types.expr.Constant;
import com.mysema.query.types.expr.EString;
import com.mysema.query.types.expr.EStringConst;

// TODO : move somewhere else
public final class EStringEscape {
    
    private EStringEscape(){}

    @SuppressWarnings("unchecked")
    public static EString escapeForLike(EString expr){
        if (expr instanceof Constant){
            String str = ((Constant<String>) expr).getConstant();
            if (str.contains("%") || str.contains("_")){
                str = str.replace("%", "\\%").replace("_", "\\_");
                return EStringConst.create(str);
            }                
        }        
        return expr;
    }
    
}
