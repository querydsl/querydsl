/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.jdoql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ClassUtils;

import com.mysema.query.JoinExpression;
import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryModifiers;
import com.mysema.query.serialization.BaseSerializer;
import com.mysema.query.types.ListSubQuery;
import com.mysema.query.types.ObjectSubQuery;
import com.mysema.query.types.Order;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.SubQuery;
import com.mysema.query.types.expr.EBoolean;
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

    private Expr<?> candidatePath;
    
    private List<Object> constants = new ArrayList<Object>();
    
    public JDOQLSerializer(JDOQLPatterns patterns, Expr<?> candidate) {
        super(patterns);
        this.candidatePath = candidate;
    }
    
    public void serialize(QueryMetadata<Object> metadata, boolean forCountRow, boolean subquery) {
        List<? extends Expr<?>> select = metadata.getProjection();
        List<JoinExpression<Object>> joins = metadata.getJoins();
        Expr<?> source = joins.get(0).getTarget();
        EBoolean where = metadata.getWhere();
        List<? extends Expr<?>> groupBy = metadata.getGroupBy();
        EBoolean having = metadata.getHaving();
        List<OrderSpecifier<?>> orderBy = metadata.getOrderBy();

        //SELECT
        if (forCountRow) {
            append("SELECT count(this)\n");
        } else if (!select.isEmpty()) {
            if (metadata.isDistinct()){
                append("SELECT DISTINCT ");
            }else if (metadata.isUnique()){
                append("SELECT UNIQUE ");
            }else{
                append("SELECT ");
            }
            handle(", ", select);
        }
        
        // FROM
        append("\nFROM ").append(source.getType().getName());
        if (!source.equals(candidatePath)){
            append(" ").handle(source);
        }

        // WHERE
        if (where != null) {
            append("\nWHERE ").handle(where);
        }
        
        // VARIABLES
        for (int i = 1; i < joins.size(); i++) {
            append("\nVARIABLES ");
            JoinExpression<Object> je = joins.get(i);
            if (i > 1) {
                append(", ");
            }
            
            // type specifier
            if (je.getTarget() instanceof PEntity) {
                PEntity<?> pe = (PEntity<?>) je.getTarget();
                if (pe.getMetadata().getParent() == null) {
                    append(pe.getType().getName()).append(" ");
                }
            }
            handle(je.getTarget());
        }
        
        // PARAMETERS
        if (!subquery && !getConstantToLabel().isEmpty()){
            append("\nPARAMETERS ");
            boolean first = true;
            for (Map.Entry<Object, String> entry : getConstantToLabel().entrySet()){
                if (!first){
                    append(", ");
                }
                constants.add(entry.getKey());
                append(entry.getKey().getClass().getName()).append(" ").append(entry.getValue());
                first = false;
            }    
        }        
                
        // GROUP BY
        if (!groupBy.isEmpty()) {
            append("\nGROUP BY ").handle(", ", groupBy);
        }
        // HAVING
        if (having != null) {
            if (groupBy.isEmpty()) {
                throw new IllegalArgumentException(
                        "having, but not groupBy was given");
            }
            append("\nHAVING ").handle(having);
        }
        // ORDER BY
        if (!orderBy.isEmpty() && !forCountRow) {
            append("\nORDER BY ");
            boolean first = true;
            for (OrderSpecifier<?> os : orderBy) {
                if (!first){
                    builder.append(", ");
                }                    
                handle(os.getTarget());
                append(os.getOrder() == Order.ASC ? " ASC" : " DESC");
                first = false;
            }
        }
        // RANGE
        if (!forCountRow && metadata.getModifiers().isRestricting()){
            QueryModifiers modifiers = metadata.getModifiers();
            append("\nRANGE ");
            if (modifiers.getOffset() != null){
                append(String.valueOf(modifiers.getOffset()));
                if (modifiers.getLimit() != null){
                    append(", ");
                    append(String.valueOf(modifiers.getOffset() + modifiers.getLimit()));    
                }                
            }else{
                append("0, ").append(String.valueOf(modifiers.getLimit()));
            }
        }
        
    }
    
    protected void visit(ObjectSubQuery<Object, ?> query) {
        visit((SubQuery<Object>)query);
    }
    
    protected void visit(ListSubQuery<Object, ?> query) {
        visit((SubQuery<Object>)query);
    }
    
    protected void visit(SubQuery<Object> query) {
        append("(");
        serialize(query.getMetadata(), false, true);
        append(")");
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void visitOperation(Class<?> type, Operator<?> operator,
            List<Expr<?>> args) {
        // TODO : these should be handled as serialization patterns
        if (operator.equals(Ops.INSTANCEOF)) {
            handle(args.get(0)).append(" instanceof ");
            append(((EConstant<Class<?>>) args.get(1)).getConstant().getName());
        } else if (operator.equals(Ops.STRING_CAST)) {
            append("(String)").handle(args.get(0));
        } else if (operator.equals(Ops.NUMCAST)) {
            Class<?> clazz = ((EConstant<Class<?>>)args.get(1)).getConstant();
            if (Number.class.isAssignableFrom(clazz) && ClassUtils.wrapperToPrimitive(clazz) != null){
                clazz = ClassUtils.wrapperToPrimitive(clazz);
            }
            append("(",clazz.getSimpleName(),")").handle(args.get(0));
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

    public Expr<?> getCandidatePath() {
        return candidatePath;
    }

    public List<Object> getConstants() {
        return constants;
    }
    
    
}
