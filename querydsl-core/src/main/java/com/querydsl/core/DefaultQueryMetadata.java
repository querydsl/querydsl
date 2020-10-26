/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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
package com.querydsl.core;

import static com.querydsl.core.util.CollectionUtils.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.jetbrains.annotations.Nullable;

import com.querydsl.core.types.*;
import com.querydsl.core.util.CollectionUtils;

/**
 * {@code DefaultQueryMetadata} is the default implementation of the {@link QueryMetadata} interface.
 *
 * <p>{@code DefaultQueryMetadata} is mutable, but {@link DefaultQueryMetadata#clone()} can be used to
 * created deep copies to refine the state without modifying the initial instance.</p>
 *
 * @author tiwe
 */
public class DefaultQueryMetadata implements QueryMetadata, Cloneable {

    private static final long serialVersionUID = 317736313966701232L;

    private boolean distinct;

    private Set<Expression<?>> exprInJoins = new LinkedHashSet<>();

    private List<Expression<?>> groupBy = new ArrayList<>();

    @Nullable
    private Predicate having;

    private List<JoinExpression> joins = new ArrayList<>();

    private Expression<?> joinTarget;

    private JoinType joinType;

    @Nullable
    private Predicate joinCondition;

    private Set<JoinFlag> joinFlags = new LinkedHashSet<>();

    private QueryModifiers modifiers = QueryModifiers.EMPTY;

    private List<OrderSpecifier<?>> orderBy = new ArrayList<>();

    @Nullable
    private Expression<?> projection;

    // NOTE : this is not necessarily serializable
    private Map<ParamExpression<?>, Object> params = new LinkedHashMap<>();

    private boolean unique;

    @Nullable
    private Predicate where;

    private Set<QueryFlag> flags = new LinkedHashSet<>();

    private boolean extractParams = true;

    private boolean validate = false;

    private ValidatingVisitor validatingVisitor = ValidatingVisitor.DEFAULT;

    private static Predicate and(Predicate lhs, Predicate rhs) {
        if (lhs == null) {
            return rhs;
        } else {
            return ExpressionUtils.and(lhs, rhs);
        }
    }

    /**
     * Create an empty DefaultQueryMetadata instance
     */
    public DefaultQueryMetadata() { }

    /**
     * Disable validation
     *
     * @return the current object
     */
    public DefaultQueryMetadata noValidate() {
        validate = false;
        return this;
    }


    @Override
    public void addFlag(QueryFlag flag) {
        flags = addSorted(flags, flag);
    }

    @Override
    public void addJoinFlag(JoinFlag flag) {
        joinFlags.add(flag);
    }

    @Override
    public void addGroupBy(Expression<?> o) {
        // group by elements can't be validated, since they can refer to projection elements
        // that are declared later
        groupBy.add(o);
    }

    @Override
    public void addHaving(Predicate e) {
        if (e == null) {
            return;
        }
        e = (Predicate) ExpressionUtils.extract(e);
        if (e != null) {
            // having elements can't be validated, since they can refer to projection elements
            // that are declared later
            having = and(having, e);
        }
    }

    private void addLastJoin() {
        if (joinTarget != null) {
            joins.add(new JoinExpression(joinType, joinTarget, joinCondition, CollectionUtils.unmodifiableSet(joinFlags)));
            joinType = null;
            joinTarget = null;
            joinCondition = null;
            joinFlags.clear();
        }
    }

    @Override
    public void addJoin(JoinType joinType, Expression<?> expr) {
        addLastJoin();
        if (!exprInJoins.contains(expr)) {
            if (expr instanceof Path && ((Path<?>) expr).getMetadata().isRoot()) {
                exprInJoins.add(expr);
            } else {
                validate(expr);
            }
            this.joinType = joinType;
            this.joinTarget = expr;
        } else if (validate) {
            throw new IllegalStateException(expr + " is already used");
        }
    }

    @Override
    public void addJoinCondition(Predicate o) {
        validate(o);
        joinCondition = and(joinCondition, o);
    }

    @Override
    public void addOrderBy(OrderSpecifier<?> o) {
        // order specifiers can't be validated, since they can refer to projection elements
        // that are declared later
        orderBy.add(o);
    }

    @Override
    public void setProjection(Expression<?> o) {
        validate(o);
        projection = o;
    }

    @Override
    public void addWhere(Predicate e) {
        if (e == null) {
            return;
        }
        e = (Predicate) ExpressionUtils.extract(e);
        if (e != null) {
            validate(e);
            where = and(where, e);
        }
    }

