/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;


/**
 * ExtractorVisitor is a Visitor implementation for Expression extraction
 * 
 * @author tiwe
 *
 */
public final class ExtractorVisitor implements Visitor<Expression<?>,Void>{
    
    public static final ExtractorVisitor DEFAULT = new ExtractorVisitor();
    
    private ExtractorVisitor(){}
    
    @Override
    public Expression<?> visit(Constant<?> expr, Void context) {
        return expr;
    }

    @Override
    public Expression<?> visit(FactoryExpression<?> expr, Void context) {
        return expr;
    }

    @Override
    public Expression<?> visit(Operation<?> expr, Void context) {
        return expr;
    }

    @Override
    public Expression<?> visit(ParamExpression<?> expr, Void context) {
        return expr;
    }

    @Override
    public Expression<?> visit(Path<?> expr, Void context) {
        return expr;
    }

    @Override
    public Expression<?> visit(SubQueryExpression<?> expr, Void context) {
        return expr;
    }

    @Override
    public Expression<?> visit(TemplateExpression<?> expr, Void context) {
        return expr;
    }
    
}
