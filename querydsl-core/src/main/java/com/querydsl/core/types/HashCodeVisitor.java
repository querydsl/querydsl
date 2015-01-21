package com.querydsl.core.types;


/**
 * HashCodeVisitor is used for hashCode generation in {@link Expression} implementations.
 *
 * @author tiwe
 */
public final class HashCodeVisitor implements Visitor<Integer,Void> {

    public static final HashCodeVisitor DEFAULT = new HashCodeVisitor();

    private HashCodeVisitor() {}
    
    @Override
    public Integer visit(Constant<?> expr, Void context) {
        return expr.getConstant().hashCode();
    }

    @Override
    public Integer visit(FactoryExpression<?> expr, Void context) {
        int result = expr.getType().hashCode();
        return 31 * result + expr.getArgs().hashCode();
    }

    @Override
    public Integer visit(Operation<?> expr, Void context) {
        int result = expr.getOperator().hashCode();
        return 31 * result + expr.getArgs().hashCode();
    }

    @Override
    public Integer visit(ParamExpression<?> expr, Void context) {
        return expr.getName().hashCode();
    }

    @Override
    public Integer visit(Path<?> expr, Void context) {
        return expr.getMetadata().hashCode();
    }

    @Override
    public Integer visit(SubQueryExpression<?> expr, Void context) {
        return expr.getMetadata().hashCode();
    }

    @Override
    public Integer visit(TemplateExpression<?> expr, Void context) {
        int result = expr.getTemplate().hashCode();
        return 31 * result + expr.getArgs().hashCode();        
    }

}
