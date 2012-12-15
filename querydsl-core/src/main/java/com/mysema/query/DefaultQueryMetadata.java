/*
 * Copyright 2011, Mysema Ltd
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
package com.mysema.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.mysema.query.types.Expression;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.ParamExpression;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.ValidatingVisitor;

/**
 * DefaultQueryMetadata is the default implementation of the {@link QueryMetadata} interface
 *
 * @author tiwe
 */
public class DefaultQueryMetadata implements QueryMetadata, Cloneable {

    private static final long serialVersionUID = 317736313966701232L;

    private boolean distinct;

    private Set<Expression<?>> exprInJoins = ImmutableSet.of();

    private List<Expression<?>> groupBy = ImmutableList.of();

    private BooleanBuilder having = new BooleanBuilder();

    private List<JoinExpression> joins = ImmutableList.of();

    @Nullable
    private QueryModifiers modifiers = QueryModifiers.EMPTY;

    private List<OrderSpecifier<?>> orderBy = ImmutableList.of();

    private List<Expression<?>> projection = ImmutableList.of();

    // NOTE : this is not necessarily serializable
    private Map<ParamExpression<?>,Object> params = ImmutableMap.<ParamExpression<?>, Object>of();

    private boolean unique;

    private BooleanBuilder where = new BooleanBuilder();

    // TODO : make sure this is sorted
    private Set<QueryFlag> flags = ImmutableSet.of();
    
    private boolean validate = true;
    
    private static <T> List<T> add(List<T> list, T element) {
        if (list.isEmpty()) {
            return ImmutableList.of(element);
        } else if (list.size() == 1) {
            list = new ArrayList<T>(list);
        }
        list.add(element);
        return list;
    }
    
    private static <T> Set<T> add(Set<T> set, T element) {
        if (set.isEmpty()) {
            return ImmutableSet.of(element);
        } else if (set.size() == 1) {
            set = new HashSet<T>(set);
        }
        set.add(element);
        return set;
    }
    
    private static <K,V> Map<K,V> put(Map<K,V> map, K key, V value) {
        if (map.isEmpty()) {
            return ImmutableMap.of(key, value);
        } else if (map.size() == 1) {
            map = new HashMap<K,V>(map);
        }
        map.put(key, value);
        return map;
    }
    
    /**
     * Create an empty DefaultQueryMetadata instance
     */
    public DefaultQueryMetadata() {
        
    }
        
    /**
     * Disable validation
     * 
     * @return
     */
    public DefaultQueryMetadata noValidate() {
        validate = false;
        return this;
    }
            
    @Override
    public void addGroupBy(Expression<?> o) {
        validate(o);
        groupBy = add(groupBy, o);
    }

    @Override
    public void addHaving(Predicate e) {
        if (e == null) {
            return;
        }
        e = (Predicate)ExpressionUtils.extract(e);
        if (e != null) {
            validate(e);
            having.and(e);
        }
    }

    @Override
    public void addJoin(JoinType joinType, Expression<?> expr) {
        addJoin(new JoinExpression(joinType, expr));
    }
    
    @Override
    public void addJoin(JoinExpression join) {
        Expression<?> expr = join.getTarget();
        if (!exprInJoins.contains(expr)) {
            validateJoin(join);
            exprInJoins = add(exprInJoins, expr);
            validate(expr);
            joins = add(joins, join);
        } else {
            throw new IllegalStateException(expr + " is already used");
        }
    }

    private void validateJoin(JoinExpression join) {
        if (validate && join.getTarget() instanceof Path) {
            Path<?> path = (Path<?>)join.getTarget();
            if (join.getType() == JoinType.DEFAULT) {
                ensureRoot(path);
            } 
        }         
    }
    
    @Override
    public void addJoinCondition(Predicate o) {
        if (!joins.isEmpty()) {
            validate(o);
            joins.get(joins.size() - 1).addCondition(o);
        }
    }

    @Override
    public void addOrderBy(OrderSpecifier<?> o) {
        // order specifiers can't be validated, since they can refer to projection elements
        // that are declared later
        orderBy = add(orderBy, o);
    }

    @Override
    public void addProjection(Expression<?> o) {
        validate(o);
        projection = add(projection, o);
    }

    @Override
    public void addWhere(Predicate e) {
        if (e == null) {
            return;
        }
        e = (Predicate)ExpressionUtils.extract(e);
        if (e != null) {
            validate(e);
            where.and(e);
        }
    }

    public void clearOrderBy(){
        orderBy = ImmutableList.of();
    }

    public void clearProjection(){
        projection = ImmutableList.of();
    }

    public void clearWhere(){
        where = new BooleanBuilder();
    }

    @Override
    public QueryMetadata clone(){
        try {
            DefaultQueryMetadata clone = (DefaultQueryMetadata) super.clone();
            clone.exprInJoins = new HashSet<Expression<?>>(exprInJoins);
            clone.groupBy = ImmutableList.copyOf(groupBy);
            clone.having = having.clone();
            clone.joins = ImmutableList.copyOf(joins);
            clone.modifiers = new QueryModifiers(modifiers);
            clone.orderBy = ImmutableList.copyOf(orderBy);
            clone.projection = ImmutableList.copyOf(projection);
            clone.params = ImmutableMap.copyOf(params);
            clone.where = where.clone();
            clone.flags = ImmutableSortedSet.copyOf(flags);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new QueryException(e);
        }
    }

    private void ensureRoot(Path<?> path){        
        if (!path.getMetadata().isRoot()) {
            throw new IllegalArgumentException("Only root paths are allowed for joins : " + path);
        }
    }

    @Override
    public List<Expression<?>> getGroupBy() {
        return groupBy;
    }

    @Override
    public Predicate getHaving() {
        return having.getValue();
    }

    @Override
    public List<JoinExpression> getJoins() {
        return joins;
    }

    @Override
    @Nullable
    public QueryModifiers getModifiers() {
        return modifiers;
    }

    public Map<ParamExpression<?>,Object> getParams(){
        return params;
    }

    @Override
    public List<OrderSpecifier<?>> getOrderBy() {
        return orderBy;
    }

    @Override
    public List<Expression<?>> getProjection() {
        return projection;
    }

    @Override
    public Predicate getWhere() {
        return where.hasValue() ? where.getValue() : null;
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
        clearProjection();
        params = ImmutableMap.of();
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
    public void setModifiers(@Nullable QueryModifiers restriction) {
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
    public void addFlag(QueryFlag flag) {
        flags = add(flags, flag);        
    }

    @Override
    public Set<QueryFlag> getFlags() {
        return flags;
    }

    @Override
    public boolean hasFlag(QueryFlag flag) {
        return flags.contains(flag);
    }
    
    private void validate(Expression<?> expr){
        if (validate) {
            expr.accept(ValidatingVisitor.DEFAULT, exprInJoins);
        }
    }
    
    public void setValidate(boolean v) {
        this.validate = v;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof QueryMetadata) {
            QueryMetadata q = (QueryMetadata)o;
            return q.getFlags().equals(flags)
                && q.getGroupBy().equals(groupBy)
                && Objects.equal(q.getHaving(), having.getValue())
                && q.isDistinct() == distinct
                && q.isUnique() == unique
                && q.getJoins().equals(joins)
                && Objects.equal(q.getModifiers(), modifiers)
                && q.getOrderBy().equals(orderBy)
                && q.getParams().equals(params)
                && q.getProjection().equals(projection)
                && Objects.equal(q.getWhere(), where.getValue());
            
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(flags, groupBy, having, joins, modifiers, 
                orderBy, params, projection, unique, where);        
    }
    
    
}
