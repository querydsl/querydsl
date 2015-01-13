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
package com.querydsl.core.support;

import static com.querydsl.core.alias.Alias.$;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.querydsl.core.alias.Alias;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.NullExpression;
import com.querydsl.core.types.PathImpl;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.core.types.query.ListSubQuery;


@SuppressWarnings("unchecked")
public class DetachableMixinTest {

    private QueryMixin query;

    private DetachableMixin detachable;

    @Before
    public void setUp() {
        query = new QueryMixin();
        detachable = new DetachableMixin(query);
    }

    @Test
    public void Projections() {
        DummyEntity e = Alias.alias(DummyEntity.class);
        query.from($(e));
        assertNotNull(detachable.exists());
        assertNotNull(detachable.notExists());
        assertNotNull(detachable.list($(e)));
        assertNotNull(detachable.unique($(e)));
        assertNotNull(detachable.list($(e), $(e.getOther())));
        assertNotNull(detachable.unique($(e), $(e.getOther())));
    }

    @Test
    public void List_Objects() {
        query.from(new PathImpl(Object.class, "x"));
        ListSubQuery subQuery = detachable.list(new PathImpl(Object.class, "x"), "XXX");
        List<? extends Expression<?>> exprs = subQuery.getMetadata().getProjection();
        assertEquals(new PathImpl(Object.class, "x"), exprs.get(0));
        assertEquals(ConstantImpl.create("XXX"), exprs.get(1));
    }

    @Test
    public void Unique_Objects() {
        query.from(new PathImpl(Object.class, "x"));
        SubQueryExpression<?> subQuery = detachable.unique(new PathImpl(Object.class, "x"), "XXX");
        List<? extends Expression<?>> exprs = subQuery.getMetadata().getProjection();
        assertEquals(new PathImpl(Object.class, "x"), exprs.get(0));
        assertEquals(ConstantImpl.create("XXX"), exprs.get(1));
    }

    @Test
    public void Null_As_Template() {
        query.from(new PathImpl(Object.class, "x"));
        SubQueryExpression<?> subQuery = detachable.unique(new PathImpl(Object.class, "x"), null);
        List<? extends Expression<?>> exprs = subQuery.getMetadata().getProjection();
        assertEquals(new PathImpl(Object.class, "x"), exprs.get(0));
        assertEquals(NullExpression.DEFAULT, exprs.get(1));
    }

}
