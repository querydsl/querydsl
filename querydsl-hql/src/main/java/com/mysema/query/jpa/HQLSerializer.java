/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa;

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
import javax.persistence.DiscriminatorValue;

import com.mysema.query.JoinExpression;
import com.mysema.query.JoinType;
import com.mysema.query.QueryMetadata;
import com.mysema.query.types.*;
import com.mysema.query.types.expr.ConstructorExpression;
import com.mysema.query.types.expr.SimpleConstant;
import com.mysema.query.types.expr.SimpleOperation;
import com.mysema.query.types.expr.StringConstant;
import com.mysema.util.MathUtils;

/**
 * HQLSerializer serializes Querydsl expressions into HQL syntax.
 *
 * @author tiwe
 * @version $Id$
 */
public class HQLSerializer extends SerializerBase<HQLSerializer> {

    private static final Set<Operator<?>> NUMERIC = new HashSet<Operator<?>>(Arrays.<Operator<?>>asList(
            Ops.ADD, Ops.SUB, Ops.MULT, Ops.DIV,
            Ops.LT, Ops.LOE, Ops.GT, Ops.GOE, Ops.BETWEEN,
            Ops.BEFORE, Ops.AFTER, Ops.BOE, Ops.AOE));

    private static final String SELECT_COUNT_DISTINCT = "select count(distinct ";

    private static final String COMMA = ", ";

    private static final String DELETE = "delete ";

    private static final String FROM = "from ";

    private static final String GROUP_BY = "\ngroup by ";

    private static final String HAVING = "\nhaving ";

    private static final String ORDER_BY = "\norder by ";

    private static final String SELECT = "select ";

    private static final String SELECT_COUNT = "select count(";

    private static final String SELECT_DISTINCT = "select distinct ";

    private static final String SET = "\nset ";

    private static final String UPDATE = "update ";

    private static final String WHERE = "\nwhere ";

    private static final String WITH = " with ";

    private static final Map<JoinType, String> joinTypes = new HashMap<JoinType, String>();

    private final JPQLTemplates templates;

    static{
        joinTypes.put(JoinType.DEFAULT, COMMA);
        joinTypes.put(JoinType.FULLJOIN, "\n  full join ");
        joinTypes.put(JoinType.INNERJOIN, "\n  inner join ");
        joinTypes.put(JoinType.JOIN, "\n  join ");
        joinTypes.put(JoinType.LEFTJOIN, "\n  left join ");
    }

    private boolean wrapElements = false;

    public HQLSerializer(JPQLTemplates templates) {
        super(templates);
        this.templates = templates;
    }

    private void handleJoinTarget(JoinExpression je) {
        // type specifier
        if (je.getTarget() instanceof EntityPath<?>) {
            EntityPath<?> pe = (EntityPath<?>) je.getTarget();
            if (pe.getMetadata().getParent() == null) {
                if (pe.getType().getPackage() != null){
                    String pn = pe.getType().getPackage().getName();
                    String typeName = pe.getType().getName().substring(pn.length() + 1);
                    append(typeName);    
                }else{
                    append(pe.getType().getName());
                }                
                append(" ");
            }
        }
        handle(je.getTarget());
    }

