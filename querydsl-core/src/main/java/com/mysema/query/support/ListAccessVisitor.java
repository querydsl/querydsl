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
package com.mysema.query.support;


import com.google.common.collect.ImmutableList;
import com.mysema.query.types.Constant;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.Expression;
import com.mysema.query.types.FactoryExpression;
import com.mysema.query.types.Operation;
import com.mysema.query.types.OperationImpl;
import com.mysema.query.types.Operator;
import com.mysema.query.types.ParamExpression;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathImpl;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.PathType;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.PredicateOperation;
import com.mysema.query.types.PredicateTemplate;
import com.mysema.query.types.SubQueryExpression;
import com.mysema.query.types.TemplateExpression;
import com.mysema.query.types.TemplateExpressionImpl;
import com.mysema.query.types.Templates;
import com.mysema.query.types.ToStringVisitor;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.path.EntityPathBase;

/**
 * ListAccessVisitor is expression visitor implementation
 * 
 * @author tiwe
 *
 */
@Deprecated
public class ListAccessVisitor implements Visitor<Expression<?>,Context> {
    
    private static final class UnderscoreTemplates extends Templates {
        {
            add(PathType.PROPERTY, "{0}_{1}");
            add(PathType.LISTVALUE, "{0}_{1}");
            add(PathType.LISTVALUE_CONSTANT, "{0}_{1}");
        }
    }

    public static final ListAccessVisitor DEFAULT = new ListAccessVisitor();
    
    public static final Templates TEMPLATE = new UnderscoreTemplates();
        
    @SuppressWarnings("unchecked")
    private static <T> Path<T> replaceParent(Path<T> path, Path<?> parent) {
        PathMetadata<?> metadata = new PathMetadata(parent, path.getMetadata().getElement(), 
                path.getMetadata().getPathType());
        return new PathImpl<T>(path.getType(), metadata);
    }

    @Override
    public Expression<?> visit(Constant<?> expr, Context context) {
        return expr;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Expression<?> visit(TemplateExpression<?> expr, Context context) {
        final Object[] args = new Object[expr.getArgs().size()];        
        for (int i = 0; i < args.length; i++) {
            Context c = new Context();
            if (expr.getArg(i) instanceof Expression) {
                args[i] = ((Expression)expr.getArg(i)).accept(this, c);    
            } else {
                args[i] = expr.getArg(i);
            }            
            context.add(c);
        }
        if (context.replace) {             
            if (expr.getType().equals(Boolean.class)) {
                Predicate predicate = new PredicateTemplate(expr.getTemplate(), args);
                return !context.paths.isEmpty() ? exists(context, predicate) : predicate;           
            } else {
                return new TemplateExpressionImpl(expr.getType(), expr.getTemplate(), ImmutableList.copyOf(args));    
            }    
        } else {
            return expr;
        }         
    }

    @Override
    public Expression<?> visit(FactoryExpression<?> expr, Context context) {
        return expr;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Expression<?> visit(Operation<?> expr, Context context) {
        final Expression<?>[] args = new Expression<?>[expr.getArgs().size()];        
        for (int i = 0; i < args.length; i++) {
            Context c = new Context();
            args[i] = expr.getArg(i).accept(this, c);
            context.add(c);
        }
        if (context.replace) {            
            if (expr.getType().equals(Boolean.class)) {
                Predicate predicate = new PredicateOperation((Operator)expr.getOperator(), ImmutableList.copyOf(args));
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

    @SuppressWarnings("unchecked")
    @Override
    public Expression<?> visit(Path<?> expr, Context context) {
        final PathType pathType = expr.getMetadata().getPathType();
        if (pathType == PathType.LISTVALUE_CONSTANT || pathType == PathType.LISTVALUE) {
            final String variable = expr.accept(ToStringVisitor.DEFAULT, TEMPLATE).replace('.', '_');
            final EntityPath<?> replacement = new EntityPathBase(expr.getType(), variable);
            context.add(expr, replacement);
            return replacement;
            
        } else if (expr.getMetadata().getParent() != null) {
            Context c = new Context();
            final Path<?> parent = (Path<?>) expr.getMetadata().getParent().accept(this, c);
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
