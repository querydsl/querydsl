/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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
package com.querydsl.core.support;


import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.SimplePath;

/**
 * {@code CollectionAnyVisitor} is an expression visitor which transforms any() path expressions which are
 * often transformed into subqueries
 *
 * @author tiwe
 *
 */
@SuppressWarnings("unchecked")
public class CollectionAnyVisitor implements Visitor<Expression<?>,Context> {

    private int replacedCounter;

    @SuppressWarnings("rawtypes")
    private static <T> Path<T> replaceParent(Path<T> path, Path<?> parent) {
        PathMetadata metadata = new PathMetadata(parent, path.getMetadata().getElement(),
                path.getMetadata().getPathType());
        if (path instanceof CollectionExpression) {
            CollectionExpression<?,?> col = (CollectionExpression<?,?>)path;
            return (Path<T>)Expressions.listPath(col.getParameter(0), SimplePath.class, metadata);
        } else {
            return ExpressionUtils.path(path.getType(), metadata);
        }
    }

    @Override
    public Expression<?> visit(Constant<?> expr, Context context) {
        return expr;
    }

    @Override
    public Expression<?> visit(TemplateExpression<?> expr, Context context) {
        Object[] args = new Object[expr.getArgs().size()];
        for (int i = 0; i < args.length; i++) {
            Context c = new Context();
            if (expr.getArg(i) instanceof Expression) {
                args[i] = ((Expression<?>)expr.getArg(i)).accept(this, c);
            } else {
                args[i] = expr.getArg(i);
            }
            context.add(c);
        }
        if (context.replace) {
            if (expr.getType().equals(Boolean.class)) {
                Predicate predicate = Expressions.booleanTemplate(expr.getTemplate(), args);
                return !context.paths.isEmpty() ? exists(context, predicate) : predicate;
            } else {
                return ExpressionUtils.template(expr.getType(), expr.getTemplate(), args);
            }
        } else {
            return expr;
        }
    }

    @Override
    public Expression<?> visit(FactoryExpression<?> expr, Context context) {
        return expr;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Expression<?> visit(Operation<?> expr, Context context) {
        Expression<?>[] args = new Expression<?>[expr.getArgs().size()];
        for (int i = 0; i < args.length; i++) {
            Context c = new Context();
            args[i] = expr.getArg(i).accept(this, c);
            context.add(c);
        }
        if (context.replace) {
            if (expr.getType().equals(Boolean.class)) {
                Predicate predicate = ExpressionUtils.predicate(expr.getOperator(), args);
                return !context.paths.isEmpty() ? exists(context, predicate) : predicate;
            } else {
                return ExpressionUtils.operation(expr.getType(), expr.getOperator(), args);
            }
        } else {
            return expr;
        }
    }

    protected Predicate exists(Context c, Predicate condition) {
        return condition;
    }

    @Override
    public Expression<?> visit(Path<?> expr, Context context) {
        if (expr.getMetadata().getPathType() == PathType.COLLECTION_ANY) {
            Path<?> parent = (Path<?>) expr.getMetadata().getParent().accept(this, context);
            expr = ExpressionUtils.path(expr.getType(), PathMetadataFactory.forCollectionAny(parent));
            EntityPath<?> replacement = new EntityPathBase<Object>(expr.getType(),
                    ExpressionUtils.createRootVariable(expr, replacedCounter++));
            context.add(expr, replacement);
            return replacement;

        } else if (expr.getMetadata().getParent() != null) {
            Context c = new Context();
            Path<?> parent = (Path<?>) expr.getMetadata().getParent().accept(this, c);
            if (c.replace) {
                context.add(c);
                return replaceParent(expr, parent);
            }
        }
        return expr;
    }

    @Override
    public Expression<?> visit(SubQueryExpression<?> expr, Context context) {
        return expr;
    }

    @Override
    public Expression<?> visit(ParamExpression<?> expr, Context context) {
        return expr;
    }

}
