package com.mysema.query.types;


/**
 * @author tiwe
 *
 */
public final class ExtractorVisitor implements Visitor<Expr<?>,Void>{
    
    public static final ExtractorVisitor DEFAULT = new ExtractorVisitor();
    
    private ExtractorVisitor(){}
    
    @Override
    public Expr<?> visit(Constant<?> expr, Void context) {
        return expr.asExpr();
    }

    @Override
    public Expr<?> visit(Custom<?> expr, Void context) {
        return expr.asExpr();
    }

    @Override
    public Expr<?> visit(FactoryExpression<?> expr, Void context) {
        return expr.asExpr();
    }

    @Override
    public Expr<?> visit(Operation<?> expr, Void context) {
        return expr.asExpr();
    }

    @Override
    public Expr<?> visit(Path<?> expr, Void context) {
        return expr.asExpr();
    }

    @Override
    public Expr<?> visit(SubQueryExpression<?> expr, Void context) {
        return expr.asExpr();
    }

    @Override
    public Expr<?> visit(Param<?> expr, Void context) {
        return expr.asExpr();
    }
    
}
