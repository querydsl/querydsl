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
import com.mysema.query.types.expr.EDate;

/**
 * @author tiwe
 *
 * @param <T>
 */
public class CDate<T extends Comparable<?>> extends EDate<T> implements Custom<T> {

    private static final long serialVersionUID = 4975559746071238026L;

    public static <T extends Comparable<?>> EDate<T> create(Class<T> type, String template, Expr<?>... args){
        return new CDate<T>(type, TemplateFactory.DEFAULT.create(template), Arrays.<Expr<?>>asList(args));
    }

    public static <T extends Comparable<?>> EDate<T> create(Class<T> type, Template template, Expr<?>... args){
        return new CDate<T>(type, template, Arrays.<Expr<?>>asList(args));
    }

    private final Custom<T> customMixin;

    public CDate(Class<T> type, Template template, List<Expr<?>> args) {
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
