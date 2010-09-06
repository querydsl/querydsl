/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.custom;

import java.util.Arrays;
import java.util.List;

import com.mysema.query.types.Custom;
import com.mysema.query.types.Expr;
import com.mysema.query.types.Template;
import com.mysema.query.types.TemplateFactory;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.EDateTime;

/**
 * @author tiwe
 *
 * @param <T>
 */
public class CDateTime<T extends Comparable<?>> extends EDateTime<T> implements Custom<T> {

    private static final long serialVersionUID = -2289699666347576749L;

    public static <T extends Comparable<?>> EDateTime<T> create(Class<T> type, String template, Expr<?>... args){
        return new CDateTime<T>(type, TemplateFactory.DEFAULT.create(template), Arrays.<Expr<?>>asList(args));
    }

    public static <T extends Comparable<?>> EDateTime<T> create(Class<T> type, Template template, Expr<?>... args){
        return new CDateTime<T>(type, template, Arrays.<Expr<?>>asList(args));
    }

    private final Custom<T> customMixin;

    public CDateTime(Class<T> type, Template template, List<Expr<?>> args) {
        super(type);
        customMixin = new CustomMixin<T>(this, args, template);
    }

    @Override
    public <R,C> R accept(Visitor<R,C> v, C context) {
        return v.visit(this, context);
    }

    @Override
    public Expr<?> getArg(int index) {
        return customMixin.getArg(index);
    }

    @Override
    public List<Expr<?>> getArgs() {
        return customMixin.getArgs();
    }

    @Override
    public Template getTemplate() {
        return customMixin.getTemplate();
    }

    @Override
    public boolean equals(Object o){
        return customMixin.equals(o);
    }

    @Override
    public int hashCode(){
        return getType().hashCode();
    }

}
