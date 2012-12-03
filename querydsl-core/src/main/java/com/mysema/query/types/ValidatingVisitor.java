/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mysema.query.types;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mysema.query.JoinExpression;
import com.mysema.query.QueryMetadata;

/**
 * ValidatingVisitor visits expressions and ensures that only known path instances are used
 * 
 * @author tiwe
 *
 */
public final class ValidatingVisitor implements Visitor<Void, Set<Expression<?>>>, Serializable{

    private static final long serialVersionUID = 691350069621050872L;
    
    public static final ValidatingVisitor DEFAULT = new ValidatingVisitor();
    
    @Override
    public Void visit(Constant<?> expr, Set<Expression<?>> known) {
        return null;
    }

    @Override
    public Void visit(FactoryExpression<?> expr, Set<Expression<?>> known) {
        for (Expression<?> arg : expr.getArgs()) {
            arg.accept(this, known);
        }
        return null;
    }

    @Override
    public Void visit(Operation<?> expr, Set<Expression<?>> known) {
        if (expr.getOperator() == Ops.ALIAS){
            known.add(expr.getArg(1));
        }
        for (Expression<?> arg : expr.getArgs()) {
            arg.accept(this, known);            
        }
        return null;
    }

    @Override
    public Void visit(ParamExpression<?> expr, Set<Expression<?>> known) {
        return null;
    }

    @Override
    public Void visit(Path<?> expr, Set<Expression<?>> known) {               
        PathMetadata<?> metadata = expr.getMetadata();
        if (metadata.getParent() != null) {
            metadata.getParent().accept(this, known);
        } else if (!known.contains(expr.getRoot())) {
            throw new IllegalArgumentException("Undeclared path '" + expr.getRoot() + "'. " +
                    "Add this path as a source to the query to be able to reference it.");
        }
        return null;
    }

    @Override
    public Void visit(SubQueryExpression<?> expr, Set<Expression<?>> known) {
        known = new HashSet<Expression<?>>(known);
        QueryMetadata md = expr.getMetadata();
        visitJoins(md.getJoins(), known);
        for (OrderSpecifier<?> o : md.getOrderBy()) { 
            o.getTarget().accept(this, known);
        }      
        for (Expression<?> p : md.getProjection()) {
            p.accept(this, known);
        }
        for (Expression<?> g : md.getGroupBy()) {
            g.accept(this, known);
        }
        if (md.getHaving() != null) {
            md.getHaving().accept(this, known);
        }
        if (md.getWhere() != null) {
            md.getWhere().accept(this, known);
        }
        return null;
    }


    @Override
    public Void visit(TemplateExpression<?> expr, Set<Expression<?>> known) {
        for (Object arg : expr.getArgs()) {
            if (arg instanceof Expression<?>) {
                ((Expression<?>)arg).accept(this, known);
            }
        }
        return null;
    }
    
    private void visitJoins(Iterable<JoinExpression> joins, Set<Expression<?>> known) {
        for (JoinExpression j : joins) {
            known.add(j.getTarget());
            j.getTarget().accept(this, known);
            if (j.getCondition() != null) {
                j.getCondition().accept(this, known);
            }
        }
    }

    private ValidatingVisitor() {}
}
