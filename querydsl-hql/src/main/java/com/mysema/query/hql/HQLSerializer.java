/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import com.mysema.query.JoinExpression;
import com.mysema.query.JoinType;
import com.mysema.query.QueryMetadata;
import com.mysema.query.serialization.SerializerBase;
import com.mysema.query.types.Constant;
import com.mysema.query.types.Expr;
import com.mysema.query.types.Operation;
import com.mysema.query.types.Operator;
import com.mysema.query.types.Ops;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathType;
import com.mysema.query.types.SubQuery;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EStringConst;
import com.mysema.query.types.expr.ExprConst;
import com.mysema.query.types.expr.OSimple;
import com.mysema.query.types.path.PEntity;
import com.mysema.util.MathUtils;

/**
 * HQLSerializer serializes querydsl expressions into HQL syntax.
 * 
 * @author tiwe
 * @version $Id$
 */
public final class HQLSerializer extends SerializerBase<HQLSerializer> {

    private static final Set<Operator<?>> NUMERIC = new HashSet<Operator<?>>(Arrays.<Operator<?>>asList(
            Ops.ADD, Ops.SUB, Ops.MULT, Ops.DIV,
            Ops.LT, Ops.LOE, Ops.GT, Ops.GOE, Ops.BETWEEN,
            Ops.BEFORE, Ops.AFTER, Ops.BOE, Ops.AOE));
    
    private static final String SELECT_COUNT_DISTINCT = "select count(distinct ";

    private static final String AS = " as ";

    private static final String COMMA = ", ";

    private static final String DELETE = "delete ";

    private static final String FETCH = "fetch ";

    private static final String FETCH_ALL_PROPERTIES = " fetch all properties";
    
    private static final String FROM = "from ";
    
    private static final String GROUP_BY = "\ngroup by ";
    
    private static final String HAVING = "\nhaving ";
    
    private static final String ORDER_BY = "\norder by ";
    
    private static final String SELECT = "select ";
    
    private static final String SELECT_COUNT_ALL = "select count(*)\n";
    
    private static final String SELECT_DISTINCT = "select distinct ";
    
    private static final String SET = "\nset ";
    
    private static final String UPDATE = "update ";
    
    private static final String WHERE = "\nwhere ";
    
    private static final String WITH = " with ";
    
    private static final Map<JoinType, String> joinTypes = new HashMap<JoinType, String>();
    
    static{
        joinTypes.put(JoinType.DEFAULT, COMMA);
        joinTypes.put(JoinType.FULLJOIN, "\n  full join ");
        joinTypes.put(JoinType.INNERJOIN, "\n  inner join ");
        joinTypes.put(JoinType.JOIN, "\n  join ");
        joinTypes.put(JoinType.LEFTJOIN, "\n  left join ");
    }
    
    private boolean wrapElements = false;

    public HQLSerializer(HQLTemplates patterns) {
        super(patterns);
    }

    @SuppressWarnings("unchecked")
    private void handleJoinTarget(JoinExpression je) {
        // type specifier
        if (je.getTarget() instanceof PEntity) {
            PEntity<?> pe = (PEntity<?>) je.getTarget();
            if (pe.getMetadata().getParent() == null) {
                String pn = pe.getType().getPackage().getName();
                String typeName = pe.getType().getName().substring(pn.length() + 1);
                append(typeName).append(" ");
            }
        }
        handle(je.getTarget());
    }

