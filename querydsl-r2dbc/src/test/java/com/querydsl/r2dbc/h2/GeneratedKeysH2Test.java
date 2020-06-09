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
package com.querydsl.r2dbc.h2;

import com.querydsl.r2dbc.Connections;
import com.querydsl.r2dbc.H2Templates;
import com.querydsl.r2dbc.QGeneratedKeysEntity;
import com.querydsl.r2dbc.dml.R2DBCInsertClause;
import io.r2dbc.spi.Connection;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class GeneratedKeysH2Test {

    private Connection conn;

    @Before
    public void setUp() {
        conn = Connections.getH2().getConnection().block();
    }

    @Test
    @Ignore("currently not supported")
    public void test() {
        Mono.from(conn.createStatement("drop table GENERATED_KEYS if exists").execute()).block();
        Mono.from(conn.createStatement("create table GENERATED_KEYS(" +
                "ID int AUTO_INCREMENT PRIMARY KEY, " +
                "NAME varchar(30))").execute()).block();

        QGeneratedKeysEntity entity = new QGeneratedKeysEntity("entity");
        R2DBCInsertClause insertClause = new R2DBCInsertClause(conn, new H2Templates(), entity);
        Collection<Integer> key = insertClause.set(entity.name, "Hello").executeWithKeys(entity.id).collectList().block();

        assertEquals(1, key.size());

        insertClause = new R2DBCInsertClause(conn, new H2Templates(), entity);
        key = insertClause.set(entity.name, "World").executeWithKeys(entity.id).collectList().block();
        assertEquals(2, key.size());

        insertClause = new R2DBCInsertClause(conn, new H2Templates(), entity);
        assertEquals(3, (long) insertClause.set(entity.name, "World").executeWithKey(entity.id).block());

        insertClause = new R2DBCInsertClause(conn, new H2Templates(), entity);
        assertEquals(Collections.singletonList(4), insertClause.set(entity.name, "World").executeWithKeys(entity.id));
    }

}
