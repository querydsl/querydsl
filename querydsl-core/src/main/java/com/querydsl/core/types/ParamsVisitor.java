/*
 * Copyright 2013, Mysema Ltd
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
package com.querydsl.core.types;

import java.util.List;
import java.util.Map;

import com.querydsl.core.JoinExpression;
import com.querydsl.core.QueryMetadata;

/**
 * Copies ParameterExpression bindings from subexpressions to QueryMetadata in the context
 * 
 * @author tiwe
 *
 */
public final class ParamsVisitor implements Visitor<Void, QueryMetadata> {
    
    public static final ParamsVisitor DEFAULT = new ParamsVisitor();
    
    private ParamsVisitor() {}

    @Override
    public Void visit(Constant<?> expr, QueryMetadata context) {
        return null;
    }

    @Override
    public Void visit(FactoryExpression<?> expr, QueryMetadata context) {
        visit(expr.getArgs(), context);
        return null;
    }

    @Override
    public Void visit(Operation<?> expr, QueryMetadata context) {
        visit(expr.getArgs(), context);
        return null;
    }

    @Override
    public Void visit(ParamExpression<?> expr, QueryMetadata context) {
        return null;
    }

    @Override
    public Void visit(Path<?> expr, QueryMetadata context) {
        return null;
    }

    @Override
    public Void visit(SubQueryExpression<?> expr, QueryMetadata context) {
        QueryMetadata md = expr.getMetadata();
        for (Map.Entry<ParamExpression<?>, Object> entry : md.getParams().entrySet()) {
            context.setParam((ParamExpression)entry.getKey(), entry.getValue());
        }
        visit(md.getGroupBy(), context);        
        visit(md.getHaving(), context);
        for (JoinExpression join : md.getJoins()) {
            visit(join.getTarget(), context);
            visit(join.getCondition(), context);
        }
        visit(md.getProjection(), context);
        visit(md.getWhere(), context);
        
        return null;
    }

    @Override
    public Void visit(TemplateExpression<?> expr, QueryMetadata context) {
        for (Object arg : expr.getArgs()) {
            if (arg instanceof Expression<?>) {
                ((Expression<?>)arg).accept(this, context);
            }
        }
        return null;
    }
    
    private void visit(Expression<?> expr, QueryMetadata context) {
        if (expr != null) {
            expr.accept(this, context);
        }
    }
    
    private void visit(List<Expression<?>> exprs, QueryMetadata context) {
        for (Expression<?> arg : exprs) {
            arg.accept(this, context);
        }
    }

}
