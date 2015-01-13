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
package com.querydsl.core.support;


import com.google.common.collect.ImmutableList;
import com.querydsl.core.types.*;
import com.querydsl.core.types.path.EntityPathBase;
import com.querydsl.core.types.path.ListPath;
import com.querydsl.core.types.path.SimplePath;
import com.querydsl.core.types.template.BooleanTemplate;

/**
 * CollectionAnyVisitor is an expression visitor which transforms any() path expressions which are
 * often transformed into subqueries
 *
 * @author tiwe
 *
 */
@SuppressWarnings("unchecked")
public class CollectionAnyVisitor implements Visitor<Expression<?>,Context> {

    @Deprecated
    public static final Templates TEMPLATES = new Templates() {{
        add(PathType.PROPERTY, "{0}_{1}");
        add(PathType.COLLECTION_ANY, "{0}");
    }};

    public static final CollectionAnyVisitor DEFAULT = new CollectionAnyVisitor();

    @SuppressWarnings("rawtypes")
    private static <T> Path<T> replaceParent(Path<T> path, Path<?> parent) {
        PathMetadata<?> metadata = new PathMetadata<Object>(parent, path.getMetadata().getElement(),
                path.getMetadata().getPathType());
        if (path instanceof CollectionExpression) {
            CollectionExpression<?,?> col = (CollectionExpression<?,?>)path;
            return new ListPath(col.getParameter(0), SimplePath.class, metadata);
        } else {
            return new PathImpl<T>(path.getType(), metadata);
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
                Predicate predicate = BooleanTemplate.create(expr.getTemplate(), args);
                return !context.paths.isEmpty() ? exists(context, predicate) : predicate;
            } else {
                return TemplateExpressionImpl.create(expr.getType(), expr.getTemplate(), args);
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
                Predicate predicate = new PredicateOperation((Operator<Boolean>)expr.getOperator(), ImmutableList.copyOf(args));
                return !context.paths.isEmpty() ? exists(context, predicate) : predicate;
            } else {
                return new OperationImpl(expr.getType(), expr.getOperator(), ImmutableList.copyOf(args));
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
            expr = new PathImpl<Object>(expr.getType(), PathMetadataFactory.forCollectionAny(parent));
            EntityPath<?> replacement = new EntityPathBase<Object>(expr.getType(),
                    ExpressionUtils.createRootVariable(expr));
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

    protected CollectionAnyVisitor() {}

}
