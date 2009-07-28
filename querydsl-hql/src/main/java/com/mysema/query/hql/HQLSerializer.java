/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.mysema.query.JoinExpression;
import com.mysema.query.QueryMetadata;
import com.mysema.query.serialization.BaseSerializer;
import com.mysema.query.types.Order;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EConstant;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.Operator;
import com.mysema.query.types.operation.Ops;
import com.mysema.query.types.path.PCollection;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.Path;
import com.mysema.query.types.path.PathType;
import com.mysema.query.types.query.ListSubQuery;
import com.mysema.query.types.query.ObjectSubQuery;
import com.mysema.query.types.query.SubQuery;

/**
 * HQLSerializer serializes querydsl expressions into HQL syntax.
 * 
 * @author tiwe
 * @version $Id$
 */
public class HQLSerializer extends BaseSerializer<HQLSerializer> {

    private boolean wrapElements = false;

    public HQLSerializer(HQLPatterns patterns) {
        super(patterns);
    }

    @SuppressWarnings("unchecked")
    public void serialize(QueryMetadata metadata,
            boolean forCountRow) {
        List<? extends Expr<?>> select = metadata.getProjection();
        List<JoinExpression> joins = metadata.getJoins();
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
            JoinExpression je = joins.get(i);
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
            if (je.isFetch() && !forCountRow){
                append("fetch ");
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
                if (!first){
                    builder.append(", ");
                }                    
                handle(os.getTarget());
                append(os.getOrder() == Order.ASC ? " asc" : " desc");
                first = false;
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void visit(EConstant<?> expr) {
        boolean wrap = expr.getConstant().getClass().isArray()
            || expr.getConstant() instanceof Collection;
        if (wrap) {
            append("(");
        }
        append(":");
        if (!constantToLabel.containsKey(expr.getConstant())) {
            String constLabel = constantPrefix + (constantToLabel.size()+1);
            constantToLabel.put(expr.getConstant(), constLabel);
            append(constLabel);
        } else {
            append(constantToLabel.get(expr.getConstant()));
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

    private void visitCast(Operator<?> operator, Expr<?> source, Class<?> targetType) {
        append("cast(").handle(source);
        append(" as ");
        append(targetType.getSimpleName().toLowerCase()).append(")");
    }

    @SuppressWarnings("unchecked")
    protected void visitOperation(Class<?> type, Operator<?> operator,
            List<Expr<?>> args) {
        boolean old = wrapElements;
        wrapElements = HQLPatterns.wrapCollectionsForOp.contains(operator);
        // 
        if (operator.equals(Ops.INSTANCEOF)) {
            args = new ArrayList<Expr<?>>(args);
            args.set(1, EConstant.create(((Class<?>) ((EConstant<?>) args
                    .get(1)).getConstant()).getName()));
            super.visitOperation(type, operator, args);
            
        } else if (operator.equals(Ops.NUMCAST)) {
            visitCast(operator, args.get(0), (Class<?>) ((EConstant<?>) args.get(1)).getConstant());
            
        } else if (operator.equals(Ops.SUBSTR1ARG)){
            args = new ArrayList<Expr<?>>(args);
            if (args.get(1) instanceof EConstant){
                int arg1 = ((EConstant<Integer>)args.get(1)).getConstant();
                args.set(1, EConstant.create(arg1 + 1));
            }else{
                throw new IllegalArgumentException("Unsupported substr variant");
            }
            super.visitOperation(type, operator, args);
            
        } else if (operator.equals(Ops.SUBSTR2ARGS)){
            args = new ArrayList<Expr<?>>(args);
            if (args.get(2) instanceof EConstant){
                int arg1 = ((EConstant<Integer>)args.get(1)).getConstant();
                int arg2 = ((EConstant<Integer>)args.get(2)).getConstant();
                args.set(1, EConstant.create(arg1 + 1));
                args.set(2, EConstant.create(arg2 - arg1));                
            }else{
                throw new IllegalArgumentException("Unsupported substr variant");
            }
            super.visitOperation(type, operator, args);
            
        } else if (operator.equals(Ops.MATCHES)){
            args = new ArrayList<Expr<?>>(args);
            if (args.get(1) instanceof EConstant){
                args.set(1, EConstant.create(args.get(1).toString().replace(".*", "%").replace(".", "_")));
            }
            super.visitOperation(type, operator, args);
            
        } else {
            super.visitOperation(type, operator, args);
        }
        //
        wrapElements = old;
    }

}
