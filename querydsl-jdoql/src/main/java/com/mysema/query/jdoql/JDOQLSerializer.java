/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.jdoql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import org.apache.commons.lang.ClassUtils;

import com.mysema.query.JoinExpression;
import com.mysema.query.QueryMetadata;
import com.mysema.query.serialization.SerializerBase;
import com.mysema.query.types.Constant;
import com.mysema.query.types.Expr;
import com.mysema.query.types.Operation;
import com.mysema.query.types.Operator;
import com.mysema.query.types.Ops;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Path;
import com.mysema.query.types.SubQuery;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EStringConst;
import com.mysema.query.types.operation.OSimple;
import com.mysema.query.types.path.PEntity;

/**
 * JDOQLSerializer serializes Querydsl queries and expressions into JDOQL strings
 * 
 * @author tiwe
 * 
 */
public final class JDOQLSerializer extends SerializerBase<JDOQLSerializer> {
    
    private static final String COMMA = ", ";

    private static final String FROM = "\nFROM ";

    private static final String GROUP_BY = "\nGROUP BY ";

    private static final String HAVING = "\nHAVING ";

    private static final String ORDER_BY = "\nORDER BY ";

    private static final String PARAMETERS = "\nPARAMETERS ";

    private static final String RANGE = "\nRANGE ";

    private static final String SELECT = "SELECT ";

    private static final String SELECT_COUNT = "SELECT count(";
    
    private static final String SELECT_COUNT_THIS = "SELECT count(this)\n";

    private static final String SELECT_DISTINCT = "SELECT DISTINCT ";

    private static final String SELECT_UNIQUE = "SELECT UNIQUE ";

    private static final String THIS = "this";

    private static final String VARIABLES = "\nVARIABLES ";

    private static final String WHERE = "\nWHERE ";
   
    private static Comparator<Map.Entry<Object,String>> comparator = new Comparator<Map.Entry<Object,String>>(){
        @Override
        public int compare(Entry<Object, String> o1, Entry<Object, String> o2) {
            return o1.getValue().compareTo(o2.getValue());
        }        
    };

    private Expr<?> candidatePath;
    
    private List<Object> constants = new ArrayList<Object>();
    
    public JDOQLSerializer(JDOQLTemplates patterns, Expr<?> candidate) {
        super(patterns);
        this.candidatePath = candidate;
    }
        
    public Expr<?> getCandidatePath() {
        return candidatePath;
    }

    public List<Object> getConstants() {
        return constants;
    }

    @SuppressWarnings("unchecked")
    private Expr<?> regexToLike(Operation<?,?> operation) {
        List<Expr<?>> args = new ArrayList<Expr<?>>();
        for (Expr<?> arg : operation.getArgs()){
            if (!arg.getType().equals(String.class)){
                args.add(arg);
            }else if (arg instanceof Constant){
                args.add(regexToLike(arg.toString()));
            }else if (arg instanceof Operation){
                args.add(regexToLike((Operation)arg));
            }else{
                args.add(arg);
            }
        }
        return OSimple.create(
                operation.getType(),
                operation.getOperator(), 
                args.<Expr<?>>toArray(new Expr[args.size()]));
    }

    private Expr<?> regexToLike(String str){
        return EStringConst.create(str.replace(".*", "%").replace(".", "_"));
    }
    
