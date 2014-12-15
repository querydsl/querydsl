/*
 * Copyright 2014, Mysema Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mysema.query.elasticsearch;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.mysema.query.types.*;
import org.apache.lucene.queryparser.flexible.core.util.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.IdsQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Set;

import static org.elasticsearch.index.query.QueryBuilders.queryString;

/**
 * Serializes the given Querydsl query to a String query for Elasticsearch
 *
 * @author Kevin Leturc
 */
public class ElasticsearchSerializer implements Visitor<Object, BoolQueryBuilder> {

    /** AND and OR operands. */
    private static final Set<Operator<?>> AND_OR = Sets.<Operator<?>>newHashSet(Ops.AND, Ops.OR);

    public Object handle(Expression<?> expression) {
        BoolQueryBuilder context = QueryBuilders.boolQuery();
        QueryBuilder query = (QueryBuilder) expression.accept(this, context);
        if (!context.hasClauses() && query != null) {
            context.must(query);
        }
        return context;
    }

    public SortBuilder toSort(OrderSpecifier<?> orderBy) {
        Object key = orderBy.getTarget().accept(this, null);
        return SortBuilders.fieldSort(key.toString()).order(orderBy.getOrder() == Order.ASC ? SortOrder.ASC : SortOrder.DESC);
    }

    @Nullable
    @Override
    public Object visit(Constant<?> expr, @Nullable BoolQueryBuilder context) {
        if (Enum.class.isAssignableFrom(expr.getType())) {
            return ((Enum<?>) expr.getConstant()).name();
        } else {
            return expr.getConstant();
        }
    }

    @Nullable
    @Override
    public Object visit(FactoryExpression<?> expr, @Nullable BoolQueryBuilder context) {
        return null;
    }

    public String asDBKey(Operation<?> expr, int index) {
        return StringUtils.toString(asDBValue(expr, index));
    }

    public Object asDBValue(Operation<?> expr, int index) {
        return expr.getArg(index).accept(this, null);
    }

