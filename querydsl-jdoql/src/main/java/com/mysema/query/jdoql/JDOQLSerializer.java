/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.jdoql;

import java.util.List;

import com.mysema.query.serialization.BaseSerializer;
import com.mysema.query.types.expr.EConstant;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.Operator;
import com.mysema.query.types.operation.Ops;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.Path;

/**
 * 
 * @author tiwe
 * 
 */
public class JDOQLSerializer extends BaseSerializer<JDOQLSerializer> {

    private PEntity<?> candidatePath;

    public JDOQLSerializer(JDOQLPatterns ops, PEntity<?> candidate) {
        super(ops);
        this.candidatePath = candidate;
    }

    @Override
    protected void visit(EConstant<?> expr) {
        boolean wrap = expr.getConstant().getClass().isArray();
        if (wrap) {
            append("(");
        }
        append("a");
        if (!constants.contains(expr.getConstant())) {
            constants.add(expr.getConstant());
            append(Integer.toString(constants.size()));
        } else {
            append(Integer.toString(constants.indexOf(expr.getConstant()) + 1));
        }
        if (wrap) {
            append(")");
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void visitOperation(Class<?> type, Operator<?> operator,
            List<Expr<?>> args) {
        if (operator.equals(Ops.ISTYPEOF)) {
            handle(args.get(0)).append(" instanceof ");
            append(((EConstant<Class<?>>) args.get(1)).getConstant().getName());
        } else if (operator.equals(Ops.STRING_CAST)) {
            // TODO
        } else if (operator.equals(Ops.NUMCAST)) {
            // TODO
        } else {
            super.visitOperation(type, operator, args);
        }
    }

    @Override
    protected void visit(Path<?> path) {
        if (path.equals(candidatePath)) {
            append("this");
        } else {
            super.visit(path);
        }
    }

}
