package com.mysema.query.support;

import java.util.ArrayList;
import java.util.List;

import com.mysema.query.types.*;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.template.BooleanTemplate;

/**
 * @author tiwe
 *
 */
public abstract class CollectionAnyVisitor implements Visitor<Expression<?>,CollectionAnyVisitor.Context>{
    
    public static class Context {
        
        public boolean replace;
        
        public final List<Path<?>> anyPaths = new ArrayList<Path<?>>(); 
            
        public final List<EntityPath<?>> replacements = new ArrayList<EntityPath<?>>();    
                
        public void add(Path<?> anyPath, EntityPath<?> replacement){
            replace = true;
            anyPaths.add(anyPath);
            replacements.add(replacement);
        }
        
        public void add(Context c){
            replace |= c.replace;
            anyPaths.addAll(c.anyPaths);
            replacements.addAll(c.replacements);

        }

        public void clear() {
            anyPaths.clear();
            replacements.clear();            
        }
        
    }
    
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
                return !context.anyPaths.isEmpty() ? exists(context, predicate) : predicate;           
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
                return !context.anyPaths.isEmpty() ? exists(context, predicate) : predicate;           
            }else{
                return new OperationImpl(expr.getType(), expr.getOperator(), args);    
            }    
        }else{
            return expr;
        }        
    }
    
    protected abstract Predicate exists(Context c, Predicate condition);

    @SuppressWarnings("unchecked")
    @Override
    public Expression<?> visit(Path<?> expr, Context context) {
        if (expr.getMetadata().getPathType() == PathType.COLLECTION_ANY){
            String variable = expr.getMetadata().getParent().toString().replace('.', '_');
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
