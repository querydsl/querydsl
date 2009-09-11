/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.custom;

import java.util.List;

import com.mysema.query.types.Template;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.EString;
import com.mysema.query.types.expr.Expr;

/**
 * CString defines custom String expressions
 * 
 * @author tiwe
 *
 */
@SuppressWarnings("serial")
public class CString extends EString implements Custom<String> {
    
    public static EString create(List<Expr<?>> args, Template template){
        return new CString(args, template);
    }
    
    private final List<Expr<?>> args;
    
    private final Template template;
    
    public CString(List<Expr<?>> args, Template template){
        this.args = args;
        this.template = template;
    }
    
    @Override
    public void accept(Visitor v){
        v.visit(this);
    }
    
    @Override
    public Expr<?> getArg(int index) {
        return getArgs().get(index);
    }

    @Override
    public List<Expr<?>> getArgs() {
        return args;
    }
    
    @Override
    public Template getTemplate() {
        return template;
    }
}