    @SuppressWarnings("unchecked")
    public void serialize(QueryMetadata metadata, boolean forCountRow, boolean subquery) {
        List<? extends Expr<?>> select = metadata.getProjection();
        List<JoinExpression> joins = metadata.getJoins();
        Expr<?> source = joins.get(0).getTarget();
        EBoolean where = metadata.getWhere();
        List<? extends Expr<?>> groupBy = metadata.getGroupBy();
        EBoolean having = metadata.getHaving();
        List<OrderSpecifier<?>> orderBy = metadata.getOrderBy();

        // select 
        if (forCountRow) {
            if (joins.size() == 1){
                append(SELECT_COUNT_THIS);    
            }else{
                append(SELECT_COUNT);
                boolean first = true;
                for (JoinExpression je : joins){
                    if (!first){
                        append(COMMA);
                    }
                    handle(je.getTarget());
                    first = false;
                }
                append(")");
            }
            
            
        } else if (!select.isEmpty()) {
            if (metadata.isDistinct()){
                append(SELECT_DISTINCT);
            }else if (metadata.isUnique() && !subquery){
                append(SELECT_UNIQUE);
            }else{
                append(SELECT);
            }
            handle(COMMA, select);
        }
        
        // from        
        append(FROM);
        if (source instanceof Operation && subquery){
            handle(source);
        }else{
            append(source.getType().getName());
            if (!source.equals(candidatePath)){
                append(" ").handle(source);
            }    
        }        

        // where
        if (where != null) {
            append(WHERE).handle(where);
        }
        
        // variables
        if (joins.size() > 1){
            serializeVariables(joins);
        }        
        
        // parameters
        if (!subquery && !getConstantToLabel().isEmpty()){
            serializeParameters();    
        }        
                
        // group by
        if (!groupBy.isEmpty()) {
            append(GROUP_BY).handle(COMMA, groupBy);
        }
        
        // having
        if (having != null) {
            if (groupBy.isEmpty()) {
                throw new IllegalArgumentException(
                        "having, but not groupBy was given");
            }
            append(HAVING).handle(having);
        }
        
        // order by
        if (!orderBy.isEmpty() && !forCountRow) {
            append(ORDER_BY);
            boolean first = true;
            for (OrderSpecifier<?> os : orderBy) {
                if (!first){
                    append(COMMA);
                }                    
                handle(os.getTarget());
                append(" " + os.getOrder());
                first = false;
            }
        }
        
        // range
        if (!forCountRow && metadata.getModifiers().isRestricting()){
            Long limit = metadata.getModifiers().getLimit();
            Long offset = metadata.getModifiers().getOffset();
            serializeModifiers(limit, offset);
        }
        
    }

    private void serializeModifiers(@Nullable Long limit, @Nullable Long offset) {
        append(RANGE);
        if (offset != null){
            append(String.valueOf(offset));
            if (limit != null){
                append(COMMA);
                append(String.valueOf(offset + limit));    
            }                
        }else{
            append("0, ").append(String.valueOf(limit));
        }
    }
    
    private void serializeParameters() {
        append(PARAMETERS);
        boolean first = true;
        List<Map.Entry<Object, String>> entries = new ArrayList<Map.Entry<Object, String>>(getConstantToLabel().entrySet());
        Collections.sort(entries, comparator);            
        for (Map.Entry<Object, String> entry : entries){
            if (!first){
                append(COMMA);
            }
            constants.add(entry.getKey());
            append(entry.getKey().getClass().getName()).append(" ").append(entry.getValue());
            first = false;
        }
    }

    @SuppressWarnings("unchecked")
    private void serializeVariables(List<JoinExpression> joins) {
        append(VARIABLES);
        for (int i = 1; i < joins.size(); i++) {                
            JoinExpression je = joins.get(i);
            if (i > 1) {
                append("; ");
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
    }

    @Override
    public void visit(Path<?> path) {
        if (path.equals(candidatePath)) {
            append(THIS);
        } else {
            super.visit(path);
        }
    }

    @Override
    public void visit(SubQuery<?> query) {
        append("(");
        serialize(query.getMetadata(), false, true);
        append(")");
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void visitOperation(Class<?> type, Operator<?> operator, List<Expr<?>> args) {
        // TODO : these should be handled as serialization patterns
        if (operator.equals(Ops.INSTANCE_OF)) {
            handle(args.get(0)).append(" instanceof ");
            append(((Constant<Class<?>>) args.get(1)).getConstant().getName());
            
        } else if (operator.equals(Ops.MATCHES)){
            // switch from regex to like if the regex expression is an operation
            if (args.get(1) instanceof Operation){
                operator = Ops.LIKE;
                args = Arrays.asList(args.get(0), regexToLike((Operation<?, ?>) args.get(1)));                
            }
            super.visitOperation(type, operator, args);
            
        } else if (operator.equals(Ops.NUMCAST)) {
            Class<?> clazz = ((Constant<Class<?>>)args.get(1)).getConstant();
            if (Number.class.isAssignableFrom(clazz) && ClassUtils.wrapperToPrimitive(clazz) != null){
                clazz = ClassUtils.wrapperToPrimitive(clazz);
            }
            append("(",clazz.getSimpleName(),")").handle(args.get(0));
            
        } else {
            super.visitOperation(type, operator, args);
        }
    }


    
    
}
