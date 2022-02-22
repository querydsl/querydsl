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
package com.querydsl.jpa;

import javax.persistence.Entity;

import com.querydsl.core.JoinType;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.support.CollectionAnyVisitor;
import com.querydsl.core.support.Context;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.SimplePath;

/**
 * {@code JPACollectionAnyVisitor} extends the {@link CollectionAnyVisitor} class with module specific
 * extensions
 *
 * @author tiwe
 *
 */
public class JPACollectionAnyVisitor extends CollectionAnyVisitor {

    @SuppressWarnings("unchecked")
    @Override
    protected Predicate exists(Context c, Predicate condition) {
        JPAQueryMixin<?> query = new JPAQueryMixin<Object>();
        query.setProjection(Expressions.ONE);
        for (int i = 0; i < c.paths.size(); i++) {
            Path<?> child = c.paths.get(i).getMetadata().getParent();
            EntityPath<Object> replacement = (EntityPath<Object>) c.replacements.get(i);
            if (c.paths.get(i).getType().isAnnotationPresent(Entity.class)) {
                query.addJoin(i == 0 ? JoinType.DEFAULT : JoinType.INNERJOIN, Expressions.as(
                        Expressions.listPath((Class) c.paths.get(i).getType(), SimplePath.class,
                                child.getMetadata()), replacement));
            } else {
                // join via parent
                Path<?> parent = child.getMetadata().getParent();
                EntityPathBase<Object> newParent = new EntityPathBase<Object>(parent.getType(),
                        ExpressionUtils.createRootVariable(parent, Math.abs(condition.hashCode())));
                EntityPath<Object> newChild = new EntityPathBase<Object>(child.getType(),
                        PathMetadataFactory.forProperty(newParent, child.getMetadata().getName()));
                query.from(newParent);
                query.addJoin(JoinType.INNERJOIN, Expressions.as(newChild, replacement));
                query.where(ExpressionUtils.eq(newParent, parent));
            }
        }
        c.clear();
        query.where(condition);
        return ExpressionUtils.predicate(Ops.EXISTS, asExpression(query.getMetadata()));
    }

    protected Expression<?> asExpression(QueryMetadata metadata) {
        return new SubQueryExpressionImpl<Object>(metadata.getProjection().getType(), metadata);
    }

}
