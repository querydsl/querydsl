package com.mysema.query.collections.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.codehaus.janino.ExpressionEvaluator;

import com.mysema.query.grammar.JavaOps;
import com.mysema.query.grammar.JavaSerializer;
import com.mysema.query.grammar.types.Expr;

/**
 * EvaluatorUtils provides
 *
 * @author tiwe
 * @version $Id$
 */
public class EvaluatorUtils {

    public static <T> T evaluate(ExpressionEvaluator ev, Object... args){
        try {
            return (T) ev.evaluate(args);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static ExpressionEvaluator create(JavaOps ops, List<Expr<?>> sources, Expr<?> expr){
        try {
            return new JavaSerializer(ops).handle(expr).createExpressionEvaluator(sources, expr);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
