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
package com.mysema.query.jpa;

import com.mysema.query.QueryMetadata;
import com.mysema.query.sql.Configuration;
import com.mysema.query.sql.ProjectableSQLQuery;
import com.mysema.query.support.QueryMixin;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Operation;
import com.mysema.query.types.TemplateExpression;

import javax.persistence.Entity;

/**
 * Abstract super class for SQLQuery implementation for JPA and Hibernate
 *
 * @author tiwe
 *
 * @param <Q> concrete subtype
 */
public abstract class AbstractSQLQuery<Q extends AbstractSQLQuery<Q>> extends ProjectableSQLQuery<Q> {

    private static final class NativeQueryMixin<T> extends QueryMixin<T> {
        private NativeQueryMixin(QueryMetadata metadata) {
            super(metadata, false);
        }

        @Override
        public <RT> Expression<RT> convert(Expression<RT> expr, boolean forOrder) {
            return Conversions.convertForNativeQuery(super.convert(expr, forOrder));
        }
    }

    @SuppressWarnings("unchecked")
    public AbstractSQLQuery(QueryMetadata metadata, Configuration configuration) {
        super(new NativeQueryMixin<Q>(metadata), configuration);
        this.queryMixin.setSelf((Q) this);
    }

    protected boolean isEntityExpression(Expression<?> expr) {
        return expr instanceof EntityPath || expr.getType().isAnnotationPresent(Entity.class);
    }

    protected Expression<?> extractEntityExpression(Expression<?> expr) {
        if (expr instanceof Operation) {
            return ((Operation<?>)expr).getArg(0);
        } else if (expr instanceof TemplateExpression) {
            return (Expression<?>) ((TemplateExpression<?>)expr).getArg(0);
        } else {
            return expr;
        }
    }

}
