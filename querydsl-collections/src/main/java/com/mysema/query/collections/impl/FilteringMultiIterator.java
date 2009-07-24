/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.codehaus.janino.CompileException;
import org.codehaus.janino.ExpressionEvaluator;
import org.codehaus.janino.Parser.ParseException;
import org.codehaus.janino.Scanner.ScanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.commons.lang.Assert;
import com.mysema.query.collections.IteratorSource;
import com.mysema.query.collections.eval.ColQueryPatterns;
import com.mysema.query.collections.eval.Evaluator;
import com.mysema.query.collections.eval.FilteredJavaSerializer;
import com.mysema.query.collections.eval.JaninoEvaluator;
import com.mysema.query.collections.eval.JavaSerializer;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;

/**
 * FilteringMultiIterator extends the MultiIterator to provide a filtered view
 * of the combined sources
 * 
 * @author tiwe
 * @version $Id$
 */
// TODO : simplify this
public class FilteringMultiIterator extends MultiIterator implements
        IteratorSource {

    private static final Logger logger = LoggerFactory
            .getLogger(FilteringMultiIterator.class);

    private Map<Expr<?>, Evaluator> exprToEvaluator = new HashMap<Expr<?>, Evaluator>();

    private IteratorSource iteratorSource;

    private ColQueryPatterns patterns;

    private EBoolean where;

    public FilteringMultiIterator(ColQueryPatterns patterns, EBoolean where) {
        this.patterns = patterns;
        this.where = where;
    }

    // TODO : simplify this
    private Evaluator createEvaluator(List<Expr<?>> sources,
            final int lastElement) throws CompileException, ParseException,
            ScanException {
        JavaSerializer serializer = new FilteredJavaSerializer(patterns,
                sources, lastElement) {
            @Override
            protected ExpressionEvaluator instantiateExpressionEvaluator(
                    Class<?> targetType, String expr,
                    final Object[] constArray, Class<?>[] types, String[] names)
                    throws CompileException, ParseException, ScanException {
                try {
                    return new ExpressionEvaluator(expr, targetType, names,
                            types) {
                        @Override
                        public Object evaluate(Object[] origArgs)
                                throws InvocationTargetException {
                            Object[] args = JavaSerializer.combine(
                                    constArray.length + values.length,
                                    constArray, values);
                            args[constArray.length + lastElement] = origArgs[0];
                            return super.evaluate(args);
                        }
                    };
                } catch (CompileException e) {
                    throw new IllegalArgumentException(
                            "Caught exception when trying to compile " + expr
                                    + " with types " + Arrays.asList(types));
                }

            }

        };
        serializer.handle(where);
        logger.info("Filtering iterator for source");
        ExpressionEvaluator ev = serializer.createExpressionEvaluator(sources,
                boolean.class);
        if (ev != null) {
            return new JaninoEvaluator(ev, sources, where);
        } else {
            return null;
        }

    }

    public <A> Iterator<A> getIterator(Expr<A> expr) {
        return iteratorSource.getIterator(expr);
    }

    public <A> Iterator<A> getIterator(Expr<A> expr, Object[] bindings) {
        Iterator<A> it = Assert.notNull(iteratorSource.getIterator(expr,
                bindings));
        if (exprToEvaluator.containsKey(expr)) {
            return QueryIteratorUtils.singleArgFilter(it, exprToEvaluator
                    .get(expr));
        } else {
            return it;
        }
    }

    @Override
    public MultiIterator init(IteratorSource iteratorSource) {
        this.iteratorSource = iteratorSource;
        super.init(this);
        int index = 0;
        for (Expr<?> expr : sources) {
            try {
                Evaluator ev = createEvaluator(sources, index++);
                if (ev != null){
                    exprToEvaluator.put(expr, ev);
                }                    
            } catch (Exception e) {
                String error = "Caught " + e.getClass().getName();
                logger.error(error, e);
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        return this;
    }

}