    @Override
    public void clearOrderBy() {
        orderBy.clear();
    }

    @Override
    public void clearWhere() {
        where = new BooleanBuilder();
    }

    @Override
    public QueryMetadata clone() {
        try {
            DefaultQueryMetadata clone = (DefaultQueryMetadata) super.clone();
            clone.exprInJoins = copyOf(exprInJoins);
            clone.groupBy = copyOf(groupBy);
            clone.having = having;
            clone.joins = copyOf(joins);
            clone.joinTarget = joinTarget;
            clone.joinCondition = joinCondition;
            clone.joinFlags = copyOf(joinFlags);
            clone.joinType = joinType;
            clone.modifiers = modifiers;
            clone.orderBy = copyOf(orderBy);
            clone.projection = projection;
            clone.params = copyOf(params);
            clone.where = where;
            clone.flags = copyOfSorted(flags);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new QueryException(e);
        }
    }

    @Override
    public List<Expression<?>> getGroupBy() {
        return Collections.unmodifiableList(groupBy);
    }

    @Override
    public Predicate getHaving() {
        return having;
    }

    @Override
    public List<JoinExpression> getJoins() {
        if (joinTarget == null) {
            return Collections.unmodifiableList(joins);
        } else {
            List<JoinExpression> j = new ArrayList<>(joins);
            j.add(new JoinExpression(joinType, joinTarget, joinCondition, Collections.unmodifiableSet(new HashSet<>(joinFlags))));
            return j;
        }
    }

    @Override
    public QueryModifiers getModifiers() {
        return modifiers;
    }

    @Override
    public Map<ParamExpression<?>,Object> getParams() {
        return Collections.unmodifiableMap(params);
    }

    @Override
    public List<OrderSpecifier<?>> getOrderBy() {
        return Collections.unmodifiableList(orderBy);
    }

    @Override
    public Expression<?> getProjection() {
        return projection;
    }

    @Override
    public Predicate getWhere() {
        return where;
    }

    @Override
    public boolean isDistinct() {
        return distinct;
    }

    @Override
    public boolean isUnique() {
        return unique;
    }

    @Override
    public void reset() {
        params = new LinkedHashMap<>();
        modifiers = QueryModifiers.EMPTY;
    }

    @Override
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public void setLimit(Long limit) {
        if (modifiers == null || modifiers.getOffset() == null) {
            modifiers = QueryModifiers.limit(limit);
        } else {
            modifiers = new QueryModifiers(limit, modifiers.getOffset());
        }
    }

    @Override
    public void setModifiers(QueryModifiers restriction) {
        if (restriction == null) {
            throw new NullPointerException();
        }
        this.modifiers = restriction;
    }

    @Override
    public void setOffset(Long offset) {
        if (modifiers == null || modifiers.getLimit() == null) {
            modifiers = QueryModifiers.offset(offset);
        } else {
            modifiers = new QueryModifiers(modifiers.getLimit(), offset);
        }
    }

    @Override
    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    @Override
    public <T> void setParam(ParamExpression<T> param, T value) {
        params = put(params, param, value);
    }

    @Override
    public Set<QueryFlag> getFlags() {
        return flags;
    }

    @Override
    public boolean hasFlag(QueryFlag flag) {
        return flags.contains(flag);
    }

    @Override
    public void removeFlag(QueryFlag flag) {
        flags = removeSorted(flags, flag);
    }

    private void validate(Expression<?> expr) {
        if (extractParams) {
            expr.accept(ParamsVisitor.DEFAULT, this);
        }
        if (validate) {
            exprInJoins = expr.accept(validatingVisitor, exprInJoins);
        }
    }

    @Override
    public void setValidate(boolean v) {
        this.validate = v;
    }

    public void setValidatingVisitor(ValidatingVisitor visitor) {
        this.validatingVisitor = visitor;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof QueryMetadata) {
            QueryMetadata q = (QueryMetadata) o;
            return q.getFlags().equals(flags)
                && q.getGroupBy().equals(groupBy)
                && Objects.equals(q.getHaving(), having)
                && q.isDistinct() == distinct
                && q.isUnique() == unique
                && q.getJoins().equals(getJoins())
                && q.getModifiers().equals(modifiers)
                && q.getOrderBy().equals(orderBy)
                && q.getParams().equals(params)
                && Objects.equals(q.getProjection(), projection)
                && Objects.equals(q.getWhere(), where);

        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(flags, groupBy, having, getJoins(), modifiers,
                orderBy, params, projection, unique, where);
    }


}