    // TODO : generalize this!
    @SuppressWarnings("unchecked")
    private <T> Expression<?> regexToLike(Operation<T> operation) {
        List<Expression<?>> args = new ArrayList<Expression<?>>();
        for (Expression<?> arg : operation.getArgs()){
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
        return SimpleOperation.create(
                operation.getType(),
                operation.getOperator(),
                args.<Expression<?>>toArray(new Expression[args.size()]));
    }

    private Expression<?> regexToLike(String str){
        return StringConstant.create(str.replace(".*", "%").replace(".", "_"));
    }

    public void serialize(QueryMetadata metadata, boolean forCountRow, @Nullable String projection) {
        List<? extends Expression<?>> select = metadata.getProjection();
        List<JoinExpression> joins = metadata.getJoins();
        Predicate where = metadata.getWhere();
        List<? extends Expression<?>> groupBy = metadata.getGroupBy();
        Predicate having = metadata.getHaving();
        List<OrderSpecifier<?>> orderBy = metadata.getOrderBy();

        // select
        if (projection != null){
            append(SELECT).append(projection).append("\n");

        }else if (forCountRow) {
            if (!metadata.isDistinct()){
                append(SELECT_COUNT);
            }else{
                append(SELECT_COUNT_DISTINCT);
            }
            if(!select.isEmpty()){
                handle(COMMA, select);
            }else{
                handle(joins.get(0).getTarget());
            }
            append(")\n");

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
            if (je.hasFlag(HQLQueryMixin.FETCH) && !forCountRow){
                handle(HQLQueryMixin.FETCH);
            }
            handleJoinTarget(je);
            if (je.hasFlag(HQLQueryMixin.FETCH_ALL_PROPERTIES) && !forCountRow){
                handle(HQLQueryMixin.FETCH_ALL_PROPERTIES);
            }

            if (je.getCondition() != null) {
                append(WITH).handle(je.getCondition());
            }
        }
    }

    @Override
    public Void visit(Constant<?> expr, Void context) {
        boolean wrap = templates.wrapConstant(expr);
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
        return null;
    }

    @Override
    public Void visit(Param<?> param, Void context){
        append(":");
        super.visit(param, context);
        return null;
    }

    @Override
    public Void visit(SubQueryExpression<?> query, Void context) {
        append("(");
        serialize(query.getMetadata(), false, null);
        append(")");
        return null;
    }

    @Override
    public Void visit(Path<?> expr, Void context){
        // only wrap a PathCollection, if it the pathType is PROPERTY
        boolean wrap = wrapElements
        && (Collection.class.isAssignableFrom(expr.getType()) || Map.class.isAssignableFrom(expr.getType()))
        && expr.getMetadata().getPathType().equals(PathType.PROPERTY);
        if (wrap) {
            append("elements(");
        }
        super.visit(expr, context);
        if (wrap) {
            append(")");
        }
        return null;
    }

    @Override
    public Void visit(FactoryExpression<?> expr, Void context) {
        if (expr instanceof ConstructorExpression<?>){
            append("new " + expr.getType().getName() + "(");
            handle(", ", expr.getArgs());
            append(")");
        }else{
            // serialize arguments only
            super.visit(expr, context);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    protected void visitOperation(Class<?> type, Operator<?> operator, List<Expression<?>> args) {
        boolean old = wrapElements;
        wrapElements = templates.wrapElements(operator);

        if (operator.equals(Ops.IN)){
            if (args.get(1) instanceof Path){
                super.visitOperation(type, JPQLTemplates.MEMBER_OF, args);
            }else{
                super.visitOperation(type, operator, args);
            }

        } else if (operator.equals(Ops.INSTANCE_OF)) {
            if (templates.isTypeAsString()){
                List<Expression<?>> newArgs = new ArrayList<Expression<?>>(args);
                Class<?> cl = ((Class<?>) ((Constant<?>) newArgs.get(1)).getConstant());
                // use discriminator value instead of fqnm
                if (cl.getAnnotation(DiscriminatorValue.class) != null){
                    newArgs.set(1, StringConstant.create(cl.getAnnotation(DiscriminatorValue.class).value()));
                }else{
                    newArgs.set(1, StringConstant.create(cl.getName()));
                }
                super.visitOperation(type, operator, newArgs);
            }else{
                super.visitOperation(type, operator, args);
            }

        } else if (operator.equals(Ops.NUMCAST)) {
            Class<?> targetType = (Class<?>) ((Constant<?>) args.get(1)).getConstant();
            String typeName = targetType.getSimpleName().toLowerCase(Locale.ENGLISH);
            visitOperation(targetType, JPQLTemplates.CAST, Arrays.<Expression<?>>asList(args.get(0), SimpleConstant.create(typeName)));

        } else if (operator.equals(Ops.EXISTS) && args.get(0) instanceof SubQueryExpression){
            SubQueryExpression subQuery = (SubQueryExpression) args.get(0);
            append("exists (");
            serialize(subQuery.getMetadata(), false, "1");
            append(")");

        } else if (operator.equals(Ops.MATCHES)){
            List<Expression<?>> newArgs = new ArrayList<Expression<?>>(args);
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
    private List<Expression<?>> normalizeNumericArgs(List<Expression<?>> args) {
        boolean hasConstants = false;
        Class<? extends Number> numType = null;
        for (Expression<?> arg : args){
            if (Number.class.isAssignableFrom(arg.getType())){
                if (arg instanceof Constant){
                    hasConstants = true;
                }else{
                    numType = (Class<? extends Number>) arg.getType();
                }
            }
        }
        if (hasConstants && numType != null){
            List<Expression<?>> newArgs = new ArrayList<Expression<?>>(args.size());
            for (Expression<?> arg : args){
                if (arg instanceof Constant && Number.class.isAssignableFrom(arg.getType())
                        && !arg.getType().equals(numType)){
                    Number number = (Number) ((Constant)arg).getConstant();
                    newArgs.add(SimpleConstant.create(MathUtils.cast(number, (Class)numType)));
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
