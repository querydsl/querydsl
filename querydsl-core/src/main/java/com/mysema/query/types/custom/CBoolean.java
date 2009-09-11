/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.custom;

import java.util.List;

import com.mysema.query.types.Template;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;

/**
 * CBoolean is a custom boolean expression
 * 
 * @author tiwe
 *
 */
@SuppressWarnings("serial")
public class CBoolean extends EBoolean implements Custom<Boolean> {
    
    public static EBoolean create(List<Expr<?>> args, Template template){
        return new CBoolean(args, template);
    }
    
    private final List<Expr<?>> args;
    
    private final Template template;
    
    public CBoolean(List<Expr<?>> args, Template template){
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