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
package com.mysema.query;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mysema.query.types.Expression;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.ParamExpression;
import com.mysema.query.types.Predicate;

/**
 * EmptyMetadata provides an immutable empty QueryMetadata instance
 *
 * @author tiwe
 *
 */
public final class EmptyMetadata implements QueryMetadata {

    private static final long serialVersionUID = 134750105981272499L;

    public static final QueryMetadata DEFAULT = new EmptyMetadata();

    @Override
    public void addGroupBy(Expression<?> o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addHaving(Predicate o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addJoin(JoinType joinType, Expression<?> expr) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addJoinCondition(Predicate o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addOrderBy(OrderSpecifier<?> o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addProjection(Expression<?> o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addWhere(Predicate o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clearOrderBy() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clearProjection() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clearWhere() {
        throw new UnsupportedOperationException();
    }

    @Override
    public QueryMetadata clone() {
        return this;
    }

    @Override
    public List<Expression<?>> getGroupBy() {
        return Collections.emptyList();
    }

    @Override
    public Predicate getHaving() {
        return null;
    }

    @Override
    public List<JoinExpression> getJoins() {
        return Collections.emptyList();
    }

    @Override
    public QueryModifiers getModifiers() {
        return null;
    }

    @Override
    public List<OrderSpecifier<?>> getOrderBy() {
        return Collections.emptyList();
    }

    @Override
    public List<Expression<?>> getProjection() {
        return Collections.emptyList();
    }

    @Override
    public Map<ParamExpression<?>, Object> getParams() {
        return Collections.emptyMap();
    }

    @Override
    public Predicate getWhere() {
        return null;
    }

    @Override
    public boolean isDistinct() {
        return false;
    }

    @Override
    public boolean isUnique() {
        return false;
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDistinct(boolean distinct) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLimit(Long limit) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setModifiers(QueryModifiers restriction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setOffset(Long offset) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setUnique(boolean unique) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> void setParam(ParamExpression<T> param, T value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addFlag(QueryFlag flag) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasFlag(QueryFlag flag) {
        return false;
    }

    @Override
    public Set<QueryFlag> getFlags() {
        return Collections.emptySet();
    }

    @Override
    public void setValidate(boolean v) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addJoinFlag(JoinFlag flag) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeFlag(QueryFlag flag) {

    }

}
