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
package com.mysema.query.domain;

import java.util.Map;

import javax.jdo.annotations.PersistenceCapable;
import javax.persistence.Entity;

import org.junit.Test;

import com.mysema.query.annotations.PropertyType;
import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QueryProjection;
import com.mysema.query.annotations.QueryType;
import com.mysema.query.types.expr.NumberExpression;
import com.mysema.query.types.expr.StringExpression;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;

public class QueryProjectionTest {

    // all three annotations are needed, because all are used as entity annotations
    @QueryEntity
    @Entity
    @PersistenceCapable
    public static class EntityWithProjection {

        public EntityWithProjection(long id) {

        }

        @QueryProjection
        public EntityWithProjection(String name) {

        }

        @QueryProjection
        public EntityWithProjection(@QueryType(PropertyType.SIMPLE) Long id) {

        }

        @QueryProjection
        public EntityWithProjection(long id, CharSequence name) {

        }

        @QueryProjection
        public EntityWithProjection(String id, CharSequence name) {

        }

    }

    @Test
    public void Entity_Case() {
        NumberExpression<Long> longExpr = new NumberPath<Long>(Long.class, "x");
        StringExpression stringExpr = new StringPath("x");
        
        QQueryProjectionTest_EntityWithProjection.create(longExpr).newInstance(0l);
        QQueryProjectionTest_EntityWithProjection.create(stringExpr).newInstance("");
        QQueryProjectionTest_EntityWithProjection.create(longExpr, stringExpr).newInstance(0l,"");
        QQueryProjectionTest_EntityWithProjection.create(stringExpr,stringExpr).newInstance("","");
    }

    public static class DTOWithProjection {

        public DTOWithProjection(long id) {

        }

        @QueryProjection
        public DTOWithProjection(@QueryType(PropertyType.SIMPLE) Long id) {

        }

        @QueryProjection
        public DTOWithProjection(String name) {

        }

        @QueryProjection
        public DTOWithProjection(EntityWithProjection entity) {

        }

        @QueryProjection
        public DTOWithProjection(long id, CharSequence name) {

        }

        @QueryProjection
        public DTOWithProjection(String id, CharSequence name) {

        }

        @QueryProjection
        public DTOWithProjection(DTOWithProjection dto, long id, Long id2, String str, CharSequence c) {

        }

        @QueryProjection
        public DTOWithProjection(String id, CharSequence name, Map<Long,String> map1, Map<DTOWithProjection, Long> map2) {

        }
    }

    @Test
    public void Dto_Case() throws SecurityException, NoSuchMethodException{
        NumberExpression<Long> longExpr = new NumberPath<Long>(Long.class, "x");
        StringExpression stringExpr = new StringPath("x");
        
        new QQueryProjectionTest_DTOWithProjection(longExpr).newInstance(0l);
        new QQueryProjectionTest_DTOWithProjection(stringExpr).newInstance("");
        new QQueryProjectionTest_DTOWithProjection(longExpr, stringExpr).newInstance(0l,"");
        new QQueryProjectionTest_DTOWithProjection(stringExpr, stringExpr).newInstance("","");

    }
}
