/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.support;

import java.util.List;

import com.mysema.commons.lang.Assert;
import com.mysema.query.collections.JavaOps;
import com.mysema.query.collections.eval.Evaluator;
import com.mysema.query.collections.support.DefaultIndexSupport.IndexedPath;
import com.mysema.query.collections.utils.EvaluatorUtils;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EConstant;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.Operation;
import com.mysema.query.types.operation.Ops;
import com.mysema.query.types.path.Path;

/**
 * IndexCreationTask provides
 * 
 * @author tiwe
 * @version $Id$
 */
public class DefaultIndexCreationTask {

    private final EBoolean condition;

    private final DefaultIndexSupport indexSupport;

    private final JavaOps ops;

    private final List<? extends Expr<?>> sources;

    /**
     * Create a new DefaultIndexCreationTask instance
     * 
     * @param indexSupport
     * @param sources
     * @param ops
     * @param condition
     */
    public DefaultIndexCreationTask(DefaultIndexSupport indexSupport,
            List<? extends Expr<?>> sources, JavaOps ops, EBoolean condition) {
        this.indexSupport = Assert.notNull(indexSupport);
        this.sources = Assert.notNull(sources);
        this.ops = ops;
        this.condition = Assert.notNull(condition);
    }

    private void indexPathEqExpr(Path<?> path, Expr<?> key) {
        if (!indexSupport.isIndexed(path.getRoot())) {
            indexSupport.indexToHash(path);

            // create the key creator
            Evaluator keyCreator;
            if (key instanceof EConstant) {
                final Object constant = ((EConstant<?>) key).getConstant();
                keyCreator = new Evaluator() {
                    @SuppressWarnings("unchecked")
                    public <T> T evaluate(Object... args) {
                        return (T) constant;
                    }
                };
            } else {
                keyCreator = EvaluatorUtils.create(ops, sources, key);
            }

            // update the index
            indexSupport.addPath(path.getRoot(), new IndexedPath(path,
                    keyCreator));
        }
    }

    private void indexPathEqPath(Expr<?> e1, Expr<?> e2) {
        Path<?> p1 = (Path<?>) e1, p2 = (Path<?>) e2;
        int i1 = sources.indexOf(p1.getRoot());
        int i2 = sources.indexOf(p2.getRoot());

        // index the path at higher position
        if (i1 < i2) {
            indexPathEqExpr(p2, e1);
        } else if (i1 > i2) {
            indexPathEqExpr(p1, e2);
        }
    }

    public void run() {
        if (condition instanceof Operation) {
            visitOperation((Operation<?, ?>) condition);
        }
    }

    public void visitOperation(Operation<?, ?> op) {
        if (op.getOperator() == Ops.EQ_OBJECT
                || op.getOperator() == Ops.EQ_PRIMITIVE) {
            Expr<?> e1 = op.getArg(0);
            Expr<?> e2 = op.getArg(1);
            if (e1 instanceof Path && e2 instanceof Path) {
                indexPathEqPath(e1, e2);

            } else if (e1 instanceof Path) {
                if (e2 instanceof EConstant) {
                    indexPathEqExpr((Path<?>) e1, e2);
                }

            } else if (e2 instanceof Path) {
                if (e1 instanceof EConstant) {
                    indexPathEqExpr((Path<?>) e2, e1);
                }
            }

        } else if (op.getOperator() == Ops.LIKE) {
            // TODO

        } else if (op.getOperator() == Ops.NOT) {
            // skip negative condition paths

        } else if (op.getOperator() == Ops.AND) {
            for (Expr<?> e : op.getArgs()) {
                if (e instanceof Operation)
                    visitOperation((Operation<?, ?>) e);
            }
        }
    }

}
