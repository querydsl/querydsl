/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.lucene;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.util.NumericUtils;

import com.mysema.query.QueryMetadata;
import com.mysema.query.types.*;
import com.mysema.query.types.expr.Param;

/**
 * Serializes Querydsl queries to Lucene queries.
 *
 * @author vema
 *
 */
public class LuceneSerializer {

    private static final Map<Class<?>, Integer> sortFields = new HashMap<Class<?>, Integer>();

    static {
        sortFields.put(Integer.class, SortField.INT);
        sortFields.put(Float.class, SortField.FLOAT);
        sortFields.put(Long.class, SortField.LONG);
        sortFields.put(Double.class, SortField.DOUBLE);
        sortFields.put(Short.class, SortField.SHORT);
        sortFields.put(Byte.class, SortField.BYTE);
        sortFields.put(BigDecimal.class, SortField.DOUBLE);
        sortFields.put(BigInteger.class, SortField.LONG);
    }

    public static final LuceneSerializer DEFAULT = new LuceneSerializer(false, true);

    private final boolean lowerCase;

    private final boolean splitTerms;

    public LuceneSerializer(boolean lowerCase, boolean splitTerms) {
        this.lowerCase = lowerCase;
        this.splitTerms = splitTerms;
    }

    private Query toQuery(Operation<?> operation, QueryMetadata metadata) {
        Operator<?> op = operation.getOperator();
        if (op == Ops.OR) {
            return toTwoHandSidedQuery(operation, Occur.SHOULD, metadata);
        } else if (op == Ops.AND) {
            return toTwoHandSidedQuery(operation, Occur.MUST, metadata);
        } else if (op == Ops.NOT) {
            BooleanQuery bq = new BooleanQuery();
            bq.add(new BooleanClause(toQuery(operation.getArg(0), metadata), Occur.MUST_NOT));
            return bq;
        } else if (op == Ops.LIKE) {
            return like(operation, metadata);
        } else if (op == Ops.EQ_OBJECT || op == Ops.EQ_PRIMITIVE || op == Ops.EQ_IGNORE_CASE) {
            return eq(operation, metadata);
        } else if (op == Ops.NE_OBJECT || op == Ops.NE_PRIMITIVE) {
            return ne(operation, metadata);
        } else if (op == Ops.STARTS_WITH || op == Ops.STARTS_WITH_IC) {
            return startsWith(metadata, operation);
        } else if (op == Ops.ENDS_WITH || op == Ops.ENDS_WITH_IC) {
            return endsWith(operation, metadata);
        } else if (op == Ops.STRING_CONTAINS || op == Ops.STRING_CONTAINS_IC) {
            return stringContains(operation, metadata);
        } else if (op == Ops.BETWEEN) {
            return between(operation, metadata);
        } else if (op == Ops.IN) {
            return in(operation, metadata);
        } else if (op == Ops.LT || op == Ops.BEFORE) {
            return lt(operation, metadata);
        } else if (op == Ops.GT || op == Ops.AFTER) {
            return gt(operation, metadata);
        } else if (op == Ops.LOE || op == Ops.BOE) {
            return le(operation, metadata);
        } else if (op == Ops.GOE || op == Ops.AOE) {
            return ge(operation, metadata);
        } else if (op == Ops.DELEGATE){
            return toQuery(operation.getArg(0), metadata);
        }
        throw new UnsupportedOperationException("Illegal operation " + operation);
    }

    private Query toTwoHandSidedQuery(Operation<?> operation, Occur occur, QueryMetadata metadata) {
        Query lhs = toQuery(operation.getArg(0), metadata);
        Query rhs = toQuery(operation.getArg(1), metadata);
        BooleanQuery bq = new BooleanQuery();
        bq.add(createBooleanClause(lhs, occur));
        bq.add(createBooleanClause(rhs, occur));
        return bq;
    }

    /**
     * If the query is a BooleanQuery and it contains a single Occur.MUST_NOT
     * clause it will be returned as is. Otherwise it will be wrapped in a
     * BooleanClause with the given Occur.
     */
    private BooleanClause createBooleanClause(Query query, Occur occur) {
        if (query instanceof BooleanQuery) {
            BooleanClause[] clauses = ((BooleanQuery) query).getClauses();
            if (clauses.length == 1 && clauses[0].getOccur().equals(Occur.MUST_NOT)) {
                return clauses[0];
            }
        }
        return new BooleanClause(query, occur);
    }

