package com.mysema.query.types;

import com.google.common.base.Objects;

/**
 * HashCodeVisitor is used for hashCode generation in {@link Expression} implementations.
 *
 * @author tiwe
 */
public final class HashCodeVisitor implements Visitor<Integer,Void> {

    public static final HashCodeVisitor DEFAULT = new HashCodeVisitor();

    private HashCodeVisitor(){}
    
    @Override
    public Integer visit(Constant<?> expr, Void context) {
        return Objects.hashCode(expr.getConstant());
    }

    @Override
    public Integer visit(FactoryExpression<?> expr, Void context) {
        return Objects.hashCode(expr.getType(), expr.getArgs());
    }

    @Override
    public Integer visit(Operation<?> expr, Void context) {
        return Objects.hashCode(expr.getOperator(), expr.getArgs());
    }

    @Override
    public Integer visit(ParamExpression<?> expr, Void context) {
        return Objects.hashCode(expr.getName());
    }

    @Override
    public Integer visit(Path<?> expr, Void context) {
        PathMetadata<?> md = expr.getMetadata();
        return Objects.hashCode(md.getPathType(), md.getParent(), md.getExpression());
    }

    @Override
    public Integer visit(SubQueryExpression<?> expr, Void context) {
        return Objects.hashCode(expr.getMetadata());
    }

    @Override
    public Integer visit(TemplateExpression<?> expr, Void context) {
        return Objects.hashCode(expr.getTemplate(), expr.getArgs());
    }

}