    @Nullable
    @Override
    public Object visit(Operation<?> expr, @Nullable BoolQueryBuilder context) {
        Preconditions.checkNotNull(context);
        Operator<?> op = expr.getOperator();
        if (op == Ops.EQ) {
            Expression<?> keyArg = expr.getArg(0);
            String value = StringUtils.toString(asDBValue(expr, 1));
            if (keyArg instanceof Path<?> && isIdPath((Path<?>) expr.getArg(0))) {
                return QueryBuilders.idsQuery().ids(value);
            } else {
                // Currently all queries are made with ignore case sensitive
                // Because the query to get exact value have to be run on a not_analyzed field
                return QueryBuilders.queryString(value).field(asDBKey(expr, 0));
            }

        } else if (op == Ops.EQ_IGNORE_CASE) {
            String value = StringUtils.toString(asDBValue(expr, 1));
            return QueryBuilders.queryString(value).field(asDBKey(expr, 0));

        } else if (op == Ops.NE) {
            // Decompose the query as NOT and EQ query
            return visit(
                    OperationImpl.create(
                            Boolean.class,
                            Ops.NOT,
                            OperationImpl.create(
                                    Boolean.class,
                                    Ops.EQ,
                                    expr.getArg(0),
                                    expr.getArg(1))),
                    context);

        } else if (op == Ops.STRING_IS_EMPTY) {
            return QueryBuilders.queryString("").field(asDBKey(expr, 0));

        } else if (op == Ops.AND || op == Ops.OR) {
            Operation<?> left = (Operation<?>) expr.getArg(0);
            Operation<?> right = (Operation<?>) expr.getArg(1);
            // Perform the left expression
            QueryBuilder leftResult = visitSubAndOr(op, context, left);
            // Perform the right expression
            QueryBuilder rightResult = visitSubAndOr(op, context, right);

            if (op == Ops.AND) {
                safeMust(context, leftResult);
                safeMust(context, rightResult);
            } else {
                safeShould(context, leftResult);
                safeShould(context, rightResult);
            }
            return null;

        } else if (op == Ops.IN) {

            int constIndex = 0;
            int exprIndex = 1;
            if (expr.getArg(1) instanceof Constant<?>) {
                constIndex = 1;
                exprIndex = 0;
            }
            Expression<?> keyExpr = expr.getArg(exprIndex);
            if (keyExpr instanceof Path<?> && isIdPath((Path<?>) keyExpr)) {
                IdsQueryBuilder idsQuery = QueryBuilders.idsQuery();
                // Hope this is the only case for Elasticsearch ids
                Collection<?> values = (Collection<?>) ((Constant<?>) expr.getArg(constIndex)).getConstant();
                for (Object value : values) {
                    idsQuery.addIds(StringUtils.toString(value));
                }
                return idsQuery;
            } else {
                // Currently all queries are made with ignore case sensitive
                // Because the query to get exact value have to be run on a not_analyzed field
                BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
                String key = asDBKey(expr, exprIndex);
                if (Collection.class.isAssignableFrom(expr.getArg(constIndex).getType())) {
                    Collection<?> values = (Collection<?>) ((Constant<?>) expr.getArg(constIndex)).getConstant();
                    for (Object value : values) {
                        boolQuery.should(QueryBuilders.queryString(StringUtils.toString(value)).field(key));
                    }
                    return boolQuery;

                } else {
                }
            }

        } else if (op == Ops.NOT_IN) {
            // Decompose the query as NOT and IN query
            return visit(
                    OperationImpl.create(
                            Boolean.class,
                            Ops.NOT,
                            OperationImpl.create(
                                    Boolean.class,
                                    Ops.IN,
                                    expr.getArg(1))),
                    context);

        } else if (op == Ops.BETWEEN) {
            Object from = asDBValue(expr, 1);
            Object to = asDBValue(expr, 2);
            return QueryBuilders.rangeQuery(asDBKey(expr, 0)).from(from).to(to);

        } else if (op == Ops.LT) {
            return QueryBuilders.rangeQuery(asDBKey(expr, 0)).lt(asDBValue(expr, 1));

        } else if (op == Ops.GT) {
            return QueryBuilders.rangeQuery(asDBKey(expr, 0)).gt(asDBValue(expr, 1));

        } else if (op == Ops.LOE) {
            return QueryBuilders.rangeQuery(asDBKey(expr, 0)).lte(asDBValue(expr, 1));

        } else if (op == Ops.GOE) {
            return QueryBuilders.rangeQuery(asDBKey(expr, 0)).gte(asDBValue(expr, 1));

        } else if (op == Ops.STARTS_WITH) {
            // Currently all queries are made with ignore case sensitive
            String value = StringUtils.toString(asDBValue(expr, 1));
            return QueryBuilders.queryString(value + "*").field(asDBKey(expr, 0)).analyzeWildcard(true);

        } else if (op == Ops.STARTS_WITH_IC) {
            String value = StringUtils.toString(asDBValue(expr, 1));
            return QueryBuilders.queryString(value + "*").field(asDBKey(expr, 0)).analyzeWildcard(true);

        } else if (op == Ops.ENDS_WITH) {
            // Currently all queries are made with ignore case sensitive
            String value = StringUtils.toString(asDBValue(expr, 1));
            return QueryBuilders.queryString("*" + value).field(asDBKey(expr, 0)).analyzeWildcard(true);

        } else if (op == Ops.ENDS_WITH_IC) {
            String value = StringUtils.toString(asDBValue(expr, 1));
            return QueryBuilders.queryString("*" + value).field(asDBKey(expr, 0)).analyzeWildcard(true);

        } else if (op == Ops.STRING_CONTAINS) {
            String value = StringUtils.toString(asDBValue(expr, 1));
            return QueryBuilders.queryString("*" + value + "*").field(asDBKey(expr, 0)).analyzeWildcard(true);

        } else if (op == Ops.NOT) {
            // Handle the not's child
            BoolQueryBuilder subContext = QueryBuilders.boolQuery();
            QueryBuilder result = (QueryBuilder) expr.getArg(0).accept(this, subContext);
            if (result == null) {
                result = subContext;
            }
            return QueryBuilders.boolQuery().mustNot(result);

        }

        throw new UnsupportedOperationException("Illegal operation " + expr);
    }

    @Nullable
    @Override
    public Object visit(ParamExpression<?> expr, @Nullable BoolQueryBuilder context) {
        return null;
    }

    @Nullable
    @Override
    public Object visit(Path<?> expr, @Nullable BoolQueryBuilder context) {
        PathMetadata<?> metadata = expr.getMetadata();
        return getKeyForPath(expr, metadata);
    }

    @Nullable
    @Override
    public Object visit(SubQueryExpression<?> expr, @Nullable BoolQueryBuilder context) {
        return null;
    }

    @Nullable
    @Override
    public Object visit(TemplateExpression<?> expr, @Nullable BoolQueryBuilder context) {
        return null;
    }

    protected String getKeyForPath(Path<?> expr, PathMetadata<?> metadata) {
        if (isIdPath(expr)) {
            return "_id";
        } else {
            return metadata.getElement().toString();
        }
    }

    protected boolean isIdPath(Path<?> expr) {
        return "id".equals(expr.getMetadata().getElement().toString());
    }

    private QueryBuilder visitSubAndOr(Operator<?> op, BoolQueryBuilder context, Operation<?> subOperation) {
        QueryBuilder result;
        if (AND_OR.contains(subOperation.getOperator()) && subOperation.getOperator() != op) {
            // Opposite case, if current operator is an AND so sub operation is a OR, so create a sub query
            result = QueryBuilders.boolQuery();
            subOperation.accept(this, (BoolQueryBuilder) result);
        } else {
            // Here let's do recursive if sub operation has the same operator than the current one (result is null)
            // or it's another operator than AND/OR so add it to query
            result = (QueryBuilder) subOperation.accept(this, context);
        }
        return result;
    }

    private void safeMust(BoolQueryBuilder context, QueryBuilder query) {
        if (query != null) {
            context.must(query);
        }
    }

    private void safeShould(BoolQueryBuilder context, QueryBuilder query) {
        if (query != null) {
            context.should(query);
        }
    }

}
