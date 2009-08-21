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
import com.mysema.query.serialization.SerializerBase;
import com.mysema.query.types.Order;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.expr.Constant;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EConstructor;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.Operator;
import com.mysema.query.types.operation.Ops;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.query.ListSubQuery;
import com.mysema.query.types.query.ObjectSubQuery;
import com.mysema.query.types.query.SubQuery;

/**
 * SqlSerializer serializes Querydsl queries into SQL
 * 
 * @author tiwe
 * @version $Id$
 */
public class SQLSerializer extends SerializerBase<SQLSerializer> {

    private final List<Object> constants = new ArrayList<Object>();
    
    protected final SQLTemplates templates;

    public SQLSerializer(SQLTemplates templates) {
        super(templates);
        this.templates = templates;
    }

    protected void beforeOrderBy() {
        // template method, for subclasses do override
    }
    
    public List<Object> getConstants(){
        return constants;
    }

    @SuppressWarnings("unchecked")
    public void serialize(QueryMetadata metadata, boolean forCountRow) {
        List<? extends Expr<?>> select = metadata.getProjection();
        List<JoinExpression> joins = metadata.getJoins();
        EBoolean where = metadata.getWhere();
        List<? extends Expr<?>> groupBy = metadata.getGroupBy();
        EBoolean having = metadata.getHaving();
        List<OrderSpecifier<?>> orderBy = metadata.getOrderBy();

        if (forCountRow) {
            append(templates.select()).append(templates.countStar());
        } else if (!select.isEmpty()) {
            if (!metadata.isDistinct()) {
                append(templates.select());
            } else {
                append(templates.selectDistinct());
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
        append(templates.from());
        if (joins.isEmpty()) {
            // TODO : disallow usage of dummy table ?!?
            append(templates.dummyTable());

        }
        for (int i = 0; i < joins.size(); i++) {
            JoinExpression je = joins.get(i);
            if (i > 0) {
                String sep = ", ";
                switch (je.getType()) {
                case FULLJOIN:
                    sep = templates.fullJoin();
                    break;
                case INNERJOIN:
                    sep = templates.innerJoin();
                    break;
                case JOIN:
                    sep = templates.join();
                    break;
                case LEFTJOIN:
                    sep = templates.leftJoin();
                    break;
                }
                append(sep);
            }

            // type specifier
            if (je.getTarget() instanceof PEntity && templates.supportsAlias()) {
                PEntity<?> pe = (PEntity<?>) je.getTarget();
                if (pe.getMetadata().getParent() == null) {
                    append(pe.getEntityName()).append(templates.tableAlias());
                }
            }
            handle(je.getTarget());
            if (je.getCondition() != null) {
                append(templates.on()).handle(je.getCondition());
            }
        }

        if (where != null) {
            append(templates.where()).handle(where);
        }
        if (!groupBy.isEmpty()) {
            append(templates.groupBy()).handle(", ", groupBy);
        }
        if (having != null) {
            if (groupBy.isEmpty()) {
                throw new IllegalArgumentException(
                        "having, but not groupBy was given");
            }
            append(templates.having()).handle(having);
        }

        beforeOrderBy();

        Long limit = metadata.getModifiers().getLimit();
        Long offset = metadata.getModifiers().getOffset();

        if (!templates.limitAndOffsetSymbols()
                && metadata.getModifiers().isRestricting() && !forCountRow) {
            if (where == null){
                append(templates.where());
            }                
            append(templates.limitOffsetCondition(limit, offset));
        }

        if (!orderBy.isEmpty() && !forCountRow) {
            append(templates.orderBy());
            boolean first = true;
            for (OrderSpecifier<?> os : orderBy) {
                if (!first){
                    append(", ");
                }                    
                handle(os.getTarget());
                append(os.getOrder() == Order.ASC ? templates.asc() : templates.desc());
                first = false;
            }
        }
        if (templates.limitAndOffsetSymbols()
                && metadata.getModifiers().isRestricting() && !forCountRow) {
            if (limit != null) {
                append(templates.limit()).append(String.valueOf(limit));
            }
            if (offset != null) {
                append(templates.offset()).append(String.valueOf(offset));
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void serializeUnion(SubQuery[] sqs,
            List<OrderSpecifier<?>> orderBy) {
        // union
        handle(templates.union(), (List)Arrays.asList(sqs));

        // order by
        if (!orderBy.isEmpty()) {
            append(templates.orderBy());
            boolean first = true;
            for (OrderSpecifier<?> os : orderBy) {
                if (!first){
                    append(", ");
                }                    
                handle(os.getTarget());
                append(os.getOrder() == Order.ASC ? templates.asc() : templates.desc());
                first = false;
            }
        }

    }

    @Override
    protected void visit(Constant<?> expr) {
        append("?");
        constants.add(expr.getConstant());
    }

    private void visitCast(Operator<?> operator, Expr<?> source, Class<?> targetType) {
        // TODO : move constants to SqlOps
        append("cast(").handle(source);
        append(" as ");
        append(templates.getClass2Type().get(targetType)).append(")");

    }

    @Override
    protected void visitOperation(Class<?> type, Operator<?> operator,
            List<Expr<?>> args) {
        if (operator.equals(Ops.STRING_CAST)) {
            visitCast(operator, args.get(0), String.class);
        } else if (operator.equals(Ops.NUMCAST)) {
            visitCast(operator, args.get(0), (Class<?>) ((Constant<?>) args.get(1)).getConstant());
        } else {
            super.visitOperation(type, operator, args);
        }
    }

    protected void visit(ObjectSubQuery<?> query) {
        visit((SubQuery)query);
    }
    
    protected void visit(ListSubQuery<?> query) {
        visit((SubQuery)query);
    }
    
    protected void visit(SubQuery query) {
        append("(");
        serialize(query.getMetadata(), false);
        append(")");
    }

    protected void visit(SumOver<?> expr) {
        append(templates.sum()).append("(").handle(expr.getTarget()).append(") ");
        append(templates.over());
        append(" (");
        if (expr.getPartitionBy() != null) {
            append(templates.partitionBy()).handle(expr.getPartitionBy());
        }
        if (!expr.getOrderBy().isEmpty()) {
            append(templates.orderBy()).handle(", ", expr.getOrderBy());
        }
        append(")");
    }

}
