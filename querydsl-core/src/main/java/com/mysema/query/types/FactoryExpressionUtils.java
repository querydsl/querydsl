package com.mysema.query.types;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

/**
 * Utility class to expand FactoryExpression constructor arguments and compress FactoryExpression invocation arguments
 *
 * @author tiwe
 *
 */
public final class FactoryExpressionUtils {

    public static class FactoryExpressionAdapter<T> extends ExpressionBase<T> implements FactoryExpression<T> {

        private static final long serialVersionUID = -2742333128230913512L;

        private final FactoryExpression<T> inner;

        private final List<Expression<?>> args;

        FactoryExpressionAdapter(FactoryExpression<T> inner) {
            super(inner.getType());
            this.inner = inner;
            this.args = expand(inner.getArgs());
        }

        @Override
        public List<Expression<?>> getArgs() {
            return args;
        }

        @Override
        public T newInstance(Object... a) {
            return inner.newInstance(compress(inner.getArgs(), a));
        }

        @Override
        public <R, C> R accept(Visitor<R, C> v, C context) {
            return v.visit(this, context);
        }

    }

    public static <T> FactoryExpression<T> wrap(FactoryExpression<T> expr){
        for (Expression<?> arg : expr.getArgs()) {
            if (arg instanceof ProjectionRole) {
                arg = ((ProjectionRole) arg).getProjection();
            }            
            if (arg instanceof FactoryExpression<?>) {
                return new FactoryExpressionAdapter<T>(expr);
            }
        }
        return expr;
    }

    private static List<Expression<?>> expand(List<Expression<?>> exprs){
        List<Expression<?>> rv = new ArrayList<Expression<?>>(exprs.size());
        for (Expression<?> expr : exprs) {
            if (expr instanceof ProjectionRole) {
                expr = ((ProjectionRole) expr).getProjection();
            }            
            if (expr instanceof FactoryExpression<?>) {
                rv.addAll(expand(((FactoryExpression<?>)expr).getArgs()));
            } else {
                rv.add(expr);
            }
        }
        return rv;
    }

    private static int countArguments(FactoryExpression<?> expr){
        int counter = 0;
        for (Expression<?> arg : expr.getArgs()) {
            if (arg instanceof FactoryExpression<?>) {
                counter += countArguments((FactoryExpression<?>)arg);
            } else {
                counter++;
            }
        }
        return counter;
    }

    private static Object[] compress(List<Expression<?>> exprs, Object[] args){
        if (exprs.size() != args.length){
            Object[] rv = new Object[exprs.size()];
            int offset = 0;
            for (int i = 0; i < exprs.size(); i++) {
                Expression<?> expr = exprs.get(i);
                if (expr instanceof ProjectionRole) {
                    expr = ((ProjectionRole) expr).getProjection();
                }                
                if (expr instanceof FactoryExpression<?>) {
                    FactoryExpression<?> fe = (FactoryExpression<?>)expr;
                    int fullArgsLength = countArguments(fe);
                    Object[] compressed = compress(fe.getArgs(), ArrayUtils.subarray(args, offset, offset + fullArgsLength));
                    rv[i] = fe.newInstance(compressed);
                    offset += fullArgsLength;
                } else {
                    rv[i] = args[offset];
                    offset++;
                }
            }
            return rv;
        } else {
            return args;
        }
    }

    private FactoryExpressionUtils(){}

}
