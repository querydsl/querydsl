/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;

import com.mysema.query.JoinExpression;
import com.mysema.query.QueryException;
import com.mysema.query.QueryMetadata;
import com.mysema.query.serialization.SerializerBase;
import com.mysema.query.sql.oracle.SumOver;
import com.mysema.query.types.Order;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.expr.Constant;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EConstructor;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.Operator;
import com.mysema.query.types.operation.Ops;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.query.SubQuery;

/**
 * SqlSerializer serializes Querydsl queries into SQL
 * 
 * @author tiwe
 * @version $Id$
 */
public class SQLSerializer extends SerializerBase<SQLSerializer> {
    
    private static final String COMMA = ", ";

    private final List<Object> constants = new ArrayList<Object>();
    
    private final SQLTemplates templates;

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
    
    protected SQLTemplates getTemplates(){
        return templates;
    }
    
    @SuppressWarnings("unchecked")
    private void handleJoinTarget(JoinExpression je) {
        // type specifier
        if (je.getTarget() instanceof PEntity && templates.isSupportsAlias()) {
            PEntity<?> pe = (PEntity<?>) je.getTarget();
            if (pe.getMetadata().getParent() == null) {
                String table = pe.getType().getAnnotation(Table.class).value();
                append(table).append(templates.getTableAlias());
            }
        }
        handle(je.getTarget());
    }

    @SuppressWarnings("unchecked")
    public void serialize(QueryMetadata metadata, boolean forCountRow) {
        List<? extends Expr<?>> select = metadata.getProjection();
        List<JoinExpression> joins = metadata.getJoins();
        EBoolean where = metadata.getWhere();
        List<? extends Expr<?>> groupBy = metadata.getGroupBy();
        EBoolean having = metadata.getHaving();
        List<OrderSpecifier<?>> orderBy = metadata.getOrderBy();

        List<Expr<?>> sqlSelect = new ArrayList<Expr<?>>();
        for (Expr<?> selectExpr : select) {
            if (selectExpr instanceof EConstructor) {
                // transforms constructor arguments into individual select expressions
                sqlSelect.addAll(((EConstructor<?>) selectExpr).getArgs());
            } else {
                sqlSelect.add(selectExpr);
            }
        }
        
        // select 
        if (forCountRow) {            
            append(templates.getSelect());
            if (!metadata.isDistinct()){
                append(templates.getCountStar());
            }else{
                append(templates.getDistinctCountStart());
                if (sqlSelect.isEmpty()){                    
                    List<Expr<?>> columns = getIdentifierColumns(joins);
                    handle(columns.get(0));
//                    handle(COMMA, columns);
                }else{
                    handle(COMMA, sqlSelect);    
                }                
                append(templates.getDistinctCountEnd());
            }
            
        } else if (!sqlSelect.isEmpty()) {
            if (!metadata.isDistinct()) {
                append(templates.getSelect());
            } else {
                append(templates.getSelectDistinct());
            }            
            handle(COMMA, sqlSelect);
        }
        
        // from
        serializeSources(joins);

        // where 
        if (where != null) {
            append(templates.getWhere()).handle(where);
        }
        
        // group by
        if (!groupBy.isEmpty()) {
            append(templates.getGroupBy()).handle(COMMA, groupBy);
        }
        
        // having
        if (having != null) {
            if (groupBy.isEmpty()) {
                throw new IllegalArgumentException("having, but not groupBy was given");
            }
            append(templates.getHaving()).handle(having);
        }

        beforeOrderBy();

        // order by
        if (!orderBy.isEmpty() && !forCountRow) {
            serializeOrderBy(orderBy);
        }
        
        // limit & offset
        if (metadata.getModifiers().isRestricting() && !forCountRow){
            Long limit = metadata.getModifiers().getLimit();
            Long offset = metadata.getModifiers().getOffset();
            serializeModifiers(limit, offset);
        }
        
    }

    private List<Expr<?>> getIdentifierColumns(List<JoinExpression> joins) {
        // TODO : get only identifier columns, maybe from @Table annotation
        try {
            List<Expr<?>> columns = new ArrayList<Expr<?>>();
            for (JoinExpression j : joins) {
                for (Field field : j.getTarget().getClass().getFields()) {
                    if (Expr.class.isAssignableFrom(field.getType())){
                        Expr<?> column = (Expr<?>) field.get(j.getTarget());
                        columns.add(column);    
                    }                    
                }
            }
            return columns;
        } catch (IllegalArgumentException e) {
            throw new QueryException(e);
        } catch (IllegalAccessException e) {
            throw new QueryException(e);
        }
            
    }

