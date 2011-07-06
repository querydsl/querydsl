/*
 * Copyright (c) 2011 Mysema Ltd.
 * All rights reserved.
 *
 */

package com.mysema.query.support;


import com.mysema.query.types.*;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.template.BooleanTemplate;

/**
 * @author tiwe
 *
 */
public class CollectionAnyVisitor implements Visitor<Expression<?>,Context>{
    
    public static final CollectionAnyVisitor DEFAULT = new CollectionAnyVisitor();
    
    public static final Templates TEMPLATE = new Templates(){
    {
        add(PathType.PROPERTY, "{0}_{1}");
        add(PathType.COLLECTION_ANY, "{0}");
    }};
    
    
    @SuppressWarnings("unchecked")
    private static <T> Path<T> replaceParent(Path<T> path, Path<?> parent) {
        PathMetadata<?> metadata = new PathMetadata(parent, path.getMetadata().getExpression(), path.getMetadata().getPathType());
        return new PathImpl<T>(path.getType(), metadata);
    }

    @Override
    public Expression<?> visit(Constant<?> expr, Context context) {
        return expr;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Expression<?> visit(TemplateExpression<?> expr, Context context) {
        Expression<?>[] args = new Expression<?>[expr.getArgs().size()];        
        for (int i = 0; i < args.length; i++){
            Context c = new Context();
            args[i] = expr.getArg(i).accept(this, c);
            context.add(c);
        }
        if (context.replace){            
            if (expr.getType().equals(Boolean.class)){
                Predicate predicate = BooleanTemplate.create(expr.getTemplate(), args);
                return !context.paths.isEmpty() ? exists(context, predicate) : predicate;           
            }else{
                return new TemplateExpressionImpl(expr.getType(), expr.getTemplate(), args);    
            }    
        }else{
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
        Expression<?>[] args = new Expression<?>[expr.getArgs().size()];        
        for (int i = 0; i < args.length; i++){
            Context c = new Context();
            args[i] = expr.getArg(i).accept(this, c);
            context.add(c);
        }
        if (context.replace){            
            if (expr.getType().equals(Boolean.class)){
                Predicate predicate = new PredicateOperation((Operator)expr.getOperator(), args);
                return !context.paths.isEmpty() ? exists(context, predicate) : predicate;           
            }else{
                return new OperationImpl(expr.getType(), expr.getOperator(), args);    
            }    
        }else{
            return expr;
        }        
    }
    
    protected Predicate exists(Context c, Predicate condition){
        return condition;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Expression<?> visit(Path<?> expr, Context context) {
        if (expr.getMetadata().getPathType() == PathType.COLLECTION_ANY){
            String variable = expr.accept(ToStringVisitor.DEFAULT, TEMPLATE).replace('.', '_');
            EntityPath<?> replacement = new EntityPathBase(expr.getType(), variable);
            context.add(expr, replacement);
            return replacement;
            
        }else if (expr.getMetadata().getParent() != null){
            Context c = new Context();
            Path<?> parent = (Path<?>) expr.getMetadata().getParent().accept(this, c);
            if (c.replace){
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
