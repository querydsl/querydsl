/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import java.util.ArrayList;
import java.util.List;

import com.mysema.query.JoinExpression;
import com.mysema.query.QueryMetadata;
import com.mysema.query.serialization.BaseSerializer;
import com.mysema.query.types.Order;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.SubQuery;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EConstant;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.Operator;
import com.mysema.query.types.operation.Ops;
import com.mysema.query.types.path.PCollection;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.Path;
import com.mysema.query.types.path.PathType;

/**
 * HqlSerializer serializes querydsl expressions into HQL syntax.
 * 
 * @author tiwe
 * @version $Id$
 */
public class HQLSerializer extends BaseSerializer<HQLSerializer> {

    private boolean wrapElements = false;

    public HQLSerializer(HQLPatterns ops) {
        super(ops);
    }

    public void serialize(QueryMetadata<HQLJoinMeta> metadata,
            boolean forCountRow) {
        List<? extends Expr<?>> select = metadata.getProjection();
        List<JoinExpression<HQLJoinMeta>> joins = metadata.getJoins();
        EBoolean where = metadata.getWhere();
        List<? extends Expr<?>> groupBy = metadata.getGroupBy();
        EBoolean having = metadata.getHaving();
        List<OrderSpecifier<?>> orderBy = metadata.getOrderBy();

        if (forCountRow) {
            append("select count(*)\n");
        } else if (!select.isEmpty()) {
            if (!metadata.isDistinct()) {
                append("select ");
            } else {
                append("select distinct ");
            }
            handle(", ", select).append("\n");
        }
        append("from ");
        for (int i = 0; i < joins.size(); i++) {
            JoinExpression<HQLJoinMeta> je = joins.get(i);
            if (i > 0) {
                String sep = ", ";
                switch (je.getType()) {
                case FULLJOIN:
                    sep = "\n  full join ";
                    break;
                case INNERJOIN:
                    sep = "\n  inner join ";
                    break;
                case JOIN:
                    sep = "\n  join ";
                    break;
                case LEFTJOIN:
                    sep = "\n  left join ";
                    break;
                }
                append(sep);
            }
            if (je.getMetadata() != null) {
                switch (je.getMetadata()) {
                case FETCH:
                    if (!forCountRow)
                        append("fetch ");
                    break;
                }
            }

            // type specifier
            if (je.getTarget() instanceof PEntity) {
                PEntity<?> pe = (PEntity<?>) je.getTarget();
                if (pe.getMetadata().getParent() == null) {
                    String pn = pe.getType().getPackage().getName();
                    String typeName = pe.getType().getName().substring(
                            pn.length() + 1);
                    append(typeName).append(" ");
                }
            }
            handle(je.getTarget());
            if (je.getCondition() != null) {
                append(" with ").handle(je.getCondition());
            }
        }

        if (where != null) {
            append("\nwhere ").handle(where);
        }
        if (!groupBy.isEmpty()) {
            append("\ngroup by ").handle(", ", groupBy);
        }
        if (having != null) {
            if (groupBy.isEmpty()) {
                throw new IllegalArgumentException(
                        "having, but not groupBy was given");
            }
            append("\nhaving ").handle(having);
        }
        if (!orderBy.isEmpty() && !forCountRow) {
            append("\norder by ");
            boolean first = true;
            for (OrderSpecifier<?> os : orderBy) {
                if (!first)
                    builder.append(", ");
                handle(os.getTarget());
                append(os.getOrder() == Order.ASC ? " asc" : " desc");
                first = false;
            }
        }
    }

    @Override
    protected void visit(EConstant<?> expr) {
        boolean wrap = expr.getConstant().getClass().isArray();
        if (wrap) {
            append("(");
        }
        append(":a");
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

    @Override
    protected void visit(PCollection<?> expr) {
        // only wrap a PathCollection, if it the pathType is PROPERTY
        boolean wrap = wrapElements
                && expr.getMetadata().getPathType().equals(PathType.PROPERTY);
        if (wrap) {
            append("elements(");
        }
        visit((Path<?>) expr);
        if (wrap) {
            append(")");
        }
    }

    protected void visit(SubQuery<HQLJoinMeta, ?> query) {
        append("(");
        serialize(query.getMetadata(), false);
        append(")");
    }

    private void visitCast(Operator<?> operator, Expr<?> source, Class<?> targetType) {
        append("cast(").handle(source);
        append(" as ");
        append(targetType.getSimpleName().toLowerCase()).append(")");

    }

    protected void visitOperation(Class<?> type, Operator<?> operator,
            List<Expr<?>> args) {
        boolean old = wrapElements;
        wrapElements = HQLPatterns.wrapCollectionsForOp.contains(operator);
        // 
        if (operator.equals(Ops.ISTYPEOF)) {
            args = new ArrayList<Expr<?>>(args);
            args.set(1, new EConstant<String>(((Class<?>) ((EConstant<?>) args
                    .get(1)).getConstant()).getName()));
            super.visitOperation(type, operator, args);
        } else if (operator.equals(Ops.STRING_CAST)) {
            visitCast(operator, args.get(0), String.class);
        } else if (operator.equals(Ops.NUMCAST)) {
            visitCast(operator, args.get(0), (Class<?>) ((EConstant<?>) args
                    .get(1)).getConstant());
        } else {
            super.visitOperation(type, operator, args);
        }
        //
        wrapElements = old;
    }

}