    private Query like(Operation<?> operation, QueryMetadata metadata) {
        verifyArguments(operation);
        String field = toField(operation.getArg(0));
        String[] terms = createTerms(operation.getArg(1), metadata);
        if (terms.length > 1) {
            BooleanQuery bq = new BooleanQuery();
            for (String s : terms) {
                bq.add(new WildcardQuery(new Term(field, "*" + normalize(s) + "*")), Occur.MUST);
            }
            return bq;
        }
        return new WildcardQuery(new Term(field, normalize(terms[0])));
    }

    @SuppressWarnings("unchecked")
    private Query eq(Operation<?> operation, QueryMetadata metadata) {
        verifyArguments(operation);
        String field = toField(operation.getArg(0));
        if (Number.class.isAssignableFrom(operation.getArg(1).getType())) {
            return new TermQuery(new Term(field, convertNumber(((Constant<Number>) operation
                    .getArg(1)).getConstant())));
        }
        return eq(field, createTerms(operation.getArg(1), metadata), metadata);
    }

    private String convertNumber(Number number) {
        if (Integer.class.isInstance(number)) {
            return NumericUtils.intToPrefixCoded(number.intValue());
        } else if (Double.class.isInstance(number)) {
            return NumericUtils.doubleToPrefixCoded(number.doubleValue());
        } else if (Long.class.isInstance(number)) {
            return NumericUtils.longToPrefixCoded(number.longValue());
        } else if (Float.class.isInstance(number)) {
            return NumericUtils.floatToPrefixCoded(number.floatValue());
        } else if (Byte.class.isInstance(number)) {
            return NumericUtils.intToPrefixCoded(number.intValue());
        } else if (Short.class.isInstance(number)) {
            return NumericUtils.intToPrefixCoded(number.intValue());
        } else if (BigDecimal.class.isInstance(number)) {
            return NumericUtils.doubleToPrefixCoded(number.doubleValue());
        } else if (BigInteger.class.isInstance(number)) {
            return NumericUtils.longToPrefixCoded(number.longValue());
        } else {
            throw new IllegalArgumentException("Unsupported numeric type "
                    + number.getClass().getName());
        }
    }

    private Query eq(String field, String[] terms, QueryMetadata metadata) {
        if (terms.length > 1) {
            PhraseQuery pq = new PhraseQuery();
            for (String s : terms) {
                pq.add(new Term(field, normalize(s)));
            }
            return pq;
        }
        return new TermQuery(new Term(field, normalize(terms[0])));
    }

    @SuppressWarnings("unchecked")
    private Query in(Operation<?> operation, QueryMetadata metadata) {
        String field = toField(operation.getArg(0));
        Collection values = (Collection) ((Constant) operation.getArg(1)).getConstant();
        BooleanQuery bq = new BooleanQuery();
        for (Object value : values) {
            bq.add(eq(field, StringUtils.split(value.toString()), metadata), Occur.SHOULD);
        }
        return bq;
    }

    private Query ne(Operation<?> operation, QueryMetadata metadata) {
        BooleanQuery bq = new BooleanQuery();
        bq.add(new BooleanClause(eq(operation, metadata), Occur.MUST_NOT));
        return bq;
    }

    private Query startsWith(QueryMetadata metadata, Operation<?> operation) {
        verifyArguments(operation);
        String field = toField(operation.getArg(0));
        String[] terms = createEscapedTerms(operation.getArg(1), metadata);
        if (terms.length > 1) {
            BooleanQuery bq = new BooleanQuery();
            for (int i = 0; i < terms.length; ++i) {
                String s = i == 0 ? terms[i] + "*" : "*" + terms[i] + "*";
                bq.add(new WildcardQuery(new Term(field, normalize(s))), Occur.MUST);
            }
            return bq;
        }
        return new PrefixQuery(new Term(field, normalize(terms[0])));
    }

