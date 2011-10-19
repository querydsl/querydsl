package com.mysema.query.types;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

import com.mysema.query.JoinExpression;
import com.mysema.query.QueryMetadata;

public class ValidatingVisitor implements Visitor<Void, Void>, Serializable{

    private static final long serialVersionUID = 691350069621050872L;

    private Collection<Expression<?>> known;
    
    public ValidatingVisitor(Collection<Expression<?>> known) {
        this.known = known;
    }
    
    @Override
    public Void visit(Constant<?> expr, Void context) {
        return null;
    }

    @Override
    public Void visit(FactoryExpression<?> expr, Void context) {
        visit(expr.getArgs());
        return null;
    }

    @Override
    public Void visit(Operation<?> expr, Void context) {
        if (expr.getOperator() == Ops.ALIAS){
            known.add(expr.getArg(1));
        }
        visit(expr.getArgs());
        return null;
    }

    @Override
    public Void visit(ParamExpression<?> expr, Void context) {
        return null;
    }

    @Override
    public Void visit(Path<?> expr, Void context) {
        if (!known.contains(expr.getRoot())){
            throw new IllegalArgumentException("Undeclared path '" + expr.getRoot() + "'. " +
            	"Add this path as a source to the query to be able to reference it.");
        }
        if (expr.getMetadata().getParent() != null){
            expr.getMetadata().getParent().accept(this, null);
        }
        return null;
    }

    @Override
    public Void visit(SubQueryExpression<?> expr, Void context) {
        Collection<Expression<?>> k = known;
        known = new HashSet<Expression<?>>(known);
        QueryMetadata md = expr.getMetadata();
        visitJoins(md.getJoins());
        visitOrder(md.getOrderBy());
        visit(md.getProjection());
        visit(md.getGroupBy());
        if (md.getHaving() != null) {
            md.getHaving().accept(this, null);
        }
        if (md.getWhere() != null) {
            md.getWhere().accept(this, null);
        }
        known = k;
        return null;
    }


    @Override
    public Void visit(TemplateExpression<?> expr, Void context) {
        visit(expr.getArgs());
        return null;
    }
    

    private void visitJoins(Iterable<JoinExpression> joins) {
        for (JoinExpression j : joins) {
            known.add(j.getTarget());
            j.getTarget().accept(this, null);
            if (j.getCondition() != null) {
                j.getCondition().accept(this, null);
            }
        }
    }

    private void visitOrder(Iterable<OrderSpecifier<?>> order) {
        for (OrderSpecifier<?> o : order) { 
            o.getTarget().accept(this, null);
        }        
    }
    
    private void visit(Iterable<? extends Expression<?>> exprs){
        for (Expression<?> e : exprs) {
            e.accept(this, null);
        }
    }

}
