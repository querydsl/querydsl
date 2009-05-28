/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mysema.query.JoinExpression;
import com.mysema.query.QueryMetadata;
import com.mysema.query.serialization.BaseSerializer;
import com.mysema.query.types.Order;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.SubQuery;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EConstant;
import com.mysema.query.types.expr.EConstructor;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.Operator;
import com.mysema.query.types.operation.Ops;
import com.mysema.query.types.path.PEntity;

/**
 * SqlSerializer serializes Querydsl queries into SQL
 * 
 * @author tiwe
 * @version $Id$
 */
public class SQLSerializer extends BaseSerializer<SQLSerializer> {

    protected final SQLPatterns patterns;

    public SQLSerializer(SQLPatterns patterns) {
        super(patterns);
        this.patterns = patterns;
    }

    protected void beforeOrderBy() {
        // template method, for subclasses do override
    }

    public void serialize(QueryMetadata<Object> metadata, boolean forCountRow) {
        List<? extends Expr<?>> select = metadata.getProjection();
        List<JoinExpression<Object>> joins = metadata.getJoins();
        EBoolean where = metadata.getWhere();
        List<? extends Expr<?>> groupBy = metadata.getGroupBy();
        EBoolean having = metadata.getHaving();
        List<OrderSpecifier<?>> orderBy = metadata.getOrderBy();

        if (forCountRow) {
            append(patterns.select()).append(patterns.countStar());
        } else if (!select.isEmpty()) {
            if (!metadata.isDistinct()) {
                append(patterns.select());
            } else {
                append(patterns.selectDistinct());
            }
            List<Expr<?>> sqlSelect = new ArrayList<Expr<?>>();
            for (Expr<?> selectExpr : select) {
                if (selectExpr instanceof EConstructor) {
                    // transforms constructor arguments into individual select
                    // expressions
                    sqlSelect.addAll(((EConstructor<?>) selectExpr).getArgs());
                } else {
                    sqlSelect.add(selectExpr);
                }
            }
            handle(", ", sqlSelect);
        }
        append(patterns.from());
        if (joins.isEmpty()) {
            // TODO : disallow usage of dummy table ?!?
            append(patterns.dummyTable());

        }
        for (int i = 0; i < joins.size(); i++) {
            JoinExpression<Object> je = joins.get(i);
            if (i > 0) {
                String sep = ", ";
                switch (je.getType()) {
                case FULLJOIN:
                    sep = patterns.fullJoin();
                    break;
                case INNERJOIN:
                    sep = patterns.innerJoin();
                    break;
                case JOIN:
                    sep = patterns.join();
                    break;
                case LEFTJOIN:
                    sep = patterns.leftJoin();
                    break;
                }
                append(sep);
            }

            // type specifier
            if (je.getTarget() instanceof PEntity && patterns.supportsAlias()) {
                PEntity<?> pe = (PEntity<?>) je.getTarget();
                if (pe.getMetadata().getParent() == null) {
                    append(pe.getEntityName()).append(patterns.tableAlias());
                }
            }
            handle(je.getTarget());
            if (je.getCondition() != null) {
                append(patterns.on()).handle(je.getCondition());
            }
        }

        if (where != null) {
            append(patterns.where()).handle(where);
        }
        if (!groupBy.isEmpty()) {
            append(patterns.groupBy()).handle(", ", groupBy);
        }
        if (having != null) {
            if (groupBy.isEmpty()) {
                throw new IllegalArgumentException(
                        "having, but not groupBy was given");
            }
            append(patterns.having()).handle(having);
        }

        beforeOrderBy();

        Long limit = metadata.getModifiers().getLimit();
        Long offset = metadata.getModifiers().getOffset();

        if (!patterns.limitAndOffsetSymbols()
                && metadata.getModifiers().isRestricting() && !forCountRow) {
            if (where == null)
                append(patterns.where());
            append(patterns.limitOffsetCondition(limit, offset));
        }

        if (!orderBy.isEmpty() && !forCountRow) {
            append(patterns.orderBy());
            boolean first = true;
            for (OrderSpecifier<?> os : orderBy) {
                if (!first)
                    builder.append(", ");
                handle(os.getTarget());
                append(os.getOrder() == Order.ASC ? patterns.asc() : patterns.desc());
                first = false;
            }
        }
        if (patterns.limitAndOffsetSymbols()
                && metadata.getModifiers().isRestricting() && !forCountRow) {
            if (limit != null) {
                append(patterns.limit()).append(String.valueOf(limit));
            }
            if (offset != null) {
                append(patterns.offset()).append(String.valueOf(offset));
            }
        }
    }

    public void serializeUnion(SubQuery<Object, ?>[] sqs,
            List<OrderSpecifier<?>> orderBy) {
        // union
        handle(patterns.union(), Arrays.asList(sqs));

        // order by
        if (!orderBy.isEmpty()) {
            append(patterns.orderBy());
            boolean first = true;
            for (OrderSpecifier<?> os : orderBy) {
                if (!first)
                    builder.append(", ");
                handle(os.getTarget());
                append(os.getOrder() == Order.ASC ? patterns.asc() : patterns.desc());
                first = false;
            }
        }

    }

//    protected void visit(AToPath expr) {
//        handle(expr.getFrom()).append(patterns.tableAlias()).visit(expr.getTo());
//    }
//
//    @Override
//    protected void visit(ASimple<?> expr) {
//        handle(expr.getFrom()).append(patterns.columnAlias()).append(expr.getTo());
//    }

    @Override
    protected void visit(EConstant<?> expr) {
        append("?");
        constants.add(expr.getConstant());
    }

    private void visitCast(Operator<?> operator, Expr<?> source, Class<?> targetType) {
        // TODO : move constants to SqlOps
        append("cast(").handle(source);
        append(" as ");
        append(patterns.getClass2Type().get(targetType)).append(")");

    }

    @Override
    protected void visitOperation(Class<?> type, Operator<?> operator,
            List<Expr<?>> args) {
        if (operator.equals(Ops.STRING_CAST)) {
            visitCast(operator, args.get(0), String.class);
        } else if (operator.equals(Ops.NUMCAST)) {
            visitCast(operator, args.get(0), (Class<?>) ((EConstant<?>) args
                    .get(1)).getConstant());
        } else {
            super.visitOperation(type, operator, args);
        }
    }

//    protected void visit(Projection expr) {
//        visit((PEntity<?>) expr);
//    }

    protected void visit(SubQuery<Object, ?> query) {
        append("(");
        serialize(query.getMetadata(), false);
        append(")");
    }

    protected void visit(SumOver<?> expr) {
        append(patterns.sum()).append("(").handle(expr.getTarget()).append(") ");
        append(patterns.over());
        append(" (");
        if (expr.getPartitionBy() != null) {
            append(patterns.partitionBy()).handle(expr.getPartitionBy());
        }
        if (!expr.getOrderBy().isEmpty()) {
            append(patterns.orderBy()).handle(", ", expr.getOrderBy());
        }
        append(")");
    }

}
