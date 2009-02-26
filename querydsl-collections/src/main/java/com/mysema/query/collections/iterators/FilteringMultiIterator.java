/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.iterators;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.janino.CompileException;
import org.codehaus.janino.ExpressionEvaluator;
import org.codehaus.janino.Parser.ParseException;
import org.codehaus.janino.Scanner.ScanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.query.grammar.FilteredJavaSerializer;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Expr.EBoolean;
import com.mysema.query.serialization.OperationPatterns;

/**
 * FilteringMultiIterator extends the MultiIterator to provide a filtered view 
 * of the combined sources
 *
 * @author tiwe
 * @version $Id$
 */
public class FilteringMultiIterator extends MultiIterator{
    
    private static final Logger logger = LoggerFactory.getLogger(FilteringMultiIterator.class);
    
    private List<Expr<?>> expressions = new ArrayList<Expr<?>>();
    
    private OperationPatterns ops;
    
    private EBoolean where;

    public FilteringMultiIterator(OperationPatterns ops, EBoolean where){
        this.ops = ops;
        this.where = where;
    }
        
    @Override
    public MultiIterator add(Expr<?> expr, final Iterable<?> iterable) {
        expressions.add(expr);
        // each iterator needs own copy of expressions
        final List<Expr<?>> exprCopy = new ArrayList<Expr<?>>(expressions);
        try {            
            final ExpressionEvaluator ev = new FilteredJavaSerializer(ops, exprCopy){
                
                @Override
                protected ExpressionEvaluator instantiateExpressionEvaluator(
                        Class<?> targetType, String expr, final Object[] constArray,
                        Class<?>[] types, String[] names) throws CompileException,
                        ParseException, ScanException {
                    
                    return new ExpressionEvaluator(expr, targetType, names, types){
                        @Override
                        public Object evaluate(Object[] origArgs) throws InvocationTargetException{
                            Object[] args = new Object[constArray.length + exprCopy.size()];
                            System.arraycopy(constArray, 0, args, 0, constArray.length);
                            System.arraycopy(values, 0, args, constArray.length, exprCopy.size());
                            args[args.length - 1] = origArgs[0];
                            return super.evaluate(args);
                        }
                    };
                }
                
                }.handle(where)
                .createExpressionEvaluator(exprCopy, boolean.class);
            
            return super.add(expr, new Iterable<Object>(){
                public Iterator<Object> iterator() {
                    return new SingleArgFilteringIterator<Object>(iterable.iterator(), ev);
                }            
            });
        } catch (Exception e) {
            String error = "Caught " + e.getClass().getName();
            logger.error(error, e);
            throw new RuntimeException(error, e);
        }        
    }    
    
}
