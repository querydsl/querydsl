/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.iterators;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
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

import com.mysema.query.collections.IndexSupport;
import com.mysema.query.collections.eval.Evaluator;
import com.mysema.query.collections.eval.FilteredJavaSerializer;
import com.mysema.query.collections.eval.JaninoEvaluator;
import com.mysema.query.collections.eval.JavaSerializer;
import com.mysema.query.collections.utils.QueryIteratorUtils;
import com.mysema.query.grammar.JavaOps;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Expr.EBoolean;

/**
 * FilteringMultiIterator extends the MultiIterator to provide a filtered view 
 * of the combined sources
 *
 * @author tiwe
 * @version $Id$
 */
public class FilteringMultiIterator extends MultiIterator implements IndexSupport{
    
    private static final Logger logger = LoggerFactory.getLogger(FilteringMultiIterator.class);
    
    private Map<Expr<?>,Evaluator> exprToEvaluator = new HashMap<Expr<?>,Evaluator>();
    
    private IndexSupport indexSupport;
    
    private JavaOps ops;
    
    private EBoolean where;
    
    public FilteringMultiIterator(JavaOps ops, EBoolean where){
        this.ops = ops;
        this.where = where;
    }
    
    @Override
    public MultiIterator add(Expr<?> expr) {
        super.add(expr);
        try {
            // each iterator needs own copy of expressions
            Evaluator ev = createEvaluator(new ArrayList<Expr<?>>(sources), sources.size());
            exprToEvaluator.put(expr, ev);
            return this;
        } catch (Exception e) {
            String error = "Caught " + e.getClass().getName();
            logger.error(error, e);
            throw new RuntimeException(error, e);
        }
        
    }
    
    private Evaluator createEvaluator(List<Expr<?>> exprCopy,
            final int extraArgsSize) throws CompileException, ParseException,
            ScanException {
         JavaSerializer serializer = new FilteredJavaSerializer(ops, exprCopy){            
            @Override
            protected ExpressionEvaluator instantiateExpressionEvaluator(
                    Class<?> targetType, String expr, final Object[] constArray,
                    Class<?>[] types, String[] names) throws CompileException,
                    ParseException, ScanException {
                
                return new ExpressionEvaluator(expr, targetType, names, types){
                    @Override
                    public Object evaluate(Object[] origArgs) throws InvocationTargetException{
                        Object[] args = new Object[constArray.length + extraArgsSize];
                        System.arraycopy(constArray, 0, args, 0, constArray.length);
                        System.arraycopy(values, 0, args, constArray.length, extraArgsSize);
                        args[args.length - 1] = origArgs[0];
                        return super.evaluate(args);
                    }
                };
            }
            
        };
        serializer.handle(where);        
        // TODO : find out if the expression resolved always to true
        ExpressionEvaluator ev = serializer.createExpressionEvaluator(exprCopy, boolean.class);
        return new JaninoEvaluator(ev);
    }
    
    public <A> Iterator<A> getIterator(Expr<A> expr) {
        return indexSupport.getIterator(expr);
    }

    public <A> Iterator<A> getIterator(Expr<A> expr, Object[] bindings) {
        Iterator<A> it = indexSupport.getIterator(expr, bindings);
        Evaluator ev = exprToEvaluator.get(expr);        
        if (ev != null){
            it = QueryIteratorUtils.singleArgFilter(indexSupport.getIterator(expr, bindings), ev);
        }
        return it;        
    }
    
    @Override
    public MultiIterator init(IndexSupport iteratorFactory){
        this.indexSupport = iteratorFactory;
        super.init(this);
        return this;
    }

    public void init(Map<Expr<?>,Iterable<?>> exprToIt, JavaOps ops, List<? extends Expr<?>> orderedSources, EBoolean condition) {
        indexSupport.init(exprToIt, ops, orderedSources, condition);
    }
    
}
