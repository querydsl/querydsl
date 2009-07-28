/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.serialization;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysema.commons.lang.Assert;
import com.mysema.query.types.AbstractVisitor;
import com.mysema.query.types.custom.Custom;
import com.mysema.query.types.expr.EArrayConstructor;
import com.mysema.query.types.expr.EConstant;
import com.mysema.query.types.expr.EConstructor;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.Operation;
import com.mysema.query.types.operation.Operator;
import com.mysema.query.types.path.Path;
import com.mysema.query.types.path.PathType;

/**
 * BaseSerializer is a stub for Serializer implementations
 * 
 * @author tiwe
 * @version $Id$
 */
public abstract class BaseSerializer<SubType extends BaseSerializer<SubType>>
        extends AbstractVisitor<SubType> {

    protected StringBuilder builder = new StringBuilder();

    protected Map<Object,String> constantToLabel = new HashMap<Object,String>();
    
    protected String constantPrefix = "a";
    
    protected final OperationPatterns patterns;

    @SuppressWarnings("unchecked")
    private final SubType _this = (SubType) this;

    public BaseSerializer(OperationPatterns patterns) {
        this.patterns = Assert.notNull(patterns,"patterns is null");
    }

    public final SubType append(String... str) {
        for (String s : str) {
            builder.append(s);
        }
        return _this;
    }

    protected void appendOperationResult(Operator<?> operator, String result) {
        append(result);
    }

    public Map<Object,String> getConstantToLabel() {
        return constantToLabel;
    }

    public final SubType handle(String sep, List<? extends Expr<?>> expressions) {
        boolean first = true;
        for (Expr<?> expr : expressions) {
            if (!first) {
                builder.append(sep);
            }
            handle(expr);
            first = false;
        }
        return _this;
    }

    public void setConstantPrefix(String prefix){
        this.constantPrefix = prefix;
    }
    
    
    public String toString() {
        return builder.toString();
    }

    protected final String toString(Expr<?> expr, boolean wrap) {
        StringBuilder old = builder;
        builder = new StringBuilder();
        if (wrap) {
            builder.append("(");
        }
        handle(expr);
        if (wrap) {
            builder.append(")");
        }
        String ret = builder.toString();
        builder = old;
        return ret;
    }

    protected void visit(Custom<?> expr) {
        Object[] strings = new String[expr.getArgs().size()];
        for (int i = 0; i < strings.length; i++) {
            strings[i] = toString(expr.getArg(i), false);
        }
        append(String.format(expr.getPattern(), strings));
    }

    protected void visit(EArrayConstructor<?> oa) {
        append("new ").append(oa.getElementType().getName()).append("[]{");
        handle(", ", oa.getArgs()).append("}");
    }

    @Override
    protected void visit(EConstant<?> expr) {        
        if (!constantToLabel.containsKey(expr.getConstant())) {
            String constLabel = constantPrefix + (constantToLabel.size() + 1);
            constantToLabel.put(expr.getConstant(), constLabel);
            append(constLabel);
        } else {
            append(constantToLabel.get(expr.getConstant()));
        }
    }

    protected void visit(EConstructor<?> expr) {
        append("new ").append(expr.getType().getName()).append("(");
        handle(", ", expr.getArgs()).append(")");
    }

    @Override
    protected final void visit(Operation<?, ?> expr) {
        visitOperation(expr.getType(), expr.getOperator(), expr.getArgs());
    }

    protected void visit(Path<?> path) {
        PathType pathType = path.getMetadata().getPathType();
        String parentAsString = null, exprAsString = null;

        if (path.getMetadata().getParent() != null) {
            parentAsString = toString((Expr<?>) path.getMetadata().getParent(),
                    false);
        }
        if (pathType == PathType.PROPERTY 
                || pathType == PathType.VARIABLE
                || pathType == PathType.LISTVALUE_CONSTANT
                || pathType == PathType.ARRAYVALUE_CONSTANT) {
            exprAsString = path.getMetadata().getExpression().toString();
        } else if (path.getMetadata().getExpression() != null) {
            exprAsString = toString(path.getMetadata().getExpression(), false);
        }

        String pattern = patterns.getPattern(pathType);
        if (parentAsString != null) {
            append(String.format(pattern, parentAsString, exprAsString));
        } else {
            append(String.format(pattern, exprAsString));
        }

    }
    
    @SuppressWarnings("unchecked")
    protected void visitOperation(Class<?> type, Operator<?> operator,List<Expr<?>> args) {
        String pattern = patterns.getPattern(operator);
        if (pattern == null) {
            throw new IllegalArgumentException("Got no pattern for " + operator);
        }
        Object[] strings = new String[args.size()];
        int precedence = patterns.getPrecedence(operator);
        for (int i = 0; i < strings.length; i++) {
            boolean wrap = false;
            if (args.get(i) instanceof Operation) {
                // wrap if outer operator precedes
                wrap = precedence < patterns.getPrecedence(((Operation<?, ?>) args
                        .get(i)).getOperator());
            }
            strings[i] = toString(args.get(i), wrap);
        }
        // TODO : use faster custom rendering
        appendOperationResult(operator, String.format(pattern, strings));
    }
}
