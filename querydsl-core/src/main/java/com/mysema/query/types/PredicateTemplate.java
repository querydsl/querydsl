/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import java.util.List;

import javax.annotation.Nullable;

/**
 * PredicateTemplate provides a Boolean typed TemplateExpression implementation 
 * 
 * @author tiwe
 *
 */
public class PredicateTemplate extends TemplateExpressionImpl<Boolean> implements Predicate{
    
    private static final long serialVersionUID = -5371430939203772072L;

    @Nullable
    private Predicate not;
    
    public PredicateTemplate(Template template, Expression<?>... args){
        super(Boolean.class, template, args);
    }

    public PredicateTemplate(Template template, List<Expression<?>> args){
        super(Boolean.class, template, args);
    }
    
    @Override
    public Predicate not() {
        if (not == null){
            not = new PredicateOperation(Ops.NOT, this);
        }
        return not;
    }

}