    // TODO : generalize this!
    @SuppressWarnings("unchecked")
    private <T> Expr<?> regexToLike(Operation<T> operation) {
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

    public void serialize(QueryMetadata metadata, boolean forCountRow, @Nullable String projection) {
        List<? extends Expr<?>> select = metadata.getProjection();
        List<JoinExpression> joins = metadata.getJoins();
        EBoolean where = metadata.getWhere();
        List<? extends Expr<?>> groupBy = metadata.getGroupBy();
        EBoolean having = metadata.getHaving();
        List<OrderSpecifier<?>> orderBy = metadata.getOrderBy();

        // select
        if (projection != null){
            append(SELECT).append(projection).append("\n");
            
        }else if (forCountRow) {
            if (!metadata.isDistinct()){
                append(SELECT_COUNT_ALL);
            }else{
                append(SELECT_COUNT_DISTINCT);
                if(!select.isEmpty()){
                    handle(COMMA, select);    
                }else{
                    handle(joins.get(0).getTarget());
                }                
                append(")\n"); 
            }
            
        } else if (!select.isEmpty()) {
            if (!metadata.isDistinct()) {
                append(SELECT);
            } else {
                append(SELECT_DISTINCT);
            }
            handle(COMMA, select).append("\n");
            
        }
        
        // from
        append(FROM);
        serializeSources(forCountRow, joins);

        // where
        if (where != null) {
            append(WHERE).handle(where);
        }
        
        // group by
        if (!groupBy.isEmpty()) {
            append(GROUP_BY).handle(COMMA, groupBy);
        }
        
        // having
        if (having != null) {
            if (groupBy.isEmpty()) {
                throw new IllegalArgumentException("having, but not groupBy was given");
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
                append(" " + os.getOrder().toString().toLowerCase(Locale.ENGLISH));
                first = false;
            }
        }
    }

    public void serializeForDelete(QueryMetadata md) {
        append(DELETE);
        handleJoinTarget(md.getJoins().get(0));        
        if (md.getWhere() != null) {
            append(WHERE).handle(md.getWhere());
        }
    }

    public void serializeForUpdate(QueryMetadata md) {
        append(UPDATE);
        handleJoinTarget(md.getJoins().get(0));
        append(SET);
        handle(COMMA, md.getProjection());
        if (md.getWhere() != null) {
            append(WHERE).handle(md.getWhere());
        }
    }

    private void serializeSources(boolean forCountRow, List<JoinExpression> joins) {
        for (int i = 0; i < joins.size(); i++) {
            JoinExpression je = joins.get(i);
            if (i > 0) {
                append(joinTypes.get(je.getType()));
            }            
            if (je.hasFlag(HQLFlags.FETCH) && !forCountRow){
                append(FETCH);
            }            
            handleJoinTarget(je);
            if (je.hasFlag(HQLFlags.FETCH_ALL) && !forCountRow){
                append(FETCH_ALL_PROPERTIES);
            }

            if (je.getCondition() != null) {
                append(WITH).handle(je.getCondition());
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void visit(Constant<?> expr) {
        boolean wrap = expr.getConstant().getClass().isArray() || expr.getConstant() instanceof Collection;
        if (wrap) {
            append("(");
        }
        append(":");
        if (!getConstantToLabel().containsKey(expr.getConstant())) {
            String constLabel = getConstantPrefix() + (getConstantToLabel().size()+1);
            getConstantToLabel().put(expr.getConstant(), constLabel);
            append(constLabel);
        } else {
            append(getConstantToLabel().get(expr.getConstant()));
        }
        if (wrap) {
            append(")");
        }
    }
    
    @Override
    public void visit(SubQuery<?> query) {
        append("(");       
        serialize(query.getMetadata(), false, null);
        append(")");
    }

    private void visitCast(Expr<?> source, Class<?> targetType) {
        append("cast(").handle(source);
        append(AS);
        append(targetType.getSimpleName().toLowerCase(Locale.ENGLISH)).append(")");
    }

    @Override
    public void visit(Path<?> expr){
        // only wrap a PathCollection, if it the pathType is PROPERTY
        boolean wrap = wrapElements 
            && (Collection.class.isAssignableFrom(expr.getType()) || Map.class.isAssignableFrom(expr.getType()))
            && expr.getMetadata().getPathType().equals(PathType.PROPERTY);
        if (wrap) {
            append("elements(");
        }
        super.visit((Path<?>) expr);
        if (wrap) {
            append(")");
        }
    }

    @SuppressWarnings("unchecked")
    protected void visitOperation(Class<?> type, Operator<?> operator, List<Expr<?>> args) {
        boolean old = wrapElements;
        wrapElements = HQLTemplates.wrapCollectionsForOp.contains(operator);
        // 
        if (operator.equals(Ops.INSTANCE_OF)) {
            List<Expr<?>> newArgs = new ArrayList<Expr<?>>(args);
            newArgs.set(1, EStringConst.create(((Class<?>) ((Constant<?>) newArgs.get(1)).getConstant()).getName()));
            super.visitOperation(type, operator, newArgs);
            
        } else if (operator.equals(Ops.NUMCAST)) {
            visitCast(args.get(0), (Class<?>) ((Constant<?>) args.get(1)).getConstant());
            
        } else if (operator.equals(Ops.EXISTS) && args.get(0) instanceof SubQuery){
            SubQuery subQuery = (SubQuery) args.get(0);            
            append("exists (");
            serialize(subQuery.getMetadata(), false, "1");
            append(")");
            
        } else if (operator.equals(Ops.MATCHES)){
            List<Expr<?>> newArgs = new ArrayList<Expr<?>>(args);
            if (newArgs.get(1) instanceof Constant){
                newArgs.set(1, regexToLike(newArgs.get(1).toString()));
            }else if (newArgs.get(1) instanceof Operation){
                newArgs.set(1, regexToLike((Operation)newArgs.get(1)));
            }
            super.visitOperation(type, operator, newArgs);
            
        }else if(NUMERIC.contains(operator)){
            super.visitOperation(type, operator, normalizeNumericArgs(args));
            
        } else {
            super.visitOperation(type, operator, args);
        }
        //
        wrapElements = old;
    }

    @SuppressWarnings("unchecked")
    private List<Expr<?>> normalizeNumericArgs(List<Expr<?>> args) {
        boolean hasConstants = false;
        Class<? extends Number> numType = null;
        for (Expr<?> arg : args){
            if (Number.class.isAssignableFrom(arg.getType())){
                if (arg instanceof Constant){
                    hasConstants = true;
                }else{
                    numType = (Class<? extends Number>) arg.getType();
                }
            }
        }
        if (hasConstants && numType != null){
            List<Expr<?>> newArgs = new ArrayList<Expr<?>>(args.size());
            for (Expr<?> arg : args){
                if (arg instanceof Constant && Number.class.isAssignableFrom(arg.getType())
                    && !arg.getType().equals(numType)){
                    Number number = (Number) ((Constant)arg).getConstant();
                    newArgs.add(ExprConst.create(MathUtils.cast(number, (Class)numType)));
                }else{
                    newArgs.add(arg);    
                }                
            }
            return newArgs;
        }else{
            return args;
        }
    }

}
