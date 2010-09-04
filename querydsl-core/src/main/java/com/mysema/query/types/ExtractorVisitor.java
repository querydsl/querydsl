package com.mysema.query.types;

import com.mysema.query.types.Constant;
import com.mysema.query.types.Custom;
import com.mysema.query.types.Expr;
import com.mysema.query.types.FactoryExpression;
import com.mysema.query.types.Operation;
import com.mysema.query.types.Param;
import com.mysema.query.types.Path;
import com.mysema.query.types.SubQueryExpression;
import com.mysema.query.types.Visitor;

/**
 * @author tiwe
 *
 */
public class ExtractorVisitor implements Visitor{

    private Expr<?> expr;
    
    public ExtractorVisitor(Expr<?> e) {
        e.accept(this);
    }
    
    @Override
    public void visit(Constant<?> expr) {
        this.expr = expr.asExpr();
    }

    @Override
    public void visit(Custom<?> expr) {
        this.expr = expr.asExpr();
    }

    @Override
    public void visit(FactoryExpression<?> expr) {
        this.expr = expr.asExpr();
    }

    @Override
    public void visit(Operation<?> expr) {
        this.expr = expr.asExpr();
    }

    @Override
    public void visit(Path<?> expr) {
        this.expr = expr.asExpr();
    }

    @Override
    public void visit(SubQueryExpression<?> expr) {
        this.expr = expr.asExpr();
    }

    @Override
    public void visit(Param<?> expr) {
        this.expr = expr.asExpr();
    }

    public Expr<?> getExpr() {
        return expr;
    }
    
}