    private Query stringContains(Operation<?> operation, QueryMetadata metadata) {
        verifyArguments(operation);
        String field = toField(operation.getArg(0));
        String[] terms = createEscapedTerms(operation.getArg(1), metadata);
        if (terms.length > 1) {
            BooleanQuery bq = new BooleanQuery();
            for (String s : terms) {
                bq.add(new WildcardQuery(new Term(field, "*" + normalize(s) + "*")), Occur.MUST);
            }
            return bq;
        }
        return new WildcardQuery(new Term(field, "*" + normalize(terms[0]) + "*"));
    }

    private Query endsWith(Operation<?> operation, QueryMetadata metadata) {
        verifyArguments(operation);
        String field = toField(operation.getArg(0));
        String[] terms = createEscapedTerms(operation.getArg(1), metadata);
        if (terms.length > 1) {
            BooleanQuery bq = new BooleanQuery();
            for (int i = 0; i < terms.length; ++i) {
                String s = i == terms.length - 1 ? "*" + terms[i] : "*" + terms[i] + "*";
                bq.add(new WildcardQuery(new Term(field, normalize(s))), Occur.MUST);
            }
            return bq;
        }
        return new WildcardQuery(new Term(field, "*" + normalize(terms[0])));
    }

    private Query between(Operation<?> operation, QueryMetadata metadata) {
        verifyArguments(operation);
        // TODO Phrase not properly supported
        return range(toField(operation.getArg(0)), operation.getArg(1), operation.getArg(2), true, true, metadata);
    }

    private Query lt(Operation<?> operation, QueryMetadata metadata) {
        verifyArguments(operation);
        return range(toField(operation.getArg(0)), null, operation.getArg(1), false, false, metadata);
    }

    private Query gt(Operation<?> operation, QueryMetadata metadata) {
        verifyArguments(operation);
        return range(toField(operation.getArg(0)), operation.getArg(1), null, false, false, metadata);
    }

    private Query le(Operation<?> operation, QueryMetadata metadata) {
        verifyArguments(operation);
        return range(toField(operation.getArg(0)), null, operation.getArg(1), true, true, metadata);
    }

    private Query ge(Operation<?> operation, QueryMetadata metadata) {
        verifyArguments(operation);
        return range(toField(operation.getArg(0)), operation.getArg(1), null, true, true, metadata);
    }

    @SuppressWarnings("unchecked")
    private Query range(String field, Expression<?> min, Expression<?> max, boolean minInc, boolean maxInc, QueryMetadata metadata) {
        if (min != null && Number.class.isAssignableFrom(min.getType()) || max != null
                && Number.class.isAssignableFrom(max.getType())) {
            Class<? extends Number> numType = (Class) (min != null ? min.getType() : max.getType());
            return numericRange((Class) numType, field, (Number) (min == null ? null
                    : ((Constant) min).getConstant()), (Number) (max == null ? null
                    : ((Constant) max).getConstant()), minInc, maxInc);
        }
        return stringRange(field, min, max, minInc, maxInc, metadata);
    }

    private <N extends Number> NumericRangeQuery<?> numericRange(Class<N> clazz, String field,
            N min, N max, boolean minInc, boolean maxInc) {
        if (clazz.equals(Integer.class)) {
            return NumericRangeQuery.newIntRange(field, (Integer) min, (Integer) max, minInc, maxInc);
        } else if (clazz.equals(Double.class)) {
            return NumericRangeQuery.newDoubleRange(field, (Double) min, (Double) max, minInc, minInc);
        } else if (clazz.equals(Float.class)) {
            return NumericRangeQuery.newFloatRange(field, (Float) min, (Float) max, minInc, minInc);
        } else if (clazz.equals(Long.class)) {
            return NumericRangeQuery.newLongRange(field, (Long) min, (Long) max, minInc, minInc);
        } else if (clazz.equals(Byte.class) || clazz.equals(Short.class)) {
            return NumericRangeQuery.newIntRange(field, min != null ? min.intValue() : null,
                    max != null ? max.intValue() : null, minInc, maxInc);
        } else {
            throw new IllegalArgumentException("Unsupported numeric type " + clazz.getName());
        }
    }

