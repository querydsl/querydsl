package com.mysema.query.types.expr;

import java.util.Collection;

import javax.annotation.Nullable;

import com.mysema.query.types.Expression;
import com.mysema.query.types.Operator;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.path.SimplePath;

/**
 * @author tiwe
 *
 */
public final class ExpressionUtils {
    
    @Nullable
    public static Predicate allOf(Predicate... exprs){
        Predicate rv = null;
        for (Predicate b : exprs){
            rv = rv == null ? b : ExpressionUtils.and(rv,b);
        }
        return rv;
    }

    public static Predicate and(Predicate left, Predicate right){
        return BooleanOperation.create(Ops.AND, left, right);
    }
    
    @Nullable
    public static Predicate anyOf(Predicate... exprs){
        Predicate rv = null;
        for (Predicate b : exprs){
            rv = rv == null ? b : ExpressionUtils.or(rv,b);
        }
        return rv;
    }
    
    @SuppressWarnings("unchecked")
    public static <D> Expression<D> as(Expression<D> source, Path<D> alias) {
        return SimpleOperation.create(source.getType(),(Operator)Ops.ALIAS, source, alias);
    }
    
    @SuppressWarnings("unchecked")
    public static <D> Expression<D> as(Expression<D> source, String alias) {
        return SimpleOperation.create(source.getType(),(Operator)Ops.ALIAS, source, new SimplePath<D>(source.getType(), alias));
    }

    public static <D> Predicate eqConst(Expression<D> left, D constant) {
        return eq(left, SimpleConstant.create(constant));
    }
    
    public static <D> Predicate eq(Expression<D> left, Expression<? super D> right) {
        if (isPrimitive(left.getType())) {
            return BooleanOperation.create(Ops.EQ_PRIMITIVE, left, right);
        } else {
            return BooleanOperation.create(Ops.EQ_OBJECT, left, right);
        }
    }
    
    public static <D> Predicate in(Expression<D> left, Collection<? extends D> right) {
        if (right.size() == 1){
            return eqConst(left, right.iterator().next());
        }else{
            return BooleanOperation.create(Ops.IN, left, SimpleConstant.create(right));
        }
    }
    
    public static Predicate isNull(Expression<?> left) {
        if (left instanceof SimpleExpression<?>){
            return ((SimpleExpression<?>)left).isNull();
        }else{
            return BooleanOperation.create(Ops.IS_NULL, left);
        }
    }
    
    public static Predicate isNotNull(Expression<?> left) {
        if (left instanceof SimpleExpression<?>){
            return ((SimpleExpression<?>)left).isNotNull();
        }else{
            return BooleanOperation.create(Ops.IS_NOT_NULL, left);
        }
    }
    
    private static boolean isPrimitive(Class<?> type){
        return type.isPrimitive()
            || Number.class.isAssignableFrom(type)
            || Boolean.class.equals(type)
            || Character.class.equals(type);
    }
    
    public static <D> Predicate neConst(Expression<D> left, D constant) {
        return ne(left, SimpleConstant.create(constant));
    }
    
    public static <D> Predicate ne(Expression<D> left, Expression<? super D> right) {
        if (isPrimitive(left.getType())) {
            return BooleanOperation.create(Ops.NE_PRIMITIVE, left, right);
        } else {
            return BooleanOperation.create(Ops.NE_OBJECT, left, right);
        }
    }
    
    public static Predicate not(Predicate expr){
        if (expr instanceof BooleanExpression){
            return ((BooleanExpression)expr).not();
        }else{
            return BooleanOperation.create(Ops.NOT, expr);
        }
    }

    public static Predicate or(Predicate left, Predicate right){
        return BooleanOperation.create(Ops.OR, left, right);
    }
    
    private ExpressionUtils(){}
    
}
