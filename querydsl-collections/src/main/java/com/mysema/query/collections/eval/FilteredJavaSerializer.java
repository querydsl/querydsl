/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.eval;

import java.util.List;
import java.util.Stack;

import javax.annotation.Nullable;

import org.codehaus.janino.CompileException;
import org.codehaus.janino.ExpressionEvaluator;
import org.codehaus.janino.Parser.ParseException;
import org.codehaus.janino.Scanner.ScanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.Operation;
import com.mysema.query.types.operation.Operator;
import com.mysema.query.types.operation.Ops;
import com.mysema.query.types.path.Path;

/**
 * FilteredJavaSerializer is a JavaSerializer extensions for skipping
 * expressions in the serialization process
 * 
 * @author tiwe
 * @version $Id$
 */
public class FilteredJavaSerializer extends JavaSerializer {

    private static final Logger logger = LoggerFactory.getLogger(FilteredJavaSerializer.class);

    private boolean skipPath = false;

    private List<Expr<?>> exprs;

    private Expr<?> last;

    private boolean inNotOperation = false;
    
    public FilteredJavaSerializer(ColQueryTemplates patterns, List<Expr<?>> expressions) {
        super(patterns);
        this.exprs = expressions;
        this.last = expressions.get(expressions.size() - 1);
    }

    public FilteredJavaSerializer(ColQueryTemplates patterns, List<Expr<?>> expressions, int lastElement) {
        super(patterns);
        this.exprs = expressions.subList(0, lastElement + 1);
        this.last = expressions.get(lastElement);
    }

    @Nullable
    public ExpressionEvaluator createExpressionEvaluator(
            List<? extends Expr<?>> sources, Class<?> targetType)
            throws CompileException, ParseException, ScanException {
        String expr = super.toString();
        String filtered = expr.replace("true", "")
            .replace(" ", "")
            .replace("&", "").replace("|", "");
        if ("".equals(filtered)) {
            logger.info("-- no filtering");
            return null;
        } else {
            return super.createExpressionEvaluator(sources, targetType);
        }
    }

    protected String normalize(String expr) {
        return expr.replace("&& true", "").replace("true &&", "");
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void visitOperation(Class<?> type, Operator<?> operator, List<Expr<?>> args) {
        if (!skipPath) {
            boolean unknownPaths = false;
            boolean knownPaths = false;
            boolean targetIncluded = false;
            // iterate over arguments
            Stack<Expr<?>> exprToVisit = new Stack<Expr<?>>();
            exprToVisit.addAll(args);
            while (!exprToVisit.isEmpty()){
                Expr<?> expr = exprToVisit.pop();
                if (expr instanceof Path) {
                    Path<?> path = ((Path<?>) expr).getRoot();
                    if (!exprs.contains(path)) {
                        unknownPaths = true;
                    } else if (path.equals(last)) {
                        targetIncluded = true;
                    } else {
                        knownPaths = true;
                    }
                }else if (expr instanceof Operation){
                    Operation<?,?> op = (Operation)expr;
                    if (!op.getType().equals(Boolean.class)){
                        exprToVisit.addAll(op.getArgs());
                    }                    
                }
            }
            
            if (unknownPaths) { 
                // all expressions with unknown paths can be filtered out
                skipPath = true;
            } else if (!targetIncluded && knownPaths && type.equals(Boolean.class)) {
                // boolean expressions without the last path, but without
                // unknown paths can be filtered out
                skipPath = true;
            } else {
                boolean old = inNotOperation;
                inNotOperation = (operator == Ops.NOT) ? !old : old;
                super.visitOperation(type, operator, args);
                inNotOperation = old;
            }
        }
        if (skipPath) {
            if (type.equals(Boolean.class)) {
                skipPath = false;
                append(inNotOperation ? "false" : "true");                
            }
        }
    }
    
    @Override
    public FilteredJavaSerializer append(String... str) {
        if (!skipPath){
            super.append(str);
        }
        return this;
    }
    
}