    private Query stringRange(String field, Expression<?> min, Expression<?> max, boolean minInc, boolean maxInc, QueryMetadata metadata) {
        if (min == null) {
            return new TermRangeQuery(field, null, normalize(createTerms(max, metadata)[0]), minInc, maxInc);
        } else if (max == null) {
            return new TermRangeQuery(field, normalize(createTerms(min, metadata)[0]), null, minInc, maxInc);
        } else {
            return new TermRangeQuery(field, normalize(createTerms(min, metadata)[0]), normalize(createTerms(max, metadata)[0]), minInc, maxInc);
        }
    }

    @SuppressWarnings("unchecked")
    private String toField(Expression<?> expr) {
        if (expr instanceof Path) {
            return toField((Path<?>) expr);
        } else if (expr instanceof Operation) {
            Operation<?> operation = (Operation<?>) expr;
            if (operation.getOperator() == Ops.LOWER || operation.getOperator() == Ops.UPPER) {
                return toField(operation.getArg(0));
            }
        }
        throw new IllegalArgumentException("Unable to transform " + expr + " to field");
    }

    public String toField(Path<?> path) {
        return path.getMetadata().getExpression().toString();
    }

    private void verifyArguments(Operation<?> operation) {
        List<Expression<?>> arguments = operation.getArgs();
        for (int i = 1; i < arguments.size(); ++i) {
            if (!(arguments.get(i) instanceof Constant<?>)
                    && !(arguments.get(i) instanceof Param<?>)
                    && !(arguments.get(i) instanceof PhraseElement)
                    && !(arguments.get(i) instanceof TermElement)) {
                throw new IllegalArgumentException("operand was of unsupported type "
                        + arguments.get(i).getClass().getName());
            }
        }
    }

    private String[] createTerms(Expression<?> expr, QueryMetadata metadata) {
        if (expr instanceof Param<?>){
            Object value = metadata.getParams().get(expr);
            if (value == null){
                throw new ParamNotSetException((Param<?>) expr);
            }
            return split(expr, value.toString());
        }else{
            return split(expr, expr.toString());
        }
    }

    private String[] createEscapedTerms(Expression<?> expr, QueryMetadata metadata) {
        if (expr instanceof Param<?>){
            Object value = metadata.getParams().get(expr);
            if (value == null){
                throw new ParamNotSetException((Param<?>) expr);
            }
            return split(expr, QueryParser.escape(value.toString()));
        }else{
            return split(expr, QueryParser.escape(expr.toString()));
        }
    }

    private String[] split(Expression<?> expr, String str) {
        if (expr instanceof PhraseElement) {
            return StringUtils.split(str);
        } else if (expr instanceof TermElement) {
            return new String[] { str };
        } else if (splitTerms) {
            return StringUtils.split(str);
        } else {
            return new String[] { str };
        }
    }

    private String normalize(String s) {
        return lowerCase ? s.toLowerCase(Locale.ENGLISH) : s;
    }

    public Query toQuery(Expression<?> expr, QueryMetadata metadata) {
        if (expr instanceof Operation<?>) {
            return toQuery((Operation<?>) expr, metadata);
        } else if (expr instanceof QueryElement) {
            return ((QueryElement) expr).getQuery();
        } else{
            throw new IllegalArgumentException("expr was of unsupported type " + expr.getClass().getName());
        }
    }

    public Sort toSort(List<OrderSpecifier<?>> orderBys) {
        List<SortField> sorts = new ArrayList<SortField>(orderBys.size());
        for (OrderSpecifier<?> order : orderBys) {
            if (!(order.getTarget() instanceof Path<?>)) {
                throw new IllegalArgumentException("argument was not of type Path.");
            }
            Class<?> type = order.getTarget().getType();
            boolean reverse = !order.isAscending();
            if (Number.class.isAssignableFrom(type)) {
                sorts.add(new SortField(toField(order.getTarget()), sortFields.get(type), reverse));
            } else {
                sorts.add(new SortField(toField(order.getTarget()), Locale.ENGLISH, reverse));
            }
        }
        Sort sort = new Sort();
        sort.setSort(sorts.toArray(new SortField[sorts.size()]));
        return sort;
    }
}
