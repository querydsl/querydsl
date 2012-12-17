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
package com.mysema.query.jdo.sql;

import javax.annotation.Nullable;

import com.mysema.query.JoinFlag;
import com.mysema.query.QueryFlag;
import com.mysema.query.QueryFlag.Position;
import com.mysema.query.QueryMetadata;
import com.mysema.query.sql.ForeignKey;
import com.mysema.query.sql.RelationalFunctionCall;
import com.mysema.query.sql.RelationalPath;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.sql.Union;
import com.mysema.query.sql.UnionImpl;
import com.mysema.query.sql.UnionUtils;
import com.mysema.query.support.ProjectableQuery;
import com.mysema.query.support.QueryMixin;
import com.mysema.query.types.Expression;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.SubQueryExpression;
import com.mysema.query.types.expr.Wildcard;
import com.mysema.query.types.query.ListSubQuery;
import com.mysema.query.types.template.NumberTemplate;
import com.mysema.query.types.template.SimpleTemplate;

/**
 * Base class for JDO based SQLQuery implementations
 *
 * @author tiwe
 *
 * @param <T>
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractSQLQuery<T extends AbstractSQLQuery<T> & com.mysema.query.Query> extends ProjectableQuery<T> {

    protected final QueryMixin<T> queryMixin;
    
    @Nullable
    protected Expression<?> union;
    
    protected boolean unionAll;
    
    protected final SQLTemplates templates;
    
    @SuppressWarnings("unchecked")
    public AbstractSQLQuery(QueryMetadata metadata, SQLTemplates templates) {
        super(new QueryMixin<T>(metadata, false));
        this.templates = templates;
        this.queryMixin = (QueryMixin<T>)super.queryMixin;
        this.queryMixin.setSelf((T)this);
    }

    @Override
    public long count() {
        return uniqueResult(Wildcard.countAsInt);
    }

    @Override
    public boolean exists() {
        return limit(1).uniqueResult(NumberTemplate.ONE) != null;
    }
    
    public T from(Expression<?> arg) {
        return queryMixin.from(arg);
    }

    public T from(Expression<?>... args) {
        return queryMixin.from(args);
    }

    @SuppressWarnings("unchecked")
    public T from(SubQueryExpression<?> subQuery, Path<?> alias) {
        return queryMixin.from(ExpressionUtils.as((Expression)subQuery, alias));
    }

    public T fullJoin(RelationalPath<?> o) {
        return queryMixin.fullJoin(o);
    }
    
    public <E> T fullJoin(RelationalFunctionCall<E> target, Path<E> alias) {
        return queryMixin.fullJoin(target, alias);
    }
    
    public <E> T fullJoin(ForeignKey<E> key, RelationalPath<E> entity) {
        return queryMixin.fullJoin(entity).on(key.on(entity));
    }

    public T fullJoin(SubQueryExpression<?> o, Path<?> alias) {
        return queryMixin.fullJoin(o, alias);
    }

    public QueryMetadata getMetadata() {
        return queryMixin.getMetadata();
    }

    public T innerJoin(RelationalPath<?> o) {
        return queryMixin.innerJoin(o);
    }
    
    public <E> T innerJoin(RelationalFunctionCall<E> target, Path<E> alias) {
        return queryMixin.innerJoin(target, alias);
    }
    
    public <E> T innerJoin(ForeignKey<E> key, RelationalPath<E> entity) {
        return queryMixin.innerJoin(entity).on(key.on(entity));
    }

    public T innerJoin(SubQueryExpression<?> o, Path<?> alias) {
        return queryMixin.innerJoin(o, alias);
    }
    
    public T join(RelationalPath<?> o) {
        return queryMixin.join(o);
    }
    
    public <E> T join(RelationalFunctionCall<E> target, Path<E> alias) {
        return queryMixin.join(target, alias);
    }
    
    public <E> T join(ForeignKey<E> key, RelationalPath<E> entity) {
        return queryMixin.join(entity).on(key.on(entity));
    }

    public T join(SubQueryExpression<?> o, Path<?> alias) {
        return queryMixin.join(o, alias);
    }

    public T leftJoin(RelationalPath<?> o) {
        return queryMixin.leftJoin(o);
    }
    
    public <E> T leftJoin(RelationalFunctionCall<E> target, Path<E> alias) {
        return queryMixin.leftJoin(target, alias);
    }

    public <E> T leftJoin(ForeignKey<E> key, RelationalPath<E> entity) {
        return queryMixin.leftJoin(entity).on(key.on(entity));
    }

    public T leftJoin(SubQueryExpression<?> o, Path<?> alias) {
        return queryMixin.leftJoin(o, alias);
    }

    public T on(Predicate condition) {
        return queryMixin.on(condition);
    }
    
    public T on(Predicate... conditions) {
        return queryMixin.on(conditions);
    }

    public T rightJoin(RelationalPath<?> o) {
        return queryMixin.rightJoin(o);
    }
    
    public <E> T rightJoin(RelationalFunctionCall<E> target, Path<E> alias) {
        return queryMixin.rightJoin(target, alias);
    }

    public <E> T rightJoin(ForeignKey<E> key, RelationalPath<E> entity) {
        return queryMixin.rightJoin(entity).on(key.on(entity));
    }
    
    public T rightJoin(SubQueryExpression<?> o, Path<?> alias) {
        return queryMixin.rightJoin(o, alias);
    }

    public T addJoinFlag(String flag) {
        return addJoinFlag(flag, JoinFlag.Position.BEFORE_TARGET);
    }

    @SuppressWarnings("unchecked")
    public T addJoinFlag(String flag, JoinFlag.Position position) {
        queryMixin.addJoinFlag(new JoinFlag(flag, position));                   
        return (T)this;
    }

    public T addFlag(Position position, String prefix, Expression<?> expr) {
        Expression<?> flag = SimpleTemplate.create(expr.getType(), prefix + "{0}", expr);
        return queryMixin.addFlag(new QueryFlag(position, flag));
    }

    public T addFlag(Position position, String flag) {
        return queryMixin.addFlag(new QueryFlag(position, flag));
    }

    public T addFlag(Position position, Expression<?> flag) {
        return queryMixin.addFlag(new QueryFlag(position, flag));
    }
    
    public <RT> Union<RT> union(ListSubQuery<RT>... sq) {
        return innerUnion(sq);
    }

    public <RT> Union<RT> union(SubQueryExpression<RT>... sq) {
        return innerUnion(sq);
    }
    
    public <RT> Union<RT> unionAll(ListSubQuery<RT>... sq) {
        unionAll = true;
        return innerUnion(sq);
    }

    public <RT> Union<RT> unionAll(SubQueryExpression<RT>... sq) {
        unionAll = true;
        return innerUnion(sq);
    }
    
    public <RT> T union(Path<?> alias, ListSubQuery<RT>... sq) {
        return from(UnionUtils.union(sq, alias, false));
    }
    
    public <RT> T union(Path<?> alias, SubQueryExpression<RT>... sq) {
        return from(UnionUtils.union(sq, alias, false));
    }
        
    public <RT> T unionAll(Path<?> alias, ListSubQuery<RT>... sq) {
        return from(UnionUtils.union(sq, alias, true));
    }
    
    public <RT> T unionAll(Path<?> alias, SubQueryExpression<RT>... sq) {
        return from(UnionUtils.union(sq, alias, true));
    }    
    
    @SuppressWarnings("unchecked")
    private <RT> Union<RT> innerUnion(SubQueryExpression<?>... sq) {
        queryMixin.getMetadata().setValidate(false);
        if (!queryMixin.getMetadata().getJoins().isEmpty()) {
            throw new IllegalArgumentException("Don't mix union and from");
        }
        this.union = UnionUtils.union(sq, unionAll);
        return new UnionImpl<T, RT>((T)this, sq[0].getMetadata().getProjection());
    }

}