    private void serializeSources(List<JoinExpression> joins) {
        append(templates.getFrom());
        if (joins.isEmpty()) {
            // TODO : disallow usage of dummy table ?!?
            append(templates.getDummyTable());

        }
        for (int i = 0; i < joins.size(); i++) {
            JoinExpression je = joins.get(i);
            if (i > 0) {
                append(templates.getJoinSymbol(je.getType()));
            }
            handleJoinTarget(je);            
            if (je.getCondition() != null) {
                append(templates.getOn()).handle(je.getCondition());
            }
        }
    }

    private void serializeOrderBy(List<OrderSpecifier<?>> orderBy) {
        append(templates.getOrderBy());
        boolean first = true;
        for (OrderSpecifier<?> os : orderBy) {
            if (!first){
                append(COMMA);
            }                    
            handle(os.getTarget());
            append(os.getOrder() == Order.ASC ? templates.getAsc() : templates.getDesc());
            first = false;
        }
    }

    private void serializeModifiers(Long limit, Long offset) {
        if (!templates.isLimitAndOffsetSymbols()){
            append(" ");
            append(templates.getLimitOffsetCondition(limit, offset));
        }else{
            if (limit != null) {
                append(templates.getLimit()).append(String.valueOf(limit));
            }
            if (offset != null) {
                append(templates.getOffset()).append(String.valueOf(offset));
            }   
        }
    }
    
    public void serializeForDelete(QueryMetadata md) {
        append(templates.getDeleteFrom());
        handleJoinTarget(md.getJoins().get(0));        
        if (md.getWhere() != null) {
            append(templates.getWhere()).handle(md.getWhere());
        }
    }

    public void serializeForUpdate(QueryMetadata md) {
        append(templates.getUpdate());
        handleJoinTarget(md.getJoins().get(0));
        append("\nset ");
        handle(COMMA, md.getProjection());
        if (md.getWhere() != null) {
            append(templates.getWhere()).handle(md.getWhere());
        }
    }

    public void serializeForInsert(PEntity<?> entity, List<Expr<?>> columns, List<Expr<?>> values, @Nullable SubQuery<?> subQuery) {
        append(templates.getInsertInto());
        handle(entity);
        // columns
        if (!columns.isEmpty()){
            append("(");
            handle(COMMA, columns);
            append(")");
        }
        
        if (subQuery != null){
            append("\n");
            serialize(subQuery.getMetadata(), false);
        }else{            
            // values
            append(templates.getValues());
            append("(");
            handle(COMMA, values);
            append(")");            
        }
    }


    @SuppressWarnings("unchecked")
    public void serializeUnion(SubQuery[] sqs,
            List<OrderSpecifier<?>> orderBy) {
        // union
        handle(templates.getUnion(), (List)Arrays.asList(sqs));

        // order by
        if (!orderBy.isEmpty()) {
            append(templates.getOrderBy());
            boolean first = true;
            for (OrderSpecifier<?> os : orderBy) {
                if (!first){
                    append(COMMA);
                }                    
                handle(os.getTarget());
                append(os.getOrder() == Order.ASC ? templates.getAsc() : templates.getDesc());
                first = false;
            }
        }

    }

    @SuppressWarnings("unchecked")
    @Override
    public void visit(Constant<?> expr) {
        if (expr.getConstant() instanceof Collection){
            append("(");
            boolean first = true;
            for (Object o : ((Collection)expr.getConstant())){
                if (!first){
                    append(COMMA);
                }
                append("?");
                constants.add(o);
                first = false;
            }
            append(")");
        }else{
            append("?");
            constants.add(expr.getConstant());    
        }        
    }

    @Override
    public void visit(SubQuery query) {
        append("(");
        serialize(query.getMetadata(), false);
        append(")");
    }

    public void visit(SumOver<?> expr) {
        append(templates.getSum()).append("(").handle(expr.getTarget()).append(") ");
        append(templates.getOver());
        append(" (");
        if (expr.getPartitionBy() != null) {
            append(templates.getPartitionBy()).handle(expr.getPartitionBy());
        }
        if (!expr.getOrderBy().isEmpty()) {
            append(templates.getOrderBy()).handle(COMMA, expr.getOrderBy());
        }
        append(")");
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

}
