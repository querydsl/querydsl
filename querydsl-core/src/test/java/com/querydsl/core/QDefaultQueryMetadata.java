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
package com.querydsl.core;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.ParamExpression;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.ValidatingVisitor;
import com.querydsl.core.types.path.BeanPath;
import com.querydsl.core.types.path.BooleanPath;
import com.querydsl.core.types.path.EntityPathBase;
import com.querydsl.core.types.path.ListPath;
import com.querydsl.core.types.path.MapPath;
import com.querydsl.core.types.path.PathInits;
import com.querydsl.core.types.path.SetPath;
import com.querydsl.core.types.path.SimplePath;


/**
 * QDefaultQueryMetadata is a Querydsl querydsl type for DefaultQueryMetadata
 */
public class QDefaultQueryMetadata extends EntityPathBase<DefaultQueryMetadata> {

    private static final long serialVersionUID = 2000363531;

    public static final QDefaultQueryMetadata defaultQueryMetadata = new QDefaultQueryMetadata("defaultQueryMetadata");

    public final BooleanPath distinct = createBoolean("distinct");

    public final SetPath<Expression<?>, SimplePath<Expression<?>>> exprInJoins = this.<Expression<?>, SimplePath<Expression<?>>>createSet("exprInJoins", Expression.class, SimplePath.class, PathInits.DIRECT);

    public final SetPath<QueryFlag, SimplePath<QueryFlag>> flags = this.<QueryFlag, SimplePath<QueryFlag>>createSet("flags", QueryFlag.class, SimplePath.class, PathInits.DIRECT);

    public final ListPath<Expression<?>, SimplePath<Expression<?>>> groupBy = this.<Expression<?>, SimplePath<Expression<?>>>createList("groupBy", Expression.class, SimplePath.class, PathInits.DIRECT);

    public final SimplePath<Predicate> having = createSimple("having", Predicate.class);

    public final ListPath<JoinExpression, SimplePath<JoinExpression>> joins = this.<JoinExpression, SimplePath<JoinExpression>>createList("joins", JoinExpression.class, SimplePath.class, PathInits.DIRECT);

    public final SimplePath<QueryModifiers> modifiers = createSimple("modifiers", QueryModifiers.class);

    public final ListPath<OrderSpecifier<?>, SimplePath<OrderSpecifier<?>>> orderBy = this.<OrderSpecifier<?>, SimplePath<OrderSpecifier<?>>>createList("orderBy", OrderSpecifier.class, SimplePath.class, PathInits.DIRECT);

    public final MapPath<ParamExpression<?>, Object, SimplePath<Object>> params = this.<ParamExpression<?>, Object, SimplePath<Object>>createMap("params", ParamExpression.class, Object.class, SimplePath.class);

    public final ListPath<Expression<?>, SimplePath<Expression<?>>> projection = this.<Expression<?>, SimplePath<Expression<?>>>createList("projection", Expression.class, SimplePath.class, PathInits.DIRECT);

    public final BooleanPath unique = createBoolean("unique");

    public final BooleanPath validate = createBoolean("validate");

    public final SimplePath<ValidatingVisitor> validatingVisitor = createSimple("validatingVisitor", ValidatingVisitor.class);

    public final SimplePath<Predicate> where = createSimple("where", Predicate.class);

    public QDefaultQueryMetadata(String variable) {
        super(DefaultQueryMetadata.class, forVariable(variable));
    }

    public QDefaultQueryMetadata(BeanPath<? extends DefaultQueryMetadata> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QDefaultQueryMetadata(PathMetadata<?> metadata) {
        super(DefaultQueryMetadata.class, metadata);
    }

}

