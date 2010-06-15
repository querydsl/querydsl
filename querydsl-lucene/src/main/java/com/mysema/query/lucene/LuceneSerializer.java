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
import com.mysema.query.types.Constant;
import com.mysema.query.types.Expr;
import com.mysema.query.types.Operation;
import com.mysema.query.types.Operator;
import com.mysema.query.types.Ops;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Param;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathType;

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

    private Query toQuery(QueryMetadata metadata, Operation<?> operation) {
        Operator<?> op = operation.getOperator();
        if (op == Ops.OR) {
            return toTwoHandSidedQuery(metadata, operation, Occur.SHOULD);
        } else if (op == Ops.AND) {
            return toTwoHandSidedQuery(metadata, operation, Occur.MUST);
        } else if (op == Ops.NOT) {
            BooleanQuery bq = new BooleanQuery();
            bq.add(new BooleanClause(toQuery(metadata, operation.getArg(0)), Occur.MUST_NOT));
            return bq;
        } else if (op == Ops.LIKE) {
            return like(metadata, operation);
        } else if (op == Ops.EQ_OBJECT || op == Ops.EQ_PRIMITIVE || op == Ops.EQ_IGNORE_CASE) {
            return eq(metadata, operation);
        } else if (op == Ops.NE_OBJECT || op == Ops.NE_PRIMITIVE) {
            return ne(metadata, operation);
        } else if (op == Ops.STARTS_WITH || op == Ops.STARTS_WITH_IC) {
            return startsWith(metadata, operation);
        } else if (op == Ops.ENDS_WITH || op == Ops.ENDS_WITH_IC) {
            return endsWith(metadata, operation);
        } else if (op == Ops.STRING_CONTAINS || op == Ops.STRING_CONTAINS_IC) {
            return stringContains(metadata, operation);
        } else if (op == Ops.BETWEEN) {
            return between(metadata, operation);
        } else if (op == Ops.IN) {
            return in(metadata, operation);
        } else if (op == Ops.LT || op == Ops.BEFORE) {
            return lt(metadata, operation);
        } else if (op == Ops.GT || op == Ops.AFTER) {
            return gt(metadata, operation);
        } else if (op == Ops.LOE || op == Ops.BOE) {
            return le(metadata, operation);
        } else if (op == Ops.GOE || op == Ops.AOE) {
            return ge(metadata, operation);
        } else if (op == PathType.DELEGATE) {
            return toQuery(metadata, operation.getArg(0));
        }
        throw new UnsupportedOperationException("Illegal operation " + operation);
    }

    private Query toTwoHandSidedQuery(QueryMetadata metadata, Operation<?> operation, Occur occur) {
        Query lhs = toQuery(metadata, operation.getArg(0));
        Query rhs = toQuery(metadata, operation.getArg(1));
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

    private Query like(QueryMetadata metadata, Operation<?> operation) {
        verifyArguments(operation);
        String field = toField(operation.getArg(0));
        String[] terms = createTerms(metadata, operation.getArg(1));
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
    private Query eq(QueryMetadata metadata, Operation<?> operation) {
        verifyArguments(operation);
        String field = toField(operation.getArg(0));
        if (Number.class.isAssignableFrom(operation.getArg(1).getType())) {
            return new TermQuery(new Term(field, convertNumber(((Constant<Number>) operation
                    .getArg(1)).getConstant())));
        }
        return eq(metadata, field, createTerms(metadata, operation.getArg(1)));
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

    private Query eq(QueryMetadata metadata, String field, String[] terms) {
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
    private Query in(QueryMetadata metadata, Operation<?> operation) {
        String field = toField(operation.getArg(0));
        Collection values = (Collection) ((Constant) operation.getArg(1)).getConstant();
        BooleanQuery bq = new BooleanQuery();
        for (Object value : values) {
            bq.add(eq(metadata, field, StringUtils.split(value.toString())), Occur.SHOULD);
        }
        return bq;
    }

    private Query ne(QueryMetadata metadata, Operation<?> operation) {
        BooleanQuery bq = new BooleanQuery();
        bq.add(new BooleanClause(eq(metadata, operation), Occur.MUST_NOT));
        return bq;
    }

    private Query startsWith(QueryMetadata metadata, Operation<?> operation) {
        verifyArguments(operation);
        String field = toField(operation.getArg(0));
        String[] terms = createEscapedTerms(metadata, operation.getArg(1));
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

    private Query stringContains(QueryMetadata metadata, Operation<?> operation) {
        verifyArguments(operation);
        String field = toField(operation.getArg(0));
        String[] terms = createEscapedTerms(metadata, operation.getArg(1));
        if (terms.length > 1) {
            BooleanQuery bq = new BooleanQuery();
            for (String s : terms) {
                bq.add(new WildcardQuery(new Term(field, "*" + normalize(s) + "*")), Occur.MUST);
            }
            return bq;
        }
        return new WildcardQuery(new Term(field, "*" + normalize(terms[0]) + "*"));
    }

    private Query endsWith(QueryMetadata metadata, Operation<?> operation) {
        verifyArguments(operation);
        String field = toField(operation.getArg(0));
        String[] terms = createEscapedTerms(metadata, operation.getArg(1));
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

    private Query between(QueryMetadata metadata, Operation<?> operation) {
        verifyArguments(operation);
        // TODO Phrase not properly supported
        return range(metadata, toField(operation.getArg(0)), operation.getArg(1), operation.getArg(2), true, true);
    }

    private Query lt(QueryMetadata metadata, Operation<?> operation) {
        verifyArguments(operation);
        return range(metadata, toField(operation.getArg(0)), null, operation.getArg(1), false, false);
    }

    private Query gt(QueryMetadata metadata, Operation<?> operation) {
        verifyArguments(operation);
        return range(metadata, toField(operation.getArg(0)), operation.getArg(1), null, false, false);
    }

    private Query le(QueryMetadata metadata, Operation<?> operation) {
        verifyArguments(operation);
        return range(metadata, toField(operation.getArg(0)), null, operation.getArg(1), true, true);
    }

    private Query ge(QueryMetadata metadata, Operation<?> operation) {
        verifyArguments(operation);
        return range(metadata, toField(operation.getArg(0)), operation.getArg(1), null, true, true);
    }

    @SuppressWarnings("unchecked")
    private Query range(QueryMetadata metadata, String field, Expr<?> min, Expr<?> max, boolean minInc, boolean maxInc) {
        if (min != null && Number.class.isAssignableFrom(min.getType()) || max != null
                && Number.class.isAssignableFrom(max.getType())) {
            Class<? extends Number> numType = (Class) (min != null ? min.getType() : max.getType());
            return numericRange((Class) numType, field, (Number) (min == null ? null
                    : ((Constant) min).getConstant()), (Number) (max == null ? null
                    : ((Constant) max).getConstant()), minInc, maxInc);
        }
        return stringRange(metadata, field, min, max, minInc, maxInc);
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

    private Query stringRange(QueryMetadata metadata, String field, Expr<?> min, Expr<?> max, boolean minInc, boolean maxInc) {
        if (min == null) {
            return new TermRangeQuery(field, null, normalize(createTerms(metadata, max)[0]), minInc, maxInc);
        } else if (max == null) {
            return new TermRangeQuery(field, normalize(createTerms(metadata, min)[0]), null, minInc, maxInc);
        } else {
            return new TermRangeQuery(field, normalize(createTerms(metadata, min)[0]), normalize(createTerms(metadata, max)[0]), minInc, maxInc);
        }
    }

    @SuppressWarnings("unchecked")
    private String toField(Expr<?> expr) {
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
        List<Expr<?>> arguments = operation.getArgs();
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

    private String[] createTerms(QueryMetadata metadata, Expr<?> expr) {
        if (expr instanceof Param<?>){
            return split(expr, metadata.getParams().get(expr).toString());
        }else{
            return split(expr, expr.toString());    
        }        
    }

    private String[] createEscapedTerms(QueryMetadata metadata, Expr<?> expr) {
        if (expr instanceof Param<?>){
            return split(expr, QueryParser.escape(metadata.getParams().get(expr).toString()));
        }else{
            return split(expr, QueryParser.escape(expr.toString()));
        }        
    }

    private String[] split(Expr<?> expr, String str) {
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

    public Query toQuery(QueryMetadata metadata, Expr<?> expr) {
        if (expr instanceof Operation<?>) {
            return toQuery(metadata, (Operation<?>) expr);
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
