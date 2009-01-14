package com.mysema.query.sql.dto;

import com.mysema.query.grammar.types.Constructor;
import com.mysema.query.grammar.types.Expr;

public class QIdName extends Constructor<IdName>{
    public QIdName(Expr<java.lang.Integer> id, Expr<java.lang.String> name){
        super(IdName.class,id,name);
    }
}